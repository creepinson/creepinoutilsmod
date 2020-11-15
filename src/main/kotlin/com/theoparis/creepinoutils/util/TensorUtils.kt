package com.theoparis.creepinoutils.util

import dev.throwouterror.util.math.Direction.Companion.byIndex
import dev.throwouterror.util.math.Tensor
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.DoubleNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.util.math.vector.Vector3i
import net.minecraft.util.math.vector.Vector4f
import net.minecraft.world.World

/**
 * A utility class used to convert between classes such as Tensor and BlockPos.
 *
 * @author Theo Paris https://theoparis.com
 */
object TensorUtils {
    fun exists(v: Tensor, world: World): Boolean {
        return world.chunkExists(v.intX(), v.intZ())
    }

    /**
     * Helper method to turn a Tensor into a Minecraft block position.
     */
    @JvmStatic
    fun toBlockPos(v: Tensor): BlockPos {
        return BlockPos(v.x(), v.y(), v.z())
    }

    fun fromBlockPos(pos: BlockPos): Tensor {
        return Tensor(pos.x, pos.y, pos.z)
    }

    fun toVector3i(v: Tensor): Vector3i {
        return Vector3i(v.x(), v.y(), v.z())
    }

    fun fromVector3i(pos: Vector3i): Tensor {
        return Tensor(pos.x, pos.y, pos.z)
    }

    fun toVec(v: Tensor): Vector3d {
        return Vector3d(v.x(), v.y(), v.z())
    }

    fun fromVec(pos: Vector3d): Tensor {
        return Tensor(pos.getX(), pos.getY(), pos.getZ())
    }

    @JvmStatic
    fun toNBT(v: Tensor): CompoundNBT {
        val tag = CompoundNBT()
        val list = ListNBT()
        for (f in v) {
            list.add(DoubleNBT.valueOf(f))
        }
        tag.put("data", list)
        return tag
    }

    @JvmStatic
    fun fromNBT(tag: CompoundNBT): Tensor {
        val list = tag.getList("data", 5)
        val data = DoubleArray(list.size)
        for (i in list.indices) {
            data[i] = list.getDouble(i)
        }
        return Tensor(*data)
    }

    fun fromTile(tile: TileEntity): Tensor {
        return fromBlockPos(tile.pos)
    }

    fun getTile(w: World, v: Tensor): TileEntity? {
        return w.getTileEntity(toBlockPos(v))
    }

    fun offset(origin: Tensor, side: Direction): Tensor {
        return origin.offset(byIndex(side.index), 1)
    }

    fun getBlock(world: World, pos: Tensor): Block {
        return world.getBlockState(toBlockPos(pos)).block
    }

    fun getBlockState(world: World, pos: Tensor): BlockState {
        return world.getBlockState(toBlockPos(pos))
    }

    fun getTileOffset(world: World, position: Tensor, facing: Direction): TileEntity? {
        return getTile(world, offset(position, facing))
    }

    @JvmStatic
    fun toVector(pos: Tensor): Vector3f {
        return Vector3f(pos.floatX(), pos.floatY(), pos.floatZ())
    }

    @JvmStatic
    fun toVector4(pos: Tensor): Vector4f {
        return Vector4f(pos.floatX(), pos.floatY(), pos.floatZ(), pos.floatW())
    }

    fun fromVector(pos: Vector3f): Tensor {
        return Tensor(pos.x, pos.y, pos.z)
    }

    @JvmStatic
    fun fromVector4(pos: Vector4f): Tensor {
        return Tensor(pos.x, pos.y, pos.z, pos.w)
    }

    fun fromBytes(buf: PacketBuffer): Tensor {
        val size = buf.readInt()
        val data = DoubleArray(size)
        for (i in 0 until size) {
            data[i] = buf.readDouble()
        }
        return Tensor(*data)
    }

    fun toBytes(buf: PacketBuffer, t: Tensor) {
        buf.writeInt(t.data.size)
        for (d in t.data) {
            buf.writeDouble(d)
        }
    }
}