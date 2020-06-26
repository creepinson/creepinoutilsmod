package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.util.TensorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * @author Theo Paris https://theoparis.com
 * Project creepinoutils
 **/
public abstract class BaseBlockWithTile extends Block {

    public BaseBlockWithTile(Properties properties) {
        super(properties);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BaseTile) {
            ((BaseTile) te).onNeighborChange(TensorUtils.fromBlockPos(pos));
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
