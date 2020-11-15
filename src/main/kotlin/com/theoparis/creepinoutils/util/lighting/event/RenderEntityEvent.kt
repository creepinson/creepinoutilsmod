package com.theoparis.creepinoutils.util.lighting.event

import net.minecraft.entity.Entity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event

class RenderEntityEvent private constructor(val entity: Entity) : Event() {
    override fun isCancelable(): Boolean {
        return false
    }

    companion object {
        fun postNewEvent(e: Entity) {
            MinecraftForge.EVENT_BUS.post(RenderEntityEvent(e))
        }
    }
}