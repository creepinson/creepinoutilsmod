package me.creepinson.creepinoutils.base;

import dev.throwouterror.util.ISerializable;
import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.util.TensorUtils;
import me.creepinson.creepinoutils.util.network.IBaseTile;
import me.creepinson.creepinoutils.util.upgrade.Upgrade;
import me.creepinson.creepinoutils.util.upgrade.UpgradeInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Theo Paris (https://theoparis.com)
 */
public abstract class BaseTile extends TileEntity implements IBaseTile, ISerializable<Block> {
    private boolean active;
    private boolean connectable;
    protected Set<BlockPos> connections = new HashSet<>();

    public BaseTile(TileEntityType<?> type) {
        super(type);
    }

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
        for (Direction f : Direction.values()) {
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
    public String toString() {
        return world != null ? world.getBlockState(pos).getBlock().getRegistryName().toString() : "minecraft:unknown";
    }


    @Override
    public Block fromString(String s) {
        return Registry.BLOCK.getOrDefault(new ResourceLocation(s != null && !s.isEmpty() ? s : "minecraft:unknown"));
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
    public boolean canConnectTo(IWorld blockAccess, BlockPos pos, Direction f) {
        if (connections.isEmpty())
            refresh();
        return isConnectable() && isActive() && connections.contains(pos.offset(f));
    }

    public Tensor getPosition() {
        return TensorUtils.fromBlockPos(getPos());
    }

    @Override
    public boolean canConnectToStrict(IWorld blockAccess, BlockPos pos, Direction side) {
        return canConnectTo(blockAccess, pos, side);
    }

    public Set<BlockPos> getConnections() {
        return connections;
    }

    public void onNeighborChange(Tensor pos) {
        refresh();
        updateConnectedBlocks();
    }
}
