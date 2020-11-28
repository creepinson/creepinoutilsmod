package com.theoparis.creepinoutils.util.client

import com.mojang.blaze3d.platform.GlStateManager
import com.theoparis.creepinoutils.util.ReflectionUtils
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.crash.CrashReport
import net.minecraft.crash.ReportedException
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.util.math.vector.Vector4f
import net.minecraftforge.client.model.pipeline.LightUtil
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer
import java.lang.reflect.Field
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.*
import java.util.stream.Collectors
import javax.annotation.Nonnull


object ClientUtil {
    /**
     * A vertex definition for a simple 2-dimensional quad defined in counter-clockwise order with the top-left origin.
     */
    val SIMPLE_QUAD = arrayOf(
        Vector4f(1f, 1f, 0f, 0f),
        Vector4f(1f, 0f, 0f, 0f),
        Vector4f(0f, 0f, 0f, 0f),
        Vector4f(0f, 1f, 0f, 0f)
    )

    // add or subtract from the sprites UV location to remove transparent lines in between textures
    private const val UV_CORRECT = 1f / 16f / 10000

    /**
     * A field reference to the rawIntBuffer of the BufferBuilder class. Need reflection since the field is private.
     */
    //	private static final Field bufferBuilder_rawIntBuffer = ReflectionHelper.findField(BufferBuilder.class, "rawIntBuffer", "field_178999_b");
    // use the old (Class, String...) instead of the new (Class, String, String) for backwards compatibility
    //TODO: change back to (Class, String, String) soon
    private val bufferBuilder_rawIntBuffer: Field =
        ReflectionUtils.findField(BufferBuilder::class.java, "byteBuffer/**/")!!

    /**
     * Rotation algorithm Taken off Max_the_Technomancer from [here](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2772267-tesr-getting-darker-and-lighter-as-it-rotates)
     *
     * @param face the [face][Direction] to rotate for
     */
    fun rotateForFace(face: Direction) {
        // TODO: use matrixStack?
        GlStateManager.rotatef(
            if (face === Direction.DOWN) 0f else if (face === Direction.UP) 180f else if (face === Direction.NORTH || face === Direction.EAST) 90f else -90f,
            if (face.axis === Direction.Axis.Z) 1f else 0f,
            0f,
            if (face.axis === Direction.Axis.Z) 0f else 1f
        )
        GlStateManager.rotatef(-90f, 0f, 0f, 1f)
    }

    /**
     * Put a lot of effort into this, it gets the entities exact (really, really exact) position
     *
     * @param entity       The entity to calculate the position of
     * @param partialTicks The multiplier used to predict where the entity is/will be
     * @return The position of the entity as a Vector3d
     */
    @Nonnull
    fun getEntityRenderPos(@Nonnull entity: Entity, @Nonnull partialTicks: Double): Vector3d {
        var flyingMultiplier = 1.825
        var yFlying = 1.02
        var yAdd = 0.0784000015258789
        if (entity is PlayerEntity && (entity).abilities.isFlying) {
            flyingMultiplier = 1.1
            yFlying = 1.67
            yAdd = 0.0
        }
        val yGround =
            if (entity.motion.y + yAdd === 0.0 && entity.prevPosY > entity.posY) entity.posY - entity.prevPosY else 0.toDouble()
        var xFall = 1.0
        if (flyingMultiplier == 1.825) {
            if (entity.motion.x !== 0.0) {
                if (entity.motion.y + yAdd !== 0.0) {
                    xFall = 0.6
                } else if (yGround != 0.0) {
                    xFall = 0.6
                }
            } else {
                xFall = 0.6
            }
        }
        var zFall = 1.0
        if (flyingMultiplier == 1.825) {
            if (entity.motion.z !== 0.0) {
                if (entity.motion.y + yAdd !== 0.0) {
                    zFall = 0.6
                } else if (yGround != 0.0) {
                    zFall = 0.6
                }
            } else {
                zFall = 0.6
            }
        }
        val dX: Double =
            entity.posX - (entity.prevPosX - entity.posX) * partialTicks - entity.motion.x * xFall * flyingMultiplier
        val dY: Double =
            entity.posY - yGround - (entity.prevPosY - entity.posY) * partialTicks - (entity.motion.y + yAdd) * yFlying
        val dZ: Double =
            entity.posZ - (entity.prevPosZ - entity.posZ) * partialTicks - entity.motion.z * zFall * flyingMultiplier
        return Vector3d(dX, dY, dZ)
    }

    /**
     * Rotates around X axis based on Pitch input and around Y axis based on Yaw input
     *
     * @param pitch the pitch
     * @param yaw   the yaw
     */
    fun rotateForPitchYaw(pitch: Double, yaw: Double) {
        GlStateManager.rotatef(yaw.toFloat(), 0f, 1f, 0f)
        GlStateManager.rotatef(pitch.toFloat(), 1f, 0f, 0f)
    }

    /**
     * Gets the pitch rotation between two vectors
     *
     * @param source      the source vector
     * @param destination the destination vector
     * @return the pitch rotation
     */
    fun getPitch(@Nonnull source: Vector3d?, @Nonnull destination: Vector3d): Double {
        var pitch = Math.atan2(destination.y, Math.sqrt(destination.x * destination.x + destination.z * destination.z))
        pitch = pitch * (180 / Math.PI)
        pitch = if (pitch < 0) 360 - -pitch else pitch
        return 90 - pitch
    }

    /**
     * Gets the yaw rotation between two vectors
     *
     * @param source      the source vector
     * @param destination the destination vector
     * @return the yaw rotation
     */
    fun getYaw(@Nonnull source: Vector3d, @Nonnull destination: Vector3d): Double {
        var yaw = Math.atan2(destination.x - source.x, destination.z - source.z)
        yaw = yaw * (180 / Math.PI)
        yaw = if (yaw < 0) 360 - -yaw else yaw
        return yaw + 90
    }

    /**
     * @param red   the red value of the color, between 0x00 (decimal 0) and 0xFF (decimal 255)
     * @param green the red value of the color, between 0x00 (decimal 0) and 0xFF (decimal 255)
     * @param blue  the red value of the color, between 0x00 (decimal 0) and 0xFF (decimal 255)
     * @return the color in ARGB format
     */
    fun color(red: Int, green: Int, blue: Int): Int {
        var red = red
        var green = green
        var blue = blue
        red = MathHelper.clamp(red, 0x00, 0xFF)
        green = MathHelper.clamp(green, 0x00, 0xFF)
        blue = MathHelper.clamp(blue, 0x00, 0xFF)
        val alpha = 0xFF

        // 0x alpha red green blue
        // 0xaarrggbb

        // int colorRGBA = 0;
        // colorRGBA |= red << 16;
        // colorRGBA |= green << 8;
        // colorRGBA |= blue << 0;
        // colorRGBA |= alpha << 24;
        return blue or (red shl 16) or (green shl 8) or (alpha shl 24)
    }

    /**
     * @param red   the red value of the color, 0F and 1F
     * @param green the green value of the color, 0F and 1F
     * @param blue  the blue value of the color, 0F and 1F
     * @return the color in ARGB format
     */
    fun colorf(red: Float, green: Float, blue: Float): Int {
        val redInt = Math.max(0, Math.min(255, Math.round(red * 255)))
        val greenInt = Math.max(0, Math.min(255, Math.round(green * 255)))
        val blueInt = Math.max(0, Math.min(255, Math.round(blue * 255)))
        return color(redInt, greenInt, blueInt)
    }

    // Below are some helper methods to upload data to the buffer for use by FastTESRs
    fun getLightmapSkyLightCoordsFromPackedLightmapCoords(packedLightmapCoords: Int): Int {
        return packedLightmapCoords shr 16 and 0xFFFF // get upper 4 bytes
    }

    fun getLightmapBlockLightCoordsFromPackedLightmapCoords(packedLightmapCoords: Int): Int {
        return packedLightmapCoords and 0xFFFF // get lower 4 bytes
    }

    /**
     * Renders a simple 2 dimensional quad at a given position to a given buffer with the given transforms, color, texture and lightmap values.
     *
     * @param baseOffset         the base offset. This will be untouched by the model matrix transformations.
     * @param buffer             the buffer to upload the quads to. Vertex format of BLOCK is assumed.
     * @param transform          the model matrix to use as the transform matrix.
     * @param color              the color of the quad. The format is ARGB where each component is represented by a byte.
     * @param texture            the TextureAtlasSprite object to gain the UV data from.
     * @param lightmapSkyLight   the skylight lightmap coordinates for the quad.
     * @param lightmapBlockLight the blocklight lightmap coordinates for the quad.
     */
    fun renderSimpleQuad(
        baseOffset: Vector3f,
        buffer: BufferBuilder,
        transform: Matrix4f?,
        color: Int,
        texture: TextureAtlasSprite,
        lightmapSkyLight: Int,
        lightmapBlockLight: Int
    ) {
        renderCustomQuad(
            SIMPLE_QUAD,
            baseOffset,
            buffer,
            transform,
            color,
            texture,
            lightmapSkyLight,
            lightmapBlockLight
        )
    }

    /**
     * Renders a simple 2 dimensional quad at a given position to a given buffer with the given transforms, color, texture and lightmap values.
     *
     * @param baseOffset         the base offset. This will be untouched by the model matrix transformations.
     * @param buffer             the buffer to upload the quads to. Vertex format of BLOCK is assumed.
     * @param transform          the model matrix to use as the transform matrix.
     * @param color              the color of the quad. The format is ARGB where each component is represented by a byte.
     * @param texture            the TextureAtlasSprite object to gain the UV data from.
     * @param lightmapSkyLight   the skylight lightmap coordinates for the quad.
     * @param lightmapBlockLight the blocklight lightmap coordinates for the quad.
     */
    fun renderCustomQuad(
        customQuad: Array<Vector4f>,
        baseOffset: Vector3f,
        buffer: BufferBuilder,
        transform: Matrix4f?,
        color: Int,
        texture: TextureAtlasSprite,
        lightmapSkyLight: Int,
        lightmapBlockLight: Int
    ) {
        // A quad consists of 4 vertices so the loop is executed 4 times.
        for (i in 0..3) {
            // Getting the vertex position from a set of predefined positions for a basic quad.
            var quadPos = customQuad[i]

            // Getting the RGBA values from the color. (The color is in ARGB format)
            // To put it another way - unpacking an int representation of a color to a 4-component float vector representation.
            val r = (color and 0xFF0000 shr 16) / 255f
            val g = (color and 0xFF00 shr 8) / 255f
            val b = (color and 0xFF) / 255f
            val a = (color and -0x1000000 shr 24) / 255f

            // Getting the texture UV coordinates from an index. The quad looks like this
            /*0 3
			  1 2*/
            val u = if (i < 2) texture.maxU - UV_CORRECT else texture.minU + UV_CORRECT
            val v = if (i == 1 || i == 2) texture.maxV - UV_CORRECT else texture.minV + UV_CORRECT

            // Uploading the quad data to the buffer.
            buffer.pos(
                quadPos.x + baseOffset.x.toDouble(),
                quadPos.y + baseOffset.y.toDouble(),
                quadPos.z + baseOffset.z.toDouble()
            ).color(r, g, b, a).tex(u, v).lightmap(lightmapSkyLight, lightmapBlockLight).endVertex()
        }
    }

    /**
     * Renders a collection of BakedQuads into the BufferBuilder given. This method allows you to render any model in game in the FastTESR, be it a block model or an item model.
     * Alternatively a custom list of quads may be constructed at runtime to render things like text.
     * Drawbacks: doesn't transform normals as they are not guaranteed to be present in the buffer. Not relevant for a FastTESR but may cause issues with Optifine's shaders.
     *
     * @param quads      the iterable of BakedQuads. This may be any iterable object.
     * @param baseOffset the base position offset for the rendering. This position will not be transformed by the model matrix.
     * @param pipeline   the vertex consumer object. It is a parameter for optimization reasons. It may simply be constructed as new VertexBufferConsumer(buffer) and may be reused indefinately in the scope of the render pass.
     * @param buffer     the buffer to upload vertices to.
     * @param transform  the model matrix that is used to transform quad vertices.
     * @param brightness the brightness of the model. The packed lightmap coordinate system is pretty complex and a lot of parameters are not necessary here so only the dominant one is implemented.
     * @param color      the color of the quad. This is a color multiplier in the ARGB format.
     */
    fun renderQuads(
        quads: Iterable<BakedQuad>,
        baseOffset: Vector3f,
        pipeline: VertexBufferConsumer,
        buffer: BufferBuilder,
        brightness: Float,
        color: Vector4f
    ) {
        // Get the raw int buffer of the buffer builder object.
        val intBuf = getIntBuffer(buffer)

        // Iterate the iterable
        for (quad in quads) {
            // Push the quad to the consumer so it can be uploaded onto the buffer.
            LightUtil.putBakedQuad(pipeline, quad)

            // After the quad has been uploaded the buffer contains enough info to apply the model matrix transformation.
            // Getting the vertex size for the given format.
            val vertexSize = buffer.vertexFormat.integerSize

            // Getting the offset for the current quad.
            val quadOffset: Int = ((ReflectionUtils.getField("vertexCount", buffer) as Int) - 4) * vertexSize

            // Each quad is made out of 4 vertices, so looping 4 times.
            for (k in 0..3) {
                // Getting the offset for the current vertex.
                val vertexIndex = quadOffset + k * vertexSize

                // Grabbing the position vector from the buffer.
                val vertX = java.lang.Float.intBitsToFloat(intBuf[vertexIndex])
                val vertY = java.lang.Float.intBitsToFloat(intBuf[vertexIndex + 1])
                val vertZ = java.lang.Float.intBitsToFloat(intBuf[vertexIndex + 2])
                var vert = Vector4f(vertX, vertY, vertZ, 1f)


                // Uploading the difference back to the buffer. Have to use the helper function since the provided putX methods upload the data for a quad, not a vertex and this data is vertex-dependent.
                putPositionForVertex(
                    buffer,
                    intBuf,
                    vertexIndex,
                    Vector3f(vert.x - vertX, vert.y - vertY, vert.z - vertZ)
                )
            }

            // Uploading the origin position to the buffer. This is an addition operation.
            buffer.pos(baseOffset.x.toDouble(), baseOffset.y.toDouble(), baseOffset.z.toDouble())

            // Constructing the most basic packed lightmap data with a mask of 0x00FF0000.
            val bVal: Int = (brightness * 255).toInt() shl 16

            // Uploading the brightness to the buffer.
            buffer.lightmap(bVal)

            // Uploading the color multiplier to the buffer
            buffer.color(color.x, color.y, color.z, color.w)
        }
    }

    /**
     * A helper method that grabs all BakedQuads of a given model of a given IBlockState and joins them onto a single iterable.
     *
     * @param state the block state object to get the quads from.
     * @return the iterable of BakedQuads.
     */
    fun iterateQuadsOfBlock(state: BlockState): MutableList<BakedQuad> {
        return Arrays.stream(Direction.values()).map { q ->
            Minecraft.getInstance().blockRendererDispatcher.getModelForState(state).getQuads(state, q, Random())
        }.flatMap { obj: Collection<*> -> obj.stream() }.distinct()
            .collect(Collectors.toList()) as MutableList<BakedQuad>
    }

    /**
     * A setter for the vertex-based positions for a given BufferBuilder object.
     *
     * @param buffer the buffer to set the positions in.
     * @param intBuf the raw int buffer.
     * @param offset the offset for the int buffer, in ints.
     * @param pos    the position to add to the buffer.
     */
    fun putPositionForVertex(buffer: BufferBuilder?, intBuf: IntBuffer, offset: Int, pos: Vector3f) {
        // Getting the old position data in the buffer currently.
        val ox = java.lang.Float.intBitsToFloat(intBuf[offset])
        val oy = java.lang.Float.intBitsToFloat(intBuf[offset + 1])
        val oz = java.lang.Float.intBitsToFloat(intBuf[offset + 2])

        // Converting the new data to ints.
        val x = java.lang.Float.floatToIntBits(pos.x + ox)
        val y = java.lang.Float.floatToIntBits(pos.y + oy)
        val z = java.lang.Float.floatToIntBits(pos.z + oz)

        // Putting the data into the buffer
        intBuf.put(offset, x)
        intBuf.put(offset + 1, y)
        intBuf.put(offset + 2, z)
    }

    /**
     * A getter for the rawIntBuffer field value of the BufferBuilder.
     *
     * @param buffer the buffer builder to get the buffer from
     * @return the rawIntbuffer component
     */
    @Nonnull
    fun getIntBuffer(buffer: BufferBuilder?): IntBuffer {
        return try {
            (bufferBuilder_rawIntBuffer.get(buffer) as ByteBuffer).asIntBuffer()
        } catch (exception: IllegalAccessException) {
            // Some other mod messed up and reset the access flag of the field.
            val crashReport = CrashReport("An impossible error has occurred!", exception)
            crashReport.makeCategory("Reflectively Accessing BufferBuilder#rawIntBuffer")
            throw ReportedException(crashReport)
        }
    }
}