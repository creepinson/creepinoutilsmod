package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public abstract class BaseBlockWithTile extends BaseBlock implements ITileEntityProvider {

    public BaseBlockWithTile(Material mat, ResourceLocation name, CreativeTabs tab, float hardness, float resistance, int harvest, String tool) {
        super(mat, name, tab, hardness, resistance, harvest, tool);
    }

    public BaseBlockWithTile(Material mat, ResourceLocation name, CreativeTabs tab, float hardness, float resistance) {
        super(mat, name, tab, hardness, resistance);
    }

    public BaseBlockWithTile(Material mat, ResourceLocation name, CreativeTabs tab) {
        super(mat, name, tab);
    }

    public BaseBlockWithTile(Material mat, ResourceLocation name) {
        super(mat, name);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BaseTile) {
            ((BaseTile) te).onNeighborChange(VectorUtils.fromBlockPos(pos));
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        worldIn.removeTileEntity(pos);
    }
}
