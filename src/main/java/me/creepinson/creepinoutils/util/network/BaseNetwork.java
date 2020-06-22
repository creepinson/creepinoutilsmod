package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.api.util.math.Vector;
import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class BaseNetwork<T> implements INetwork<T> {
    protected World world;
    protected Set<Vector> connections;

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
        for (Vector v : connections) {
            TileEntity conn = world.getTileEntity(VectorUtils.toBlockPos(v));
            if (conn instanceof IBaseTile) {
                ((IBaseTile) conn).refresh();
            }
        }
    }

    @Override
    public Set<Vector> getConnections() {
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
