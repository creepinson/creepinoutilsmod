package me.creepinson.creepinoutils.api.util.client.glutils;

import me.creepinson.creepinoutils.api.util.client.glutils.animation.AnimatedOBJModel;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Made By Creepinson
 */
public enum AnimatedOBJLoader implements ICustomModelLoader {
    INSTANCE;

    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<>();
    private final Map<ResourceLocation, AnimatedOBJModel> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();

    public void addDomain(String domain) {
        enabledDomains.add(domain.toLowerCase());
        FMLLog.log.info("OBJLoader: Domain {} has been added.", domain.toLowerCase());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        cache.clear();
        errors.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return enabledDomains.contains(modelLocation.getNamespace()) && modelLocation.getPath().endsWith(".obj");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        ResourceLocation file = new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath());
        if (!cache.containsKey(file)) {
            IResource resource = null;
            try {
                try {
                    resource = manager.getResource(file);
                } catch (FileNotFoundException e) {
                    if (modelLocation.getPath().startsWith("models/block/"))
                        resource = manager.getResource(new ResourceLocation(file.getNamespace(), "models/item/" + file.getPath().substring("models/block/".length())));
                    else if (modelLocation.getPath().startsWith("models/item/"))
                        resource = manager.getResource(new ResourceLocation(file.getNamespace(), "models/block/" + file.getPath().substring("models/item/".length())));
                    else throw e;
                }
                AnimatedOBJModel.Parser parser = new AnimatedOBJModel.Parser(resource, manager);
                AnimatedOBJModel model = null;
                try {
                    model = parser.parse();
                } catch (Exception e) {
                    errors.put(modelLocation, e);
                } finally {
                    cache.put(modelLocation, model);
                }
            } finally {
                IOUtils.closeQuietly(resource);
            }
        }
        AnimatedOBJModel model = cache.get(file);
        if (model == null)
            throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + file, errors.get(modelLocation));
        return model;
    }
}