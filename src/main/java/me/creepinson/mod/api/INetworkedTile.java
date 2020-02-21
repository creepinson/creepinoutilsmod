package me.creepinson.mod.api;

import me.creepinson.mod.api.network.INetwork;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkedTile<T> extends IConnectable, ITickable {
    boolean isActive();
    void setActive(boolean value);
    INetwork<T> getNetwork();
}
