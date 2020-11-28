package com.theoparis.creepinoutils.util.upgrade

import net.minecraft.item.ItemStack

interface IUpgradeable {
    fun upgrade(info: UpgradeInfo): Boolean
    fun removeUpgrade(upgrade: Upgrade): ItemStack
    fun canUpgrade(): Boolean
    val storedUpgrades: List<Any?>
}