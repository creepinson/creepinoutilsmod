package me.creepinson.creepinoutils.util.crash;

import net.minecraft.crash.CrashReport;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Made By Creepinson
 * If canceled, the minecraft instance will not quit, but instead display the error message.
 */

@Cancelable
//TODO: add post and pre
public class CrashEvent extends Event {
    private CrashReport crash;
    private boolean isClient;

    public CrashEvent(CrashReport report, boolean client) {
        this.crash = report;
        this.isClient = client;
    }

}
