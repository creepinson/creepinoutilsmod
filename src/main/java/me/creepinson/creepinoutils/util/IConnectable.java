package me.creepinson.creepinoutils.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author Theo Paris https://theoparis.com
 * Project creepinoutils
 **/
public interface IConnectable {
    boolean canConnectTo(IWorld blockAccess, BlockPos pos, Direction side);

    boolean canConnectToStrict(IWorld blockAccess, BlockPos pos, Direction side);
}
