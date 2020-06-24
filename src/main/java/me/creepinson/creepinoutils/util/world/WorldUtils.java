package me.creepinson.creepinoutils.util.world;

import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.util.TensorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author Theo Paris https://theoparis.com Project creepinoutils
 **/
public class WorldUtils {

    /**
     * Pre-calculated cache of translated block orientations
     */
    private static final Direction[][] baseOrientations = new Direction[Direction.values().length][Direction
            .values().length];

    /**
     * Gets the left side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return left side
     */
    public static Direction getLeft(Direction orientation) {
        return orientation.rotateY();
    }

    /**
     * Gets the right side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return right side
     */
    public static Direction getRight(Direction orientation) {
        return orientation.rotateYCCW();
    }

    /**
     * Gets the opposite side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return opposite side
     */
    public static Direction getBack(Direction orientation) {
        return orientation.getOpposite();
    }

    /**
     * Returns the sides in the modified order relative to the machine-based
     * orientation.
     *
     * @param blockFacing - what orientation the block is facing
     * @return Direction.VALUES, translated to machine orientation
     */
    public static Direction[] getBaseOrientations(Direction blockFacing) {
        return baseOrientations[blockFacing.ordinal()];
    }

    /**
     * Returns an integer facing that converts a world-based orientation to a
     * machine-based orientation.
     *
     * @param side        - world based
     * @param blockFacing - what orientation the block is facing
     * @return machine orientation
     */
    public static Direction getBaseOrientation(Direction side, Direction blockFacing) {
        if (blockFacing == Direction.DOWN) {
            switch (side) {
                case DOWN:
                    return Direction.NORTH;
                case UP:
                    return Direction.SOUTH;
                case NORTH:
                    return Direction.UP;
                case SOUTH:
                    return Direction.DOWN;
                default:
                    return side;
            }
        } else if (blockFacing == Direction.UP) {
            switch (side) {
                case DOWN:
                    return Direction.SOUTH;
                case UP:
                    return Direction.NORTH;
                case NORTH:
                    return Direction.DOWN;
                case SOUTH:
                    return Direction.UP;
                default:
                    return side;
            }
        } else if (blockFacing == Direction.SOUTH || side.getAxis() == Direction.Axis.Y) {
            if (side.getAxis() == Direction.Axis.Z) {
                return side.getOpposite();
            }
            return side;
        } else if (blockFacing == Direction.NORTH) {
            if (side.getAxis() == Direction.Axis.Z) {
                return side;
            }
            return side.getOpposite();
        } else if (blockFacing == Direction.WEST) {
            if (side.getAxis() == Direction.Axis.Z) {
                return getRight(side);
            }
            return getLeft(side);
        } else if (blockFacing == Direction.EAST) {
            if (side.getAxis() == Direction.Axis.Z) {
                return getLeft(side);
            }
            return getRight(side);
        }
        return side;
    }

    public static <T extends TileEntity> T getTileEntitySafe(IWorld world, BlockPos pos, Class<T> expectedClass) {
        TileEntity te = world.getTileEntity(pos);
        if (expectedClass.isInstance(te)) {
            return expectedClass.cast(te);
        }
        return null;
    }

    /**
     * Notifies neighboring blocks of a TileEntity change without loading chunks.
     *
     * @param world - world to perform the operation in
     * @param coord - Tensor to perform the operation on
     */
    public static void notifyLoadedNeighborsOfTileChange(World world, Tensor coord) {
        for (Direction dir : Direction.values()) {
            Tensor offset = TensorUtils.offset(coord, dir);
            notifyNeighborofChange(world, offset, TensorUtils.toBlockPos(coord));
            if (TensorUtils.getBlockState(world, offset).isNormalCube(world, TensorUtils.toBlockPos(coord))) {
                offset = TensorUtils.offset(offset, dir);
                Block block1 = TensorUtils.getBlock(world, offset);
                BlockState blockState1 = TensorUtils.getBlockState(world, offset);
                if (block1.getWeakChanges(blockState1, world, TensorUtils.toBlockPos(offset))) {
                    block1.onNeighborChange(blockState1, world, TensorUtils.toBlockPos(offset),
                            TensorUtils.toBlockPos(coord));
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
    public static void notifyNeighborofChange(World world, Tensor coord, BlockPos fromPos) {
        BlockState state = TensorUtils.getBlockState(world, coord);
        state.getBlock().onNeighborChange(state, world, TensorUtils.toBlockPos(coord), fromPos);
        state.neighborChanged(world, TensorUtils.toBlockPos(coord), world.getBlockState(fromPos).getBlock(), fromPos,
                true);
    }

    /**
     * Checks if a block is directly getting powered by any of its neighbors without
     * loading any chunks.
     *
     * @param world - the world to perform the check in
     * @param coord - the Tensor of the block to check
     * @return if the block is directly getting powered
     */
    public static boolean isDirectlyGettingPowered(World world, Tensor coord) {
        for (Direction side : Direction.values()) {
            Tensor sideCoord = TensorUtils.offset(coord, side);
            if (world.getRedstonePower(TensorUtils.toBlockPos(coord), side) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Better version of the World.getRedstonePowerFromNeighbors() method that
     * doesn't load chunks.
     *
     * @param world - the world to perform the check in
     * @param coord - the coordinate of the block performing the check
     * @return if the block is indirectly getting powered by LOADED chunks
     */
    public static boolean isGettingPowered(World world, Tensor coord) {
        for (Direction side : Direction.values()) {
            Tensor sideCoord = TensorUtils.offset(coord, side);
            BlockState blockState = TensorUtils.getBlockState(world, coord);
            boolean weakPower = blockState.getBlock().shouldCheckWeakPower(blockState, world,
                    TensorUtils.toBlockPos(coord), side);
            if (weakPower && isDirectlyGettingPowered(world, sideCoord)) {
                return true;
            } else if (!weakPower && blockState.getWeakPower(world, TensorUtils.toBlockPos(sideCoord), side) > 0) {
                return true;
            }
        }
        return false;
    }
}
