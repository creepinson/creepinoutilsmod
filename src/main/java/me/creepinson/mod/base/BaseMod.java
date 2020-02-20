package me.creepinson.mod.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 */
public abstract class BaseMod {
    protected File _CONFIG_BASE;
    protected Logger _LOGGER;
    protected String _MOD_URL, _MOD_ID, _MOD_NAME, _MOD_VER;
    protected BaseProxy _PROXY;
    protected String UPDATE_LATEST_VER = null;

    public boolean hasCreativeTab = true;
    public boolean CHECK_FOR_UPDATES = true;
    public CreativeTab creativeTab;

    /**
     * @param id   The modid used by minecraft forge
     * @param name The display name of the mod
     *             If you are extending this class, then you can set BaseMod#hasCreativeTab to false if you do not want a
     *             simple creative tab to be added to your mod.
     */
    public BaseMod(String url, String id, String short_id, String name, String ver) {
        _MOD_URL = url;
        _MOD_ID = id;
        _MOD_NAME = name;
        _MOD_VER = ver;
        _LOGGER = LogManager.getLogger(_MOD_NAME);
    }

    public Logger getLogger() {
        return _LOGGER;
    }

    protected void checkForUpdates() {
        try {
            URL versionIn = new URL(_MOD_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
            UPDATE_LATEST_VER = in.readLine();

        } catch (Exception e) {
            getLogger().warn("Unable to get the latest version information");
            UPDATE_LATEST_VER = _MOD_VER;
        }
    }

    public void init(FMLInitializationEvent event) {
        _PROXY.init();
    }

    public void preInit(FMLPreInitializationEvent event, BaseProxy proxy) {
        _PROXY = proxy;
        _CONFIG_BASE = event.getSuggestedConfigurationFile().getParentFile();
        if (hasCreativeTab) {
            creativeTab = new CreativeTab(_MOD_ID);
        }
        _PROXY.preInit();
        _PROXY.registerBlocks();
        _PROXY.registerTileEntities();
        _PROXY.registerItems();
        _PROXY.registerPackets();
        _PROXY.registerPotions();
        MinecraftForge.EVENT_BUS.register(_PROXY);
    }

    public void postInit(FMLPostInitializationEvent event) {
        _PROXY.postInit();
        _PROXY.registerRecipes();

        if (CHECK_FOR_UPDATES)
            checkForUpdates();
    }
}
