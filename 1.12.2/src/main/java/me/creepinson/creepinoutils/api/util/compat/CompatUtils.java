package me.creepinson.creepinoutils.api.util.compat;

import net.minecraftforge.fml.common.Loader;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project CreepinoUtilsMod
 **/
public class CompatUtils {
    public static final String MEKANISM = "mekanism";
    public static final String INDUSTRIALCRAFT = "ic2";
    public static final String CRAYFISH_VEHICLES = "vehicle";

    public static boolean isMekanismLoaded() {
        return Loader.isModLoaded(MEKANISM);
    }

    public static boolean isIC2Loaded() {
        return Loader.isModLoaded(INDUSTRIALCRAFT);
    }

    public static boolean isRFLoaded() {
        return EnergyConfigHandler.isRFAPILoaded();
    }

    public static boolean isVehiclesLoaded() {
        return Loader.isModLoaded(CRAYFISH_VEHICLES);
    }

    public static boolean isEnergySupported() {
        return isIC2Loaded() && isMekanismLoaded() && isRFLoaded();
    }
}
