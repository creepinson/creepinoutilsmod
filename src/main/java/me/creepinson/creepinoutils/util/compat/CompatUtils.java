package me.creepinson.creepinoutils.util.compat;

import net.minecraftforge.fml.common.Loader;
public class CompatUtils {
    public static final String CRAYFISH_VEHICLES = "vehicle";

    public static boolean isVehicleModLoaded() {
        return Loader.isModLoaded(CRAYFISH_VEHICLES);
    }
}