package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.util.BlockUtils;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * A base tile entity that can be used to make your own tile entity have an inventory and to be able connect with other blocks.
 **/

public abstract class InventoryNetworkTileEntity extends BaseTile {

    public InventoryNetworkTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void refresh() {
        connections = BlockUtils.getTilesWithCapability(world, pos,
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    private LazyOptional<IItemHandler> lazyStorage;

    public InventoryNetworkTileEntity(TileEntityType<?> type, int capacity) {
        super(type);
        this.lazyStorage = net.minecraftforge.common.util.LazyOptional.of(this::getItemHandler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    protected abstract IItemHandler getItemHandler();
}
