package me.creepinson.creepinoutils.util.network;

import dev.throwouterror.util.math.Tensor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.Serializable;
import java.util.Set;

public interface INetwork<T> extends INBTSerializable<CompoundNBT>, Serializable {
    World getWorld();

    void refresh();

    Set<Tensor> getConnections();

    void merge(INetwork<T> net);

    void split(IBaseTile splitPoint);
}
