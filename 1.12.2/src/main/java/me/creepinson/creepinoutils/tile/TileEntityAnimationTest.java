package me.creepinson.creepinoutils.tile;


import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.api.network.INetwork;
import me.creepinson.creepinoutils.api.network.INetworkProducer;
import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.upgrade.Upgrade;
import me.creepinson.creepinoutils.api.upgrade.UpgradeInfo;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.base.EnergyNetworkTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/

public class TileEntityAnimationTest extends EnergyNetworkTileEntity implements INetworkProducer<Integer> {
    public void onClick() {
        setActive(!isActive());
    }

    @Override
    public void setActive(boolean value) {
        super.setActive(value);
        if (CreepinoUtilsMod.getInstance().isDebug() && !world.isRemote) {
            CreepinoUtilsMod.getInstance().getLogger().info("New Active Value: " + this.isActive());
        }
    }

    @Override
    public void setNetwork(INetwork<Integer> newNetwork) {

    }

    @Override
    public INetwork<Integer> getNetwork() {
        return null;
    }

    public ItemStack getStackInSlot(int slot) {
        return isActive() ? new ItemStack(Items.APPLE, 1) : ItemStack.EMPTY;
    }

    @Nonnull

    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return isActive() ? new ItemStack(Items.APPLE, 2) : ItemStack.EMPTY;
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 v, EnumFacing f) {
        return super.canConnectTo(blockAccess, v, f) && (blockAccess.getTileEntity(v.toBlockPos()) instanceof IEnergyStorage || blockAccess.getTileEntity(v.toBlockPos()) instanceof INetworkTile);
    }

    @Override
    public Integer getStored() {
        return 0;
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
        return new ArrayList<>();
    }

    @Override
    public Integer produce(int maxExtract) {
        return maxExtract;
    }

    @Override
    public boolean canProduce(EnumFacing direction) {
        return isActive() && isConnectable();
    }

    @Override
    public void update() {
        
    }
}
