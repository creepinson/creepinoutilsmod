package me.creepinson.mod.base;


import me.creepinson.mod.api.network.INetworkedTile;
import mekanism.api.IConfigurable;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public abstract class InventoryNetworkTileEntity extends TileEntity implements INetworkedTile, IConfigurable, IItemHandler, ITickable {

    protected boolean connectable = true;

    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean value) {
        this.active = value;
    }


    @Override
    public boolean isConnectable() {
        return connectable;
    }

    @Override
    public void setConnectable(boolean value) {
        this.connectable = value;
    }

    protected boolean active;


    public void updateConnectedBlocks() {
        for (EnumFacing f : EnumFacing.values()) {
            world.scheduleBlockUpdate(pos.offset(f), world.getBlockState(pos.offset(f)).getBlock(), 0, 1);
        }
    }


    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return connectable;
        } else if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this;
        } else if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }
}
