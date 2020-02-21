package me.creepinson.mod.api;

import me.creepinson.mod.api.network.INetwork;
import me.creepinson.mod.api.upgrade.IUpgradeable;
import net.minecraft.util.ITickable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkedTile<T> extends IConnectable, IUpgradeable, ITickable {
    boolean isActive();
    void setActive(boolean value);
    void setNetwork(INetwork<T> newNetwork);
    INetwork<T> getNetwork();
    T getStored();
}
