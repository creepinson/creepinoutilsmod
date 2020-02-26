package me.creepinson.mod.api.network;

import me.creepinson.mod.api.IConnectable;
import me.creepinson.mod.api.upgrade.IUpgradeable;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.util.ITickable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkTile<T> extends IConnectable, IUpgradeable, ITickable {
    boolean isActive();
    void setActive(boolean value);
    void setNetwork(INetwork<T> newNetwork);
    INetwork<T> getNetwork();
    T getStored();
    void onNeighborChange(Vector3 vector3);
}
