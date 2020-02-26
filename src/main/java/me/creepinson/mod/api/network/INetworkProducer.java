package me.creepinson.mod.api.network;

import net.minecraft.util.EnumFacing;

public interface INetworkProducer<T> extends INetworkTile<T> {
    /**
     * @return How much energy does this TileEntity want?
     */
    T produce(int maxExtract);
    boolean canProduce(EnumFacing direction);
}
