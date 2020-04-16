package me.creepinson.creepinoutils.util.util;

import me.creepinson.creepinoutils.util.util.math.ForgeVector;
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
import java.util.HashSet;
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

    public static TileEntity getTile(World world, BlockPos pos) {
        return world.getTileEntity(pos);
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

    public static boolean isDirect(ForgeVector p, ForgeVector t) {
        return ((p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1)
                && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && (p.y == t.y + 1 || p.y == t.y - 1)
                && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1)
                && (p.z == t.z + 1 || p.z == t.z - 1));
    }

    public static boolean isIndirect(ForgeVector p, ForgeVector t) {
        return !isDirect(p, t);
    }

    /**
     * Returns a set containing the positions of each tile entity that is found to
     * be connected to the starting position.
     */
    public static Set<ForgeVector> getTilesWithCapability(World world, ForgeVector startingPosition, Capability... search) {
        Set<ForgeVector> set = new HashSet<>();
        getTilesWithCapabilityRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesWithCapabilityRecursive(Set<ForgeVector> done, World world, ForgeVector start, EnumFacing from,
                                                       Capability... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from)
                continue;
            if (world.getTileEntity(start.toBlockPos()) != null
                    && !world.getTileEntity(start.toBlockPos()).isInvalid()) {
                TileEntity tile = world.getTileEntity(start.toBlockPos());
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
    public static Set<ForgeVector> getTiles(World world, ForgeVector startingPosition, Class... search) {
        Set<ForgeVector> set = new HashSet<>();
        getTilesRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesRecursive(Set<ForgeVector> done, World world, ForgeVector start, EnumFacing from,
                                         Class... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from)
                continue;
            if (world.getTileEntity(start.toBlockPos()) != null
                    && !world.getTileEntity(start.toBlockPos()).isInvalid()) {
                TileEntity tile = world.getTileEntity(start.toBlockPos());
                for (Class c : search) {
                    if (c.isInstance(tile)) {
                        done.add(start);
                    }
                }

                if (done.contains(start.offset(side))) {
                    getTilesRecursive(done, world, start.offset(side), side.getOpposite(), search);
                }
            }
        }
    }

    public static Set<ForgeVector> getBlocks(World world, ForgeVector startingPosition, Class... search) {
        Set<ForgeVector> set = new HashSet<>();
        getBlocksRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getBlocksRecursive(Set<ForgeVector> done, World world, ForgeVector start, EnumFacing from,
                                          Class... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from)
                continue;
            IBlockState state = world.getBlockState(start.toBlockPos());
            for (Class c : search) {
                if (c.isInstance(state.getBlock())) {
                    done.add(start);
                }
            }

            if (done.contains(start.offset(side))) {
                getBlocksRecursive(done, world, start.offset(side), side.getOpposite(), search);
            }
        }
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
        if (block != null && !(block instanceof BlockAir))
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
