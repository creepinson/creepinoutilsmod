package me.creepinson.creepinoutils.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.upgrade.Upgrade;
import me.creepinson.creepinoutils.api.upgrade.UpgradeInfo;
import me.creepinson.creepinoutils.api.util.BlockUtils;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Creepinson http://gitlab.com/creepinson Project creepinoutils
 **/
// @Optional.InterfaceList(value = {@Optional.Interface(iface =
// "mekanism.api.IConfigurable", modid = Hooks.MEKANISM, striprefs = true)})

public abstract class InventoryNetworkTileEntity extends TileEntity implements INetworkTile, IItemHandler {
    private Set<Vector3> connections = new HashSet<>();

    @Override
    public void refresh() {
        connections = BlockUtils.getTilesWithCapability(world, new Vector3(pos),
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

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
    public ItemStack removeUpgrade(Upgrade upgrade) {
        ItemStack i = getByUpgrade(upgrade).upgradeItem.copy();
        upgrades.remove(getByUpgrade(upgrade));
        return i;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public List<UpgradeInfo> getStoredUpgrades() {
        return upgrades;
    }

    protected boolean connectable = true;

    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean value) {
        this.active = value;
    }

    public boolean isConnectable() {
        return connectable;
    }

    public void setConnectable(boolean value) {
        this.connectable = value;
    }

    protected boolean active;

    public void updateConnectedBlocks() {
        for (EnumFacing f : EnumFacing.values()) {
            world.notifyBlockUpdate(pos.offset(f), world.getBlockState(pos.offset(f)),
                    world.getBlockState(pos.offset(f)), 2);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return connectable;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public Set<Vector3> getConnections() {
        return connections;
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(pos);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return isConnectable() && isActive();
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, EnumFacing f) {
        if (connections.isEmpty())
            refresh();
        return isConnectable() && isActive() && connections.contains(getPosition().offset(f));
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, EnumFacing side) {
        return canConnectTo(blockAccess, side);
    }
}
