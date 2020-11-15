package com.theoparis.creepinoutils.util.lighting.event

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.ChunkRender
import net.minecraftforge.eventbus.api.Event

class RenderChunkUniformsEvent(val chunk: ChunkRender) : Event() {
    override fun isCancelable(): Boolean {
        return false
    }
}