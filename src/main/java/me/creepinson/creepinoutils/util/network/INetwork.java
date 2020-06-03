package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.api.util.math.Vector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.Serializable;
import java.util.Set;

public interface INetwork<T> extends INBTSerializable<NBTTagCompound>, Serializable {
    World getWorld();

    void refresh();

    Set<Vector> getConnections();

    void merge(INetwork<T> net);

    void split(IBaseTile splitPoint);
}
