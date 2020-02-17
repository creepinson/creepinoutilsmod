package me.creepinson.mod.api;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IConnectable {
    boolean isConnectable();
    void setConnectable(boolean value);
    void updateConnectedBlocks();
}
