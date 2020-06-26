package me.creepinson.creepinoutils.block;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.base.BaseBlockWithTile;
import me.creepinson.creepinoutils.base.BaseTile;
import me.creepinson.creepinoutils.tile.AnimationTestTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Theo Paris https://theoparis.com Project creepinoutils
 **/
public class AnimationTestBlock extends BaseBlockWithTile {

    public AnimationTestBlock(String name, ItemGroup tab) {
        super(new ResourceLocation(CreepinoUtilsMod.MOD_ID, name),
                Block.Properties.create(Material.ROCK).hardnessAndResistance(1));
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof AnimationTestTile) {
                AnimationTestTile tile = (AnimationTestTile) te;
                if (player.isCrouching() && player.getHeldItem(hand).isEmpty()) {
                    tile.setConnectable(!tile.isConnectable());
                } else {
                    tile.onClick();
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te,
            ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        if (te != null) {
            ((BaseTile) te).refresh();
        }
    }
    /*
     * @Nullable
     * 
     * @Override public TileEntity createTileEntity(BlockState state, IBlockReader
     * world) { return new TileEntityAnimationTest(); }
     */

}
