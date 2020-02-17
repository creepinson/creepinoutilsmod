package me.creepinson.mod.block;

import me.creepinson.mod.api.util.world.WorldUtils;
import me.creepinson.mod.base.BaseBlockWithTile;
import me.creepinson.mod.tile.TileEntityAnimationTest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class BlockAnimationTest extends BaseBlockWithTile implements ITileEntityProvider {

    public BlockAnimationTest(Material mat, ResourceLocation name, CreativeTabs tab) {
        super(mat, name, tab);
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            TileEntity te = WorldUtils.getTileEntity(world, pos);
            if(te instanceof TileEntityAnimationTest) {
                TileEntityAnimationTest tile = (TileEntityAnimationTest)te;
            }
            return true;
        } else {
            return false;
        }

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAnimationTest();
    }
}
