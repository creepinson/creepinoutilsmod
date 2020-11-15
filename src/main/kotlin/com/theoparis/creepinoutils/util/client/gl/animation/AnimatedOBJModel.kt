package com.theoparis.creepinoutils.util.client.gl.animation

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.theoparis.creepinoutils.util.TensorUtils.fromVector4
import com.theoparis.creepinoutils.util.TensorUtils.toVector
import com.theoparis.creepinoutils.util.TensorUtils.toVector4
import dev.throwouterror.util.math.Tensor
import joptsimple.internal.Strings
import net.minecraft.client.renderer.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.VertexFormatElement
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.TransformationMatrix
import net.minecraft.util.math.vector.Vector2f
import net.minecraft.util.math.vector.Vector4f
import net.minecraftforge.client.model.IModelBuilder
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.client.model.geometry.IModelGeometryPart
import net.minecraftforge.client.model.geometry.IMultipartModelGeometry
import net.minecraftforge.client.model.obj.LineReader
import net.minecraftforge.client.model.obj.MaterialLibrary
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder
import net.minecraftforge.client.model.pipeline.IVertexConsumer
import org.apache.commons.lang3.tuple.Pair
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import javax.annotation.Nonnull

class AnimatedOBJModel(reader: LineReader, settings: ModelSettings) : IMultipartModelGeometry<AnimatedOBJModel?> {
    private val parts: MutableMap<String, ModelGroup> = Maps.newHashMap()
    private val positions: MutableList<Tensor> = Lists.newArrayList()
    private val texCoords: MutableList<Vector2f> = Lists.newArrayList()
    private val normals: MutableList<Tensor> = Lists.newArrayList()
    private val colors: MutableList<Tensor> = Lists.newArrayList()
    val detectCullableFaces: Boolean
    val diffuseLighting: Boolean
    val flipV: Boolean
    val ambientToFullbright: Boolean
    val modelLocation: ResourceLocation
    val materialLibraryOverrideLocation: String?
    override fun getParts(): Collection<IModelGeometryPart> {
        return parts.values
    }

    override fun getPart(name: String): Optional<out IModelGeometryPart> {
        return Optional.ofNullable(parts[name])
    }

    private fun makeQuad(
        indices: Array<IntArray?>,
        tintIndex: Int,
        colorTint: Tensor,
        ambientColor: Vector4f,
        texture: TextureAtlasSprite,
        transform: TransformationMatrix
    ): Pair<BakedQuad, Direction?> {
        var needsNormalRecalculation = false
        for (ints in indices) {
            needsNormalRecalculation = needsNormalRecalculation or (ints!!.size < 3)
        }
        var faceNormal: Tensor? = Tensor(0, 0, 0)
        if (needsNormalRecalculation) {
            val a = positions[indices[0]!![0]]
            val ab = positions[indices[1]!![0]]
            val ac = positions[indices[2]!![0]]
            var abs = ab.clone()
            abs.sub(a)
            val acs = ac.clone()
            acs.sub(a)
            abs = abs.cross(acs)
            abs.normalize()
            faceNormal = abs
        }
        val pos = arrayOfNulls<Tensor>(4)
        val norm = arrayOfNulls<Tensor>(4)
        val builder = BakedQuadBuilder(texture)
        builder.setQuadTint(tintIndex)
        var uv2 = Vector2f(0f, 0f)
        if (ambientToFullbright) {
            val fakeLight = ((ambientColor.x + ambientColor.y + ambientColor.z) * 15 / 3.0f).toInt()
            uv2 = Vector2f((fakeLight shl 4) / 32767.0f, (fakeLight shl 4) / 32767.0f)
            builder.setApplyDiffuseLighting(fakeLight == 0)
        } else {
            builder.setApplyDiffuseLighting(diffuseLighting)
        }
        val hasTransform = !transform.isIdentity
        // The incoming transform is referenced on the center of the block, but our coords are referenced on the corner
        val transformation = if (hasTransform) transform.blockCenterToCorner() else transform
        for (i in 0..3) {
            val index = indices[Math.min(i, indices.size - 1)]
            val pos0 = positions[index!![0]]
            val position = pos0.clone()
            val texCoord = if (index.size >= 2 && texCoords.size > 0) texCoords[index[1]] else DEFAULT_COORDS[i]
            val norm0 =
                if (!needsNormalRecalculation && index.size >= 3 && normals.size > 0) normals[index[2]] else faceNormal!!
            var normal = norm0
            val color = if (index.size >= 4 && colors.size > 0) colors[index[3]] else COLOR_WHITE
            if (hasTransform) {
                normal = norm0.clone()
                transformation.transformPosition(toVector4(position))
                transformation.transformNormal(toVector(normal))
            }
            val tintedColor = Tensor(
                color.x() * colorTint.x(),
                color.y() * colorTint.y(),
                color.z() * colorTint.z(),
                color.w() * colorTint.w()
            )
            putVertexData(builder, position, texCoord, normal, tintedColor, uv2, texture)
            pos[i] = position
            norm[i] = normal
        }
        builder.setQuadOrientation(
            Direction.getFacingFromVector(
                norm[0]!!.x(), norm[0]!!.y(), norm[0]!!.z()
            )
        )
        var cull: Direction? = null
        if (detectCullableFaces) {
            if (MathHelper.epsilonEquals(pos[0]!!.x(), 0.0) &&  // vertex.position.x
                MathHelper.epsilonEquals(pos[1]!!.x(), 0.0) &&
                MathHelper.epsilonEquals(pos[2]!!.x(), 0.0) &&
                MathHelper.epsilonEquals(pos[3]!!.x(), 0.0) && norm[0]!!.x() < 0
            ) // vertex.normal.x
            {
                cull = Direction.WEST
            } else if (MathHelper.epsilonEquals(pos[0]!!.x(), 1.0) &&  // vertex.position.x
                MathHelper.epsilonEquals(pos[1]!!.x(), 1.0) &&
                MathHelper.epsilonEquals(pos[2]!!.x(), 1.0) &&
                MathHelper.epsilonEquals(pos[3]!!.x(), 1.0) && norm[0]!!.x() > 0
            ) // vertex.normal.x
            {
                cull = Direction.EAST
            } else if (MathHelper.epsilonEquals(pos[0]!!.z(), 0.0) &&  // vertex.position.z
                MathHelper.epsilonEquals(pos[1]!!.z(), 0.0) &&
                MathHelper.epsilonEquals(pos[2]!!.z(), 0.0) &&
                MathHelper.epsilonEquals(pos[3]!!.z(), 0.0) && norm[0]!!.z() < 0
            ) // vertex.normal.z
            {
                cull = Direction.NORTH // can never remember
            } else if (MathHelper.epsilonEquals(pos[0]!!.z(), 1.0) &&  // vertex.position.z
                MathHelper.epsilonEquals(pos[1]!!.z(), 1.0) &&
                MathHelper.epsilonEquals(pos[2]!!.z(), 1.0) &&
                MathHelper.epsilonEquals(pos[3]!!.z(), 1.0) && norm[0]!!.z() > 0
            ) // vertex.normal.z
            {
                cull = Direction.SOUTH
            } else if (MathHelper.epsilonEquals(pos[0]!!.y(), 0.0) &&  // vertex.position.y
                MathHelper.epsilonEquals(pos[1]!!.y(), 0.0) &&
                MathHelper.epsilonEquals(pos[2]!!.y(), 0.0) &&
                MathHelper.epsilonEquals(pos[3]!!.y(), 0.0) && norm[0]!!.y() < 0
            ) // vertex.normal.z
            {
                cull = Direction.DOWN // can never remember
            } else if (MathHelper.epsilonEquals(pos[0]!!.y(), 1.0) &&  // vertex.position.y
                MathHelper.epsilonEquals(pos[1]!!.y(), 1.0) &&
                MathHelper.epsilonEquals(pos[2]!!.y(), 1.0) &&
                MathHelper.epsilonEquals(pos[3]!!.y(), 1.0) && norm[0]!!.y() > 0
            ) // vertex.normal.y
            {
                cull = Direction.UP
            }
        }
        return Pair.of(builder.build(), cull)
    }

    private fun putVertexData(
        consumer: IVertexConsumer,
        position0: Tensor,
        texCoord0: Vector2f,
        normal0: Tensor,
        color0: Tensor,
        uv2: Vector2f,
        texture: TextureAtlasSprite
    ) {
        val elements = consumer.vertexFormat.elements
        for (j in elements.indices) {
            val e = elements[j]
            when (e.usage) {
                VertexFormatElement.Usage.POSITION -> consumer.put(
                    j,
                    position0.floatX(),
                    position0.floatY(),
                    position0.floatZ(),
                    position0.floatW()
                )
                VertexFormatElement.Usage.COLOR -> consumer.put(
                    j,
                    color0.floatX(),
                    color0.floatY(),
                    color0.floatZ(),
                    color0.floatW()
                )
                VertexFormatElement.Usage.UV -> when (e.index) {
                    0 -> consumer.put(
                        j,
                        texture.getInterpolatedU(texCoord0.x * 16.toDouble()),
                        texture.getInterpolatedV((if (flipV) 1 - texCoord0.y else texCoord0.y) * 16.toDouble())
                    )
                    2 -> consumer.put(j, uv2.x, uv2.y)
                    else -> consumer.put(j)
                }
                VertexFormatElement.Usage.NORMAL -> consumer.put(
                    j,
                    normal0.floatX(),
                    normal0.floatY(),
                    normal0.floatZ()
                )
                else -> consumer.put(j)
            }
        }
    }

    open inner class ModelObject internal constructor(val name: String) : IModelGeometryPart {
        var meshes: MutableList<ModelMesh?> = Lists.newArrayList()
        override fun name(): String {
            return name
        }

        override fun addQuads(
            owner: IModelConfiguration,
            modelBuilder: IModelBuilder<*>,
            bakery: ModelBakery,
            spriteGetter: Function<RenderMaterial, TextureAtlasSprite>,
            modelTransform: IModelTransform,
            modelLocation: ResourceLocation
        ) {
            for (mesh in meshes) {
                val mat = mesh!!.mat ?: continue
                val texture = spriteGetter.apply(ModelLoaderRegistry.resolveTexture(mat.diffuseColorMap, owner))
                val tintIndex = mat.diffuseTintIndex
                val colorTint = fromVector4(mat.diffuseColor)
                for (face in mesh.faces) {
                    val quad = makeQuad(face, tintIndex, colorTint, mat.ambientColor, texture, modelTransform.rotation)
                    if (quad.right == null) modelBuilder.addGeneralQuad(quad.left) else modelBuilder.addFaceQuad(
                        quad.right,
                        quad.left
                    )
                }
            }
        }

        override fun getTextures(
            owner: IModelConfiguration,
            modelGetter: Function<ResourceLocation, IUnbakedModel>,
            missingTextureErrors: Set<com.mojang.datafixers.util.Pair<String, String>>
        ): Collection<RenderMaterial> {
            return meshes.stream().map { mesh: ModelMesh? ->
                ModelLoaderRegistry.resolveTexture(
                    Objects.requireNonNull(mesh!!.mat)!!.diffuseColorMap, owner
                )
            }.collect(Collectors.toSet())
        }
    }

    inner class ModelGroup internal constructor(name: String) : ModelObject(name) {
        val parts: MutableMap<String, ModelObject?> = Maps.newHashMap()
        fun getParts(): Collection<IModelGeometryPart?> {
            return parts.values
        }

        override fun addQuads(
            owner: IModelConfiguration,
            modelBuilder: IModelBuilder<*>,
            bakery: ModelBakery,
            spriteGetter: Function<RenderMaterial, TextureAtlasSprite>,
            modelTransform: IModelTransform,
            modelLocation: ResourceLocation
        ) {
            super.addQuads(owner, modelBuilder, bakery, spriteGetter, modelTransform, modelLocation)
            getParts().stream().filter { part: IModelGeometryPart? -> owner.getPartVisibility(part) }
                .forEach { part: IModelGeometryPart? ->
                    part!!.addQuads(
                        owner,
                        modelBuilder,
                        bakery,
                        spriteGetter,
                        modelTransform,
                        modelLocation
                    )
                }
        }

        override fun getTextures(
            owner: IModelConfiguration,
            modelGetter: Function<ResourceLocation, IUnbakedModel>,
            missingTextureErrors: Set<com.mojang.datafixers.util.Pair<String, String>>
        ): Collection<RenderMaterial> {
            val combined: MutableSet<RenderMaterial> = Sets.newHashSet()
            combined.addAll(super.getTextures(owner, modelGetter, missingTextureErrors))
            for (part in getParts()) combined.addAll(part!!.getTextures(owner, modelGetter, missingTextureErrors))
            return combined
        }
    }

    inner class ModelMesh(var mat: MaterialLibrary.Material?, var smoothingGroup: String?) {
        val faces: MutableList<Array<IntArray?>> = Lists.newArrayList()
    }

    class ModelSettings(
        @field:Nonnull @param:Nonnull val modelLocation: ResourceLocation,
        val detectCullableFaces: Boolean,
        val diffuseLighting: Boolean,
        val flipV: Boolean,
        val ambientToFullbright: Boolean,
        val materialLibraryOverrideLocation: String?
    ) {
        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val that = o as ModelSettings
            return equals(that)
        }

        fun equals(@Nonnull that: ModelSettings): Boolean {
            return detectCullableFaces == that.detectCullableFaces && diffuseLighting == that.diffuseLighting && flipV == that.flipV && ambientToFullbright == that.ambientToFullbright && modelLocation == that.modelLocation &&
                    materialLibraryOverrideLocation == that.materialLibraryOverrideLocation
        }

        override fun hashCode(): Int {
            return Objects.hash(
                modelLocation,
                detectCullableFaces,
                diffuseLighting,
                flipV,
                ambientToFullbright,
                materialLibraryOverrideLocation
            )
        }
    }

    companion object {
        private val COLOR_WHITE = Tensor(1, 1, 1, 1)
        private val DEFAULT_COORDS = arrayOf(
            Vector2f(0f, 0f),
            Vector2f(0f, 1f),
            Vector2f(1f, 1f),
            Vector2f(1f, 0f)
        )

        fun parseVector4To3(line: Array<String>): Tensor {
            return when (line.size) {
                1 -> Tensor(0, 0, 0)
                2 -> Tensor(line[1].toFloat(), 0f, 0f)
                3 -> Tensor(line[1].toFloat(), line[2].toFloat(), 0f)
                4 -> Tensor(line[1].toFloat(), line[2].toFloat(), line[3].toFloat())
                else -> {
                    val vec4 = parseVector4(line)
                    Tensor(
                        vec4.x() / vec4.w(),
                        vec4.y() / vec4.w(),
                        vec4.z() / vec4.w()
                    )
                }
            }
        }

        fun parseVector2(line: Array<String>): Vector2f {
            return when (line.size) {
                1 -> Vector2f(0f, 0f)
                2 -> Vector2f(line[1].toFloat(), 0f)
                else -> Vector2f(line[1].toFloat(), line[2].toFloat())
            }
        }

        fun parseVector3(line: Array<String>): Tensor {
            return when (line.size) {
                1 -> Tensor(0, 0, 0)
                2 -> Tensor(line[1].toFloat(), 0f, 0f)
                3 -> Tensor(line[1].toFloat(), line[2].toFloat(), 0f)
                else -> Tensor(line[1].toFloat(), line[2].toFloat(), line[3].toFloat())
            }
        }

        fun parseVector4(line: Array<String>): Tensor {
            return when (line.size) {
                1 -> Tensor(0, 0, 0, 1)
                2 -> Tensor(line[1].toFloat(), 0f, 0f, 1f)
                3 -> Tensor(line[1].toFloat(), line[2].toFloat(), 0f, 1f)
                4 -> Tensor(line[1].toFloat(), line[2].toFloat(), line[3].toFloat(), 1f)
                else -> Tensor(line[1].toFloat(), line[2].toFloat(), line[3].toFloat(), line[4].toFloat())
            }
        }
    }

    init {
        modelLocation = settings.modelLocation
        detectCullableFaces = settings.detectCullableFaces
        diffuseLighting = settings.diffuseLighting
        flipV = settings.flipV
        ambientToFullbright = settings.ambientToFullbright
        materialLibraryOverrideLocation = settings.materialLibraryOverrideLocation

        // for relative references to material libraries
        val modelDomain = modelLocation.namespace
        var modelPath = modelLocation.path
        val lastSlash = modelPath.lastIndexOf('/')
        modelPath = if (lastSlash >= 0) modelPath.substring(0, lastSlash + 1) // include the '/'
        else ""
        var mtllib = MaterialLibrary.EMPTY
        var currentMat: MaterialLibrary.Material? = null
        var currentSmoothingGroup: String? = null
        var currentGroup: ModelGroup? = null
        var currentObject: ModelObject? = null
        var currentMesh: ModelMesh? = null
        var objAboveGroup = false
        if (materialLibraryOverrideLocation != null) {
            val lib: String = materialLibraryOverrideLocation
            mtllib =
                if (lib.contains(":")) AnimatedOBJLoader.INSTANCE.loadMaterialLibrary(ResourceLocation(lib)) else AnimatedOBJLoader.INSTANCE.loadMaterialLibrary(
                    ResourceLocation(modelDomain, modelPath + lib)
                )
        }
        var line: Array<String>
        while (reader.readAndSplitLine(true).also { line = it!! } != null) {
            when (line[0]) {
                "mtllib" -> {
                    if (materialLibraryOverrideLocation != null) break
                    val lib = line[1]
                    mtllib =
                        if (lib.contains(":")) AnimatedOBJLoader.INSTANCE.loadMaterialLibrary(ResourceLocation(lib)) else AnimatedOBJLoader.INSTANCE.loadMaterialLibrary(
                            ResourceLocation(modelDomain, modelPath + lib)
                        )
                }
                "usemtl" -> {
                    val mat = Strings.join(line.copyOfRange(1, line.size), " ")
                    val newMat = mtllib.getMaterial(mat)
                    if (newMat != currentMat) {
                        currentMat = newMat
                        if (currentMesh != null && currentMesh.mat == null && currentMesh.faces.size == 0) {
                            currentMesh.mat = currentMat
                        } else {
                            // Start new mesh
                            currentMesh = null
                        }
                    }
                }
                "v" -> positions.add(parseVector4To3(line))
                "vt" -> texCoords.add(parseVector2(line))
                "vn" -> normals.add(parseVector3(line))
                "vc" -> colors.add(parseVector4(line))
                "f" -> {
                    if (currentMesh == null) {
                        currentMesh = ModelMesh(currentMat, currentSmoothingGroup)
                        if (currentObject != null) {
                            currentObject.meshes.add(currentMesh)
                        } else {
                            if (currentGroup == null) {
                                currentGroup = ModelGroup("")
                                parts[""] = currentGroup
                            }
                            currentGroup.meshes.add(currentMesh)
                        }
                    }
                    val vertices = arrayOfNulls<IntArray>(line.size - 1)
                    var i = 0
                    while (i < vertices.size) {
                        val vertexData = line[i + 1]
                        val vertexParts = vertexData.split("/").toTypedArray()
                        val vertex = Arrays.stream(vertexParts)
                            .mapToInt { num: String -> if (Strings.isNullOrEmpty(num)) 0 else num.toInt() }
                            .toArray()
                        if (vertex[0] < 0) vertex[0] = positions.size + vertex[0] else vertex[0]--
                        if (vertex.size > 1) {
                            if (vertex[1] < 0) vertex[1] = texCoords.size + vertex[1] else vertex[1]--
                            if (vertex.size > 2) {
                                if (vertex[2] < 0) vertex[2] = normals.size + vertex[2] else vertex[2]--
                                if (vertex.size > 3) {
                                    if (vertex[3] < 0) vertex[3] = colors.size + vertex[3] else vertex[3]--
                                }
                            }
                        }
                        vertices[i] = vertex
                        i++
                    }
                    currentMesh.faces.add(vertices)
                }
                "s" -> {
                    val smoothingGroup = if ("off" == line[1]) null else line[1]
                    if (currentSmoothingGroup != smoothingGroup) {
                        currentSmoothingGroup = smoothingGroup
                        if (currentMesh != null && currentMesh.smoothingGroup == null && currentMesh.faces.size == 0) {
                            currentMesh.smoothingGroup = currentSmoothingGroup
                        } else {
                            // Start new mesh
                            currentMesh = null
                        }
                    }
                }
                "g" -> {
                    val name = line[1]
                    if (objAboveGroup) {
                        currentObject = ModelObject(
                            currentGroup!!.name() + "/" + name
                        )
                        currentGroup.parts[name] = currentObject
                    } else {
                        currentGroup = ModelGroup(name)
                        parts[name] = currentGroup
                        currentObject = null
                    }
                    // Start new mesh
                    currentMesh = null
                }
                "o" -> {
                    val name = line[1]
                    if (objAboveGroup || currentGroup == null) {
                        objAboveGroup = true
                        currentGroup = ModelGroup(name)
                        parts[name] = currentGroup
                        currentObject = null
                    } else {
                        currentObject = ModelObject(currentGroup.name() + "/" + name)
                        currentGroup.parts[name] = currentObject
                    }
                    // Start new mesh
                    currentMesh = null
                }
            }
        }
    }
}