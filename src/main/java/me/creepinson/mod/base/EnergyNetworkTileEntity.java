package me.creepinson.mod.base;


import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyStorage;
import me.creepinson.mod.api.network.ElectricityNetwork;
import me.creepinson.mod.api.network.INetwork;
import me.creepinson.mod.api.network.INetworkedTile;
import me.creepinson.mod.api.util.math.Vector3;
import mekanism.api.IConfigurable;
import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkedTile<Float>, ITickable {

    protected boolean connectable = true;
    protected INetwork network;


    @Override
    public INetwork getNetwork() {
        return this.network;
    }

    @Override
    public void onLoad() {
        if(this.network == null) {
            this.setNetwork(new ElectricityNetwork(new Vector3(pos), world));
            this.getNetwork().refresh();
        }
    }

    @Override
    public void setNetwork(INetwork newNetwork) {
        this.network = newNetwork;
    }

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
