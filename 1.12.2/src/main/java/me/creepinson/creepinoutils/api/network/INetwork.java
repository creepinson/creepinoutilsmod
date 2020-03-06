package me.creepinson.creepinoutils.api.network;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Set;

public interface INetwork<T> {
    Set<Vector3> getConnections();

    World getWorld();

    <T> T produce(Vector3... ignore);

    NBTTagCompound serialize();

    void deserialize(NBTTagCompound compound);

    void refresh();

    void refreshConnections();

    void merge(INetwork<T> net);

    void split(INetworkTile splitPoint);

    void updateConnectedBlocks();
}
