package me.creepinson.mod.api.util.math;

import java.util.Random;

/**
 * @author Creepinson
 */
public class MathUtils {
    public static float[] randomInUnitCircle(Random rn) {
        float t = (float) Math.PI * (2 * rn.nextFloat());
        float u = rn.nextFloat() + rn.nextFloat();
        float r = (u > 1) ? 2 - u : u;

        return new float[]{r * (float) Math.cos(t), r * (float) Math.sin(t)};
    }

    public static boolean chance(double percent) {
        if (Math.random() < percent / 100) {
            return true;
        } else {
            return false;
        }
    }
}
