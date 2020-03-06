package me.creepinson.creepinoutils.api.upgrade;

import net.minecraft.item.ItemStack;

public interface IUpgradeItem {

    Upgrade getUpgradeType(ItemStack stack);
}