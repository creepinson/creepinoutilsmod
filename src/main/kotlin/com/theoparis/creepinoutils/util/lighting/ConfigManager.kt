package com.theoparis.creepinoutils.util.lighting

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.IntValue

class ConfigManager {
    init {
        val builder = ForgeConfigSpec.Builder()
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

    companion object {
        var spec: ForgeConfigSpec? = null
        var maxLights: IntValue? = null
        var maxDistance: IntValue? = null
        var disableLights: ForgeConfigSpec.BooleanValue? = null
        var eightBitNightmare: ForgeConfigSpec.BooleanValue? = null
        var enableTorchImplementation: ForgeConfigSpec.BooleanValue? = null
        val isLightingEnabled: Boolean
            get() = !disableLights!!.get()

    }
}