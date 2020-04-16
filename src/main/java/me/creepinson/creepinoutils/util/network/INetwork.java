package me.creepinson.creepinoutils.util.network;

import me.creepinson.creepinoutils.util.util.math.ForgeVector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.Serializable;
import java.util.Set;

public interface INetwork<T> extends INBTSerializable<NBTTagCompound>, Serializable {
    World getWorld();

    void refresh();

    Set<ForgeVector> getConnections();

    void merge(INetwork<T> net);

    void split(IBaseTile splitPoint);
}
