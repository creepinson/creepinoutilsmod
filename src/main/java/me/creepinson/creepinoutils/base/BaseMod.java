package me.creepinson.creepinoutils.base;

import com.google.gson.JsonObject;
import me.creepinson.creepinoutils.api.util.GsonUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 */
public abstract class BaseMod {
    protected File _CONFIG_BASE;
    protected Logger _LOGGER;
    public final String modUrl, modId, modName, modVersion;
    protected BaseProxy _PROXY;
    protected String UPDATE_LATEST_VER = null;

    /**
     * Defaults to NULL
     */
    protected ModConfig config;
    protected boolean hasCreativeTab = true;
    public boolean CHECK_FOR_UPDATES = true;
    public CreativeTab creativeTab;
    protected boolean genConfig = true;
    protected boolean debug;

    public boolean isDebug() {
        return debug;
    }

    /**
     * @param id   The modid used by minecraft forge
     * @param name The display name of the mod
     *             If you are extending this class, then you can set BaseMod#hasCreativeTab to false if you do not want a
     *             simple creative tab to be added to your mod.
     */
    public BaseMod(String url, String id, String short_id, String name, String ver) {
        modUrl = url;
        modId = id;
        modName = name;
        modVersion = ver;
        _LOGGER = LogManager.getLogger(modName);
        Configurator.setLevel(_LOGGER.getName(), Level.DEBUG);
    }

    public Logger getLogger() {
        return _LOGGER;
    }

    protected void checkForUpdates() {
        try {
            URL versionIn = new URL(modUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
            UPDATE_LATEST_VER = in.readLine();

        } catch (Exception e) {
            getLogger().warn("Unable to get the latest version information");
            UPDATE_LATEST_VER = modVersion;
        }
    }

    public void init(FMLInitializationEvent event) {
        _PROXY.init();
    }

    public void preInit(FMLPreInitializationEvent event, BaseProxy proxy) {
        _PROXY = proxy;
        _CONFIG_BASE = event.getSuggestedConfigurationFile().getParentFile();
        if (genConfig) {
            config = new ModConfig(this, "config") {
                @Override
                public void generate() {

                }

                @Override
                protected JsonObject upgrade(JsonObject json, int version) {

                    return json;
                }

                @Override
                protected void load(JsonObject jsonObject) {
                    if (jsonObject.has("debug")) {
                        debug = jsonObject.get("debug").getAsBoolean();
                    } else {
                        debug = false;
                    }

                }

                @Override
                protected void save(FileWriter fileWriter) {
                    JsonObject main = new JsonObject();
                    main.addProperty("debug", false);
                    main.addProperty("configVersion", CURRENT_VERSION);
                    GsonUtils.getGson().toJson(main, fileWriter);
                }
            };
        }

        if (config != null) {
            if (config.fileExists()) {
                config.load();
            }
            config.save();
        }

        if (hasCreativeTab) {
            creativeTab = new CreativeTab(modId);
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
