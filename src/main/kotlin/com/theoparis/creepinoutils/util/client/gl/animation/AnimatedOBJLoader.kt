package com.theoparis.creepinoutils.util.client.gl.animation

import com.google.common.collect.Maps
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import net.minecraft.resources.IResource
import net.minecraft.resources.IResourceManager
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModelLoader
import net.minecraftforge.client.model.obj.LineReader
import net.minecraftforge.client.model.obj.MaterialLibrary
import java.io.IOException

class AnimatedOBJLoader : IModelLoader<AnimatedOBJModel> {
    private val modelCache: MutableMap<AnimatedOBJModel.ModelSettings, AnimatedOBJModel> = Maps.newHashMap()
    private val materialCache: MutableMap<ResourceLocation, MaterialLibrary> = Maps.newHashMap()
    private var manager = Minecraft.getInstance().resourceManager
    override fun onResourceManagerReload(resourceManager: IResourceManager) {
        modelCache.clear()
        materialCache.clear()
        manager = resourceManager
    }

    override fun read(deserializationContext: JsonDeserializationContext, modelContents: JsonObject): AnimatedOBJModel {
        if (!modelContents.has("model")) throw RuntimeException("OBJ Loader requires a 'model' key that points to a valid .OBJ model.")
        val modelLocation = modelContents["model"].asString
        val detectCullableFaces = JSONUtils.getBoolean(modelContents, "detectCullableFaces", true)
        val diffuseLighting = JSONUtils.getBoolean(modelContents, "diffuseLighting", false)
        val flipV = JSONUtils.getBoolean(modelContents, "flip-v", false)
        val ambientToFullbright = JSONUtils.getBoolean(modelContents, "ambientToFullbright", true)
        val materialLibraryOverrideLocation = if (modelContents.has("materialLibraryOverride")) JSONUtils.getString(
            modelContents,
            "materialLibraryOverride"
        ) else null
        return loadModel(
            AnimatedOBJModel.ModelSettings(
                ResourceLocation(modelLocation),
                detectCullableFaces,
                diffuseLighting,
                flipV,
                ambientToFullbright,
                materialLibraryOverrideLocation
            )
        )
    }

    fun loadModel(settings: AnimatedOBJModel.ModelSettings): AnimatedOBJModel {
        return modelCache.computeIfAbsent(settings) {
            val resource: IResource
            resource = try {
                manager.getResource(settings.modelLocation)
            } catch (e: IOException) {
                throw RuntimeException("Could not find OBJ model", e)
            }
            try {
                LineReader(resource).use { rdr -> return@computeIfAbsent AnimatedOBJModel(rdr, settings) }
            } catch (e: Exception) {
                throw RuntimeException("Could not read OBJ model", e)
            }
        }
    }

    fun loadMaterialLibrary(materialLocation: ResourceLocation): MaterialLibrary {
        return materialCache.computeIfAbsent(materialLocation) { location: ResourceLocation ->
            val resource: IResource
            resource = try {
                manager.getResource(location)
            } catch (e: IOException) {
                throw RuntimeException("Could not find OBJ material library", e)
            }
            try {
                LineReader(resource).use { rdr -> return@computeIfAbsent MaterialLibrary(rdr) }
            } catch (e: Exception) {
                throw RuntimeException("Could not read OBJ material library", e)
            }
        }
    }

    companion object {
        var INSTANCE = AnimatedOBJLoader()
    }
}