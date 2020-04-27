package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.util.VectorUtils;
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
    protected Set<Vector3> connections;

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
        for (Vector3 v : connections) {
            TileEntity conn = world.getTileEntity(VectorUtils.toBlockPos(v));
            if (conn instanceof IBaseTile) {
                ((IBaseTile) conn).refresh();
            }
        }
    }

    @Override
    public Set<Vector3> getConnections() {
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
