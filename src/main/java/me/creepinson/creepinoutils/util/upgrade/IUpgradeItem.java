package me.creepinson.creepinoutils.util.upgrade;

import net.minecraft.item.ItemStack;

public interface IUpgradeItem {

    Upgrade getUpgradeType(ItemStack stack);
}