package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.util.IConnectable;
import me.creepinson.creepinoutils.util.upgrade.IUpgradeable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public interface IBaseTile extends IConnectable, IUpgradeable, ITickableTileEntity {
    boolean isActive();

    void refresh();

    void onNeighborChange(BlockPos pos);

}
