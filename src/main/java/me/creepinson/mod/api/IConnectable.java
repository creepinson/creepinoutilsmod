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
<<<<<<< HEAD

    boolean canConnectTo(IBlockAccess blockAccess, Vector3 vector3, EnumFacing f);
=======
    boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing side);
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687
}
