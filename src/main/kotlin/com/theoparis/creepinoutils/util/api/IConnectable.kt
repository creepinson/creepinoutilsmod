package com.theoparis.creepinoutils.util.api

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

/**
 * @author Theo Paris https://theoparis.com
 * Project creepinoutils
 */
interface IConnectable {
    fun canConnectTo(blockAccess: IWorld, pos: BlockPos, side: Direction?): Boolean
    fun canConnectToStrict(blockAccess: IWorld, pos: BlockPos, side: Direction?): Boolean
}