package me.creepinson.creepinoutils.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IConnectable {
    boolean canConnectTo(IBlockAccess blockAccess, BlockPos pos, EnumFacing side);

    boolean canConnectToStrict(IBlockAccess blockAccess, BlockPos pos, EnumFacing side);
}
