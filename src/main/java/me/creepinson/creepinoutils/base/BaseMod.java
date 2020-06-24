package me.creepinson.creepinoutils.base;

import com.google.gson.JsonObject;

import dev.throwouterror.util.data.JsonConfiguration;
import dev.throwouterror.util.data.JsonUtils;
import me.creepinson.creepinoutils.util.ItemGroupCallback;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Theo Paris (https://theoparis.com)
 */

public abstract class BaseMod {
    public final VersionSettings modVersionSettings;
    protected File _CONFIG_BASE;
    protected Logger _LOGGER;
    public final String modId;
    public final String modVersion;
    protected String UPDATE_LATEST_VER = null;

    /**
     * Defaults to NULL
     */
    protected JsonConfiguration config;
    public boolean CHECK_FOR_UPDATES = true;
    public BaseItemGroup itemGroup;
    protected boolean genConfig = true;
    protected boolean debug;
    protected ItemGroupCallback tabCallback;

    public JsonConfiguration getConfig() {
        return config;
    }

    public boolean isDebug() {
        return debug;
    }

    /**
     * @param versionSettings Used for retrieving the latest version of the mod. If
     *                        you pas null, it will not check for the latest
     *                        version.
     * @param id              The mod id used by minecraft forge If you are
     *                        extending this class, then you can set
     *                        BaseMod#hasItemGroup to false if you do not want a
     *                        simple creative tab to be added to your mod.
     */
    public BaseMod(String id, String ver, VersionSettings versionSettings) {
        modVersionSettings = versionSettings;
        modId = id;
        modVersion = ver;
        _LOGGER = LogManager.getLogger(modId);
        Configurator.setLevel(_LOGGER.getName(), Level.DEBUG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void debug(String s) {
        if (this.isDebug()) {
            getLogger().info("[DEBUG] " + s);
        }
    }

    public void enqueueIMC(final InterModEnqueueEvent t) {
    }

    public void processIMC(final InterModProcessEvent t) {
    }

    public BaseMod withTabCallback(ItemGroupCallback callback) {
        this.tabCallback = callback;
        return this;
    }

    public void setup(final FMLCommonSetupEvent event) {
        if (tabCallback != null) {
            itemGroup = new BaseItemGroup(modId);
            tabCallback.init(itemGroup);
        }

        _CONFIG_BASE = FMLPaths.CONFIGDIR.get().resolve(modId).toFile();
        _CONFIG_BASE.mkdirs();
        if (genConfig) {
            config = new JsonConfiguration(new File(_CONFIG_BASE, "config.json"));
            if (!config.fileExists()) {
                config.getConfigMap().put("debug", false);
            }
            config.create();

            this.debug = (boolean) config.getConfigMap().get("debug");
        }

        if (CHECK_FOR_UPDATES)
            checkForUpdates();
    }

    public void clientSetup(final FMLClientSetupEvent event) {

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
                    JsonObject object = JsonUtils.get().fromJson(builder.toString(), JsonObject.class);
                    UPDATE_LATEST_VER = object.get("latestVersion").getAsString();
                }

            } catch (Exception e) {
                getLogger().warn("Unable to get the latest version information");
                e.printStackTrace();
                UPDATE_LATEST_VER = modVersion;
            }
        }
    }

}
