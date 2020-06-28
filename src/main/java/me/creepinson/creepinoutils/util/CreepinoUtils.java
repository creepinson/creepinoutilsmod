package me.creepinson.creepinoutils.util;

import dev.throwouterror.util.math.Tensor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * @author creepinson
 */

public class CreepinoUtils {

    public static void resetEntityFall(Entity entity) {
        entity.fallDistance = 0f;
    }

    public static Direction getEntityDirection(LivingEntity livingEntity) {
        int facing = MathHelper.floor(livingEntity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        // TODO: use switch statement
        if (facing == 0)
            return Direction.NORTH;
        else if (facing == 1)
            return Direction.EAST;
        else if (facing == 2)
            return Direction.SOUTH;
        else return Direction.WEST;
    }

    public static void moveEntityByRotation(Entity entity) {
        double motionX = -MathHelper.sin(entity.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entity.rotationPitch / 180.0F * (float) Math.PI) * 1.5F;
        double motionZ = MathHelper.cos(entity.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entity.rotationPitch / 180.0F * (float) Math.PI) * 1.5F;
        double motionY = -MathHelper.sin((entity.rotationPitch + 0F) / 180.0F * (float) Math.PI) * 1.5F;
        entity.addVelocity(motionX, motionY, motionZ);
    }

    static double AXIS_MIN_MIN = 0, AXIS_MIN_MAX = 0.1, AXIS_MAX_MIN = 0.9, AXIS_MAX_MAX = 1, AXIS_FLOOR_MIN = -0.01, AXIS_FLOOR_MAX = 0;

    public static AxisAlignedBB getCollisionBoxPart(int x, int y, int z, Direction direction) {
        if (direction == Direction.EAST)
            return new AxisAlignedBB(x + AXIS_MAX_MIN, y, z, x + AXIS_MAX_MAX, y + 1, z + 1);
        else if (direction == Direction.WEST)
            return new AxisAlignedBB(x + AXIS_MIN_MIN, y, z, x + AXIS_MIN_MAX, y + 1, z + 1);
        else if (direction == Direction.SOUTH)
            return new AxisAlignedBB(x, y, z + AXIS_MAX_MIN, x + 1, y + 1, z + AXIS_MAX_MAX);
        else if (direction == Direction.NORTH)
            return new AxisAlignedBB(x, y, z + AXIS_MIN_MIN, x + 1, y + 1, z + AXIS_MIN_MAX);
        else if (direction == Direction.UP)
            return new AxisAlignedBB(x, y + AXIS_MAX_MIN, z, x + 1, y + AXIS_MAX_MAX, z + 1);
        else if (direction == Direction.DOWN)
            return new AxisAlignedBB(x, y + AXIS_MIN_MIN, z, x + 1, y + AXIS_MIN_MAX, z + 1);

        return null;
    }

    public static AxisAlignedBB getCollisionBoxPart(Tensor pos, Direction direction) {
        return getCollisionBoxPart(pos.intX(), pos.intY(), pos.intZ(), direction);
    }

    public static AxisAlignedBB getCollisionBoxPartFloor(Tensor pos) {
        return getCollisionBoxPartFloor(pos.intX(), pos.intY(), pos.intZ());
    }


    public static AxisAlignedBB getCollisionBoxPartFloor(int x, int y, int z) {
        return new AxisAlignedBB(x, y + AXIS_FLOOR_MIN, z, x + 1, y + AXIS_FLOOR_MAX, z + 1);
    }

    public static BlockPos getCoordinatesFromSide(Tensor pos, Direction s) {
        return getCoordinatesFromSide(pos.intX(), pos.intY(), pos.intZ(), s.ordinal());
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

    public static Direction getDirectionFromSide(Tensor pos, int s) {
        return getDirectionFromSide(pos.intX(), pos.intY(), pos.intZ(), s);
    }

    public static Direction getDirectionFromSide(int x, int y, int z, int s) {
        if (s == 0)
            return Direction.DOWN;
        else if (s == 1)
            return Direction.UP;
        else if (s == 2)
            return Direction.NORTH;
        else if (s == 3)
            return Direction.SOUTH;
        else if (s == 4)
            return Direction.WEST;
        else if (s == 5)
            return Direction.EAST;

        return Direction.NORTH;
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

    public static void createBlockExplosion(World world, List<BlockPos> blocksToDestroy) {
        for (BlockPos block : blocksToDestroy) {

            float x = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
            float y = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
            float z = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
            FallingBlockEntity fallingBlock = new FallingBlockEntity(world, block.getX(), block.getY(), block.getZ(),
                    world.getBlockState(block));
            fallingBlock.shouldDropItem = false;
            fallingBlock.fallTime = 4;
            world.addEntity(fallingBlock);
            fallingBlock.setVelocity(x, y, z);
            world.setBlockState(block, Blocks.AIR.getDefaultState());
        }
    }

    public static BlockPos getGround(World world, BlockPos pos) {
        BlockPos blockpos;

        for (blockpos = new BlockPos(pos.getX(), world.getSeaLevel(),
                pos.getZ()); !(world.getBlockState(blockpos.up()).getBlock().getBlock() == Blocks.AIR); blockpos = blockpos
                .up()) {
        }
        return blockpos;
    }

}
