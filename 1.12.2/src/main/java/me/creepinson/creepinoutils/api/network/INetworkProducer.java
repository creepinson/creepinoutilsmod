package me.creepinson.creepinoutils.api.network;

import net.minecraft.util.EnumFacing;

public interface INetworkProducer<T> extends INetworkTile {
    /**
     * @return How much energy does this TileEntity want?
     */
    T getRequest(EnumFacing direction);

    /**
     * @return How much energy does this TileEntity want?
     */
    T produce(int maxExtract);

    boolean canProduce(EnumFacing direction);
}
