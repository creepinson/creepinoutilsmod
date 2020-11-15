package com.theoparis.creepinoutils.util.lighting.lighting

import com.theoparis.creepinoutils.util.lighting.Albedo
import com.theoparis.creepinoutils.util.lighting.ConfigManager
import com.theoparis.creepinoutils.util.lighting.event.GatherLightsEvent
import com.theoparis.creepinoutils.util.lighting.util.ShaderManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.ClippingHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.LazyOptional
import java.util.*
import kotlin.math.min

object LightManager {
    var cameraPos: Vector3d = Vector3d.ZERO
    var camera: ClippingHelper? = null
    var lights = ArrayList<Light>()
    private var distComparator = DistComparator()
    fun uploadLights() {
        val shader: ShaderManager? = ShaderManager.currentShader
        shader?.setUniform("lightCount", lights.size)
        for (i in 0 until min(ConfigManager.maxLights!!.get(), lights.size)) {
            if (i < lights.size) {
                val l = lights[i]
                shader?.setUniform("lights[$i].position", l.x, l.y, l.z)
                shader?.setUniform("lights[$i].color", l.r, l.g, l.b, l.a)
                shader?.setUniform("lights[$i].heading", l.rx, l.ry, l.rz)
                shader?.setUniform("lights[$i].angle", l.angle)
            } else {
                //shader.setUniform("lights[" + i + "].position", 0, 0, 0);
                //shader.setUniform("lights[" + i + "].color", 0, 0, 0, 0);
                //shader.setUniform("lights[" + i + "].heading", 0, 0, 0);
                //shader.setUniform("lights[" + i + "].angle", 0);
            }
        }
    }

    private fun interpolate(entity: Entity, partialTicks: Float): Vector3d {
        return Vector3d(
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks,
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks,
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
        )
    }

    fun update(world: World) {
        val mc = Minecraft.getInstance()
        val cameraEntity = mc.getRenderViewEntity()
        if (cameraEntity != null) {
            cameraPos = interpolate(cameraEntity, mc.renderPartialTicks)
            camera = ClippingHelper(Matrix4f(), Matrix4f())
            camera?.setCameraPosition(cameraPos.x, cameraPos.y, cameraPos.z)
        } else {
            camera = null
            return
        }
        val event = GatherLightsEvent(lights, ConfigManager.maxDistance!!.get().toFloat(), cameraPos, camera)
        MinecraftForge.EVENT_BUS.post(event)
        val maxDist: Int = ConfigManager.maxDistance!!.get()
        for (e in world.getEntitiesWithinAABB(
            Entity::class.java, AxisAlignedBB(
                cameraPos.x - maxDist,
                cameraPos.y - maxDist,
                cameraPos.z - maxDist,
                cameraPos.x + maxDist,
                cameraPos.y + maxDist,
                cameraPos.z + maxDist
            )
        )) {
            if (e is ItemEntity) {
                val provider: LazyOptional<ILightProvider> =
                    e.item.getCapability(Albedo.LIGHT_PROVIDER_CAPABILITY!!)
                provider.ifPresent { p: ILightProvider -> p.gatherLights(event, e) }
            }
            var provider = e.getCapability<ILightProvider>(Albedo.LIGHT_PROVIDER_CAPABILITY!!)
            provider.ifPresent { p: ILightProvider -> p.gatherLights(event, e) }
            for (itemStack in e.heldEquipment) {
                provider = itemStack.getCapability(Albedo.LIGHT_PROVIDER_CAPABILITY!!)
                provider.ifPresent { p: ILightProvider -> p.gatherLights(event, e) }
            }
            for (itemStack in e.armorInventoryList) {
                provider = itemStack.getCapability(Albedo.LIGHT_PROVIDER_CAPABILITY!!)
                provider.ifPresent { p: ILightProvider -> p.gatherLights(event, e) }
            }
        }
        for (t in world.loadedTileEntityList) {
            val provider = t.getCapability<ILightProvider>(Albedo.LIGHT_PROVIDER_CAPABILITY!!)
            provider.ifPresent { p: ILightProvider -> p.gatherLights(event, null) }
        }
        lights.sortWith(distComparator)
    }

    fun clear() {
        lights.clear()
    }

    class DistComparator : Comparator<Light> {
        override fun compare(a: Light, b: Light): Int {
            val dist1: Double = cameraPos.squareDistanceTo(a.x.toDouble(), a.y.toDouble(), a.z.toDouble())
            val dist2: Double = cameraPos.squareDistanceTo(b.x.toDouble(), b.y.toDouble(), b.z.toDouble())
            return dist1.compareTo(dist2)
        }
    }
}