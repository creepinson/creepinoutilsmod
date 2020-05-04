package me.creepinson.creepinoutils.util;

import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockUtils {

    public static boolean silentSetBlock(Chunk chunk, BlockPos pos, Block block, int meta) {
        int dx = pos.getX() & 15;
        int dz = pos.getZ() & 15;
        int y = pos.getY();

        int i1 = dz << 4 | dx;

        int[] precMap = (int[]) ReflectionUtils.getField("precipitationHeightMap", chunk);
        if (y >= precMap[i1] - 1) {
            precMap[i1] = -999;
        }
        //if (y >= chunk.precipitationHeightMap[i1] - 1) {
        //	chunk.precipitationHeightMap[i1] = -999;
        //}

        IBlockState state1 = chunk.getBlockState(dx, y, dz);
        Block block1 = state1.getBlock();
        int k1 = block1.getMetaFromState(state1);

        if (block1 == block && k1 == meta) {
            return false;
        } else {
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];

            if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE) {
                if (block == Blocks.AIR) {
                    return false;
                }

                extendedblockstorage = chunk.getBlockStorageArray()[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !chunk.getWorld().provider.isNether());
            }

            extendedblockstorage.set(dx, y & 15, dz, block.getStateFromMeta(meta));
            chunk.setModified(true);
            return true;
        }

    }

    public static void silentClear(Chunk chunk, BlockPos pos) {
        silentSetBlock(chunk, pos, Blocks.AIR, 0);
    }

    public static void breakBlockWithDrop(World world, BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        if (iblockstate.getMaterial().isLiquid()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        } else {
            world.destroyBlock(pos, true);
        }
    }

    public static boolean isValid(World world, BlockPos pos) {
        return world.isBlockLoaded(pos);
    }

    public static boolean canReplace(World world, BlockPos pos) {
        return isValid(world, pos) && (world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos));
    }

    public static void markBlockForUpdate(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 0);
    }

    @Nullable
    public static <T extends TileEntity> T getTileEntitySafely(IBlockAccess world, BlockPos pos, Class<T> tileClass) {
        TileEntity te;

        if (world instanceof ChunkCache) {
            ChunkCache chunkCache = (ChunkCache) world;
            te = chunkCache.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
        } else {
            te = world.getTileEntity(pos);
        }

        if (tileClass.isInstance(te)) {
            return tileClass.cast(te);
        } else {
            return null;
        }
    }

    public static boolean isDirect(Vector3 p, Vector3 t) {
        return ((p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1)
                && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && (p.y == t.y + 1 || p.y == t.y - 1)
                && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1)
                && (p.z == t.z + 1 || p.z == t.z - 1));
    }

    public static boolean isIndirect(Vector3 p, Vector3 t) {
        return !isDirect(p, t);
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<BlockPos> getTilesWithCapability(World world, BlockPos startingPosition, Capability... search) {
        Set<BlockPos> set = new HashSet<>();
        getTilesWithCapabilityRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesWithCapabilityRecursive(Set<BlockPos> done, World world, BlockPos start, EnumFacing from,
                                                       Capability... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from)
                continue;
            TileEntity tile = world.getTileEntity(start);
            if (tile != null && !tile.isInvalid()) {
                for (Capability c : search) {
                    if (tile.getCapability(c, from) != null) {
                        done.add(start);
                    }
                }

                if (done.contains(start.offset(side))) {
                    getTilesWithCapabilityRecursive(done, world, start.offset(side), side.getOpposite(), search);
                }
            }
        }
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<BlockPos> getTiles(World world, BlockPos startingPosition, Class... search) {
        Set<BlockPos> set = new HashSet<>();
        getTilesRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesRecursive(Set<BlockPos> done, World world, BlockPos start, EnumFacing from,
                                         Class... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from)
                continue;
            TileEntity tile = world.getTileEntity(start);
            if (tile != null && !tile.isInvalid()) {
                for (Class c : search) {
                    if (tile != null && !tile.isInvalid()) {
                        done.add(start);
                    }
                }

                if (done.contains(start.offset(side))) {
                    getTilesRecursive(done, world, start.offset(side), side.getOpposite(), search);
                }
            }
        }
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<BlockPos> getBlocks(World world, BlockPos startingPosition, Class... search) {
        Set<BlockPos> set = new HashSet<>();
        getBlocksRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getBlocksRecursive(Set<BlockPos> done, World world, BlockPos start, EnumFacing from,
                                          Class... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from)
                continue;
            Block b = world.getBlockState(start).getBlock();
            for (Class c : search) {
                if (c.isInstance(b)) {
                    done.add(start);
                }
            }

            if (done.contains(start.offset(side))) {
                getBlocksRecursive(done, world, start.offset(side), side.getOpposite(), search);
            }
        }
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block POSITION within the radius
     */
    public static List<BlockPos> getNearbyBlocks(BlockPos pos, int radius) {
        List<BlockPos> scanResult = new ArrayList<BlockPos>();
        for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
            for (int y = pos.getY() - radius; y <= pos.getY() + radius; y++) {
                for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
                    scanResult.add(new BlockPos(x, y, z));
                }
            }
        }
        return scanResult;
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block STATE within the radius.
     * Compared to the other getNearbyBlocks method, this one returns a list of blockstates to make it easier to get each block's information.
     */
    public static List<IBlockState> getNearbyBlocks(World world, BlockPos pos, int radius) {
        List<IBlockState> scanResult = new ArrayList<IBlockState>();
        List<BlockPos> scans = getNearbyBlocks(pos, radius);
        for (BlockPos scanPos : scans) {
            scanResult.add(world.getBlockState(scanPos));
        }
        return scanResult;
    }

    /**
     * @param pos    The starting position
     * @param radius The radius to expand from
     * @return A list that contains each block STATE within the radius.
     * Compared to the other getNearbyBlocks method, this one returns a list of blockstates to make it easier to get each block's information.
     */
    public static List<IBlockState> getNearbyBlocks(World world, Vector3 pos, int radius) {
        List<IBlockState> scanResult = new ArrayList<IBlockState>();
        List<BlockPos> scans = getNearbyBlocks(VectorUtils.toBlockPos(pos), radius);
        for (BlockPos scanPos : scans) {
            scanResult.add(world.getBlockState(scanPos));
        }
        return scanResult;
    }

    public static int getBlockMetadata(IBlockAccess blockAccess, Vector3 v) {
        return getBlockMetadata(blockAccess, v.intX(), v.intY(), v.intZ());
    }

    public static int getBlockMetadata(IBlockAccess blockAccess, int x, int y, int z) {
        return getBlockMetadata(blockAccess, new BlockPos(x, y, z));
    }

    public static int getBlockMetadata(IBlockAccess blockAccess, BlockPos pos) {
        return blockAccess.getBlockState(pos).getBlock().getMetaFromState(blockAccess.getBlockState(pos));
    }

    public static EnumFacing getNeighborDirection(BlockPos pos, BlockPos neighbor) {

        int dx = pos.getX() - neighbor.getX();

        if (dx == 0) {
            int dz = pos.getZ() - neighbor.getZ();
            if (dz == 0) {
                int dy = pos.getY() - neighbor.getY();
                if (dy >= 1) {
                    return EnumFacing.DOWN;
                } else {
                    return EnumFacing.UP;
                }
            } else if (dz >= 1) {
                return EnumFacing.NORTH;
            } else {
                return EnumFacing.SOUTH;
            }
        } else if (dx >= 1) {
            return EnumFacing.WEST;
        } else {
            return EnumFacing.EAST;
        }
    }

    public static IBlockState getState(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof BlockAir))
            return getState(block, stack.getMetadata());
        return Blocks.AIR.getDefaultState();
    }

    public static IBlockState getState(Block block, int meta) {
        try {
            return block.getStateFromMeta(meta);
        } catch (Exception e) {
            return block.getDefaultState();
        }
    }

}
