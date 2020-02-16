package me.creepinson.mod.api;

public interface IClientTicker {

    void clientTick();

    boolean needsTicks();
}