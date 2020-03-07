package me.creepinson.creepinoutils.block;

import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.api.util.world.WorldUtils;
import me.creepinson.creepinoutils.base.BaseBlockWithTile;
import me.creepinson.creepinoutils.tile.TileEntityAnimationTest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author Creepinson http://gitlab.com/creepinson Project creepinoutils
 **/
public class BlockAnimationTest extends BaseBlockWithTile {

    public BlockAnimationTest(Material mat, ResourceLocation name, CreativeTabs tab) {
        super(mat, name, tab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
            EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = WorldUtils.getTileEntity(world, new Vector3(pos));
            if (te instanceof TileEntityAnimationTest) {
                TileEntityAnimationTest tile = (TileEntityAnimationTest) te;
                if (player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                    tile.setConnectable(!tile.isConnectable());
                } else {
                    tile.onClick();
                }
            }
        }
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
            @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (te != null) {
            ((INetworkTile) te).refresh();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAnimationTest();
    }
}
