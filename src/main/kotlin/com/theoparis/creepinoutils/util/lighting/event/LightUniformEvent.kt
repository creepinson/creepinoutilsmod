package com.theoparis.creepinoutils.util.lighting.event

import net.minecraftforge.eventbus.api.Event

class LightUniformEvent : Event() {
    override fun isCancelable(): Boolean {
        return false
    }
}