package me.creepinson.creepinoutils.util;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public enum Gamemode {
    CREATIVE, SURVIVAL;

    public static Gamemode fromBool(boolean creative) {
        return creative ? CREATIVE : SURVIVAL;
    }
}
