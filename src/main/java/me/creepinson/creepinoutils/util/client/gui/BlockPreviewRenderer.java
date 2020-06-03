package me.creepinson.creepinoutils.util.client.gui;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.util.world.ChunkUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
/**
 * A utilty class for rendering a preview of a group blocks.
 * Rendering code taken from https://github.com/thraaawn/CompactMachines/
 */
public class BlockPreviewRenderer {
    public float rotateX, rotateY;
    private int chunkX, chunkZ;
    private int previewSize = 5;

    private int glListId = -1;
    private boolean requiresNewDisplayList;

    private PreviewChunkLoader chunkHandler;
    private boolean renderTileEntities = true, renderLivingEntities = true;

    public BlockPreviewRenderer(World world, int previewSize, int chunkX, int chunkZ) {
        this.previewSize = previewSize;
        this.chunkHandler = new PreviewChunkLoader(world);
        this.requiresNewDisplayList = true;
    }

    public void draw() {
        if (requiresNewDisplayList) {
            List<BlockPos> toRenderCopy = this.chunkHandler.getRenderListForChunk(chunkX, chunkZ);
            if (toRenderCopy != null) {
                TileEntityRendererDispatcher.instance.setWorld(chunkHandler.world);

                if (glListId != -1) {
                    GLAllocation.deleteDisplayLists(glListId);
                }

                glListId = GLAllocation.generateDisplayLists(1);
                GlStateManager.glNewList(glListId, GL11.GL_COMPILE);

                GlStateManager.pushAttrib();
                GlStateManager.pushMatrix();

                GlStateManager.translate(0, -40, 0);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();

                // Aaaand render
                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                GlStateManager.disableAlpha();
                this.renderLayer(blockrendererdispatcher, buffer, BlockRenderLayer.SOLID, toRenderCopy);
                GlStateManager.enableAlpha();
                this.renderLayer(blockrendererdispatcher, buffer, BlockRenderLayer.CUTOUT_MIPPED, toRenderCopy);
                this.renderLayer(blockrendererdispatcher, buffer, BlockRenderLayer.CUTOUT, toRenderCopy);
                GlStateManager.shadeModel(GL11.GL_FLAT);
                this.renderLayer(blockrendererdispatcher, buffer, BlockRenderLayer.TRANSLUCENT, toRenderCopy);

                tessellator.draw();


                GlStateManager.popMatrix();
                GlStateManager.popAttrib();

                GlStateManager.glEndList();
            }
        }

        if (chunkHandler.world != null) {
            renderChunk();
        } else {
            // TODO: Draw unused screen and help information; account for future updates with loot compact machines
        }
    }

    public void renderChunk() {
        // Init GlStateManager
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableFog();
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableCull();

        double scaleToWindow = 1.0d / previewSize;
        scaleToWindow *= 8.0d;
        GlStateManager.scale(scaleToWindow, scaleToWindow, scaleToWindow);

        // Increase size a bit more at the end
        GlStateManager.scale(2.0f, 2.0f, 2.0f);

        GlStateManager.scale(-1.0f, 1.0f, 1.0f);

        // Tilt a bit
        GlStateManager.rotate(-rotateY, 1.0f, 0.0f, 0.0f);

        // Turn it around
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, -1.0f);

        // Auto rotate
        /*
        int rotationTime = 120; // 6 seconds to rotate one time
        int rotationTicks = (int) (Minecraft.getMinecraft().world.getWorldTime() % rotationTime * 8);
        float percent = ((rotationTicks / 8.0f) + partialTicks) / rotationTime;
        */

        GlStateManager.rotate(rotateX, 0.0f, 1.0f, 0.0f);

        // Get rid of the wall+floor
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);

        // Now center at the middle (8 * inside offset)
        // 7x7x7 -> 5 -> 2.5 * 8 = 20.0f
        float shift = (previewSize - 1) * -4.0f;
        GlStateManager.translate(shift, shift, shift);

        GlStateManager.scale(8.0f, 8.0f, 8.0f);

        // Aaaand render
        GlStateManager.callList(glListId);

        GlStateManager.resetColor();

        List<BlockPos> toRenderCopy = chunkHandler.getRenderListForChunk(chunkX, chunkZ);
        if (renderTileEntities) {
            this.renderTileEntities(TileEntityRendererDispatcher.instance, toRenderCopy);
        }

        if (renderLivingEntities) {
            this.renderEntities();
            GlStateManager.enableBlend();
        }

        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
    }

    public void renderLayer(BlockRendererDispatcher blockrendererdispatcher, BufferBuilder buffer, BlockRenderLayer renderLayer, List<BlockPos> toRender) {
        for (BlockPos pos : toRender) {
            IBlockState state = chunkHandler.world.getBlockState(pos);

            if (!state.getBlock().canRenderInLayer(state, renderLayer)) {
                continue;
            }

            try {
                state = state.getActualState(chunkHandler.world, pos);
            } catch (Exception e) {
                CreepinoUtilsMod.getInstance().debug("Could not determine actual state of block: " + state.getBlock());
            }

            ForgeHooksClient.setRenderLayer(renderLayer);

            try {
                TileEntity te = chunkHandler.world.getTileEntity(pos);
                if (te != null && ChunkUtils.erroneousTiles.contains(te.getClass().getName())) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }

            try {
                blockrendererdispatcher.renderBlock(state, pos, chunkHandler.world, buffer);
            } catch (Throwable e) {
                CreepinoUtilsMod.getInstance().debug("Failed rendering of block: " + state.getBlock());
            }

            ForgeHooksClient.setRenderLayer(null);
        }
    }

    private void renderEntities() {
        if (chunkHandler.world == null) {
            return;
        }

        ClassInheritanceMultiMap<Entity> entities = chunkHandler.world.getChunk(chunkX, chunkZ).getEntityLists()[2];
        for (Entity entity : entities) {
            renderEntity(entity);
        }
    }

    private static void renderEntity(Entity entity) {
        GlStateManager.pushMatrix();

        double x = entity.posX % 1024;
        double y = entity.posY - 40;
        double z = entity.posZ;

        RenderHelper.enableStandardItemLighting();

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        try {
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity, x, y, z, entity.rotationYaw, 1.0F, false);
        } catch (Exception e) {
            CreepinoUtilsMod.getInstance().debug("Could not render entity " + entity.getClass().getSimpleName() + ": " + e.getMessage());
        }

        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
    }

    private void renderTileEntities(TileEntityRendererDispatcher renderer, List<BlockPos> toRender) {
        if (toRender == null) {
            return;
        }
        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        for (BlockPos pos : toRender) {
            TileEntity te;
            try {
                te = chunkHandler.world.getTileEntity(pos);
            } catch (Exception e) {
                continue;
            }

            if (te != null) {
                te.setWorld(chunkHandler.world);
                te.setPos(pos);

                if (te instanceof ITickable) {
                    try {
                        ((ITickable) te).update();
                    } catch (Exception e) {
                    }
                }

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();
                renderer.renderEngine = Minecraft.getMinecraft().renderEngine;

                renderer.preDrawBatch();
                try {
                    renderer.render(te, pos.getX() % 1024, pos.getY() - 40, pos.getZ(), 0.0f);
                } catch (Exception e) {
                    CreepinoUtilsMod.getInstance().getLogger().warn("Could not render tile entity '%s': %s", te.getClass().getSimpleName(), e.getMessage());
                }
                renderer.drawBatch(0);

                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
        }

        ForgeHooksClient.setRenderLayer(null);
    }
}
