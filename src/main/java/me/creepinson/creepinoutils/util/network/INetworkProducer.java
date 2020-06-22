package me.creepinson.creepinoutils.util.network;

import net.minecraft.util.Direction;

public interface INetworkProducer<T> {
    /**
     * @return How much energy does this TileEntity want?
     */
    T getRequest(Direction direction);

    /**
     * @return How much energy does this TileEntity want?
     */
    T produce(int maxExtract);

    boolean canProduce(Direction direction);
}
