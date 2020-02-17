package me.creepinson.mod.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface INetworkedTile extends IConnectable, ITickable {
    <T> T getCapability(Capability<T> capability, EnumFacing facing);
    boolean isActive();
    void setActive(boolean value);
}
