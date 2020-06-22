package me.creepinson.creepinoutils.base;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BaseItem extends Item {

    public BaseItem(ResourceLocation name, Properties properties) {
        super(properties);
        setRegistryName(name);
    }
}