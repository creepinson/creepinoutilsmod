package me.creepinson.creepinoutils.base;


import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.util.compat.EnergyUtils;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.api.util.world.WorldUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkTile<Integer>, ITickable {

    private Set<TileEntity> connections = new HashSet<>();

    public void refreshNetwork() {
        connections = new HashSet<TileEntity>(Arrays.asList(EnergyUtils.getAdjacentPowerConnections(this)));
    }

    protected boolean connectable = true;

    @Override
    public void onNeighborChange(Vector3 v) {
        TileEntity te = WorldUtils.getTileEntity(world, v.toBlockPos());
        refreshNetwork();
    }

    @Override
    public void onLoad() {
        this.refreshNetwork();
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
        if (this.getNetwork() != null) {
            this.getNetwork().updateConnectedBlocks();
        }
    }

    public boolean isConnectable() {
        return connectable;
    }

    protected boolean active;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 v, EnumFacing f) {
        if (connections.isEmpty()) refreshNetwork();
        return isConnectable() && isActive() && connections.contains(v.getTileEntity(world));
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, Vector3 pos, EnumFacing side) {
        return canConnectTo(blockAccess, pos, side);
    }
}
