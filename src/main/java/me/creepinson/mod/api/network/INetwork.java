package me.creepinson.mod.api.network;

import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public interface INetwork<T> {
    Set<BlockPos> getConnections();

    World getWorld();

    <T> T produce(INetworkProducer producer, Vector3... ignore);

    NBTTagCompound serialize();
    void deserialize(NBTTagCompound compound);

    T getRequest(Vector3... ignore);

    void refresh();

    void refreshConnections();

    void merge(INetwork<T> net);
    void split(INetworkedTile splitPoint);
}
