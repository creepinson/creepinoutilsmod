package me.creepinson.mod.api.util.math;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class Transformation {

    public BlockPos center;

    public int rotX;
    public int rotY;
    public int rotZ;

    public Vector3 doubledRotationCenter;
    public Vector3 offset;


    public Transformation(int[] array) {
        if (array.length != 13)
            throw new IllegalArgumentException("Invalid array when creating door transformation!");

        center = new BlockPos(array[0], array[1], array[2]);
        rotX = array[3];
        rotY = array[4];
        rotZ = array[5];
        doubledRotationCenter = new Vector3(array[6], array[7], array[8]);
        this.offset = new Vector3(array[9], array[10], array[11]);
    }

    public Transformation(BlockPos center, int rotX, int rotY, int rotZ, Vector3 doubledRotationCenter, Vector3 offset) {
        this.center = center;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.doubledRotationCenter = doubledRotationCenter;
        this.offset = offset;
    }

    public Transformation(BlockPos center, Rotation rotation) {
        this.center = center;
        this.rotX = rotation.axis == EnumFacing.Axis.X ? (rotation.clockwise ? 1 : -1) : 0;
        this.rotY = rotation.axis == EnumFacing.Axis.Y ? (rotation.clockwise ? 1 : -1) : 0;
        this.rotZ = rotation.axis == EnumFacing.Axis.Z ? (rotation.clockwise ? 1 : -1) : 0;
        this.doubledRotationCenter = new Vector3(0, 0, 0);
        this.offset = new Vector3();

    }

    public Rotation getRotation(EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                if (rotX == 0)
                    return null;
                return Rotation.getRotation(axis, rotX > 0);
            case Y:
                if (rotY == 0)
                    return null;
                return Rotation.getRotation(axis, rotY > 0);
            case Z:
                if (rotZ == 0)
                    return null;
                return Rotation.getRotation(axis, rotZ > 0);
        }
        return null;
    }

    public BlockPos transform(BlockPos pos) {
        pos = pos.subtract(center);
        if (rotX != 0) {
            Rotation rotation = getRotation(EnumFacing.Axis.X);
            for (int i = 0; i < Math.abs(rotX); i++)
                pos = RotationUtils.rotate(pos, rotation);
        }
        if (rotY != 0) {
            Rotation rotation = getRotation(EnumFacing.Axis.Y);
            for (int i = 0; i < Math.abs(rotY); i++)
                pos = RotationUtils.rotate(pos, rotation);
        }
        if (rotZ != 0) {
            Rotation rotation = getRotation(EnumFacing.Axis.Z);
            for (int i = 0; i < Math.abs(rotZ); i++)
                pos = RotationUtils.rotate(pos, rotation);
        }

        pos = pos.add(center);

        if (offset != null)
            pos = pos.add(offset.toBlockPos());
        return pos;
    }

/*    public void transform(LittleAbsolutePreviews previews) {
        if (rotX != 0) {
            Rotation rotation = getRotation(Axis.X);
            for (int i = 0; i < Math.abs(rotX); i++)
                previews.rotatePreviews(rotation, doubledRotationCenter);
        }
        if (rotY != 0) {
            Rotation rotation = getRotation(Axis.Y);
            for (int i = 0; i < Math.abs(rotY); i++)
                previews.rotatePreviews(rotation, doubledRotationCenter);
        }
        if (rotZ != 0) {
            Rotation rotation = getRotation(Axis.Z);
            for (int i = 0; i < Math.abs(rotZ); i++)
                previews.rotatePreviews(rotation, doubledRotationCenter);
        }

        if (offset != null)
            previews.movePreviews(offset.getContext(), offset.getVec());
    }*/

    public int[] array() {
        return new int[]{center.getX(), center.getY(), center.getZ(), rotX, rotY, rotZ, doubledRotationCenter.intX(), doubledRotationCenter.intY(), doubledRotationCenter.intZ(), offset.intX(), offset.intY(), offset.intZ()};
    }

    @Override
    public String toString() {
        return "center:" + center.getX() + "," + center.getY() + "," + center.getZ() + ";rotation:" + rotX + "," + rotY + "," + rotZ + ";offset:" + offset.x + "," + offset.y + "," + offset.z;
    }
}
