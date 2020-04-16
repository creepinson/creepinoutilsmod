package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.util.IConnectable;
import me.creepinson.creepinoutils.util.upgrade.IUpgradeable;
import me.creepinson.creepinoutils.util.util.math.ForgeVector;
import net.minecraft.util.ITickable;

import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public interface IBaseTile extends IConnectable, IUpgradeable, ITickable {
    Set<ForgeVector> getConnections();

    boolean isActive();

    void refresh();

    ForgeVector getPosition();

    void onNeighborChange(Vector3 vector3);

}
