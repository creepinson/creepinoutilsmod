package com.theoparis.creepinoutils.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.World
import net.minecraftforge.common.util.INBTSerializable

open class BlockData : INBTSerializable<CompoundNBT> {
    var state: BlockState = Blocks.AIR.defaultState
    var position: BlockPos = BlockPos.ZERO
    var world: World? = null

    constructor(world: World, pos: BlockPos, state: BlockState) : this(pos, state) {
        this.world = world
    }

    constructor(world: World?) {
        this.world = world
    }

    constructor(pos: BlockPos, state: BlockState) {
        position = pos
        this.state = state
    }

    /**
     * Helper method to get the block from the state stored in the data container
     */
    val block: Block
        get() = state.block

    override fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()
        nbt.put("block", NBTUtil.writeBlockState(state))
        nbt.put("position", NBTUtil.writeBlockPos(position))
        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        state = NBTUtil.readBlockState(nbt.getCompound("block"))
        position = NBTUtil.readBlockPos(nbt.getCompound("position"))
    }
}