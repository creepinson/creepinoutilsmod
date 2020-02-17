package me.creepinson.mod.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    ItemStack item;

    public CreativeTab(String name, ItemStack item) {
        super(name);
        this.item = item;
    }

    public CreativeTab(String name) {
        super(name);
    }

    @Override
    public ItemStack getIcon() {
        return item == null ? new ItemStack(Blocks.FIRE) : item;
    }

    @Override
    public ItemStack createIcon() {
        return item == null ? new ItemStack(Blocks.FIRE) : item;
    }

    public CreativeTab setItem(ItemStack stack) {
        item = stack;
        return this;
    }
}
