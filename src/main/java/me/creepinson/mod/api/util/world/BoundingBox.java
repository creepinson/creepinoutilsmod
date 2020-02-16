package me.creepinson.mod.api.util.world;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.Serializable;


public class BoundingBox extends AxisAlignedBB implements Serializable {

    public BoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    public BoundingBox(BlockPos pos) {
        super(pos);
    }

    public BoundingBox(BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
    }

    public BoundingBox(Vec3d min, Vec3d max) {
        super(min, max);
    }
}