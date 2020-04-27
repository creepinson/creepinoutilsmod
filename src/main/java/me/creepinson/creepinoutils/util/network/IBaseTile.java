package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.util.IConnectable;
import me.creepinson.creepinoutils.util.upgrade.IUpgradeable;
import net.minecraft.util.ITickable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public interface IBaseTile extends IConnectable, IUpgradeable, ITickable {
    boolean isActive();

    void refresh();

    Vector3 getPosition();

    void onNeighborChange(Vector3 vector3);

}
