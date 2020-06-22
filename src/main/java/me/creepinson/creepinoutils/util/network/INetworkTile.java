package me.creepinson.creepinoutils.util.network;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public interface INetworkTile<T> extends IBaseTile {
    INetwork<T> getNetwork();

    void setNetwork(INetwork<T> network);
}
