package me.creepinson.creepinoutils.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class BaseBlock extends Block {
    public BaseBlock(ResourceLocation name, Properties properties) {
        super(properties);
        setRegistryName(name);
    }

    public BlockItem createItem(Item.Properties properties) {
        BlockItem item = new BlockItem(this, properties);
        item.setRegistryName(this.getRegistryName());
        return item;
    }


    public BlockItem createItem(ItemGroup tab) {
        return createItem(new Item.Properties().group(tab));
    }

    public BlockItem createItem() {
        return createItem(ItemGroup.MISC);
    }

    public static class Ore extends BaseBlock {
        public Ore(ResourceLocation name) {
            super(name, Block.Properties.create(Material.ROCK));
        }
    }
}
