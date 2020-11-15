package com.theoparis.creepinoutils.util.client.gui

import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(value = Dist.CLIENT)
/**
 * A utilty class for rendering a preview of a group blocks. Rendering code
 * taken from https://github.com/thraaawn/CompactMachines/
 */
class BlockPreviewRenderer(world: World, previewSize: Int, chunkX: Int, chunkZ: Int) {
    var rotateX = 0f
    var rotateY = 0f
    private val chunkX = 0
    private val chunkZ = 0
    private val previewSize = 5
    private val glListId = -1
    private val requiresNewDisplayList: Boolean
    private val renderTileEntities = true
    private val renderLivingEntities = true

    init {
        requiresNewDisplayList = true
    } // TODO: reimplement for 1.15
}