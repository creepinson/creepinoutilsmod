package me.creepinson.mod.api.util;

import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author creepinson
 */

public class CreepinoUtils {

    public static void entityAccelerate(Entity entity, EnumFacing direction) {
        if (direction == EnumFacing.DOWN)
            entity.addVelocity(0, -0.1, 0);
        else if (direction == EnumFacing.UP)
            entity.addVelocity(0, 0.1, 0);
        else if (direction == EnumFacing.NORTH)
            entity.addVelocity(0, 0, -0.1);
        else if (direction == EnumFacing.SOUTH)
            entity.addVelocity(0, 0, 0.1);
        else if (direction == EnumFacing.EAST)
            entity.addVelocity(-0.1, 0, 0);
        else if (direction == EnumFacing.WEST)
            entity.addVelocity(0.1, 0, 0);
    }

    public static void entityAccelerate(Entity entity, EnumFacing direction, double speed) {
        if (direction == EnumFacing.DOWN)
            entity.addVelocity(0, -speed, 0);
        else if (direction == EnumFacing.UP)
            entity.addVelocity(0, speed, 0);
        else if (direction == EnumFacing.NORTH)
            entity.addVelocity(0, 0, -speed);
        else if (direction == EnumFacing.SOUTH)
            entity.addVelocity(0, 0, speed);
        else if (direction == EnumFacing.EAST)
            entity.addVelocity(-speed, 0, 0);
        else if (direction == EnumFacing.WEST)
            entity.addVelocity(speed, 0, 0);
    }

    public static void entityLimitSpeed(Entity entity, double limit) {
        entity.motionX = MathHelper.clamp(entity.motionX, -limit, limit);
        entity.motionY = MathHelper.clamp(entity.motionY, -limit, limit);
        entity.motionZ = MathHelper.clamp(entity.motionZ, -limit, limit);
    }

    public static void entityResetFall(Entity entity) {
        entity.fallDistance = 0f;
    }

    public static EnumFacing entityGetDirection(EntityLivingBase entityLiving) {
        int facing = MathHelper.floor(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0)
            return EnumFacing.NORTH;
        else if (facing == 1)
            return EnumFacing.EAST;
        else if (facing == 2)
            return EnumFacing.SOUTH;
        else if (facing == 3)
            return EnumFacing.WEST;

        return null;
    }

    public static void moveEntityByRotation(Entity entity) {
        double motionX = (double) (-MathHelper.sin(entity.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entity.rotationPitch / 180.0F * (float) Math.PI) * 1.5F);
        double motionZ = (double) (MathHelper.cos(entity.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entity.rotationPitch / 180.0F * (float) Math.PI) * 1.5F);
        double motionY = (double) (-MathHelper.sin((entity.rotationPitch + 0F) / 180.0F * (float) Math.PI) * 1.5F);
        entity.addVelocity(motionX, motionY, motionZ);
    }

    static double AXIS_MIN_MIN = 0, AXIS_MIN_MAX = 0.1, AXIS_MAX_MIN = 0.9, AXIS_MAX_MAX = 1, AXIS_FLOOR_MIN = -0.01, AXIS_FLOOR_MAX = 0;

    public static AxisAlignedBB getCollisionBoxPart(int x, int y, int z, EnumFacing direction) {
        if (direction == EnumFacing.EAST)
            return new AxisAlignedBB(x + AXIS_MAX_MIN, y, z, x + AXIS_MAX_MAX, y + 1, z + 1);
        else if (direction == EnumFacing.WEST)
            return new AxisAlignedBB(x + AXIS_MIN_MIN, y, z, x + AXIS_MIN_MAX, y + 1, z + 1);
        else if (direction == EnumFacing.SOUTH)
            return new AxisAlignedBB(x, y, z + AXIS_MAX_MIN, x + 1, y + 1, z + AXIS_MAX_MAX);
        else if (direction == EnumFacing.NORTH)
            return new AxisAlignedBB(x, y, z + AXIS_MIN_MIN, x + 1, y + 1, z + AXIS_MIN_MAX);
        else if (direction == EnumFacing.UP)
            return new AxisAlignedBB(x, y + AXIS_MAX_MIN, z, x + 1, y + AXIS_MAX_MAX, z + 1);
        else if (direction == EnumFacing.DOWN)
            return new AxisAlignedBB(x, y + AXIS_MIN_MIN, z, x + 1, y + AXIS_MIN_MAX, z + 1);

        return null;
    }

    public static AxisAlignedBB getCollisionBoxPart(Vector3 pos, EnumFacing direction) {
        return getCollisionBoxPart(pos.intX(), pos.intY(), pos.intZ(), direction);
    }

    public static AxisAlignedBB getCollisionBoxPartFloor(Vector3 pos) {
        return getCollisionBoxPartFloor(pos);
    }


    public static AxisAlignedBB getCollisionBoxPartFloor(int x, int y, int z) {
        return new AxisAlignedBB(x, y + AXIS_FLOOR_MIN, z, x + 1, y + AXIS_FLOOR_MAX, z + 1);
    }

    public static BlockPos getCoordinatesFromSide(Vector3 pos, EnumFacing s) {
        return getCoordinatesFromSide(pos, s);
    }


    public static BlockPos getCoordinatesFromSide(int x, int y, int z, int s) {
        if (s == 0)
            y++;
        else if (s == 1)
            y--;
        else if (s == 2)
            z++;
        else if (s == 3)
            z--;
        else if (s == 4)
            x++;
        else if (s == 5)
            x--;

        return new BlockPos(x, y, z);
    }

    public static EnumFacing getDirectionFromSide(Vector3 pos, int s) {
        return getDirectionFromSide(pos.intX(), pos.intY(), pos.intZ(), s);
    }

    public static EnumFacing getDirectionFromSide(int x, int y, int z, int s) {
        if (s == 0)
            return EnumFacing.DOWN;
        else if (s == 1)
            return EnumFacing.UP;
        else if (s == 2)
            return EnumFacing.NORTH;
        else if (s == 3)
            return EnumFacing.SOUTH;
        else if (s == 4)
            return EnumFacing.WEST;
        else if (s == 5)
            return EnumFacing.EAST;

        return EnumFacing.NORTH;
    }

    public static int getBlockMetadata(IBlockAccess blockAccess, Vector3 vector) {
        return getBlockMetadata(blockAccess, vector.toBlockPos());
    }

    public static int getBlockMetadata(IBlockAccess blockAccess, int x, int y, int z) {
        return getBlockMetadata(blockAccess, new BlockPos(x, y, z));
    }

    public static int getBlockMetadata(IBlockAccess blockAccess, BlockPos pos) {
        return blockAccess.getBlockState(pos).getBlock().getMetaFromState(blockAccess.getBlockState(pos));
    }

    public static <E> boolean containsInstance(Collection<E> list, Class<? extends E> clazz) {
        for (E e : list) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
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

    public static void createBlockExplosion(World world, List<BlockPos> blocksToDestroy) {
        for (BlockPos block : blocksToDestroy) {

            float x = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
            float y = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
            float z = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
            EntityFallingBlock fallingBlock = new EntityFallingBlock(world, block.getX(), block.getY(), block.getZ(),
                    world.getBlockState(block));
            fallingBlock.setDropItemsWhenDead(false);
            fallingBlock.fallTime = 4;
            world.spawnEntity(fallingBlock);
            fallingBlock.setVelocity(x, y, z);
            world.setBlockState(block, Blocks.AIR.getDefaultState());
        }
    }

    public static BlockPos getGround(World world, BlockPos pos) {
        BlockPos blockpos;

        for (blockpos = new BlockPos(pos.getX(), world.getSeaLevel(),
                pos.getZ()); !(world.getBlockState(blockpos.up()).getBlock() instanceof BlockAir); blockpos = blockpos
                .up()) {
            ;
        }
        return blockpos;
    }

    public static Map<Vector3, EnumFacing> searchForBlockOnSides(TileEntity tile, EnumFacing[] values, Class... searchFor) {
        Map<Vector3, EnumFacing> map = new HashMap<>();
        for (EnumFacing f : EnumFacing.values()) {
            BlockPos p = tile.getPos().offset(f);
            for (Class o : searchFor) {
                if (o.isInstance(tile.getWorld().getTileEntity(p))) {
                    map.put(new Vector3(p), f);
                }
            }

        }
        return map;
    }

    /**
     * @param world A Minecraft World
     * @param start The starting position to offset from
     * @return A set of block positions that have been found
     */
    public static Set<BlockPos> searchForTileClass(World world, BlockPos start, Class... searchFor) {
        Set<BlockPos> set = new HashSet();
        getBlocksRecursive(world, start, set, null, searchFor);
        return set;
    }

    public static void getBlocksRecursive(World world, BlockPos position, Set<BlockPos> done, EnumFacing from, Class... searchFor) {
        TileEntity tile = world.getTileEntity(position);
        if (tile != null && !tile.isInvalid()) {
            if (!done.contains(position)) {
                for (Class c : searchFor) {
                    if (c.isInstance(tile)) {
                        done.add(position);
                    }
                }
            } else {
                return;
            }

            for (EnumFacing side : EnumFacing.values()) {
                if (side == from) continue;
                getBlocksRecursive(world, position.offset(side), done, side.getOpposite(), searchFor);
            }
        }
    }

    public static Map<Vector3, EnumFacing> searchForBlockOnSidesRecursive(TileEntity tile, EnumFacing[] values, List<BlockPos> done, Class... searchFor) {
        Map<Vector3, EnumFacing> map = new HashMap<>();
        boolean stop = false;
        for (EnumFacing f : values) {
            BlockPos p = tile.getPos().offset(f);
            for (Class o : searchFor) {
                if (o.isInstance(tile.getWorld().getTileEntity(p))) {
                    map.put(new Vector3(p), f);
                } else {
                    if (!done.contains(p)) {
                        done.add(p);
                    }
                    continue;
                }
            }
        }
        return map;
    }

}
