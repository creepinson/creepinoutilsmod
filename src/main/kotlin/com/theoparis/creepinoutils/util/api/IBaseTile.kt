package com.theoparis.creepinoutils.util.api

import com.theoparis.creepinoutils.util.upgrade.IUpgradeable
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.math.BlockPos

interface IBaseTile : IConnectable, IUpgradeable, ITickableTileEntity {
    val isActive: Boolean
    fun refresh()
    fun onNeighborChange(pos: BlockPos?)
}