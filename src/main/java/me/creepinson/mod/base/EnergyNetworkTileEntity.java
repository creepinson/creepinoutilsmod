package me.creepinson.mod.base;


import cofh.redstoneflux.api.IEnergyProvider;
import me.creepinson.mod.api.INetworkedTile;
import me.creepinson.mod.api.util.world.WorldUtils;
import mekanism.api.IConfigurable;
import mekanism.api.energy.EnergyStack;
import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.api.transmitters.IBlockableConnection;
import mekanism.api.transmitters.ITransmitter;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.base.EnergyAcceptorWrapper;
import mekanism.common.base.ITileNetwork;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.tile.transmitter.TileEntityTransmitter;
import mekanism.common.transmitters.grid.EnergyNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;


import java.util.Collection;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkedTile, IConfigurable, IStrictEnergyAcceptor, IStrictEnergyOutputter, IEnergyProvider, ITickable {

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
        if (capability == Capabilities.ENERGY_OUTPUTTER_CAPABILITY) {
            return connectable;
        } else if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == Capabilities.ENERGY_OUTPUTTER_CAPABILITY) {
            return (T) this;
        } else if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }
}
