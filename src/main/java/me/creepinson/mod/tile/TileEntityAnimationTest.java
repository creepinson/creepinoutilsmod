package me.creepinson.mod.tile;

<<<<<<< HEAD
import me.creepinson.mod.base.EnergyNetworkTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
=======
import me.creepinson.mod.api.network.INetworkedTile;
import me.creepinson.mod.api.util.CreepinoUtils;
import me.creepinson.mod.api.util.math.Vector3;
import me.creepinson.mod.base.EnergyNetworkTileEntity;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/

public class TileEntityAnimationTest extends EnergyNetworkTileEntity {
    public void onClick() {
        setActive(!isActive());
    }

<<<<<<< HEAD
    @Override
    public void update() {

    }

/*    @Override
    public int getSlots() {
        return 0;
    }*/

    @Nonnull
=======

    @Override
    public void update() {
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687

    public ItemStack getStackInSlot(int slot) {
        return isActive() ? new ItemStack(Items.APPLE, 1) : ItemStack.EMPTY;
    }

<<<<<<< HEAD
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
=======
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == Capabilities.ENERGY_OUTPUTTER_CAPABILITY) {
            return connectable;
        } else if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return true;
        } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == Capabilities.ENERGY_OUTPUTTER_CAPABILITY) {
            return (T) this;
        } else if (capability == Capabilities.CONFIGURABLE_CAPABILITY) {
            return (T) this;
        } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getSlots() {
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687
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

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing side) {
        return isConnectable();
    }

    @Override
    public Map<Vector3, EnumFacing> getAdjacentConnections() {
        return CreepinoUtils.searchForBlockOnSidesRecursive(this, EnumFacing.values(), INetworkedTile.class, IEnergyStorage.class);
    }

    @Override
    public Object getStored() {
        return active ? 10 : 0;
    }
}
