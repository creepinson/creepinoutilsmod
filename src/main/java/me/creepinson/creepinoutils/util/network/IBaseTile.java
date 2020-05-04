package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.util.IConnectable;
import me.creepinson.creepinoutils.util.upgrade.IUpgradeable;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public interface IBaseTile extends IConnectable, IUpgradeable, ITickable {
    boolean isActive();

    void refresh();

    void onNeighborChange(BlockPos pos);

}
