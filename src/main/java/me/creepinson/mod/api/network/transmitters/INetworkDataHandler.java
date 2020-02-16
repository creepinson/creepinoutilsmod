package me.creepinson.mod.api.network.transmitters;

public interface INetworkDataHandler {

    String getNeededInfo();

    String getStoredInfo();

    String getFlowInfo();
}