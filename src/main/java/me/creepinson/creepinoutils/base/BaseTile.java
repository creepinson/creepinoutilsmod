package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.api.util.data.game.ITile;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.util.VectorUtils;
import me.creepinson.creepinoutils.util.network.IBaseTile;
import me.creepinson.creepinoutils.util.upgrade.Upgrade;
import me.creepinson.creepinoutils.util.upgrade.UpgradeInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public abstract class BaseTile extends TileEntity implements IBaseTile, ITile {
    private boolean active;
    private boolean connectable;
    protected Set<Vector3> connections = new HashSet<>();

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean value) {
        this.active = value;
    }

    @Override
    public void onLoad() {
        this.refresh();
    }

    public void updateConnectedBlocks() {
        for (EnumFacing f : EnumFacing.values()) {
            world.notifyBlockUpdate(pos.offset(f), world.getBlockState(pos.offset(f)),
                    world.getBlockState(pos.offset(f)), 2);
        }
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
    public String getName() {
        return world != null ? world.getBlockState(pos).getBlock().getRegistryName().toString() : "minecraft:unknown";
    }

    @Override
    public List<UpgradeInfo> getStoredUpgrades() {
        return upgrades;
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


    public void setConnectable(boolean value) {
        this.connectable = value;
        this.refresh();
    }

    public boolean isConnectable() {
        return connectable;
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing f) {
        if (connections.isEmpty())
            refresh();
        return isConnectable() && isActive() && connections.contains(VectorUtils.offset(pos, f));
    }

    @Override
    public Vector3 getPosition() {
        return VectorUtils.fromBlockPos(getPos());
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, Vector3 pos, EnumFacing side) {
        return canConnectTo(blockAccess, pos, side);
    }

    public Set<Vector3> getConnections() {
        return connections;
    }

    public void onNeighborChange(Vector3 vector3) {
        refresh();
        updateConnectedBlocks();
    }
}
