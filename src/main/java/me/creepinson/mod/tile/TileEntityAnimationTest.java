package me.creepinson.mod.tile;

import me.creepinson.mod.api.util.item.InventoryUtils;
import me.creepinson.mod.api.util.math.Vector3;
import me.creepinson.mod.api.util.world.WorldUtils;
import me.creepinson.mod.base.EnergyNetworkTileEntity;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.IConfigurable;
import mekanism.api.energy.EnergyStack;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.api.transmitters.ITransmitter;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.base.EnergyAcceptorWrapper;
import mekanism.common.base.ILogisticalTransporter;
import mekanism.common.block.states.BlockStateTransmitter;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.content.transporter.TransitRequest;
import mekanism.common.content.transporter.TransporterStack;
import mekanism.common.integration.redstoneflux.RFIntegration;
import mekanism.common.tile.TileEntityLogisticalSorter;
import mekanism.common.transmitters.grid.EnergyNetwork;
import mekanism.common.transmitters.grid.InventoryNetwork;
import mekanism.common.util.CableUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/

public class TileEntityAnimationTest extends EnergyNetworkTileEntity implements IItemHandler {
    public void onClick() {
        setActive(!isActive());
    }

    @Override
    public double pullEnergy(EnumFacing side, double amount, boolean simulate) {
        double toGive = isActive() ? 10 : 0;
        return toGive;
    }

    @Override
    public boolean canOutputEnergy(EnumFacing enumFacing) {
        return isConnectable() && isActive();
    }

    @Override
    public double acceptEnergy(EnumFacing enumFacing, double v, boolean b) {
        return 0;
    }

    @Override
    public boolean canReceiveEnergy(EnumFacing enumFacing) {
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public EnumActionResult onSneakRightClick(EntityPlayer entityPlayer, EnumFacing enumFacing) {
        setConnectable(!connectable);
        updateConnectedBlocks();

        System.out.println("connectable " + connectable);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public EnumActionResult onRightClick(EntityPlayer entityPlayer, EnumFacing enumFacing) {
        onClick();
        return EnumActionResult.SUCCESS;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return active ? 10 : 0;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return isConnectable();
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
}
