package me.creepinson.creepinoutils.tile;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.api.upgrade.Upgrade;
import me.creepinson.creepinoutils.api.upgrade.UpgradeInfo;
import me.creepinson.creepinoutils.api.util.math.ForgeVector;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.base.InventoryNetworkTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/

public class TileEntityAnimationTest extends InventoryNetworkTileEntity {
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
    public int getSlots() {
        return 0;
    }

    public ItemStack getStackInSlot(int slot) {
        return extractItem(slot, 1, true);
    }

    @Nonnull

    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Nonnull

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return isActive() ? new ItemStack(Items.APPLE, amount) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean canConnectTo(IBlockAccess world, EnumFacing f) {
        ForgeVector v = getPosition().offset(f);
        return super.canConnectTo(world, f);
    }

    @Override
    public void onNeighborChange(Vector3 vector3) {
        updateConnectedBlocks();
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
    public void update() {

    }
}