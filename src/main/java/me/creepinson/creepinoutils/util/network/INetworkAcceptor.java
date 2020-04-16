package me.creepinson.creepinoutils.util.network;

import net.minecraft.util.EnumFacing;

public interface INetworkAcceptor<T> {
    /**
     * @return How much energy does this TileEntity want?
     */
    T getRequest(EnumFacing direction);

    /**
     * Adds electricity to an block. Returns the quantity of electricity that was accepted. This
     * should always return 0 if the block cannot be externally charged.
     *
     * @param side      Orientation the electricity is sent in from.
     * @param receive   Maximum amount to be sent into the acceptor.
     * @param doReceive If false, the charge will only be simulated.
     * @return Amount of energy that was accepted by the block.
     */
    T receive(EnumFacing side, T receive, boolean doReceive);

    boolean canAccept(EnumFacing direction);
}
