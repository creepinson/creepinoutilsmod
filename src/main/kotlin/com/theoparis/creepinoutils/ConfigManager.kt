package com.theoparis.creepinoutils

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.IntValue
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent

@Mod.EventBusSubscriber(modid = CreepinoUtilsMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ConfigManager {
    companion object {
        var maxLights: Int = 20
        var maxDistance: Int = 64
        var disableLights: Boolean = false
        var eightBitNightmare: Boolean = false
        var enableTorchImplementation: Boolean = true
        val isLightingEnabled: Boolean
            get() = !disableLights

        var spec: ForgeConfigSpec? = null

        private var cfg: CreepinoUtilsConfig

        init {
            val specPair = ForgeConfigSpec.Builder().configure(::CreepinoUtilsConfig)
            spec = specPair.right
            cfg = specPair.left
        }

        @SubscribeEvent
        fun onModConfigEvent(configEvent: ModConfigEvent) {
            if (configEvent.config.spec === spec) {
                bakeConfig()
            }
        }

        private fun bakeConfig() {
            maxLights = cfg.maxLights?.get()!!
            maxDistance = cfg.maxDistance?.get()!!
            disableLights = cfg.disableLights?.get()!!
            eightBitNightmare = cfg.eightBitNightmare?.get()!!
            enableTorchImplementation = cfg.enableTorchImplementation?.get()!!
        }
    }

    class CreepinoUtilsConfig(builder: ForgeConfigSpec.Builder) {
        var maxLights: IntValue? = null
        var maxDistance: IntValue? = null
        var disableLights: ForgeConfigSpec.BooleanValue? = null
        var eightBitNightmare: ForgeConfigSpec.BooleanValue? = null
        var enableTorchImplementation: ForgeConfigSpec.BooleanValue? = null

        init {
            builder.comment("Albedo Config")
            builder.push("Albedo")
            disableLights = builder
                .comment("Disables albedo lighting.")
                .translation("albedo.config.enableLights")
                .define("disableLights", false)
            maxLights = builder
                .comment("The maximum number of lights allowed to render in a scene. Lights are sorted nearest-first, so further-away lights will be culled after nearer lights.")
                .translation("albedo.config.maxLights")
                .defineInRange("maxLights", 20, 0, 200)
            eightBitNightmare = builder
                .comment("Enables retro mode.")
                .translation("albedo.config.eightBitNightmare")
                .define("eightBitNightmare", false)
            enableTorchImplementation = builder
                .comment("Enables default torch item implementation.")
                .translation("albedo.config.enableTorchImplementation")
                .worldRestart()
                .define("enableTorchImplementation", true)
            maxDistance = builder
                .comment("The maximum distance lights can be before being culled.")
                .translation("albedo.config.maxDistance")
                .defineInRange("maxDistance", 64, 16, 256)
            builder.pop()
            spec = builder.build()
        }
    }
}