package me.creepinson.creepinoutils.api;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IConnectable {
    boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing side);
    boolean canConnectToStrict(IBlockAccess blockAccess, Vector3 pos, EnumFacing side);
}
