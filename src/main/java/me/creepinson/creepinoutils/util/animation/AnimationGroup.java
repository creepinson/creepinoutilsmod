package me.creepinson.creepinoutils.util.animation;

import me.creepinson.creepinoutils.api.util.math.Rotation;
import me.creepinson.creepinoutils.api.util.math.Vector;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class AnimationGroup {
    public String name;
    public final boolean removeable;
    public AnimationController controller;
    public Rotation rotation;
    public Vector translation;

    public AnimationGroup(String name, boolean removeable, AnimationController controller) {
        this.name = name;
        this.removeable = removeable;
        this.controller = controller;
    }
}
