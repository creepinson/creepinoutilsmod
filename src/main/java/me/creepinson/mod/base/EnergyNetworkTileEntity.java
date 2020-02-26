package me.creepinson.mod.base;


import me.creepinson.mod.api.network.ElectricityNetwork;
import me.creepinson.mod.api.network.INetwork;
import me.creepinson.mod.api.network.INetworkProducer;
import me.creepinson.mod.api.network.INetworkTile;
import me.creepinson.mod.api.util.math.Vector3;
import me.creepinson.mod.api.util.world.WorldUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;

public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkTile<Integer>, ITickable {

    public void refreshNetwork() {
        if (network == null) {
            setNetwork(new ElectricityNetwork(new Vector3(getPos()), world));
        }
        network.refresh();
    }

    protected boolean connectable = true;
    protected INetwork network;

    @Override
    public void onNeighborChange(Vector3 v) {
        TileEntity te = WorldUtils.getTileEntity(world, v.toBlockPos());
        getNetwork().refresh();
    }

    @Override
    public INetwork getNetwork() {
        if (network == null) {
            this.refreshNetwork();
        }
        return this.network;
    }

    @Override
    public void onLoad() {
        this.refreshNetwork();
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
        return isConnectable() && isActive() && blockAccess.getTileEntity(v.toBlockPos()) != null && !blockAccess.getTileEntity(v.toBlockPos()).isInvalid();
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, Vector3 pos, EnumFacing side) {
        return canConnectTo(blockAccess, pos, side);
    }
}
