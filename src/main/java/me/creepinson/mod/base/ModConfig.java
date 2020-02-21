package me.creepinson.mod.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.creepinson.mod.CreepinoUtilsMod;
import me.creepinson.mod.api.util.GsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

public abstract class ModConfig {
    public static final int CURRENT_VERSION = 0;
    private final File configFile;
    private final BaseMod mod;


    public ModConfig(BaseMod mod, String fileName) {
        this.mod = mod;
        configFile = new File(mod._CONFIG_BASE + "/" + mod._MOD_ID, fileName + ".json");
    }

    protected void iterateArrayObjects(JsonArray jsonArray, Consumer<JsonObject> runnable) {
        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {
                    runnable.accept(jsonObject);
                } catch (Throwable e) {
                    CreepinoUtilsMod.getInstance().getLogger().warn("Couldn't process array entry: " + jsonObject);
                    e.printStackTrace();
                }
            } else {
                mod.getLogger().warn(jsonElement.toString() + " is not an object. Skipping to next json element.");
            }
        }
    }

    public void load() {
        try {
            JsonObject json = GsonUtils.getGson().fromJson(new FileReader(configFile), JsonObject.class);
            int version = GsonUtils.getInt(json, "configVersion", -1);
            for (int i = version; i < CURRENT_VERSION; i++) {
                mod.getLogger().info("Upgrading config data file \"" + configFile.toString() + "\" to version " + (i + 1) + ".");
                json = upgrade(json, i);
            }
            load(json);
            mod.getLogger().info("Loaded config data file \"" + configFile.toString() + "\".");
            if (version != CURRENT_VERSION) {
                save();
                mod.getLogger().info("Saved upgraded config file.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            mod.getLogger().info("Couldn't load config data file \"" + configFile.toString() + "\": " + e.getClass().getCanonicalName() + ". Initiating crash...");
            System.exit(1);
        }
    }

    public void save() {
        if (!configFile.exists()) {
            overrideFile();
        }
    }

    public void overrideFile() {
        try {
            new File(configFile.getParent()).mkdirs();
            FileWriter fileWriter = new FileWriter(configFile, false);
            save(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save config data file: " + configFile.toString(), e);
        }
    }

    public abstract void generate();

    protected abstract JsonObject upgrade(JsonObject json, int version);

    protected abstract void load(JsonObject jsonObject);

    protected abstract void save(FileWriter fileWriter);

    public boolean fileExists() {
        return this.configFile.exists();
    }
}
