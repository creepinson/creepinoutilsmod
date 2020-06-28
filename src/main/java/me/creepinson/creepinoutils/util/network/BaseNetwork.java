package me.creepinson.creepinoutils.util.network;

import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.util.TensorUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Theo Paris https://theoparis.com
 **/
public class BaseNetwork<T> implements INetwork<T> {
    protected World world;
    protected Set<Tensor> connections;

    public BaseNetwork(World w) {
        this.world = w;
        this.connections = new HashSet<>();
    }

    public void setWorld(World w) {
        this.world = w;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void refresh() {
        for (Tensor v : connections) {
            TileEntity conn = world.getTileEntity(TensorUtils.toBlockPos(v));
            if (conn instanceof IBaseTile) {
                ((IBaseTile) conn).refresh();
            }
        }
    }

    @Override
    public Set<Tensor> getConnections() {
        return this.connections;
    }

    @Override
    public void merge(INetwork net) {
        // TODO: implement split and merge
    }

    @Override
    public void split(IBaseTile splitPoint) {

    }


    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        // TODO: implement this
    }
}
