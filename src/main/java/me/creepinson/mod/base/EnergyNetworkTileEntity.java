package me.creepinson.mod.base;


import me.creepinson.mod.api.network.ElectricityNetwork;
import me.creepinson.mod.api.network.INetwork;
import me.creepinson.mod.api.network.INetworkProducer;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkProducer<Integer>, IEnergyStorage, ITickable {

    public void resetNetwork() {
        if (getNetwork() == null) {
            setNetwork(new ElectricityNetwork(new Vector3(this.getPos()), world));
            getNetwork().refresh();
        }
    }

    protected boolean connectable = true;
    protected INetwork network;


    @Override
    public INetwork getNetwork() {
        return this.network;
    }

    @Override
    public void onLoad() {
        if (this.network == null) {
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
        if (getNetwork() != null) {
            getNetwork().refresh();
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return connectable;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canExtract() {
        return isConnectable() && isActive();
    }

    @Override
    public boolean canReceive() {
        return isConnectable() && isActive();
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 vector3, EnumFacing f) {
        return isConnectable() && isActive();
    }
}
