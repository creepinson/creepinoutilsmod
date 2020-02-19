package me.creepinson.mod.tile;

import me.creepinson.mod.base.EnergyNetworkTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

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

    }

/*    @Override
    public int getSlots() {
        return 0;
    }*/

    @Nonnull

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
    public int extractEnergy(int maxExtract, boolean simulate) {
        int toGive = isActive() ? 10 : 0;
        return toGive;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
