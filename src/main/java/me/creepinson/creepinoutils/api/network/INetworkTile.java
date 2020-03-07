package me.creepinson.creepinoutils.api.network;

import java.util.Set;

import me.creepinson.creepinoutils.api.IConnectable;
import me.creepinson.creepinoutils.api.upgrade.IUpgradeable;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkTile extends IConnectable, IUpgradeable, ITickable {
    boolean isActive();

    Vector3 getPosition();
    World getWorld();

    void setActive(boolean value);

    Set<Vector3> getConnections();

    void refresh();

    void onNeighborChange(Vector3 vector3);
}
