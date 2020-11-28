package com.theoparis.creepinoutils

import com.theoparis.creepinoutils.util.client.gl.animation.AnimatedOBJLoader
import com.theoparis.creepinoutils.util.lighting.Albedo
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(CreepinoUtilsMod.ID)
object CreepinoUtilsMod {
    // the modid of our mod
    const val ID: String = "creepinoutils"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger()

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigManager.spec)

        Albedo()

        // usage of the KotlinEventBus
        MOD_BUS.addListener(CreepinoUtilsMod::onClientSetup)
        FORGE_BUS.addListener(CreepinoUtilsMod::onServerAboutToStart)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
        MOD_BUS.register(this)
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    fun modelRegistry(event: ModelRegistryEvent) {
        ModelLoaderRegistry.registerLoader(ResourceLocation(ID, "obj"), AnimatedOBJLoader.INSTANCE)
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerAboutToStart(event: FMLServerAboutToStartEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }
}