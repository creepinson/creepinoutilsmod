package me.creepinson.creepinoutils.util;

import dev.throwouterror.util.math.Tensor;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockUtils {
    public static void breakBlockWithDrop(World world, BlockPos pos) {
        BlockState BlockState = world.getBlockState(pos);
        if (BlockState.getMaterial().isLiquid()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        } else {
            world.destroyBlock(pos, true);
        }
    }

    /**
     * Safely checks if the tile entity exists.
     *
     * @return a LazyOptional that you can use to check whether the tile is present.
     */
    public static LazyOptional<? extends TileEntity> getTile(IWorld world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return LazyOptional.of(tile != null && !tile.isRemoved() ? () -> tile : null);
    }

    /**
     * Safely checks if the tile entity exists.
     *
     * @return a LazyOptional that you can use to check whether the tile is present.
     */
    public static LazyOptional<? extends TileEntity> getTile(IWorld world, Tensor pos) {
        return getTile(world, TensorUtils.toBlockPos(pos));
    }

    public static boolean isValid(World world, BlockPos pos) {
        return world.isAreaLoaded(pos, 1);
    }

    public static boolean canReplace(World world, BlockPos pos) {
        return isValid(world, pos) && (world.isAirBlock(pos) || world.getBlockState(pos).getMaterial().isReplaceable());
    }

    public static boolean isDirect(Tensor p, Tensor t) {
        return ((p.x() == t.x() + 1 || p.x() == t.x() - 1) && !(p.y() == t.y() + 1 || p.y() == t.y() - 1)
                && !(p.z() == t.z() + 1 || p.z() == t.z() - 1))
                || (!(p.x() == t.x() + 1 || p.x() == t.x() - 1) && (p.y() == t.y() + 1 || p.y() == t.y() - 1)
                && !(p.z() == t.z() + 1 || p.z() == t.z() - 1))
                || (!(p.x() == t.x() + 1 || p.x() == t.x() - 1) && !(p.y() == t.y() + 1 || p.y() == t.y() - 1)
                && (p.z() == t.z() + 1 || p.z() == t.z() - 1));
    }

    public static boolean isIndirect(Tensor p, Tensor t) {
        return !isDirect(p, t);
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<BlockPos> getTilesWithCapability(World world, BlockPos startingPosition, Capability<?>... search) {
        Set<BlockPos> set = new HashSet<>();
        getTilesWithCapabilityRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesWithCapabilityRecursive(Set<BlockPos> done, World world, BlockPos start, Direction from,
                                                       Capability<?>... search) {
        for (Direction side : Direction.values()) {
            if (side == from)
                continue;
            getTile(world, start).ifPresent(tile -> {
                for (Capability<?> c : search)
                    if (tile.getCapability(c, from).isPresent())
                        done.add(start);

                if (done.contains(start.offset(side)))
                    getTilesWithCapabilityRecursive(done, world, start.offset(side), side.getOpposite(), search);
            });
        }
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<BlockPos> getTiles(World world, BlockPos startingPosition, Class<?>... search) {
        Set<BlockPos> set = new HashSet<>();
        getTilesRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesRecursive(Set<BlockPos> done, World world, BlockPos start, Direction from,
                                         Class<?>... search) {
        for (Direction side : Direction.values()) {
            if (side == from)
                continue;
            getTile(world, start).ifPresent(tile -> {
                for (Class<?> c : search)
                    if (c.isInstance(tile)) done.add(start);

                if (done.contains(start.offset(side)))
                    getTilesRecursive(done, world, start.offset(side), side.getOpposite(), search);
            });
        }
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<BlockPos> getBlocks(World world, BlockPos startingPosition, Class<?>... search) {
        Set<BlockPos> set = new HashSet<>();
        getBlocksRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getBlocksRecursive(Set<BlockPos> done, World world, BlockPos start, Direction from,
                                          Class<?>... search) {
        for (Direction side : Direction.values()) {
            if (side == from)
                continue;
            Block b = world.getBlockState(start).getBlock();
            for (Class<?> c : search)
                if (c.isInstance(b))
                    done.add(start);

            if (done.contains(start.offset(side)))
                getBlocksRecursive(done, world, start.offset(side), side.getOpposite(), search);
        }
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block POSITION within the radius
     */
    public static List<BlockPos> getNearbyBlocks(BlockPos pos, int radius) {
        List<BlockPos> scanResult = new ArrayList<BlockPos>();
        for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++)
            for (int y = pos.getY() - radius; y <= pos.getY() + radius; y++)
                for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++)
                    scanResult.add(new BlockPos(x, y, z));

        return scanResult;
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block STATE within the radius. Compared to
     * the other getNearbyBlocks method, this one returns a list of
     * blockstates to make it easier to get each block's information.
     */
    public static List<BlockState> getNearbyBlocks(World world, BlockPos pos, int radius) {
        List<BlockState> scanResult = new ArrayList<BlockState>();
        List<BlockPos> scans = getNearbyBlocks(pos, radius);
        for (BlockPos scanPos : scans)
            scanResult.add(world.getBlockState(scanPos));

        return scanResult;
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block STATE within the radius. Compared to
     * the other getNearbyBlocks method, this one returns a list of
     * blockstates to make it easier to get each block's information.
     */
    public static List<BlockState> getNearbyBlocks(World world, Tensor pos, int radius) {
        List<BlockState> scanResult = new ArrayList<BlockState>();
        List<BlockPos> scans = getNearbyBlocks(TensorUtils.toBlockPos(pos), radius);
        for (BlockPos scanPos : scans)
            scanResult.add(world.getBlockState(scanPos));

        return scanResult;
    }

    public static Direction getNeighborDirection(BlockPos pos, BlockPos neighbor) {
        int dx = pos.getX() - neighbor.getX();

        if (dx == 0) {
            int dz = pos.getZ() - neighbor.getZ();
            if (dz == 0) {
                int dy = pos.getY() - neighbor.getY();
                if (dy >= 1)
                    return Direction.DOWN;
                else
                    return Direction.UP;
            } else if (dz >= 1)
                return Direction.NORTH;
            else
                return Direction.SOUTH;
        } else if (dx >= 1)
            return Direction.WEST;
        else
            return Direction.EAST;
    }

    public static BlockState getState(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof AirBlock))
            return block.getDefaultState();
        return Blocks.AIR.getDefaultState();
    }
}
