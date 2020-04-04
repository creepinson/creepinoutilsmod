package me.creepinson.creepinoutils.base;

import com.google.gson.JsonObject;
import me.creepinson.creepinoutils.api.util.CreativeTabCallback;
import me.creepinson.creepinoutils.api.util.GsonUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 */
public abstract class BaseMod {
    public final VersionSettings modVersionSettings;
    protected File _CONFIG_BASE;
    protected Logger _LOGGER;
    public final String modUrl;
    public final String modId;
    public final String modVersion;
    protected String UPDATE_LATEST_VER = null;

    /**
     * Defaults to NULL
     */
    protected ModConfig config;
    public boolean CHECK_FOR_UPDATES = true;
    public CreativeTab creativeTab;
    protected boolean genConfig = true;
    protected boolean debug;


    public boolean isDebug() {
        return debug;
    }

    /**
     * @param versionSettings Used for retrieving the latest version of the mod.
     *                        If you pas null, it will not check for the latest version.
     * @param id              The mod id used by minecraft forge
     *                        If you are extending this class, then you can set BaseMod#hasCreativeTab to false if you do not want a
     *                        simple creative tab to be added to your mod.
     */
    public BaseMod(String url, VersionSettings versionSettings, String id, String ver) {
        modUrl = url;
        modVersionSettings = versionSettings;
        modId = id;
        modVersion = ver;
        _LOGGER = LogManager.getLogger(modId);
        Configurator.setLevel(_LOGGER.getName(), Level.DEBUG);
    }

    public void debug(String s) {
        if (this.isDebug()) {
            getLogger().info("[DEBUG] " + s);
        }
    }

    public void clientPreInit(FMLPreInitializationEvent event) {

    }

    public void clientInit(FMLInitializationEvent event) {

    }

    public void clientPostInit(FMLPostInitializationEvent event) {

    }

    public Logger getLogger() {
        return _LOGGER;
    }

    protected void checkForUpdates() {
        if (modVersionSettings != null) {
            try {
                URL versionIn = new URL(modVersionSettings.versionUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
                if (modVersionSettings.raw) {
                    UPDATE_LATEST_VER = in.readLine();
                } else {
                    StringBuilder builder = new StringBuilder();
                    in.lines().forEach(builder::append);
                    JsonObject object = GsonUtils.getGson().fromJson(builder.toString(), JsonObject.class);
                    UPDATE_LATEST_VER = object.get("latestVersion").getAsString();
                }

            } catch (Exception e) {
                getLogger().warn("Unable to get the latest version information");
                e.printStackTrace();
                UPDATE_LATEST_VER = modVersion;
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) clientInit(event);
    }

    /**
     * @param callback Callback once creative tab is created to change settings of the tab.
     *                 You can pass null to disable automatic initialization of the creative tab.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event, CreativeTabCallback callback) {
        if (callback != null) {
            creativeTab = new CreativeTab(modId);
            callback.init(creativeTab);
        }

        _CONFIG_BASE = event.getSuggestedConfigurationFile().getParentFile();
        if (genConfig) {
            config = new ModConfig(this, "config.cfg");
            if (!config.fileExists()) {
                config.writeConfig("main", "debug", false);
            }
            config.init();
            this.debug = config.getBoolean("main", "debug");
        }

        MinecraftForge.EVENT_BUS.register(this);
        if (event.getSide() == Side.CLIENT) clientPreInit(event);

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (CHECK_FOR_UPDATES)
            checkForUpdates();
        if (event.getSide() == Side.CLIENT) clientPostInit(event);
    }
}
