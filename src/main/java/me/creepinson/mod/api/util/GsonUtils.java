package me.creepinson.mod.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class GsonUtils {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    public static Gson getGson() {
        return GSON;
    }

    public static int getInt(JsonObject json, String name, int defaultVal) {
        if (json.get(name) == null) {
            json.addProperty(name, defaultVal);
        }
        return json.get(name).getAsInt();
    }


}