package com.theoparis.creepinoutils.util.lighting.util

import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL20

class ShaderManager(shader: ResourceLocation, resourceManager: IResourceManager) {
    private val program: Int = ShaderUtil.loadProgram(
        String.format("%s:shaders/%s.vs", shader.namespace, shader.path),
        String.format("%s:shaders/%s.fs", shader.namespace, shader.path),
        resourceManager
    )

    fun useShader() {
        if (!isCurrentShader(this)) {
            GL20.glUseProgram(program)
            currentProgram = program
            currentShader = this
        }
    }

    fun setUniform(uniform: String, value: Int) {
        if (isCurrentShader(this)) {
            GL20.glUniform1i(GL20.glGetUniformLocation(currentProgram, uniform), value)
        }
    }

    fun setUniform(uniform: String, value: Boolean) {
        if (isCurrentShader(this)) {
            GL20.glUniform1i(GL20.glGetUniformLocation(currentProgram, uniform), if (value) 1 else 0)
        }
    }

    fun setUniform(uniform: String, value: Float) {
        if (isCurrentShader(this)) {
            GL20.glUniform1f(GL20.glGetUniformLocation(currentProgram, uniform), value)
        }
    }

    fun setUniform(uniform: String, v1: Int, v2: Int) {
        if (isCurrentShader(this)) {
            GL20.glUniform2i(GL20.glGetUniformLocation(currentProgram, uniform), v1, v2)
        }
    }

    fun setUniform(uniform: String, v1: Int, v2: Int, v3: Int) {
        if (isCurrentShader(this)) {
            GL20.glUniform3i(GL20.glGetUniformLocation(currentProgram, uniform), v1, v2, v3)
        }
    }

    fun setUniform(uniform: String, v1: Float, v2: Float) {
        if (isCurrentShader(this)) {
            GL20.glUniform2f(GL20.glGetUniformLocation(currentProgram, uniform), v1, v2)
        }
    }

    fun setUniform(uniform: String, v1: Float, v2: Float, v3: Float) {
        if (isCurrentShader(this)) {
            GL20.glUniform3f(GL20.glGetUniformLocation(currentProgram, uniform), v1, v2, v3)
        }
    }

    fun setUniform(uniform: String, v1: Float, v2: Float, v3: Float, v4: Float) {
        if (isCurrentShader(this)) {
            GL20.glUniform4f(GL20.glGetUniformLocation(currentProgram, uniform), v1, v2, v3, v4)
        }
    }

    companion object {
        var currentShader: ShaderManager? = null
            private set
        private var currentProgram = -1
        fun stopShader() {
            if (currentProgram != 0) {
                GL20.glUseProgram(0)
                currentProgram = 0
                currentShader = null
            }
        }

        fun isCurrentShader(shader: ShaderManager?): Boolean {
            return shader != null && currentProgram == shader.program
        }
    }

}