package com.theoparis.creepinoutils.util.lighting.event

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event

class RenderTileEntityEvent private constructor(val entity: TileEntity) : Event() {
    override fun isCancelable(): Boolean {
        return false
    }

    companion object {
        fun postNewEvent(e: TileEntity) {
            MinecraftForge.EVENT_BUS.post(RenderTileEntityEvent(e))
        }
    }
}