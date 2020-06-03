package me.creepinson.creepinoutils;

import me.creepinson.creepinoutils.base.BaseBlock;
import me.creepinson.creepinoutils.block.BlockAnimationTest;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Theo Paris (https://theoparis.com)
 */

@Mod.EventBusSubscriber(modid = CreepinoUtilsMod.MOD_ID)
public class RegistryHandler {
    public static final BaseBlock BLOCK_TEST = new BlockAnimationTest(Material.CLOTH,
            new ResourceLocation(CreepinoUtilsMod.MOD_ID, "test_block"), CreativeTabs.MISC);

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        // event.getRegistry().registerAll(BLOCK_TEST);
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        // event.getRegistry().registerAll(BLOCK_TEST.createItemBlock());
    }
}
