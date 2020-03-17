package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.util.BlockUtils;
import me.creepinson.creepinoutils.api.util.math.ForgeVector;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.Set;

public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkTile, IEnergyStorage {

    private Set<Vector3> connections = new HashSet<>();

    @Override
    public void refresh() {
        connections = BlockUtils.getTilesWithCapability(world, new ForgeVector(pos), CapabilityEnergy.ENERGY);
    }

    protected boolean connectable = true;

    @Override
    public void onNeighborChange(Vector3 v) {
        refresh();
    }

    @Override
    public void onLoad() {
        this.refresh();
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean value) {
        this.active = value;
    }

    public void setConnectable(boolean value) {
        this.connectable = value;
        this.refresh();
    }

    public boolean isConnectable() {
        return connectable;
    }

    protected boolean active;

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
    public ForgeVector getPosition() {
        return new ForgeVector(pos);
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, EnumFacing f) {
        if (connections.isEmpty())
            refresh();
        return isConnectable() && isActive() && connections.contains(getPosition().offset(f));
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, EnumFacing side) {
        return canConnectTo(blockAccess, side);
    }
}
