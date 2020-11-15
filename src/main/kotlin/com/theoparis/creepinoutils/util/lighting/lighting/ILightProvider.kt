package com.theoparis.creepinoutils.util.lighting.lighting

import com.theoparis.creepinoutils.util.lighting.event.GatherLightsEvent
import net.minecraft.entity.Entity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

interface ILightProvider {
    @OnlyIn(Dist.CLIENT)
    fun gatherLights(event: GatherLightsEvent?, context: Entity?)
}