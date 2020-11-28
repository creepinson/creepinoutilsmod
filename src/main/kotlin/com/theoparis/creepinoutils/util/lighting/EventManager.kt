package com.theoparis.creepinoutils.util.lighting

import com.mojang.blaze3d.platform.GlStateManager
import com.theoparis.creepinoutils.ConfigManager
import com.theoparis.creepinoutils.util.TriConsumer
import com.theoparis.creepinoutils.util.lighting.event.*
import com.theoparis.creepinoutils.util.lighting.lighting.ILightProvider
import com.theoparis.creepinoutils.util.lighting.lighting.Light
import com.theoparis.creepinoutils.util.lighting.lighting.LightManager
import com.theoparis.creepinoutils.util.lighting.lighting.LightManager.clear
import com.theoparis.creepinoutils.util.lighting.lighting.LightManager.update
import com.theoparis.creepinoutils.util.lighting.lighting.LightManager.uploadLights
import com.theoparis.creepinoutils.util.lighting.util.ShaderManager
import com.theoparis.creepinoutils.util.lighting.util.ShaderUtil
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.ClippingHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.LightningBoltEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.EndGatewayTileEntity
import net.minecraft.tileentity.EndPortalTileEntity
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.DimensionType
import net.minecraft.world.IWorldReader
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.util.*
import javax.annotation.Nonnull

class EventManager {
    var ticks = 0
    var postedLights = false
    var precedesEntities = true
    var section = ""
    var thread: Thread? = null
    fun startThread() {
        thread = Thread {
            while (!thread!!.isInterrupted) {
                if (Minecraft.getInstance().player != null) {
                    val player: PlayerEntity? = Minecraft.getInstance().player
                    if (Minecraft.getInstance().world != null) {
                        val reader: IWorldReader? = Minecraft.getInstance().world
                        val playerPos = player!!.position
                        val maxDistance = ConfigManager.maxDistance
                        val r = maxDistance / 2
                        val posIterable: MutableIterable<BlockPos> =
                            BlockPos.getAllInBoxMutable(playerPos.add(-r, -r, -r), playerPos.add(r, r, r))
                        for (pos in posIterable) {
                            val cameraPosition: Vector3d? = LightManager.cameraPos
                            val camera: ClippingHelper? = LightManager.camera
                            val state: BlockState = reader!!.getBlockState(pos)
                            val lights = ArrayList<Light>()
                            val lightsEvent = GatherLightsEvent(lights, maxDistance.toFloat(), cameraPosition, camera)
                            val consumer: TriConsumer<BlockPos, BlockState, GatherLightsEvent>? =
                                Albedo.MAP[state.block]
                            consumer?.apply(pos, state, lightsEvent)
                            if (lights.isEmpty()) {
                                EXISTING.remove(pos)
                            } else {
                                EXISTING[pos.toImmutable()] = lights
                            }
                        }
                    }
                }
            }
        }
        thread!!.start()
    }

    @SubscribeEvent
    fun onProfilerChange(event: ProfilerStartEvent) {
        section = event.section
        if (ConfigManager.isLightingEnabled) {
            if (event.section.compareTo("terrain") == 0) {
                isGui = false
                precedesEntities = true
                ShaderUtil.fastLightProgram?.useShader()
                ShaderUtil.fastLightProgram?.setUniform("ticks", ticks + Minecraft.getInstance().renderPartialTicks)
                ShaderUtil.fastLightProgram?.setUniform("sampler", 0)
                ShaderUtil.fastLightProgram?.setUniform("lightmap", 1)
                ShaderUtil.fastLightProgram?.setUniform(
                    "playerPos",
                    Vector3f(
                        Minecraft.getInstance().player!!.posX.toFloat(),
                        Minecraft.getInstance().player!!.posY.toFloat(),
                        Minecraft.getInstance().player!!.posZ.toFloat()
                    )
                )
                if (!postedLights) {
                    if (thread == null || !thread!!.isAlive) {
                        startThread()
                    }
                    EXISTING.forEach { (_: BlockPos?, lights: List<Light>?) ->
                        LightManager.lights.addAll(
                            lights
                        )
                    }
                    update(Minecraft.getInstance().world!!)
                    ShaderManager.stopShader()
                    MinecraftForge.EVENT_BUS.post(LightUniformEvent())
                    ShaderUtil.fastLightProgram?.useShader()
                    uploadLights()
                    ShaderUtil.entityLightProgram?.useShader()
                    ShaderUtil.entityLightProgram?.setUniform(
                        "ticks",
                        ticks + Minecraft.getInstance().renderPartialTicks
                    )
                    ShaderUtil.entityLightProgram?.setUniform("sampler", 0)
                    ShaderUtil.entityLightProgram?.setUniform("lightmap", 1)
                    uploadLights()
                    ShaderUtil.entityLightProgram?.setUniform(
                        "playerPos",
                        Vector3f(
                            Minecraft.getInstance().player!!.posX.toFloat(),
                            Minecraft.getInstance().player!!.posY.toFloat(),
                            Minecraft.getInstance().player!!.posZ.toFloat()
                        )
                    )
                    ShaderUtil.entityLightProgram?.setUniform("lightingEnabled", GL11.glIsEnabled(GL11.GL_LIGHTING))
                    ShaderUtil.fastLightProgram?.useShader()
                    postedLights = true
                    clear()
                }
            }
            if (event.section.compareTo("sky") == 0) {
                ShaderManager.stopShader()
            }
            if (event.section.compareTo("litParticles") == 0) {
                ShaderUtil.fastLightProgram?.useShader()
                ShaderUtil.fastLightProgram?.setUniform("sampler", 0)
                ShaderUtil.fastLightProgram?.setUniform("lightmap", 1)
                ShaderUtil.fastLightProgram?.setUniform(
                    "playerPos",
                    Vector3f(
                        Minecraft.getInstance().player!!.posX.toFloat(),
                        Minecraft.getInstance().player!!.posY.toFloat(),
                        Minecraft.getInstance().player!!.posZ.toFloat()
                    )
                )
                ShaderUtil.fastLightProgram?.setUniform("chunkX", 0)
                ShaderUtil.fastLightProgram?.setUniform("chunkY", 0)
                ShaderUtil.fastLightProgram?.setUniform("chunkZ", 0)
            }
            if (event.section.compareTo("particles") == 0) {
                ShaderManager.stopShader()
            }
            if (event.section.compareTo("weather") == 0) {
                ShaderManager.stopShader()
            }
            if (event.section.compareTo("entities") == 0) {
                if (Minecraft.getInstance().isOnExecutionThread) {
                    ShaderUtil.entityLightProgram?.useShader()
                    ShaderUtil.entityLightProgram?.setUniform("lightingEnabled", true)
                    ShaderUtil.entityLightProgram?.setUniform(
                        "fogIntensity",
                        if (Minecraft.getInstance().world?.dimensionType
                            === DimensionType.THE_NETHER
                        ) 0.015625f else 1.0f
                    )
                }
            }
            if (event.section.compareTo("blockEntities") == 0) {
                if (Minecraft.getInstance().isOnExecutionThread) {
                    ShaderUtil.entityLightProgram?.useShader()
                    ShaderUtil.entityLightProgram?.setUniform("lightingEnabled", true)
                }
            }
            if (event.section.compareTo("outline") == 0) {
                ShaderManager.stopShader()
            }
            if (event.section.compareTo("aboveClouds") == 0) {
                ShaderManager.stopShader()
            }
            if (event.section.compareTo("destroyProgress") == 0) {
                ShaderManager.stopShader()
            }
            if (event.section.compareTo("translucent") == 0) {
                ShaderUtil.fastLightProgram?.useShader()
                ShaderUtil.fastLightProgram?.setUniform("sampler", 0)
                ShaderUtil.fastLightProgram?.setUniform("lightmap", 1)
                ShaderUtil.fastLightProgram?.setUniform(
                    "playerPos",
                    Vector3f(
                        Minecraft.getInstance().player!!.posX.toFloat(),
                        Minecraft.getInstance().player!!.posY.toFloat(),
                        Minecraft.getInstance().player!!.posZ.toFloat()
                    )
                )
            }
            if (event.section.compareTo("hand") == 0) {
                ShaderUtil.entityLightProgram?.useShader()
                ShaderUtil.fastLightProgram?.setUniform(
                    "entityPos",
                    Vector3f(
                        Minecraft.getInstance().player!!.posX.toFloat(),
                        Minecraft.getInstance().player!!.posY.toFloat(),
                        Minecraft.getInstance().player!!.posZ.toFloat()
                    )
                )
                precedesEntities = true
            }
            if (event.section.compareTo("gui") == 0) {
                isGui = true
                ShaderManager.stopShader()
            }
        }
    }

    @SubscribeEvent
    fun onRenderEntity(event: RenderEntityEvent) {
        if (ConfigManager.isLightingEnabled) {
            if (event.entity is LightningBoltEntity) {
                ShaderManager.stopShader()
            } else if (section.equals("entities", ignoreCase = true) || section.equals(
                    "blockEntities",
                    ignoreCase = true
                )
            ) {
                ShaderUtil.entityLightProgram?.useShader()
            }
            if (ShaderManager.isCurrentShader(ShaderUtil.entityLightProgram)) {
                ShaderUtil.entityLightProgram?.setUniform(
                    "entityPos",
                    Vector3f(
                        event.entity.posX.toFloat(),
                        event.entity.posY.toFloat() + event.entity.height / 2.0f,
                        event.entity.posZ.toFloat()
                    )
                )
                //ShaderUtil.entityLightProgram?.setUniform("colorMult", 1f, 1f, 1f, 0f);
                //if (event.entity instanceof EntityLivingBase) {
                //    EntityLivingBase e = (EntityLivingBase) event.entity;
                //    if (e.hurtTime > 0 || e.deathTime > 0) {
                //        ShaderUtil.entityLightProgram?.setUniform("colorMult", 1f, 0f, 0f, 0.3f);
                //    }
                //}
            }
        }
    }

    @SubscribeEvent
    fun onRenderTileEntity(event: RenderTileEntityEvent) {
        if (ConfigManager.isLightingEnabled) {
            if (event.entity is EndPortalTileEntity || event.entity is EndGatewayTileEntity) {
                ShaderManager.stopShader()
            } else if (section.equals("entities", ignoreCase = true) || section.equals(
                    "blockEntities",
                    ignoreCase = true
                )
            ) {
                ShaderUtil.entityLightProgram?.useShader()
            }
            if (ShaderManager.isCurrentShader(ShaderUtil.entityLightProgram)) {
                ShaderUtil.entityLightProgram?.setUniform(
                    "entityPos",
                    Vector3f(
                        event.entity.pos.x.toFloat(),
                        event.entity.pos.y.toFloat(),
                        event.entity.pos.z.toFloat()
                    )
                )
                //ShaderUtil.entityLightProgram?.setUniform("colorMult", 1f, 1f, 1f, 0f);
            }
        }
    }

    @SubscribeEvent
    fun onRenderChunk(event: RenderChunkUniformsEvent) {
        if (ConfigManager.isLightingEnabled) {
            if (ShaderManager.isCurrentShader(ShaderUtil.fastLightProgram)) {
                val pos: BlockPos = event.chunk.position
                ShaderUtil.fastLightProgram?.setUniform("chunkX", pos.x)
                ShaderUtil.fastLightProgram?.setUniform("chunkY", pos.y)
                ShaderUtil.fastLightProgram?.setUniform("chunkZ", pos.z)
            }
        }
    }

    @SubscribeEvent
    fun clientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase === TickEvent.Phase.START)
            ticks++
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent?) {
        postedLights = false
        if (Minecraft.getInstance().isOnExecutionThread) {
            GlStateManager.disableLighting()
            ShaderManager.stopShader()
        }
    }

    class TorchLightProvider : ILightProvider {
        override fun gatherLights(event: GatherLightsEvent, context: Entity?) {
            event.add(
                Light.builder()
                    .pos(
                        context!!.lastTickPosX + (context.posX - context.lastTickPosX) * Minecraft.getInstance().renderPartialTicks
                            .toDouble(),
                        context.lastTickPosY + (context.posY - context.lastTickPosY) * Minecraft.getInstance().renderPartialTicks
                            .toDouble(),
                        context.lastTickPosZ + (context.posZ - context.lastTickPosZ) * Minecraft.getInstance().renderPartialTicks
                            .toDouble()
                    )
                    .color(1.0f, 0.78431374f, 0f)
                    .color(1.0f, 1.0f, 1.0f) //.direction(10f, 0f, 0f, (float)(Math.PI/8.0))
                    .radius(10f)
                    .build()
            )
        }
    }

    class RedstoneTorchProvider : ILightProvider {
        override fun gatherLights(event: GatherLightsEvent, context: Entity?) {
            //float theta = entity.ticksExisted / 10f;
            //Vec3d heading = new Vec3d(10, 0, 0).rotateYaw(theta);
            event.add(
                Light.builder()
                    .pos(
                        context!!.lastTickPosX + (context.posX - context.lastTickPosX) * Minecraft.getInstance().renderPartialTicks
                            .toDouble(),
                        context.lastTickPosY + (context.posY - context.lastTickPosY) * Minecraft.getInstance().renderPartialTicks
                            .toDouble(),
                        context.lastTickPosZ + (context.posZ - context.lastTickPosZ) * Minecraft.getInstance().renderPartialTicks
                            .toDouble()
                    )
                    .color(1.0f, 0f, 0f)
                    .radius(6f) //.color(1, 1, 1)
                    //.direction(10f, 0f, 0f, (float)(Math.PI/8.0))
                    //.direction(heading, (float)(Math.PI/3.0))
                    .build()
            )
        }
    }

    private val torchProvider = LazyOptional.of<ILightProvider> { TorchLightProvider() }
    private val redstoneProvider = LazyOptional.of<ILightProvider> { RedstoneTorchProvider() }

    @SubscribeEvent
    fun attachCapabilities(event: AttachCapabilitiesEvent<ItemStack>) {
        if (ConfigManager.enableTorchImplementation) {
            if (event.getObject().item === Blocks.TORCH.asItem()) {
                event.addCapability(ResourceLocation("albedo", "light_provider"), object : ICapabilityProvider {
                    @Nonnull
                    override fun <T> getCapability(@Nonnull cap: Capability<T>, side: Direction?): LazyOptional<T> {
                        return if (cap === Albedo.LIGHT_PROVIDER_CAPABILITY) torchProvider.cast() else LazyOptional.empty()
                    }
                })
            } else if (event.getObject().item === Blocks.REDSTONE_TORCH.asItem()) {
                event.addCapability(ResourceLocation("albedo", "light_provider"), object : ICapabilityProvider {
                    @Nonnull
                    override fun <T> getCapability(@Nonnull cap: Capability<T>, side: Direction?): LazyOptional<T> {
                        return if (cap === Albedo.LIGHT_PROVIDER_CAPABILITY) redstoneProvider.cast() else LazyOptional.empty()
                    }
                })
            }
        }
    }

    companion object {
        var isGui = false
        val EXISTING: MutableMap<BlockPos, List<Light>> = Collections.synchronizedMap(HashMap<BlockPos, List<Light>>())
    }
}