package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.util.util.math.ForgeVector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class BaseNetwork<T> implements INetwork<T> {
    protected World world;
    protected Set<ForgeVector> connections;

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
        for (ForgeVector v : connections) {
            TileEntity conn = world.getTileEntity(v.toBlockPos());
            if (conn instanceof IBaseTile) {
                ((IBaseTile) conn).refresh();
            }
        }
    }

    @Override
    public Set<ForgeVector> getConnections() {
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
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        // TODO: implement this
    }
}
