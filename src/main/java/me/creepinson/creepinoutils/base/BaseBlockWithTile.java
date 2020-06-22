package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public abstract class BaseBlockWithTile extends BaseBlock {

    public BaseBlockWithTile(ResourceLocation name, Properties properties) {
        super(name, properties);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BaseTile) {
            ((BaseTile) te).onNeighborChange(VectorUtils.fromBlockPos(pos));
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
