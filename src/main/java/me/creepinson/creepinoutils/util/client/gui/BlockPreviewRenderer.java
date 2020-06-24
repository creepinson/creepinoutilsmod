package me.creepinson.creepinoutils.util.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.util.TensorUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

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
