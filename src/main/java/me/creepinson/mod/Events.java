package me.creepinson.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Made By Creepinson
 */
@Mod.EventBusSubscriber
public class Events {

    @SubscribeEvent
    public static void loadWorld(WorldEvent.Load event) {
        if (event.getWorld().isRemote) {
            //Minecraft.getMinecraft().crashed(new CrashReport("An error occured while trying to craftmine. ", new NullPointerException("idk why test crash")));
        }
    }

}
