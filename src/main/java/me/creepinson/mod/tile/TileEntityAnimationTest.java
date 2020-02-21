package me.creepinson.mod.tile;

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

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/

public class TileEntityAnimationTest extends EnergyNetworkTileEntity implements IItemHandler {
    public void onClick() {
        setActive(!isActive());
    }


    @Override
    public void update() {

    }

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
        return 0;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return isActive() ? new ItemStack(Items.APPLE, 1) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return isActive() ? new ItemStack(Items.APPLE, 1) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
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
