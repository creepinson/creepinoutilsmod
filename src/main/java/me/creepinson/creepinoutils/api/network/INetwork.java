package me.creepinson.creepinoutils.api.network;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Set;

public interface INetwork<T> {
    World getWorld();

    NBTTagCompound writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    void refresh();

    void merge(INetwork<T> net);

    void split(INetworkTile splitPoint);

    void updateConnectedBlocks();
}
