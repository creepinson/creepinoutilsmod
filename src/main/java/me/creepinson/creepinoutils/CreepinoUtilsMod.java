package me.creepinson.creepinoutils;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Theo Paris (https://theoparis.com)
 */

@Mod(CreepinoUtilsMod.MOD_ID)
public class CreepinoUtilsMod {
    public static final String MOD_ID = "creepinoutils";

    public static final Logger LOGGER = LogManager.getLogger();

    public CreepinoUtilsMod() {
/*        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);*/
    }
}
