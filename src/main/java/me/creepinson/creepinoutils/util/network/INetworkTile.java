package me.creepinson.creepinoutils.util.network;

/**
 * @author Theo Paris https://theoparis.com
 **/
public interface INetworkTile<T> extends IBaseTile {
    INetwork<T> getNetwork();

    void setNetwork(INetwork<T> network);
}
