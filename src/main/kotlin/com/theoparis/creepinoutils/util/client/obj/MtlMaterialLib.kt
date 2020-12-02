package com.theoparis.creepinoutils.util.client.obj

import com.theoparis.creepinoutils.CreepinoUtilsMod
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.*
import javax.imageio.ImageIO

class MtlMaterialLib(private val path: String) {
    private val materials: ArrayList<Material>
    private val startPath: String
    fun parse(content: String) {
        val lines = content.split("\n").toTypedArray()
        var current: Material? = null
        for (i in lines.indices) {
            val line = lines[i].trim { it <= ' ' }
            val parts = line.split(" ").toTypedArray()
            if (parts[0] == COMMENT) {
            } else if (parts[0] == NEW_MATERIAL) {
                val material = Material(parts[1])
                materials.add(material)
                current = material
            } else if (parts[0] == AMBIENT_COLOR) {
                current!!.ambientColor = Vector3f(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
            } else if (parts[0] == DIFFUSE_COLOR) {
                current!!.diffuseColor = Vector3f(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat())
            } else if (parts[0] == TEXTURE_DIFFUSE) {
                current!!.diffuseTexture = loadTexture(startPath + parts[1])
            } else if (parts[0] == TEXTURE_AMBIENT) {
                current!!.ambientTexture = loadTexture(startPath + parts[1])
            } else if (parts[0] == TRANSPARENCY_D || parts[0] == TRANSPARENCY_TR) {
                current!!.transparency = parts[1].toDouble().toFloat()
            }
        }
    }

    private fun loadTexture(string: String): Int {
        var string = string
        try {
            string =
                string.replace("models/", "textures/") // Search for textures in textures directory instead of models.
            val resourceLocation = ResourceLocation(string)
            val inputStream: InputStream =
                Minecraft.getInstance().resourceManager.getResource(resourceLocation).inputStream
            return Companion.loadTexture(ImageIO.read(inputStream))
        } catch (e: IOException) {
            CreepinoUtilsMod.logger.warn("", "Unable to load a texture from the obj material file.")
            e.printStackTrace()
        }
        return 0
    }

    fun getMaterials(): List<Material> {
        return materials
    }

    companion object {
        const val COMMENT = "#"
        const val NEW_MATERIAL = "newmtl"
        const val AMBIENT_COLOR = "Ka"
        const val DIFFUSE_COLOR = "Kd"
        const val SPECULAR_COLOR = "Ks"
        const val TRANSPARENCY_D = "d"
        const val TRANSPARENCY_TR = "Tr"
        const val ILLUMINATION = "illum"
        const val TEXTURE_AMBIENT = "map_Ka"
        const val TEXTURE_DIFFUSE = "map_Kd"
        const val TEXTURE_SPECULAR = "map_Ks"
        const val TEXTURE_TRANSPARENCY = "map_d"

        fun imageToByteBuffer(img: BufferedImage): ByteBuffer {
            val pixels: IntArray = img.getRGB(0, 0, img.width, img.height, null, 0, img.width)
            val bufLen = pixels.size * 4
            val oglPixelBuf: ByteBuffer = BufferUtils.createByteBuffer(bufLen)
            for (y in 0 until img.height) {
                for (x in 0 until img.width) {
                    val rgb = pixels[y * img.width + x]
                    val a = (rgb shr 24 and 0xFF) / 255f
                    val r = (rgb shr 16 and 0xFF) / 255f
                    val g = (rgb shr 8 and 0xFF) / 255f
                    val b = (rgb shr 0 and 0xFF) / 255f
                    oglPixelBuf.put((r * 255f).toInt().toByte())
                    oglPixelBuf.put((g * 255f).toInt().toByte())
                    oglPixelBuf.put((b * 255f).toInt().toByte())
                    oglPixelBuf.put((a * 255f).toInt().toByte())
                }
            }
            oglPixelBuf.flip()
            return oglPixelBuf
        }

        fun loadTexture(img: BufferedImage): Int {
            val oglPixelBuf = imageToByteBuffer(img)
            val id: Int = GL11.glGenTextures()
            val target: Int = GL11.GL_TEXTURE_2D
            GL11.glBindTexture(target, id)
            GL11.glTexParameterf(target, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST.toFloat())
            GL11.glTexParameterf(target, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST.toFloat())
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE.toFloat())
            GL11.glTexParameteri(target, GL12.GL_TEXTURE_BASE_LEVEL, 0)
            GL11.glTexParameteri(target, GL12.GL_TEXTURE_MAX_LEVEL, 0)
            GL11.glTexImage2D(
                target,
                0,
                GL11.GL_RGBA8,
                img.width,
                img.height,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                oglPixelBuf
            )
            GL11.glBindTexture(target, 0)
            return id
        }
    }

    init {
        startPath = path.substring(0, path.lastIndexOf('/') + 1)
        materials = ArrayList()
    }
}