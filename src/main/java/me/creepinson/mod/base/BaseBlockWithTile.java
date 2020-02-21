package me.creepinson.mod.base;

import me.creepinson.mod.api.util.math.Coord4D;
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
        if (!world.isRemote) {
            TileEntity tileEntity = new Coord4D(pos, world).vector.getTileEntity(world);
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        worldIn.removeTileEntity(pos);
    }
}
