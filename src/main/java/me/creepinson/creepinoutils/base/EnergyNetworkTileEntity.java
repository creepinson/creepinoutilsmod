package me.creepinson.creepinoutils.base;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * A base tile entity that can be used to make your own tile entity have energy storage and to be able connect with other blocks.
 */
public abstract class EnergyNetworkTileEntity extends BaseTile {

    private IEnergyStorage energyStorage;

    private LazyOptional<IEnergyStorage> lazyStorage;

    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public EnergyNetworkTileEntity(TileEntityType<?> type, int capacity) {
        super(type);
        this.energyStorage = new EnergyStorage(capacity);
        this.lazyStorage = net.minecraftforge.common.util.LazyOptional.of(() -> this.energyStorage);

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        lazyStorage.invalidate();
    }

    @Override
    public boolean canConnectTo(IWorld blockAccess, BlockPos pos, Direction f) {
        if (connections.isEmpty())
            refresh();
        return isConnectable() && isActive() && connections.contains(pos.offset(f));
    }

    @Override
    public boolean canConnectToStrict(IWorld blockAccess, BlockPos pos, Direction side) {
        return canConnectTo(blockAccess, pos, side);
    }
}
