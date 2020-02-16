package me.creepinson.mod.api.util.animation;

import me.creepinson.mod.api.util.math.Rotation;
import me.creepinson.mod.api.util.math.Vector3f;

import java.util.HashMap;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class AnimationGroup {
    public String name;
    public final boolean removeable;
    public AnimationController controller;
    public Rotation rotation;
    public Vector3f translation;

    public AnimationGroup(String name, boolean removeable, AnimationController controller) {
        this.name = name;
        this.removeable = removeable;
        this.controller = controller;
    }
}
