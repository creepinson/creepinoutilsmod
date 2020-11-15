package com.theoparis.creepinoutils.util.lighting.lighting

import com.theoparis.creepinoutils.util.lighting.event.GatherLightsEvent
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraftforge.registries.ForgeRegistryEntry
import org.apache.logging.log4j.util.TriConsumer

class BlockLighting(val block: Block, private val consumer: TriConsumer<BlockPos, BlockState, GatherLightsEvent>) :
    ForgeRegistryEntry<BlockLighting?>() {
    fun getConsumer(): TriConsumer<BlockPos, BlockState, GatherLightsEvent> {
        return consumer
    }

}