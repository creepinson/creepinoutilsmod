package me.creepinson.mod.api.network;

import me.creepinson.mod.api.INetworkedTile;
import net.minecraft.util.EnumFacing;

public interface INetworkProducer<T> extends INetworkedTile<T> {
    /**
     * @return How much energy does this TileEntity want?
     */
    T getRequest(EnumFacing direction);

}
