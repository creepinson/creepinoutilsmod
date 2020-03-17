package me.creepinson.creepinoutils.api.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface INetwork<T> {
    World getWorld();

    NBTTagCompound writeToNBT(NBTTagCompound compound);

    void readFromNBT(NBTTagCompound compound);

    void refresh();

    void merge(INetwork<T> net);

    void split(INetworkTile splitPoint);

    void updateConnectedBlocks();
}
