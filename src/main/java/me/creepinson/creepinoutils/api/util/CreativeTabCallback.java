package me.creepinson.creepinoutils.api.util;

import me.creepinson.creepinoutils.base.BaseMod;
import me.creepinson.creepinoutils.base.CreativeTab;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project CreepinoUtilsMod
 **/
public abstract class CreativeTabCallback {
    public final BaseMod mod;

    public CreativeTabCallback(BaseMod mod) {
        this.mod = mod;
    }

    public abstract void init(CreativeTab tab);

}
