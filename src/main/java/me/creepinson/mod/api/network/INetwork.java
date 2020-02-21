package me.creepinson.mod.api.network;

import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Map;

public interface INetwork<T> {
    Map<Vector3, EnumFacing> getConnections();

    World getWorld();

    <T> T produce(INetworkProducer producer, Vector3... ignore);

    T getRequest(Vector3... ignore);

    void refresh();

    void refreshConnections();

    void merge(INetwork<T> net);
    void split(INetworkedTile splitPoint);
}
