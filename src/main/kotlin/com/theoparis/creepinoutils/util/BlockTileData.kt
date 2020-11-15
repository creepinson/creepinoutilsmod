package com.theoparis.creepinoutils.util

import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockTileData(world: World?) : BlockData(world) {
    var tileEntity: TileEntity? = null
    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()
        if (tileEntity != null && !tileEntity!!.isRemoved)
            nbt.put("tile", tileEntity!!.serializeNBT())

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)
        if (nbt.contains("tile"))
            tileEntity = TileEntity.readTileEntity(state, nbt.getCompound("tile"))
    }
}