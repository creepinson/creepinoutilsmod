package me.creepinson.creepinoutils.util;

import me.creepinson.creepinoutils.api.util.math.Facing;
import me.creepinson.creepinoutils.api.util.math.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3f;

/**
 * A utility class used to convert between classes such as Vector and BlockPos.
 *
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class VectorUtils {
    public static boolean exists(Vector v, World world) {
        return world.getChunk(toBlockPos(v)).isLoaded();
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

    public static NBTTagCompound toNBT(Vector v) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (float f : v) {
            list.appendTag(new NBTTagFloat(f));
        }
        tag.setTag("data", list);
        return tag;
    }

    public static Vector fromNBT(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("data", 5);
        float[] data = new float[list.tagCount()];

        for (int i = 0; i < list.tagCount(); i++) {
            data[i] = list.getFloatAt(i);
        }

        return new Vector(data);
    }

    public static Vector fromTile(TileEntity tile) {
        return fromBlockPos(tile.getPos());
    }

    public static TileEntity getTile(World w, Vector v) {
        return w.getTileEntity(toBlockPos(v));
    }

    public static Vector offset(Vector origin, EnumFacing side) {
        return origin.offset(Facing.byIndex(side.getIndex()));
    }

    public static Block getBlock(World world, Vector pos) {
        return world.getBlockState(toBlockPos(pos)).getBlock();
    }

    public static IBlockState getBlockState(World world, Vector pos) {
        return world.getBlockState(toBlockPos(pos));
    }

    public static TileEntity getTileOffset(World world, Vector position, EnumFacing facing) {
        return getTile(world, offset(position, facing));
    }

    public static Vector3f toJava(Vector v) {
        return new Vector3f(v.x(), v.y(), v.z());
    }
}
