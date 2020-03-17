package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.upgrade.Upgrade;
import me.creepinson.creepinoutils.api.upgrade.UpgradeInfo;
import me.creepinson.creepinoutils.api.util.BlockUtils;
import me.creepinson.creepinoutils.api.util.math.ForgeVector;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;

import java.util.*;

public abstract class TileMultiBlock extends TileEntity implements ITickable, INetworkTile, IMultiBlockTile {
    private boolean hasMaster, isMaster;
    protected TileMultiBlock master;
    private boolean firstRun = true;

    public boolean isFormed() {
        return master instanceof TileMultiBlock && !master.isInvalid();
    }

    private Set<Vector3> connections = new HashSet<>(); // TODO: remove

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return !isInvalid();
    }

    @Override
    public void refresh() {
        this.connections = BlockUtils.getTiles(world, getPosition(), TileMultiBlock.class);
        this.initializeMultiBlockIfNecessary();
    }

    @Override
    public void onNeighborChange(Vector3 v) {
        refresh();
    }

    @Override
    public void update() {
        if (firstRun) {
            initializeMultiBlockIfNecessary();
            firstRun = false;
        }
    }

    @Override
    public void setActive(boolean value) {

    }

    @Override
    public boolean upgrade(UpgradeInfo info) {
        return canUpgrade();
    }

    @Override
    public ItemStack removeUpgrade(Upgrade upgrade) {
        return ItemStack.EMPTY;
    }

    public void invalidate() {
        for (EnumFacing facing : EnumFacing.values()) {
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if (te instanceof TileMultiBlock && !te.isInvalid()) {
                ((TileMultiBlock) te).refresh();
            }
        }
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public List<UpgradeInfo> getStoredUpgrades() {
        return null;
    }

    @Override
    public boolean canConnectTo(IBlockAccess world, EnumFacing side) {
        return this.connections.contains(new ForgeVector(pos).offset(side));
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, EnumFacing side) {
        return canConnectTo(blockAccess, side);
    }

    @Override
    public ForgeVector getPosition() {
        return new ForgeVector(pos);
    }

    // Reset method to be run when the master is gone or tells them to
    public void reset() {
        master = null;
        hasMaster = false;
        isMaster = false;
    }

    @Override
    public Set<Vector3> getConnections() {
        return connections;
    }

    public TileMultiBlock getMaster() {
        initializeMultiBlockIfNecessary();
        return master;
    }

    public boolean checkForMaster() {
        return (master != null && (master instanceof TileMultiBlock));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setBoolean("hasMaster", hasMaster);
        data.setBoolean("isMaster", isMaster);
        if (hasMaster() && isMaster()) {
            // Any other values should ONLY BE SAVED TO THE MASTER
        }
        return data;
    }

    public void initializeMultiBlockIfNecessary() {
        if (master == null || master.isInvalid()) {
            List<TileMultiBlock> connected = new ArrayList<>();
            Stack<TileMultiBlock> traversing = new Stack<TileMultiBlock>();
            TileMultiBlock master = this;
            traversing.add(this);
            while (!traversing.isEmpty()) {
                TileMultiBlock tile = traversing.pop();
                if (tile.isMaster) {
                    master = tile;
                }
                connected.add(tile);
                for (EnumFacing facing : EnumFacing.values()) {
                    TileEntity te = tile.getPosition().offset(facing).getTileEntity(world);
                    if (te instanceof TileMultiBlock && !connected.contains(te)) {
                        traversing.add((TileMultiBlock) te);
                    }
                }
            }
            CreepinoUtilsMod.debug(
                    "Setting master to " + master.getPosition().toString() + " for " + connected.size() + " blocks");
            for (TileMultiBlock tile : connected) {
                tile.setMaster(master, connected.size());
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        hasMaster = data.getBoolean("hasMaster");
        isMaster = data.getBoolean("isMaster");
        if (hasMaster() && isMaster()) {
            // Any other values should ONLY BE READ BY THE MASTER

        }
    }

    public boolean hasMaster() {
        return hasMaster;
    }

    public void setMaster(TileMultiBlock master, int blocks) {
        this.master = master;
        boolean wasMaster = isMaster;
        isMaster = master == this;
        if (isMaster) {
            CreepinoUtilsMod.debug("Master set to " + blocks + " blocks");
        } /*
         * else if(!isMaster && wasMaster) {
         *
         * }
         */
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setHasMaster(boolean bool) {
        hasMaster = bool;
    }

    public void setIsMaster(boolean bool) {
        isMaster = bool;
    }
}