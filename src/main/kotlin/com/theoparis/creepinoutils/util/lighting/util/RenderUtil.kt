package com.theoparis.creepinoutils.util.lighting.util

import com.theoparis.creepinoutils.util.lighting.ConfigManager.Companion.isLightingEnabled
import com.theoparis.creepinoutils.util.lighting.EventManager.Companion.isGui
import com.theoparis.creepinoutils.util.lighting.event.RenderChunkUniformsEvent
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.ChunkRender
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType
import net.minecraftforge.common.MinecraftForge

object RenderUtil {
    var lightingEnabled = false
    var previousShader: ShaderManager? = null
    var enabledLast = false
    var itemTransformType = TransformType.NONE
    fun renderChunkUniforms(c: ChunkRender?) {
        MinecraftForge.EVENT_BUS.post(RenderChunkUniformsEvent(c!!))
    }

    fun enableLightingUniforms() {
        if (!isGui && isLightingEnabled) {
            if (enabledLast) {
                previousShader?.useShader()
                enabledLast = false
            }
            if (ShaderManager.isCurrentShader(ShaderUtil.entityLightProgram)) {
                ShaderUtil.entityLightProgram?.setUniform(
                    "lightingEnabled",
                    true
                )
            }
        }
    }

    fun disableLightingUniforms() {
        if (!isGui && isLightingEnabled) {
            if (ShaderManager.isCurrentShader(ShaderUtil.entityLightProgram)) {
                ShaderUtil.entityLightProgram?.setUniform(
                    "lightingEnabled",
                    false
                )
            }
            if (!enabledLast) {
                previousShader = ShaderManager.currentShader
                enabledLast = true
                ShaderManager.stopShader()
            }
        }
    }

    fun setTransform(t: TransformType) {
        itemTransformType = t
    }
}