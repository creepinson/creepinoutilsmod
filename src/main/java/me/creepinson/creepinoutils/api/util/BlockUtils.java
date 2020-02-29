package me.creepinson.creepinoutils.api.util;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class BlockUtils {

    public static boolean isDirect(Vector3 p, Vector3 t) {
        return ((p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1) && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && (p.y == t.y + 1 || p.y == t.y - 1) && !(p.z == t.z + 1 || p.z == t.z - 1))
                || (!(p.x == t.x + 1 || p.x == t.x - 1) && !(p.y == t.y + 1 || p.y == t.y - 1) && (p.z == t.z + 1 || p.z == t.z - 1));
    }

    public static boolean isIndirect(Vector3 p, Vector3 t) {
        return !isDirect(p, t);
    }

    public static Set<Vector3> getTiles(World world, Vector3 startingPosition, Class... search) {
        // TODO: replace with Map<Vector3, EnumFacing> for getting sides.
        Set<Vector3> set = new HashSet();
        getTilesRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getTilesRecursive(Set<Vector3> done, World world, Vector3 start, EnumFacing from, Class... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from) continue;
            if (world.getTileEntity(start.toBlockPos()) != null && !world.getTileEntity(start.toBlockPos()).isInvalid()) {
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

    public static Set<Vector3> getBlocks(World world, Vector3 startingPosition, Class... search) {
        // TODO: replace with Map<Vector3, EnumFacing> for getting sides.
        Set<Vector3> set = new HashSet();
        getBlocksRecursive(set, world, startingPosition, null, search);
        return set;
    }

    public static void getBlocksRecursive(Set<Vector3> done, World world, Vector3 start, EnumFacing from, Class... search) {
        for (EnumFacing side : EnumFacing.values()) {
            if (side == from) continue;
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

    public static Vector3[] getDirectNearbyBlocks(TileEntity tileEntity) {
        Vector3[] blks = new Vector3[6];
        for (int x = tileEntity.getPos().getX() - 1; x < tileEntity.getPos().getX() + 1; x++) {
            for (int y = tileEntity.getPos().getY() - 1; y < tileEntity.getPos().getY() + 1; y++) {
                for (int z = tileEntity.getPos().getZ() - 1; z < tileEntity.getPos().getZ() + 1; z++) {
                    if (isDirect(new Vector3(x, y, z), new Vector3(tileEntity.getPos()))) {
                        for (int i = 0; i < blks.length; i++) {
                            if (blks[i] == null) {
                                blks[i] = new Vector3(x, y, z);
                            } else if (i == blks.length) {
                                CreepinoUtilsMod.getInstance().getLogger().info("Exceeded maximum blocks while getting nearby blocks.");
                            }
                        }
                    }
                }
            }
        }
        return blks;
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
