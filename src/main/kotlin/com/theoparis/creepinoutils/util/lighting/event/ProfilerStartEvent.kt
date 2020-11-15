package com.theoparis.creepinoutils.util.lighting.event

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event

class ProfilerStartEvent private constructor(val section: String) : Event() {
    override fun isCancelable(): Boolean {
        return false
    }

    companion object {
        fun postNewEvent(section: String) {
            MinecraftForge.EVENT_BUS.post(ProfilerStartEvent(section))
        }
    }
}