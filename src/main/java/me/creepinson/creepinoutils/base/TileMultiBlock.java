package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.api.util.math.Vector;
import me.creepinson.creepinoutils.util.BlockUtils;
import me.creepinson.creepinoutils.util.VectorUtils;
import me.creepinson.creepinoutils.util.upgrade.Upgrade;
import me.creepinson.creepinoutils.util.upgrade.UpgradeInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class TileMultiBlock extends BaseTile implements IMultiBlockTile {
    private boolean hasMaster, isMaster;
    protected TileMultiBlock master;
    private boolean firstRun = true;

    public boolean isFormed() {
        return master != null && !master.isInvalid();
    }

    List<TileMultiBlock> connected = new ArrayList<>();

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return !isInvalid();
    }

    @Override
    public void refresh() {
        this.connections = BlockUtils.getTiles(world, pos, TileMultiBlock.class);
        this.initializeMultiBlockIfNecessary();
    }

    @Override
    public void onNeighborChange(Vector v) {
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
    public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.connections.contains(pos.offset(side));
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return canConnectTo(blockAccess, pos, side);
    }

    // Reset method to be run when the master is gone or tells them to
    public void reset() {
        master = null;
        hasMaster = false;
        isMaster = false;
    }

    @Override
    public Set<BlockPos> getConnections() {
        return connected.stream().map(TileMultiBlock::getPos).collect(Collectors.toSet());
    }

    public TileMultiBlock getMaster() {
        initializeMultiBlockIfNecessary();
        return master;
    }

    public boolean checkForMaster() {
        return master != null;
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
                    TileEntity te = VectorUtils.getTileOffset(world, tile.getPosition(), facing);
                    if (te instanceof TileMultiBlock && !connected.contains(te) && canConnectToMultiBlock(facing, te)) {
                        traversing.add((TileMultiBlock) te);
                    }
                }
            }
            CreepinoUtilsMod.getInstance().debug(
                    "Setting master to " + master.getPosition().toString() + " for " + connected.size() + " blocks");
            for (TileMultiBlock tile : connected) {
                tile.setMaster(master);
            }
        }
    }

    /**
     * @param facing The direction the connection is coming from
     * @param te     The tile entity of the multi block that is being checked
     * @return Whether or not this multiblock tile can connect to the other one.
     */
    public boolean canConnectToMultiBlock(EnumFacing facing, TileEntity te) {
        return true;
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

    public void setMaster(TileMultiBlock master) {
        this.master = master;
        boolean wasMaster = isMaster;
        isMaster = master == this;
        if (isMaster) {
            CreepinoUtilsMod.getInstance().debug("Master set to " + master.getName());
        }
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