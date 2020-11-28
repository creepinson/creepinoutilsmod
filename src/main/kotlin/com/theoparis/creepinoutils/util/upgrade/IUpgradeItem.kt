package com.theoparis.creepinoutils.util.upgrade

import net.minecraft.item.ItemStack

interface IUpgradeItem {
    fun getUpgradeType(stack: ItemStack?): Upgrade?
}