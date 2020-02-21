package me.creepinson.mod.api.network;

import me.creepinson.mod.api.IConnectable;
import me.creepinson.mod.api.upgrade.IUpgradeable;
import net.minecraft.util.ITickable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkedTile<T> extends IConnectable, IUpgradeable, ITickable {
    boolean isActive();
    void setActive(boolean value);
    INetwork<T> getNetwork();
    void setNetwork(INetwork<T> newNetwork);
    <T> T getStored(); // TOOD: put in interface named INetworkStorage
}
