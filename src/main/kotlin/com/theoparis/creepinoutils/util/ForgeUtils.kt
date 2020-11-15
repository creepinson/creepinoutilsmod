package com.theoparis.creepinoutils.util

import net.minecraft.block.Block
import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

// TODO: move to creepinoutils
/**
 * Some kotlin forge utilities such as World#dropBlock & World#dropItem
 */
object ForgeUtils {
    fun World.dropItem(item: ItemStack, pos: BlockPos?) {
        if (!this.isRemote)
            this.addEntity(
                ItemEntity(
                    this,
                    pos?.x?.toDouble() ?: 0.0,
                    pos?.y?.toDouble() ?: 0.0,
                    pos?.z?.toDouble() ?: 0.0,
                    item
                )
            )
    }

    fun World.dropBlock(block: Block, pos: BlockPos?) =
        this.dropItem(ItemStack(block.asItem()), pos)
}