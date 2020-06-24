package me.creepinson.creepinoutils.util.animation;

import dev.throwouterror.util.math.Rotation;
import dev.throwouterror.util.math.Tensor;

/**
 * @author Theo Paris https://theoparis.com
 * Project creepinoutils
 **/
public class AnimationGroup {
    public String name;
    public final boolean removeable;
    public AnimationController controller;
    public Rotation rotation;
    public Tensor translation;

    public AnimationGroup(String name, boolean removeable, AnimationController controller) {
        this.name = name;
        this.removeable = removeable;
        this.controller = controller;
    }
}
