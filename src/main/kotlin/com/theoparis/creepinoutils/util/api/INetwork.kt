package com.theoparis.creepinoutils.util.api

import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.common.util.INBTSerializable
import java.io.Serializable

interface INetwork<T> : INBTSerializable<CompoundNBT>, Serializable {
    val world: World?
    fun refresh()
    val connections: Set<Vector3d>
    fun merge(net: INetwork<T>)
    fun split(splitPoint: IBaseTile)
}