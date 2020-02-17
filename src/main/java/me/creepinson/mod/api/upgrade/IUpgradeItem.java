package me.creepinson.mod.api.upgrade;

import net.minecraft.item.ItemStack;

public interface IUpgradeItem {

    Upgrade getUpgradeType(ItemStack stack);
}