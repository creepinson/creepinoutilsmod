package com.theoparis.creepinoutils.util.world

import com.theoparis.creepinoutils.util.getBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World

/**
 * @author Theo Paris https://theoparis.com Project creepinoutils
 */
object WorldUtils {
    /**
     * Pre-calculated cache of translated block orientations
     */
    private val baseOrientations = Array(Direction.values().size) {
        arrayOfNulls<Direction>(
            Direction
                .values().size
        )
    }

    /**
     * Gets the left side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return left side
     */
    fun getLeft(orientation: Direction): Direction {
        return orientation.rotateY()
    }

    /**
     * Gets the right side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return right side
     */
    fun getRight(orientation: Direction): Direction {
        return orientation.rotateYCCW()
    }

    /**
     * Gets the opposite side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return opposite side
     */
    fun getBack(orientation: Direction): Direction {
        return orientation.opposite
    }

    /**
     * Returns the sides in the modified order relative to the machine-based
     * orientation.
     *
     * @param blockFacing - what orientation the block is facing
     * @return Direction.VALUES, translated to machine orientation
     */
    fun getBaseOrientations(blockFacing: Direction): Array<Direction?> {
        return baseOrientations[blockFacing.ordinal]
    }

    /**
     * Returns an integer facing that converts a world-based orientation to a
     * machine-based orientation.
     *
     * @param side        - world based
     * @param blockFacing - what orientation the block is facing
     * @return machine orientation
     */
    fun getBaseOrientation(side: Direction, blockFacing: Direction): Direction {
        if (blockFacing == Direction.DOWN) {
            return when (side) {
                Direction.DOWN -> Direction.NORTH
                Direction.UP -> Direction.SOUTH
                Direction.NORTH -> Direction.UP
                Direction.SOUTH -> Direction.DOWN
                else -> side
            }
        } else if (blockFacing == Direction.UP) {
            return when (side) {
                Direction.DOWN -> Direction.SOUTH
                Direction.UP -> Direction.NORTH
                Direction.NORTH -> Direction.DOWN
                Direction.SOUTH -> Direction.UP
                else -> side
            }
        } else if (blockFacing == Direction.SOUTH || side.axis === Direction.Axis.Y) {
            return if (side.axis === Direction.Axis.Z) {
                side.opposite
            } else side
        } else if (blockFacing == Direction.NORTH) {
            return if (side.axis === Direction.Axis.Z) {
                side
            } else side.opposite
        } else if (blockFacing == Direction.WEST) {
            return if (side.axis === Direction.Axis.Z) {
                getRight(side)
            } else getLeft(side)
        } else if (blockFacing == Direction.EAST) {
            return if (side.axis === Direction.Axis.Z) {
                getLeft(side)
            } else getRight(side)
        }
        return side
    }

    fun <T : TileEntity?> getTileEntitySafe(world: IWorld, pos: BlockPos?, expectedClass: Class<T>): T? {
        val te = world.getTileEntity(pos)
        return if (expectedClass.isInstance(te)) {
            expectedClass.cast(te)
        } else null
    }

    /**
     * Notifies neighboring blocks of a TileEntity change without loading chunks.
     *
     * @param world - world to perform the operation in
     * @param coord - BlockPos to perform the operation on
     */
    fun notifyLoadedNeighborsOfTileChange(world: World, coord: BlockPos) {
        for (dir in Direction.values()) {
            var offset = coord.offset(dir)
            notifyNeighborofChange(world, offset, coord)
            if (world.getBlockState(offset).isNormalCube(world, coord)) {
                offset = offset.offset(dir)
                val block1: Block = world.getBlock(offset)
                val blockState1: BlockState = world.getBlockState(offset)
                if (block1.getWeakChanges(blockState1, world, offset)) {
                    block1.onNeighborChange(
                        blockState1, world, offset,
                        coord
                    )
                }
            }
        }
    }

    /**
     * Calls BOTH neighbour changed functions because nobody can decide on which one
     * to implement.
     *
     * @param world   world the change exists in
     * @param coord   neighbor to notify
     * @param fromPos pos of our block that updated
     */
    fun notifyNeighborofChange(world: World, coord: BlockPos, fromPos: BlockPos) {
        val state: BlockState = world.getBlockState(coord)
        state.block.onNeighborChange(state, world, coord, fromPos)
        state.neighborChanged(
            world, coord, world.getBlockState(fromPos).block, fromPos,
            true
        )
    }

    /**
     * Checks if a block is directly getting powered by any of its neighbors without
     * loading any chunks.
     *
     * @param world - the world to perform the check in
     * @param coord - the BlockPos of the block to check
     * @return if the block is directly getting powered
     */
    fun isDirectlyGettingPowered(world: World, coord: BlockPos): Boolean {
        for (side in Direction.values()) {
            val sideCoord: BlockPos = coord.offset(side)
            if (world.getRedstonePower(coord, side) > 0) {
                return true
            }
        }
        return false
    }

    /**
     * Better version of the World.getRedstonePowerFromNeighbors() method that
     * doesn't load chunks.
     *
     * @param world - the world to perform the check in
     * @param coord - the coordinate of the block performing the check
     * @return if the block is indirectly getting powered by LOADED chunks
     */
    fun isGettingPowered(world: World, coord: BlockPos): Boolean {
        for (side in Direction.values()) {
            val sideCoord: BlockPos = coord.offset(side)
            val blockState: BlockState = world.getBlockState(coord)
            val weakPower = blockState.block.shouldCheckWeakPower(
                blockState, world,
                coord, side
            )
            if (weakPower && isDirectlyGettingPowered(world, sideCoord)) {
                return true
            } else if (!weakPower && blockState.getWeakPower(world, sideCoord, side) > 0) {
                return true
            }
        }
        return false
    }
}