package me.creepinson.creepinoutils.base;

import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.util.BlockUtils;
import me.creepinson.creepinoutils.util.TensorUtils;
import me.creepinson.creepinoutils.util.upgrade.Upgrade;
import me.creepinson.creepinoutils.util.upgrade.UpgradeInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.List;
import java.util.Stack;

public abstract class AbstractMultiBlockTile extends BaseTile implements IMultiBlockTile {
    private boolean hasMaster, isMaster;
    protected AbstractMultiBlockTile master;
    private boolean firstRun = true;

    public AbstractMultiBlockTile(TileEntityType<?> type) {
        super(type);
    }

    public boolean isFormed() {
        return master != null && !master.isRemoved();
    }

    @Override
    public boolean isActive() {
        return !isRemoved();
    }

    @Override
    public void refresh() {
        this.connections = BlockUtils.getTiles(world, pos, AbstractMultiBlockTile.class);
        this.initializeMultiBlockIfNecessary();
    }

    @Override
    public void onNeighborChange(Tensor v) {
        refresh();
    }

    @Override
    public void tick() {
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
        for (Direction facing : Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if (te instanceof AbstractMultiBlockTile && !te.isRemoved()) {
                ((AbstractMultiBlockTile) te).refresh();
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
    public boolean canConnectTo(IWorld world, BlockPos pos, Direction side) {
        return this.connections.contains(pos.offset(side));
    }

    @Override
    public boolean canConnectToStrict(IWorld blockAccess, BlockPos pos, Direction side) {
        return canConnectTo(blockAccess, pos, side);
    }

    // Reset method to be run when the master is gone or tells them to
    public void reset() {
        master = null;
        hasMaster = false;
        isMaster = false;
    }

    public AbstractMultiBlockTile getMaster() {
        initializeMultiBlockIfNecessary();
        return master;
    }

    public boolean checkForMaster() {
        return master != null;
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        super.write(data);
        data.putBoolean("hasMaster", hasMaster);
        data.putBoolean("isMaster", isMaster);
        if (hasMaster() && isMaster()) {
            // Any other values should ONLY BE SAVED TO THE MASTER
        }
        return data;
    }

    public void initializeMultiBlockIfNecessary() {
        if (master == null || master.isRemoved()) {
            Stack<AbstractMultiBlockTile> traversing = new Stack<AbstractMultiBlockTile>();
            AbstractMultiBlockTile master = this;
            traversing.add(this);
            while (!traversing.isEmpty()) {
                AbstractMultiBlockTile tile = traversing.pop();
                if (tile.isMaster) {
                    master = tile;
                }
                connections.add(tile.getPos());
                for (Direction facing : Direction.values()) {
                    TileEntity te = TensorUtils.getTileOffset(world, tile.getPosition(), facing);
                    if (te instanceof AbstractMultiBlockTile && !connections.contains(te.getPos())
                            && canConnectToMultiBlock(facing, te)) {
                        traversing.add((AbstractMultiBlockTile) te);
                    }
                }
            }
            CreepinoUtilsMod.LOGGER.info(
                    "Setting master to " + master.getPosition().toString() + " for " + connections.size() + " blocks");
            for (BlockPos tilePos : connections) {
                TileEntity tile = world.getTileEntity(tilePos);
                if (tile instanceof AbstractMultiBlockTile && !tile.isRemoved()) {
                    ((AbstractMultiBlockTile) tile).setMaster(master);
                }
            }
        }
    }

    /**
     * @param facing The direction the connection is coming from
     * @param te     The tile entity of the multi block that is being checked
     * @return Whether or not this multiblock tile can connect to the other one.
     */
    public boolean canConnectToMultiBlock(Direction facing, TileEntity te) {
        return true;
    }

    @Override
    public void read(CompoundNBT data) {
        super.read(data);
        hasMaster = data.getBoolean("hasMaster");
        isMaster = data.getBoolean("isMaster");
        if (hasMaster() && isMaster()) {
            // Any other values should ONLY BE READ BY THE MASTER

        }
    }

    public boolean hasMaster() {
        return hasMaster;
    }

    public void setMaster(AbstractMultiBlockTile master) {
        this.master = master;
        boolean wasMaster = isMaster;
        isMaster = master == this;
        if (isMaster) {
            CreepinoUtilsMod.LOGGER.info("Master set to " + master.toString());
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