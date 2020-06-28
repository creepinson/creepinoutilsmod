package me.creepinson.creepinoutils.util.client.gl.animation;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.obj.LineReader;
import net.minecraftforge.client.model.obj.MaterialLibrary;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

public class AnimatedOBJLoader implements IModelLoader<AnimatedOBJModel> {
    public static AnimatedOBJLoader INSTANCE = new AnimatedOBJLoader();

    private final Map<AnimatedOBJModel.ModelSettings, AnimatedOBJModel> modelCache = Maps.newHashMap();
    private final Map<ResourceLocation, MaterialLibrary> materialCache = Maps.newHashMap();

    private IResourceManager manager = Minecraft.getInstance().getResourceManager();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        modelCache.clear();
        materialCache.clear();
        manager = resourceManager;
    }

    @Override
    public AnimatedOBJModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        if (!modelContents.has("model"))
            throw new RuntimeException("OBJ Loader requires a 'model' key that points to a valid .OBJ model.");

        String modelLocation = modelContents.get("model").getAsString();

        boolean detectCullableFaces = JSONUtils.getBoolean(modelContents, "detectCullableFaces", true);
        boolean diffuseLighting = JSONUtils.getBoolean(modelContents, "diffuseLighting", false);
        boolean flipV = JSONUtils.getBoolean(modelContents, "flip-v", false);
        boolean ambientToFullbright = JSONUtils.getBoolean(modelContents, "ambientToFullbright", true);
        @Nullable
        String materialLibraryOverrideLocation = modelContents.has("materialLibraryOverride") ? JSONUtils.getString(modelContents, "materialLibraryOverride") : null;

        return loadModel(new AnimatedOBJModel.ModelSettings(new ResourceLocation(modelLocation), detectCullableFaces, diffuseLighting, flipV, ambientToFullbright, materialLibraryOverrideLocation));
    }

    public AnimatedOBJModel loadModel(AnimatedOBJModel.ModelSettings settings) {
        return modelCache.computeIfAbsent(settings, (data) -> {
            IResource resource;
            try {
                resource = manager.getResource(settings.modelLocation);
            } catch (IOException e) {
                throw new RuntimeException("Could not find OBJ model", e);
            }

            try (LineReader rdr = new LineReader(resource)) {
                return new AnimatedOBJModel(rdr, settings);
            } catch (Exception e) {
                throw new RuntimeException("Could not read OBJ model", e);
            }
        });
    }

    public MaterialLibrary loadMaterialLibrary(ResourceLocation materialLocation) {
        return materialCache.computeIfAbsent(materialLocation, (location) -> {
            IResource resource;
            try {
                resource = manager.getResource(location);
            } catch (IOException e) {
                throw new RuntimeException("Could not find OBJ material library", e);
            }

            try (LineReader rdr = new LineReader(resource)) {
                return new MaterialLibrary(rdr);
            } catch (Exception e) {
                throw new RuntimeException("Could not read OBJ material library", e);
            }
        });
    }
}