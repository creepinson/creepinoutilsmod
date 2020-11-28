package com.theoparis.creepinoutils.util

import net.minecraft.block.Block
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.util.math.vector.Vector3i
import net.minecraft.util.math.vector.Vector4f
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

// TODO: move to creepinoutils

/**
 * Utility method to drop an itemstack in the world
 */
fun World.dropItem(item: ItemStack, pos: BlockPos) {
    if (!this.isRemote)
        this.addEntity(
            ItemEntity(
                this,
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                item
            )
        )
}

fun World.getBlock(pos: BlockPos) = this.getBlockState(pos).block

fun PlayerEntity.sendMessage(text: ITextComponent) = this.sendMessage(text, this.uniqueID)

/**
 * Utility method to drop an block in the world
 */
fun World.dropBlock(block: Block, pos: BlockPos) =
    this.dropItem(ItemStack(block.asItem()), pos)

fun Vector3f.toVector4f() = Vector4f(this.x, this.y, this.z, 0f)
fun Vector4f.toVector3f() = Vector3f(this.x, this.y, this.z)
fun BlockPos.toVector3f() = Vector3f(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
fun BlockPos.toVector3d() = Vector3d(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
fun BlockPos.toVector3i() = Vector3i(this.x, this.y, this.z)
fun Vector3d.toBlockPos() = BlockPos(this.x, this.y, this.z)
fun Vector3i.toBlockPos() = BlockPos(this.x, this.y, this.z)
fun Vector3i.toVector3d() = Vector3d(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
