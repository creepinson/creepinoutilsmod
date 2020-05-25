package me.creepinson.creepinoutils.util.world.fake;

import me.creepinson.creepinoutils.util.Gamemode;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public interface IFakePlayer {
    Gamemode getGamemode();

    void setGamemode(Gamemode gamemode);
}
