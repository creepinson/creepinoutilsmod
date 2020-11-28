package com.theoparis.creepinoutils.util

import net.minecraft.block.AirBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.common.util.NonNullSupplier
import java.util.*

object BlockUtils {
    fun breakBlockWithDrop(world: World, pos: BlockPos) {
        val blockState = world.getBlockState(pos)
        if (blockState.material.isLiquid) {
            world.setBlockState(pos, Blocks.AIR.defaultState, 3)
        } else {
            world.destroyBlock(pos, true)
        }
    }

    /**
     * Safely checks if the tile entity exists.
     *
     * @return a LazyOptional that you can use to check whether the tile is present.
     */
    fun getTile(world: World, pos: BlockPos): LazyOptional<out TileEntity> {
        val tile = world.getTileEntity(pos)
        return LazyOptional.of(if (tile != null && !tile.isRemoved) NonNullSupplier { tile } else null)
    }

    fun isValid(world: World, pos: BlockPos): Boolean {
        return world.isAreaLoaded(pos, 1)
    }

    fun canReplace(world: World, pos: BlockPos): Boolean {
        return isValid(world, pos) && (world.isAirBlock(pos) || world.getBlockState(pos).material.isReplaceable)
    }

    fun isDirect(p: BlockPos, t: BlockPos): Boolean {
        return (((p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1)
                && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && (p.y == t.y + 1 || p.y == t.y - 1)
                && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1)
                && (p.z == t.z + 1 || p.z == t.z - 1)))
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    fun getTilesWithCapability(world: World, startingPosition: BlockPos, vararg search: Capability<*>): Set<BlockPos> {
        val set: MutableSet<BlockPos> = HashSet()
        getTilesWithCapabilityRecursive(set, world, startingPosition, null, *search)
        return set
    }

    fun getTilesWithCapabilityRecursive(
        done: MutableSet<BlockPos>, world: World, start: BlockPos, from: Direction?,
        vararg search: Capability<*>
    ) {
        for (side in Direction.values()) {
            if (side == from) continue
            getTile(world, start).ifPresent { tile: TileEntity ->
                for (c in search) if (tile.getCapability(c, from).isPresent) done.add(start)
                if (done.contains(start.offset(side))) getTilesWithCapabilityRecursive(
                    done,
                    world,
                    start.offset(side),
                    side.opposite,
                    *search
                )
            }
        }
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    fun getTiles(world: World, startingPosition: BlockPos, vararg search: Class<*>): Set<BlockPos> {
        val set: MutableSet<BlockPos> = HashSet()
        getTilesRecursive(set, world, startingPosition, null, *search)
        return set
    }

    fun getTilesRecursive(
        done: MutableSet<BlockPos>, world: World, start: BlockPos, from: Direction?,
        vararg search: Class<*>
    ) {
        for (side in Direction.values()) {
            if (side == from) continue
            getTile(world, start).ifPresent { tile: TileEntity? ->
                for (c in search) if (c.isInstance(tile)) done.add(start)
                if (done.contains(start.offset(side))) getTilesRecursive(
                    done,
                    world,
                    start.offset(side),
                    side.opposite,
                    *search
                )
            }
        }
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    fun getBlocks(world: World, startingPosition: BlockPos, vararg search: Class<*>): Set<BlockPos> {
        val set: MutableSet<BlockPos> = HashSet()
        getBlocksRecursive(set, world, startingPosition, null, *search)
        return set
    }

    fun getBlocksRecursive(
        done: MutableSet<BlockPos>, world: World, start: BlockPos, from: Direction?,
        vararg search: Class<*>
    ) {
        for (side in Direction.values()) {
            if (side == from) continue
            val b = world.getBlockState(start).block
            for (c in search) if (c.isInstance(b)) done.add(start)
            if (done.contains(start.offset(side))) getBlocksRecursive(
                done,
                world,
                start.offset(side),
                side.opposite,
                *search
            )
        }
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block POSITION within the radius
     */
    fun getNearbyBlocks(pos: BlockPos, radius: Int): List<BlockPos> {
        val scanResult: MutableList<BlockPos> = ArrayList()
        for (x in pos.x - radius..pos.x + radius) for (y in pos.y - radius..pos.y + radius) for (z in pos.z - radius..pos.z + radius) scanResult.add(
            BlockPos(x, y, z)
        )
        return scanResult
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block STATE within the radius. Compared to
     * the other getNearbyBlocks method, this one returns a list of
     * block states to make it easier to get each block's information.
     */
    fun getNearbyBlocks(world: World, pos: BlockPos, radius: Int): List<BlockState> {
        val scanResult: MutableList<BlockState> = ArrayList()
        val scans = getNearbyBlocks(pos, radius)
        for (scanPos in scans) scanResult.add(world.getBlockState(scanPos))
        return scanResult
    }

    fun getNeighborDirection(pos: BlockPos, neighbor: BlockPos): Direction {
        val dx = pos.x - neighbor.x
        return if (dx == 0) {
            val dz = pos.z - neighbor.z
            if (dz == 0) {
                val dy = pos.y - neighbor.y
                if (dy >= 1) Direction.DOWN else Direction.UP
            } else if (dz >= 1) Direction.NORTH else Direction.SOUTH
        } else if (dx >= 1) Direction.WEST else Direction.EAST
    }

    fun getState(stack: ItemStack): BlockState {
        val block = Block.getBlockFromItem(stack.item)
        return if (block !is AirBlock) block.defaultState else Blocks.AIR.defaultState
    }
}