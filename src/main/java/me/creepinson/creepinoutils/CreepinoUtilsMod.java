package me.creepinson.creepinoutils;

import me.creepinson.creepinoutils.base.BaseMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @author Theo Paris (https://theoparis.com)
 */

@Mod(CreepinoUtilsMod.MOD_ID)
public class CreepinoUtilsMod extends BaseMod {
    protected static final String MOD_ID = "creepinoutils", MOD_NAME = "Creepino Utilities", MOD_VERSION = "1.0.0";

    private static CreepinoUtilsMod INSTANCE;

    public static CreepinoUtilsMod getInstance() {
        return INSTANCE;
    }

    public CreepinoUtilsMod() {
        super(MOD_ID, MOD_VERSION, null);
        INSTANCE = this;
    }

    @Override
    public void setup(FMLCommonSetupEvent event) {
        super.setup(event);
        CreepinoUtilsMod.getInstance().debug("Mod loading...");
    }
}
