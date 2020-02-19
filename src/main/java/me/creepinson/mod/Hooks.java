package me.creepinson.mod;

import net.minecraftforge.fml.common.Loader;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class Hooks {
    public static final String MEKANISM = "mekanism";

    public static boolean isMekanismLoaded() {
        return Loader.isModLoaded(MEKANISM);
    }

}
