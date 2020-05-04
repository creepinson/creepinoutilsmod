package me.creepinson.creepinoutils.util;

import me.creepinson.creepinoutils.api.util.math.Facing;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3f;

/**
 * A utility class used to convert between classes such as Vector3 and BlockPos.
 *
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class VectorUtils {
    // TODO: move to creepinoutilscore
    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    public static boolean exists(Vector3 v, World world) {
        return world.getChunk(toBlockPos(v)).isLoaded();
    }

    /**
     * Helper method to turn a Vector3 into a minecraft block position.
     */
    public static BlockPos toBlockPos(Vector3 v) {
        return new BlockPos(v.x, v.y, v.z);
    }

    public static Vector3 fromBlockPos(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static NBTTagCompound toNBT(Vector3 v) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("x", v.x);
        tag.setFloat("y", v.y);
        tag.setFloat("z", v.z);
        return tag;
    }

    public static Vector3 fromNBT(NBTTagCompound tag) {
        return new Vector3(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
    }

    public static Vector3 fromTile(TileEntity tile) {
        return fromBlockPos(tile.getPos());
    }

    public static TileEntity getTile(World w, Vector3 v) {
        return w.getTileEntity(VectorUtils.toBlockPos(v));
    }

    public static Vector3 offset(Vector3 origin, EnumFacing side) {
        return origin.offset(Facing.byIndex(side.getIndex()));
    }

    public static Block getBlock(World world, Vector3 pos) {
        return world.getBlockState(toBlockPos(pos)).getBlock();
    }

    public static IBlockState getBlockState(World world, Vector3 pos) {
        return world.getBlockState(toBlockPos(pos));
    }

    public static TileEntity getTileOffset(World world, Vector3 position, EnumFacing facing) {
        return getTile(world, offset(position, facing));
    }

    public static Vector3f toJava(Vector3 v) {
        return new Vector3f(v.x, v.y, v.z);
    }
}
