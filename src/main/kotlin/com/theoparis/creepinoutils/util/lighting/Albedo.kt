package com.theoparis.creepinoutils.util.lighting

import com.theoparis.creepinoutils.ConfigManager
import com.theoparis.creepinoutils.util.TriConsumer
import com.theoparis.creepinoutils.util.lighting.event.GatherLightsEvent
import com.theoparis.creepinoutils.util.lighting.lighting.DefaultLightProvider
import com.theoparis.creepinoutils.util.lighting.lighting.ILightProvider
import com.theoparis.creepinoutils.util.lighting.lighting.Light.Companion.builder
import com.theoparis.creepinoutils.util.lighting.util.ShaderUtil
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneTorchBlock
import net.minecraft.client.Minecraft
import net.minecraft.nbt.INBT
import net.minecraft.resources.IReloadableResourceManager
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.DeferredWorkQueue
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.*

class Albedo {
    private fun commonSetup(event: FMLCommonSetupEvent) {
        CapabilityManager.INSTANCE.register(ILightProvider::class.java, object : IStorage<ILightProvider?> {
            override fun writeNBT(
                capability: Capability<ILightProvider?>,
                instance: ILightProvider?,
                side: Direction
            ): INBT? {
                return null
            }

            override fun readNBT(
                capability: Capability<ILightProvider?>,
                instance: ILightProvider?,
                side: Direction,
                nbt: INBT
            ) {
            }
        }) { DefaultLightProvider() }
    }

    private fun loadComplete(event: FMLLoadCompleteEvent) {
        DeferredWorkQueue.runLater {
            (Minecraft.getInstance().resourceManager as IReloadableResourceManager).addReloadListener(
                ShaderUtil()
            )
        }
    }

    private fun registerBlockHandler(block: Block, consumer: TriConsumer<BlockPos, BlockState, GatherLightsEvent>) {
        MAP[block] = consumer
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        MOD_BUS.register(EventManager())
        registerBlockHandler(Blocks.REDSTONE_TORCH) { pos: BlockPos, state: BlockState, evt: GatherLightsEvent ->
            if (state.get(RedstoneTorchBlock.LIT)) {
                evt.add(
                    builder()
                        .pos(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
                        .color(1.0f, 0f, 0f, 1.0f)
                        .radius(6f)
                        .build()
                )
            }
        }
    }

    companion object {
        var MAP: MutableMap<Block, TriConsumer<BlockPos, BlockState, GatherLightsEvent>> = HashMap()

        @CapabilityInject(ILightProvider::class)
        var LIGHT_PROVIDER_CAPABILITY: Capability<ILightProvider>? = null
    }

    init {
        MOD_BUS.addListener { event: FMLLoadCompleteEvent -> loadComplete(event) }
        MOD_BUS.addListener { event: FMLCommonSetupEvent -> commonSetup(event) }
        MOD_BUS.addListener { event: FMLClientSetupEvent -> clientSetup(event) }
    }
}