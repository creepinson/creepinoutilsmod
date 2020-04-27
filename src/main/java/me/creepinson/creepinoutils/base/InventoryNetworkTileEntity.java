package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.util.BlockUtils;
import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * A base tile entity that can be used to make your own tile entity have an inventory and to be able connect with other blocks.
 **/

public abstract class InventoryNetworkTileEntity extends BaseTile {

    @Override
    public void refresh() {
        connections = BlockUtils.getTilesWithCapability(world, VectorUtils.fromBlockPos(pos),
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return isConnectable();
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.getItemHandler();
        }
        return super.getCapability(capability, facing);
    }

    protected abstract IItemHandler getItemHandler();
}
