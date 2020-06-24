package me.creepinson.creepinoutils.util;

import dev.throwouterror.util.math.Facing;
import dev.throwouterror.util.math.Tensor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * A utility class used to convert between classes such as Tensor and BlockPos.
 *
 * @author Theo Paris https://theoparis.com
 **/
public class TensorUtils {
	public static boolean exists(Tensor v, World world) {
		return world.chunkExists(v.intX(), v.intZ());
	}

	/**
	 * Helper method to turn a Tensor into a Minecraft block position.
	 */
	public static BlockPos toBlockPos(Tensor v) {
		return new BlockPos(v.x(), v.y(), v.z());
	}

	public static Tensor fromBlockPos(BlockPos pos) {
		return new Tensor(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static Vec3i toVec3i(Tensor v) {
		return new Vec3i(v.x(), v.y(), v.z());
	}

	public static Tensor fromVec3i(Vec3i pos) {
		return new Tensor(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Vec3d toVec(Tensor v) {
		return new Vec3d(v.x(), v.y(), v.z());
	}
	
	public static Tensor fromVec(Vec3d pos) {
		return new Tensor(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static CompoundNBT toNBT(Tensor v) {
		CompoundNBT tag = new CompoundNBT();
		ListNBT list = new ListNBT();
		for (double f : v) {
			list.add(DoubleNBT.valueOf(f));
		}
		tag.put("data", list);
		return tag;
	}

	public static Tensor fromNBT(CompoundNBT tag) {
		ListNBT list = tag.getList("data", 5);
		double[] data = new double[list.size()];

		for (int i = 0; i < list.size(); i++) {
			data[i] = list.getDouble(i);
		}

		return new Tensor(data);
	}

	public static Tensor fromTile(TileEntity tile) {
		return fromBlockPos(tile.getPos());
	}

	public static TileEntity getTile(World w, Tensor v) {
		return w.getTileEntity(toBlockPos(v));
	}

	public static Tensor offset(Tensor origin, Direction side) {
		return origin.offset(Facing.byIndex(side.getIndex()));
	}

	public static Block getBlock(World world, Tensor pos) {
		return world.getBlockState(toBlockPos(pos)).getBlock();
	}

	public static BlockState getBlockState(World world, Tensor pos) {
		return world.getBlockState(toBlockPos(pos));
	}

	public static TileEntity getTileOffset(World world, Tensor position, Direction facing) {
		return getTile(world, offset(position, facing));
	}

	public static Vector3f toVector(Tensor pos) {
		return new Vector3f(pos.floatX(), pos.floatY(), pos.floatZ());
	}

	public static Vector4f toVector4(Tensor pos) {
		return new Vector4f(pos.floatX(), pos.floatY(), pos.floatZ(), pos.floatW());
	}

	public static Tensor fromVector(Vector3f pos) {
		return new Tensor(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Tensor fromVector4(Vector4f pos) {
		return new Tensor(pos.getX(), pos.getY(), pos.getZ(), pos.getW());
	}
}
