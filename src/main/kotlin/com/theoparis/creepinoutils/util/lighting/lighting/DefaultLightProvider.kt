package com.theoparis.creepinoutils.util.lighting.lighting
/**/
import com.theoparis.creepinoutils.util.lighting.event.GatherLightsEvent
import net.minecraft.entity.Entity

class DefaultLightProvider : ILightProvider {
    override fun gatherLights(event: GatherLightsEvent?, context: Entity?) {}
}