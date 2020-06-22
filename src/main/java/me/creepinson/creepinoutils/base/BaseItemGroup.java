package me.creepinson.creepinoutils.base;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BaseItemGroup extends ItemGroup {
    ItemStack item;

    public BaseItemGroup(String name, ItemStack item) {
        super(name);
        this.item = item;
    }

    public BaseItemGroup(String name) {
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

    public BaseItemGroup setItem(ItemStack stack) {
        item = stack;
        return this;
    }
}
