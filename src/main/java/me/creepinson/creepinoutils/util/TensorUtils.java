package me.creepinson.creepinoutils.util;

import me.creepinson.creepinoutils.api.util.math.Facing;
import me.creepinson.creepinoutils.api.util.math.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A utility class used to convert between classes such as Vector and BlockPos.
 *
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class VectorUtils {
    public static boolean exists(Vector v, World world) {
        return world.chunkExists(v.x, v.z);
    }

    /**
     * Helper method to turn a Vector into a minecraft block position.
     */
    public static BlockPos toBlockPos(Vector v) {
        return new BlockPos(v.x(), v.y(), v.z());
    }

    public static Vector fromBlockPos(BlockPos pos) {
        return new Vector(pos.getX(), pos.getY(), pos.getZ());
    }

    public static CompoundNBT toNBT(Vector v) {
        CompoundNBT tag = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (float f : v) {
            list.add(FloatNBT.valueOf(f));
        }
        tag.put("data", list);
        return tag;
    }

    public static Vector fromNBT(CompoundNBT tag) {
        ListNBT list = tag.getList("data", 5);
        float[] data = new float[list.size()];

        for (int i = 0; i < list.size(); i++) {
            data[i] = list.getFloat(i);
        }

        return new Vector(data);
    }

    public static Vector fromTile(TileEntity tile) {
        return fromBlockPos(tile.getPos());
    }

    public static TileEntity getTile(World w, Vector v) {
        return w.getTileEntity(toBlockPos(v));
    }

    public static Vector offset(Vector origin, Direction side) {
        return origin.offset(Facing.byIndex(side.getIndex()));
    }

    public static Block getBlock(World world, Vector pos) {
        return world.getBlockState(toBlockPos(pos)).getBlock();
    }

    public static BlockState getBlockState(World world, Vector pos) {
        return world.getBlockState(toBlockPos(pos));
    }

    public static TileEntity getTileOffset(World world, Vector position, Direction facing) {
        return getTile(world, offset(position, facing));
    }

    public static Vector3f toJava(Vector v) {
        return new Vector3f(v.x(), v.y(), v.z());
    }
}
