package me.creepinson.creepinoutils.util.network;

import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.util.IConnectable;
import me.creepinson.creepinoutils.util.upgrade.IUpgradeable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Theo Paris https://theoparis.com
 **/
public interface IBaseTile extends IConnectable, IUpgradeable, ITickableTileEntity {
    boolean isActive();

    void refresh();

    void onNeighborChange(Tensor pos);

}
