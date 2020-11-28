package com.theoparis.creepinoutils.util.lighting.event

import com.google.common.collect.ImmutableList
import com.theoparis.creepinoutils.util.ReflectionUtils
import com.theoparis.creepinoutils.util.lighting.lighting.Light
import com.theoparis.creepinoutils.util.lighting.lighting.LightManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.ClippingHelper
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.eventbus.api.Event
import java.util.*

class GatherLightsEvent(
    private val lights: ArrayList<Light>,
    val maxDistance: Float,
    val cameraPosition: Vector3d?,
    val camera: ClippingHelper?
) :
    Event() {
    val lightList: ImmutableList<Light>
        get() = ImmutableList.copyOf(lights)

    fun add(light: Light?) {
        if (light == null) return
        val radius = light.radius()
        if (cameraPosition != null) {
            val dist = MathHelper.sqrt(
                cameraPosition.squareDistanceTo(
                    light.pos.x.toDouble(),
                    light.pos.y.toDouble(),
                    light.pos.y.toDouble()
                )
            ).toDouble()
            if (dist > radius + maxDistance) {
                return
            }
        }


        if (camera != null && !camera.isBoundingBoxInFrustum(
                AxisAlignedBB(
                    (light.pos.x - radius).toDouble(),
                    (light.pos.y - radius).toDouble(),
                    (light.pos.z - radius).toDouble(),
                    (light.pos.x + radius).toDouble(),
                    (light.pos.y + radius).toDouble(),
                    (light.pos.z + radius).toDouble()
                )
            )
        ) {
            return
        }
        lights.add(light)
    }

    override fun isCancelable(): Boolean {
        return false
    }
}