package com.theoparis.creepinoutils.util.lighting.util

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.resource.IResourceType
import net.minecraftforge.resource.ISelectiveResourceReloadListener
import net.minecraftforge.resource.VanillaResourceType
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.function.Predicate
import java.util.stream.Collectors

class ShaderUtil : ISelectiveResourceReloadListener {
    override fun onResourceManagerReload(
        resourceManager: IResourceManager,
        resourcePredicate: Predicate<IResourceType>
    ) {
        if (resourcePredicate.test(VanillaResourceType.SHADERS)) {
            init(resourceManager)
        }
    }

    companion object {
        //public static int currentProgram = -1;
        var fastLightProgram: ShaderManager? = null
        var entityLightProgram: ShaderManager? = null
        var depthProgram: ShaderManager? = null
        fun init(manager: IResourceManager?) {
            fastLightProgram = manager?.let { ShaderManager(ResourceLocation("albedo:fastlight"), it) }
            entityLightProgram = manager?.let { ShaderManager(ResourceLocation("albedo:entitylight"), it) }
            depthProgram = manager?.let { ShaderManager(ResourceLocation("albedo:depth"), it) }
        }

        fun loadProgram(vsh: String, fsh: String, manager: IResourceManager): Int {
            val vertexShader = createShader(vsh, GL20.GL_VERTEX_SHADER, manager)
            val fragmentShader = createShader(fsh, GL20.GL_FRAGMENT_SHADER, manager)
            val program = GlStateManager.createProgram()
            GlStateManager.attachShader(program, vertexShader)
            GlStateManager.attachShader(program, fragmentShader)
            GlStateManager.linkProgram(program)
            val s = GL20.glGetProgramInfoLog(program)
            println("GL LOG: $s")
            return program
        }

        fun createShader(filename: String, shaderType: Int, manager: IResourceManager): Int {
            val shader = GlStateManager.createShader(shaderType)
            if (shader == 0) return 0
            try {
                val s = readFileAsString(filename, manager)
                GlStateManager.shaderSource(shader, s)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            GlStateManager.compileShader(shader)
            if (GL20.glGetShaderi(
                    shader,
                    GL20.GL_COMPILE_STATUS
                ) == GL11.GL_FALSE
            ) throw RuntimeException("Error creating shader: " + getLogInfo(shader))
            return shader
        }

        fun getLogInfo(obj: Int): String {
            return ARBShaderObjects.glGetInfoLogARB(
                obj,
                ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)
            )
        }

        @Throws(Exception::class)
        fun readFileAsString(filename: String, manager: IResourceManager): String {
            println("Loading shader [$filename]...")
            var `in`: InputStream? = null
            try {
                val resource = manager.getResource(ResourceLocation(filename))
                `in` = resource.inputStream
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            var s = ""
            if (`in` != null) {
                BufferedReader(InputStreamReader(`in`, StandardCharsets.UTF_8)).use { reader ->
                    s = reader.lines().collect(Collectors.joining("\n"))
                }
            }
            return s
        }
    }
}