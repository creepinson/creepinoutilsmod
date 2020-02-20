package me.creepinson.mod.base;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public abstract class BaseProxy {
    public void init() {

    }

    public void preInit() {

    }

    public void postInit() {

    }

    public abstract void registerBlocks();

    public abstract void registerItems();

    public abstract void registerTileEntities();

    public abstract void registerRecipes();

    public abstract void registerPackets();

    public void registerPotions() {

    }

}
