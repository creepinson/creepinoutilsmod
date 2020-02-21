package me.creepinson.mod.base;


<<<<<<< HEAD
import me.creepinson.mod.Hooks;
import me.creepinson.mod.api.INetworkedTile;
import me.creepinson.mod.api.upgrade.Upgrade;
import me.creepinson.mod.api.upgrade.UpgradeInfo;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.item.ItemStack;
=======
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyStorage;
import me.creepinson.mod.api.network.ElectricityNetwork;
import me.creepinson.mod.api.network.INetwork;
import me.creepinson.mod.api.network.INetworkedTile;
import me.creepinson.mod.api.util.math.Vector3;
import mekanism.api.IConfigurable;
import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.api.energy.IStrictEnergyOutputter;
import mekanism.common.capabilities.Capabilities;
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
<<<<<<< HEAD
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 * <br/>
 * A basic
 **/
<<<<<<< HEAD
@Optional.InterfaceList(value = {@Optional.Interface(iface = "mekanism.api.energy.IStrictEnergyOutputter", modid = Hooks.MEKANISM, striprefs = true), @Optional.Interface(iface = "mekanism.api.energy.IStrictEnergyAcceptor", modid = Hooks.MEKANISM, striprefs = true)})
public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkedTile, IEnergyStorage, ITickable {

    public final List<UpgradeInfo> upgrades = new ArrayList<>();

    public UpgradeInfo getByUpgrade(Upgrade upgrade) {
        for (UpgradeInfo i : upgrades) {
            if (i.upgrade.equals(upgrade)) {
                return i;
            }
        }
        return null;
    }

    public UpgradeInfo getByStack(ItemStack stack) {
        for (UpgradeInfo i : upgrades) {
            if (ItemStack.areItemStacksEqual(stack, i.upgradeItem)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public boolean upgrade(UpgradeInfo info) {
        if (canUpgrade()) {
            upgrades.add(getByStack(info.upgradeItem));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack downgrade(Upgrade upgrade) {
        ItemStack i = getByUpgrade(upgrade).upgradeItem.copy();
        upgrades.remove(getByUpgrade(upgrade));
        return i;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public List<UpgradeInfo> getUpgrades() {
        return upgrades;
    }
=======
public abstract class EnergyNetworkTileEntity extends TileEntity implements INetworkedTile<Float>, ITickable {
>>>>>>> 44a05fc3b9b01f06372ec81546e5ed570c8f1687

    protected boolean connectable = true;
    protected INetwork network;


    @Override
    public INetwork getNetwork() {
        return this.network;
    }

    @Override
    public void onLoad() {
        if(this.network == null) {
            this.setNetwork(new ElectricityNetwork(new Vector3(pos), world));
            this.getNetwork().refresh();
        }
    }

    @Override
    public void setNetwork(INetwork newNetwork) {
        this.network = newNetwork;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean value) {
        this.active = value;
    }


    @Override
    public boolean isConnectable() {
        return connectable;
    }

    @Override
    public void setConnectable(boolean value) {
        this.connectable = value;
    }

    protected boolean active;


    public void updateConnectedBlocks() {
        for (EnumFacing f : EnumFacing.values()) {
            world.notifyBlockUpdate(pos.offset(f), world.getBlockState(pos.offset(f)), world.getBlockState(pos.offset(f)), 2);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return connectable;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canExtract() {
        return isConnectable() && isActive();
    }

    @Override
    public boolean canReceive() {
        return isConnectable() && isActive();
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 vector3, EnumFacing f) {
        return isConnectable() && isActive();
    }
}
