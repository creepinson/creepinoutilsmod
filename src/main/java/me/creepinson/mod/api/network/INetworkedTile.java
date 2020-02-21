package me.creepinson.mod.api.network;

import me.creepinson.mod.api.IConnectable;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkedTile<T> extends IConnectable, ITickable {
    boolean isActive();
    void setActive(boolean value);
    INetwork<T> getNetwork();
    void setNetwork(INetwork<T> newNetwork);
    Map<Vector3, EnumFacing> getAdjacentConnections();
    <T> T getStored(); // TOOD: put in interface named INetworkStorage
}
