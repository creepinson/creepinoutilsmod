package me.creepinson.creepinoutils.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.upgrade.Upgrade;
import me.creepinson.creepinoutils.api.upgrade.UpgradeInfo;
import me.creepinson.creepinoutils.api.util.BlockUtils;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class TileMultiBlock extends TileEntity implements ITickable, INetworkTile {
    private boolean hasMaster, isMaster;
    private Vector3 masterPos;

    private Set<Vector3> connections = new HashSet<>();

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return !isInvalid();
    }

    @Override
    public void refresh() {
        this.connections = BlockUtils.getTiles(world, getPosition(), TileMultiBlock.class);
    }

    @Override
    public void onNeighborChange(Vector3 v) {
        refresh();
    }


    @Override
    public void update() {

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
        return this.connections.contains(new Vector3(pos).offset(side));
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, EnumFacing side) {
        return canConnectTo(blockAccess, side);
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(pos);
    }

    // Reset method to be run when the master is gone or tells them to
    public void reset() {
        masterPos = new Vector3();
        hasMaster = false;
        isMaster = false;
    }
    
    
    @Override
    public Set<Vector3> getConnections() {
        return connections;
    }

    public abstract void resetStructure();

    public TileMultiBlock getMaster() {
        if(checkForMaster()) return (TileMultiBlock)masterPos.getTileEntity(world);
        else return null;
    }

    public boolean checkForMaster() {
        TileEntity tile = masterPos.getTileEntity(world);
        return (tile != null && (tile instanceof TileMultiBlock));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("masterX", masterPos.intX());
        data.setInteger("masterY", masterPos.intY());
        data.setInteger("masterZ", masterPos.intZ());
        data.setBoolean("hasMaster", hasMaster);
        data.setBoolean("isMaster", isMaster);
        if (hasMaster() && isMaster()) {
            // Any other values should ONLY BE SAVED TO THE MASTER
        }
        return data;
    }

    public abstract boolean checkMultiBlockForm();

    public abstract void setupStructure();

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        masterPos = new Vector3(data.getInteger("masterX"), data.getInteger("masterY"), data.getInteger("masterZ"));
        hasMaster = data.getBoolean("hasMaster");
        isMaster = data.getBoolean("isMaster");
        if (hasMaster() && isMaster()) {
            // Any other values should ONLY BE READ BY THE MASTER

        }
    }

    public boolean hasMaster() {
        return hasMaster;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public Vector3 getMasterPosition() {
        return masterPos;
    }

    public void setHasMaster(boolean bool) {
        hasMaster = bool;
    }

    public void setIsMaster(boolean bool) {
        isMaster = bool;
    }
}