package me.creepinson.creepinoutils.util.client.gui;

import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
/**
 * A utilty class for rendering a preview of a group blocks. Rendering code
 * taken from https://github.com/thraaawn/CompactMachines/
 */
public class BlockPreviewRenderer {
    public float rotateX, rotateY;
    private int chunkX, chunkZ;
    private int previewSize = 5;

    private int glListId = -1;
    private boolean requiresNewDisplayList;
    private boolean renderTileEntities = true, renderLivingEntities = true;

    public BlockPreviewRenderer(World world, int previewSize, int chunkX, int chunkZ) {
        this.previewSize = previewSize;
        this.requiresNewDisplayList = true;
    }

    // TODO: reimplement for 1.15
}
