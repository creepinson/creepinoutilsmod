package me.creepinson.creepinoutils.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IConnectable {
    boolean canConnectTo(IBlockAccess blockAccess, EnumFacing side);

    boolean canConnectToStrict(IBlockAccess blockAccess, EnumFacing side);
}
