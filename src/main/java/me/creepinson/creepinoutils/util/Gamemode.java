package me.creepinson.creepinoutils.util;

/**
 * @author Theo Paris https://theoparis.com
 **/
public enum Gamemode {
    CREATIVE, SURVIVAL;

    public static Gamemode fromBool(boolean creative) {
        return creative ? CREATIVE : SURVIVAL;
    }
}
