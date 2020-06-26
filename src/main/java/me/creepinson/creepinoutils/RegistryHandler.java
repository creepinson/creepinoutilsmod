package me.creepinson.creepinoutils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Theo Paris (https://theoparis.com)
 */

@Mod.EventBusSubscriber(modid = CreepinoUtilsMod.MOD_ID)
public class RegistryHandler {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        // event.getRegistry().registerAll(BLOCK_TEST);
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        // event.getRegistry().registerAll(BLOCK_TEST.createBlockItem());
    }
}
