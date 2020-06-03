package me.creepinson.creepinoutils.util.world;

import me.creepinson.creepinoutils.api.util.math.Vector;
import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class WorldUtils {

    /**
     * Pre-calculated cache of translated block orientations
     */
    private static final EnumFacing[][] baseOrientations = new EnumFacing[EnumFacing.VALUES.length][EnumFacing.VALUES.length];

    /**
     * Gets the left side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return left side
     */
    public static EnumFacing getLeft(EnumFacing orientation) {
        return orientation.rotateY();
    }

    /**
     * Gets the right side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return right side
     */
    public static EnumFacing getRight(EnumFacing orientation) {
        return orientation.rotateYCCW();
    }

    /**
     * Gets the opposite side of a certain orientation.
     *
     * @param orientation Current orientation of the machine
     * @return opposite side
     */
    public static EnumFacing getBack(EnumFacing orientation) {
        return orientation.getOpposite();
    }

    /**
     * Returns the sides in the modified order relative to the machine-based orientation.
     *
     * @param blockFacing - what orientation the block is facing
     * @return EnumFacing.VALUES, translated to machine orientation
     */
    public static EnumFacing[] getBaseOrientations(EnumFacing blockFacing) {
        return baseOrientations[blockFacing.ordinal()];
    }

    /**
     * Returns an integer facing that converts a world-based orientation to a machine-based orientation.
     *
     * @param side        - world based
     * @param blockFacing - what orientation the block is facing
     * @return machine orientation
     */
    public static EnumFacing getBaseOrientation(EnumFacing side, EnumFacing blockFacing) {
        if (blockFacing == EnumFacing.DOWN) {
            switch (side) {
                case DOWN:
                    return EnumFacing.NORTH;
                case UP:
                    return EnumFacing.SOUTH;
                case NORTH:
                    return EnumFacing.UP;
                case SOUTH:
                    return EnumFacing.DOWN;
                default:
                    return side;
            }
        } else if (blockFacing == EnumFacing.UP) {
            switch (side) {
                case DOWN:
                    return EnumFacing.SOUTH;
                case UP:
                    return EnumFacing.NORTH;
                case NORTH:
                    return EnumFacing.DOWN;
                case SOUTH:
                    return EnumFacing.UP;
                default:
                    return side;
            }
        } else if (blockFacing == EnumFacing.SOUTH || side.getAxis() == EnumFacing.Axis.Y) {
            if (side.getAxis() == EnumFacing.Axis.Z) {
                return side.getOpposite();
            }
            return side;
        } else if (blockFacing == EnumFacing.NORTH) {
            if (side.getAxis() == EnumFacing.Axis.Z) {
                return side;
            }
            return side.getOpposite();
        } else if (blockFacing == EnumFacing.WEST) {
            if (side.getAxis() == EnumFacing.Axis.Z) {
                return getRight(side);
            }
            return getLeft(side);
        } else if (blockFacing == EnumFacing.EAST) {
            if (side.getAxis() == EnumFacing.Axis.Z) {
                return getLeft(side);
            }
            return getRight(side);
        }
        return side;
    }


    public static TileEntity getTileEntitySafe(IBlockAccess worldIn, BlockPos pos) {
        return worldIn instanceof ChunkCache ? ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
    }

    public static <T extends TileEntity> T getTileEntitySafe(IBlockAccess worldIn, BlockPos pos, Class<T> expectedClass) {
        TileEntity te = getTileEntitySafe(worldIn, pos);
        if (expectedClass.isInstance(te)) {
            return expectedClass.cast(te);
        }
        return null;
    }

    /**
     * Updates a block's light value and marks it for a render update.
     *
     * @param world - world the block is in
     * @param pos   Position of the block
     */
    public static void updateBlock(World world, BlockPos pos) {
        if (!world.isBlockLoaded(pos)) {
            return;
        }
        //Schedule a render update regardless of it is an IActiveState with IActiveState#renderUpdate() as true
        // This is because that is mainly used for rendering machine effects, but we need to run a render update
        // anyways here in case IActiveState#renderUpdate() is false and we just had the block rotate.
        // For example the laser, or charge pad.
        world.markBlockRangeForRenderUpdate(pos, pos);
        updateAllLightTypes(world, pos);
    }


    /**
     * Notifies neighboring blocks of a TileEntity change without loading chunks.
     *
     * @param world - world to perform the operation in
     * @param coord - Vector to perform the operation on
     */
    public static void notifyLoadedNeighborsOfTileChange(World world, Vector coord) {
        for (EnumFacing dir : EnumFacing.VALUES) {
            Vector offset = VectorUtils.offset(coord, dir);
            notifyNeighborofChange(world, offset, VectorUtils.toBlockPos(coord));
            if (VectorUtils.getBlockState(world, offset).isNormalCube()) {
                offset = VectorUtils.offset(offset, dir);
                Block block1 = VectorUtils.getBlock(world, offset);
                if (block1.getWeakChanges(world, VectorUtils.toBlockPos(offset))) {
                    block1.onNeighborChange(world, VectorUtils.toBlockPos(offset), VectorUtils.toBlockPos(coord));
                }
            }
        }
    }

    /**
     * Calls BOTH neighbour changed functions because nobody can decide on which one to implement.
     *
     * @param world   world the change exists in
     * @param coord   neighbor to notify
     * @param fromPos pos of our block that updated
     */
    public static void notifyNeighborofChange(World world, Vector coord, BlockPos fromPos) {
        IBlockState state = VectorUtils.getBlockState(world, coord);
        state.getBlock().onNeighborChange(world, VectorUtils.toBlockPos(coord), fromPos);
        state.neighborChanged(world, VectorUtils.toBlockPos(coord), world.getBlockState(fromPos).getBlock(), fromPos);
    }

    /**
     * Updates all light types at the given coordinates.
     *
     * @param world - the world to perform the lighting update in
     * @param pos   - coordinates of the block to update
     */
    public static void updateAllLightTypes(World world, BlockPos pos) {
        world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        world.checkLightFor(EnumSkyBlock.SKY, pos);
    }

    /**
     * Checks if a block is directly getting powered by any of its neighbors without loading any chunks.
     *
     * @param world - the world to perform the check in
     * @param coord - the Vector of the block to check
     * @return if the block is directly getting powered
     */
    public static boolean isDirectlyGettingPowered(World world, Vector coord) {
        for (EnumFacing side : EnumFacing.VALUES) {
            Vector sideCoord = VectorUtils.offset(coord, side);
            if (world.getRedstonePower(VectorUtils.toBlockPos(coord), side) > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * Better version of the World.getRedstonePowerFromNeighbors() method that doesn't load chunks.
     *
     * @param world - the world to perform the check in
     * @param coord - the coordinate of the block performing the check
     * @return if the block is indirectly getting powered by LOADED chunks
     */
    public static boolean isGettingPowered(World world, Vector coord) {
        for (EnumFacing side : EnumFacing.VALUES) {
            Vector sideCoord = VectorUtils.offset(coord, side);
            IBlockState blockState = VectorUtils.getBlockState(world, coord);
            boolean weakPower = blockState.getBlock().shouldCheckWeakPower(blockState, world, VectorUtils.toBlockPos(coord), side);
            if (weakPower && isDirectlyGettingPowered(world, sideCoord)) {
                return true;
            } else if (!weakPower && blockState.getWeakPower(world, VectorUtils.toBlockPos(sideCoord), side) > 0) {
                return true;
            }
        }
        return false;
    }
}
