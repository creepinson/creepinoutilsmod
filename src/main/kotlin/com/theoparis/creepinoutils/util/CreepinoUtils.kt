package com.theoparis.creepinoutils.util

import dev.throwouterror.util.math.Tensor
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.item.FallingBlockEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author creepinson
 */
object CreepinoUtils {
    fun resetEntityFall(entity: Entity) {
        entity.fallDistance = 0f
    }

    fun getEntityDirection(livingEntity: LivingEntity): Direction {
        val facing = MathHelper.floor(livingEntity.rotationYaw * 4.0f / 360.0f + 0.5) and 3
        // TODO: use switch statement
        return if (facing == 0) Direction.NORTH else if (facing == 1) Direction.EAST else if (facing == 2) Direction.SOUTH else Direction.WEST
    }

    fun moveEntityByRotation(entity: Entity) {
        val motionX =
            -MathHelper.sin(entity.rotationYaw / 180.0f * Math.PI.toFloat()) * MathHelper.cos(entity.rotationPitch / 180.0f * Math.PI.toFloat()) * 1.5f.toDouble()
        val motionZ =
            MathHelper.cos(entity.rotationYaw / 180.0f * Math.PI.toFloat()) * MathHelper.cos(entity.rotationPitch / 180.0f * Math.PI.toFloat()) * 1.5f.toDouble()
        val motionY = -MathHelper.sin((entity.rotationPitch + 0f) / 180.0f * Math.PI.toFloat()) * 1.5f.toDouble()
        entity.addVelocity(motionX, motionY, motionZ)
    }

    fun getCollisionBoxPart(x: Int, y: Int, z: Int, direction: Direction): AxisAlignedBB? {
        val AXIS_MIN_MIN = 0.0
        val AXIS_MAX_MAX = 1.0
        val AXIS_MAX_MIN = 0.9
        val AXIS_MIN_MAX = 0.1
        if (direction == Direction.EAST) return AxisAlignedBB(
            x + AXIS_MAX_MIN,
            y.toDouble(),
            z.toDouble(),
            x + AXIS_MAX_MAX,
            (y + 1).toDouble(),
            (z + 1).toDouble()
        ) else if (direction == Direction.WEST) return AxisAlignedBB(
            x + AXIS_MIN_MIN,
            y.toDouble(),
            z.toDouble(),
            x + AXIS_MIN_MAX,
            (y + 1).toDouble(),
            (z + 1).toDouble()
        ) else if (direction == Direction.SOUTH) return AxisAlignedBB(
            x.toDouble(), y.toDouble(), z + AXIS_MAX_MIN, (x + 1).toDouble(), (y + 1).toDouble(), z + AXIS_MAX_MAX
        ) else if (direction == Direction.NORTH) return AxisAlignedBB(
            x.toDouble(), y.toDouble(), z + AXIS_MIN_MIN, (x + 1).toDouble(), (y + 1).toDouble(), z + AXIS_MIN_MAX
        ) else if (direction == Direction.UP) return AxisAlignedBB(
            x.toDouble(), y + AXIS_MAX_MIN, z.toDouble(), (x + 1).toDouble(), y + AXIS_MAX_MAX, (z + 1).toDouble()
        ) else if (direction == Direction.DOWN) return AxisAlignedBB(
            x.toDouble(),
            y + AXIS_MIN_MIN,
            z.toDouble(),
            (x + 1).toDouble(),
            y + AXIS_MIN_MAX,
            (z + 1).toDouble()
        )
        return null
    }

    fun getCollisionBoxPart(pos: Tensor, direction: Direction): AxisAlignedBB? {
        return getCollisionBoxPart(pos.intX(), pos.intY(), pos.intZ(), direction)
    }

    fun getCollisionBoxPartFloor(pos: Tensor): AxisAlignedBB {
        return getCollisionBoxPartFloor(pos.intX(), pos.intY(), pos.intZ())
    }

    fun getCollisionBoxPartFloor(x: Int, y: Int, z: Int): AxisAlignedBB {
        val AXIS_FLOOR_MAX = 0.0
        val AXIS_FLOOR_MIN = -0.01
        return AxisAlignedBB(
            x.toDouble(),
            y + AXIS_FLOOR_MIN,
            z.toDouble(),
            (x + 1).toDouble(),
            y + AXIS_FLOOR_MAX,
            (z + 1).toDouble()
        )
    }

    fun getCoordinatesFromSide(pos: Tensor, s: Direction): BlockPos {
        return getCoordinatesFromSide(pos.intX(), pos.intY(), pos.intZ(), s.ordinal)
    }

    fun getCoordinatesFromSide(x: Int, y: Int, z: Int, s: Int): BlockPos {
        var x = x
        var y = y
        var z = z
        if (s == 0) y++ else if (s == 1) y-- else if (s == 2) z++ else if (s == 3) z-- else if (s == 4) x++ else if (s == 5) x--
        return BlockPos(x, y, z)
    }

    fun getDirectionFromSide(pos: Tensor, s: Int): Direction {
        return getDirectionFromSide(pos.intX(), pos.intY(), pos.intZ(), s)
    }

    fun getDirectionFromSide(x: Int, y: Int, z: Int, s: Int): Direction {
        if (s == 0) return Direction.DOWN else if (s == 1) return Direction.UP else if (s == 2) return Direction.NORTH else if (s == 3) return Direction.SOUTH else if (s == 4) return Direction.WEST else if (s == 5) return Direction.EAST
        return Direction.NORTH
    }

    fun <E> containsInstance(list: Collection<E>, clazz: Class<out E>): Boolean {
        for (e in list) {
            if (clazz.isInstance(e)) {
                return true
            }
        }
        return false
    }

    @Throws(IOException::class)
    fun readFile(path: String?, encoding: Charset?): String {
        val encoded = Files.readAllBytes(Paths.get(path))
        return String(encoded, encoding!!)
    }

    fun createBlockExplosion(world: World, blocksToDestroy: List<BlockPos>) {
        for (block in blocksToDestroy) {
            val x = (-0.5).toFloat() + (Math.random() * (0.5 - -0.5 + 1)).toFloat()
            val y = (-1).toFloat() + (Math.random() * (1 - -1 + 1)).toFloat()
            val z = (-0.5).toFloat() + (Math.random() * (0.5 - -0.5 + 1)).toFloat()
            val fallingBlock = FallingBlockEntity(
                world, block.x.toDouble(), block.y
                    .toDouble(), block.z.toDouble(),
                world.getBlockState(block)
            )
            fallingBlock.shouldDropItem = false
            fallingBlock.fallTime = 4
            world.addEntity(fallingBlock)
            fallingBlock.setVelocity(x.toDouble(), y.toDouble(), z.toDouble())
            world.setBlockState(block, Blocks.AIR.defaultState)
        }
    }
}