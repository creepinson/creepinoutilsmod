package me.creepinson.creepinoutils.api.util.crash;

import net.minecraft.crash.CrashReport;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Made By Creepinson
 */
public class CrashReportAnalyzer {

    private CrashReport report;
    private boolean client;
    private List<ModContainer> erroredMods;

    private Collection<ModContainer> getMods() {
        return Loader.instance().getIndexedModList().values();
    }

    public CrashReportAnalyzer(CrashReport report, boolean client) {
        this.report = report;
        this.client = client;
        this.erroredMods = new ArrayList<>();
    }

    public String createDebugReport() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mods installed: ");
        for (ModContainer mod : getMods()) {
            builder.append(mod.getName() + ": ");
            builder.append("  modid: " + mod.getModId());
            builder.append("  version: " + mod.getVersion());
            if (crashReportContainsModError(report, mod)) {
                builder.append(mod.getName() + " may be causing an error. ");
                erroredMods.add(mod);
            }
        }

        builder.append("Crash report description: ");
        builder.append(report.getDescription());

        return builder.toString();
    }

    public static boolean crashReportContainsModError(CrashReport crash, ModContainer mod) {


        return false;
    }
}
