package me.creepinson.mod.tile;


import me.creepinson.mod.api.upgrade.Upgrade;
import me.creepinson.mod.api.upgrade.UpgradeInfo;
import me.creepinson.mod.api.util.math.Vector3;
import me.creepinson.mod.base.EnergyNetworkTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/

public class TileEntityAnimationTest extends EnergyNetworkTileEntity {
    public void onClick() {
        setActive(!isActive());
    }

    @Override
    public void update() {
        if(getNetwork() != null) {
            getNetwork().produce(this);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        resetNetwork();
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


    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing side) {
        return isConnectable();
    }

    @Override
    public Integer getStored() {
        return isActive() ? 10 : 0;
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
    public Integer getRequest(EnumFacing direction) {
        return getStored();
    }

    @Override
    public boolean canExtract() {
        return false;
    }
}
