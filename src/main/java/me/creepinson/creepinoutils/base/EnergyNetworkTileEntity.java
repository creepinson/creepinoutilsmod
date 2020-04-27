package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * A base tile entity that can be used to make your own tile entity have energy storage and to be able connect with other blocks.
 */
public abstract class EnergyNetworkTileEntity extends BaseTile {

    private IEnergyStorage energyStorage;

    protected boolean connectable = true;

    public IEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public EnergyNetworkTileEntity(int capacity) {
        this.energyStorage = new EnergyStorage(capacity);
    }

    protected void setEnergyStorage(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    protected boolean active;

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
            return (T) this.energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    public boolean canConnectTo(IBlockAccess blockAccess, Vector3 pos, EnumFacing f) {
        if (connections.isEmpty())
            refresh();
        return isConnectable() && isActive() && connections.contains(VectorUtils.offset(getPosition(), f));
    }

    public boolean canConnectToStrict(IBlockAccess blockAccess, Vector3 pos, EnumFacing side) {
        return canConnectTo(blockAccess, pos, side);
    }
}
