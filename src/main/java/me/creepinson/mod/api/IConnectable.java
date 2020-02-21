package me.creepinson.mod.api;

import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IConnectable {
    boolean isConnectable();

    void setConnectable(boolean value);

    boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing side);
}
