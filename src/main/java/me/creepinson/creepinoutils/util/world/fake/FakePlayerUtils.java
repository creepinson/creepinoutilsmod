package me.creepinson.creepinoutils.util.world.fake;

import com.google.common.base.Predicates;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket.Action;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FakePlayerUtils {
    /**
     * Sets up for a fake player to be usable to right click things. This player
     * will be put at the center of the using side.
     *
     * @param player    The player.
     * @param pos       The position of the using tile entity.
     * @param direction The direction to use in.
     * @param toHold    The stack the player will be using. Should probably come
     *                  from an ItemStackHandler or similar.
     */
    public static void setupFakePlayerForUse(FakeServerPlayer player, BlockPos pos, Direction direction,
                                             ItemStack toHold, boolean sneaking) {
        player.inventory.mainInventory.set(player.inventory.currentItem, toHold);
        float pitch = direction == Direction.UP ? -90 : direction == Direction.DOWN ? 90 : 0;
        float yaw = direction == Direction.SOUTH ? 0
                : direction == Direction.WEST ? 90 : direction == Direction.NORTH ? 180 : -90;
        Vec3i sideVec = direction.getDirectionVec();
        Direction.Axis a = direction.getAxis();
        Direction.AxisDirection ad = direction.getAxisDirection();
        double x = a == Direction.Axis.X && ad == Direction.AxisDirection.NEGATIVE ? -.5 : .5 + sideVec.getX() / 1.9D;
        double y = 0.5 + sideVec.getY() / 1.9D;
        double z = a == Direction.Axis.Z && ad == Direction.AxisDirection.NEGATIVE ? -.5 : .5 + sideVec.getZ() / 1.9D;
        player.setLocationAndAngles(pos.getX() + x, pos.getY() + y, pos.getZ() + z, yaw, pitch);
        if (!toHold.isEmpty())
            player.getAttributes().applyAttributeModifiers(toHold.getAttributeModifiers(EquipmentSlotType.MAINHAND));
        player.setSneaking(sneaking);
    }

    /**
     * Cleans up the fake player after use.
     *
     * @param player      The player.
     * @param resultStack The stack that was returned from
     *                    right/leftClickInDirection.
     * @param oldStack    The previous stack, from before use.
     */
    public static void cleanupFakePlayerFromUse(FakeServerPlayer player, ItemStack resultStack, ItemStack oldStack,
                                                Consumer<ItemStack> stackCallback) {
        if (!oldStack.isEmpty())
            player.getAttributes().removeAttributeModifiers(oldStack.getAttributeModifiers(EquipmentSlotType.MAINHAND));
        player.inventory.mainInventory.set(player.inventory.currentItem, ItemStack.EMPTY);
        stackCallback.accept(resultStack);
        if (!player.inventory.isEmpty())
            player.inventory.dropAllItems();
        player.setSneaking(false);
    }

    /**
     * Uses whatever the player happens to be holding in the given direction.
     *
     * @param player      The player.
     * @param world       The world of the calling tile entity. It may be a bad idea
     *                    to use {@link FakeServerPlayer#getEntityWorld()}.
     * @param pos         The pos of the calling tile entity.
     * @param side        The direction to use in.
     * @param sourceState The state of the calling tile entity, so we don't click
     *                    ourselves.
     * @return The remainder of whatever the player was holding. This should be set
     * back into the tile's stack handler or similar.
     */
    public static ItemStack rightClickInDirection(FakeServerPlayer player, World world, BlockPos pos, Direction side,
                                                  BlockState sourceState) {
        Vec3d base = new Vec3d(player.getPosX(), player.getPosY(), player.getPosZ());
        Vec3d look = player.getLookVec();
        Vec3d target = base.add(look.x * 5, look.y * 5, look.z * 5);
        RayTraceResult trace = world
                .rayTraceBlocks(new RayTraceContext(base, target, BlockMode.OUTLINE, FluidMode.NONE, player));
        RayTraceResult traceEntity = traceEntities(player, base, target, world);
        RayTraceResult toUse = trace == null ? traceEntity : trace;

        if (trace != null && traceEntity != null) {
            double d1 = trace.getHitVec().distanceTo(base);
            double d2 = traceEntity.getHitVec().distanceTo(base);
            toUse = traceEntity.getType() == RayTraceResult.Type.ENTITY && d1 > d2 ? traceEntity : trace;
        }

        if (toUse == null)
            return player.getHeldItemMainhand();

        ItemStack itemstack = player.getHeldItemMainhand();
        if (toUse.getType() == RayTraceResult.Type.ENTITY) {
            if (processUseEntity(player, world, ((EntityRayTraceResult) toUse).getEntity(),
                    toUse, CUseEntityPacket.Action.INTERACT_AT))
                return player.getHeldItemMainhand();
            else if (processUseEntity(player, world, ((EntityRayTraceResult) toUse).getEntity(), null,
                    CUseEntityPacket.Action.INTERACT))
                return player.getHeldItemMainhand();
        } else if (toUse.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockRayTraceResult) toUse).getPos();
            BlockState state = world.getBlockState(blockpos);
            if (state != sourceState && state.getMaterial() != Material.AIR) {
                float f = (float) (toUse.getHitVec().x - pos.getX());
                float f1 = (float) (toUse.getHitVec().y - pos.getY());
                float f2 = (float) (toUse.getHitVec().z - pos.getZ());
                ActionResultType enumactionresult = player.interactionManager.processRightClick(player, world,
                        itemstack, Hand.MAIN_HAND);
                if (enumactionresult == ActionResultType.SUCCESS)
                    return player.getHeldItemMainhand();
            }
        }

        if (toUse.getType() == RayTraceResult.Type.MISS) {
            for (int i = 1; i <= 5; i++) {
                BlockState state = world.getBlockState(pos.offset(side, i));
                if (state != sourceState && state.getMaterial() != Material.AIR) {
                    player.interactionManager.processRightClick(player, world, itemstack, Hand.MAIN_HAND);
                    return player.getHeldItemMainhand();
                }
            }
        }

        if (itemstack.isEmpty() && toUse.getType() == RayTraceResult.Type.MISS)
            ForgeHooks.onEmptyClick(player, Hand.MAIN_HAND);
        if (!itemstack.isEmpty())
            player.interactionManager.processRightClick(player, world, itemstack, Hand.MAIN_HAND);
        return player.getHeldItemMainhand();
    }

    /**
     * Attacks with whatever the player happens to be holding in the given
     * direction.
     *
     * @param player      The player.
     * @param world       The world of the calling tile entity. It may be a bad idea
     *                    to use {@link FakeServerPlayer#getEntityWorld()}.
     * @param pos         The pos of the calling tile entity.
     * @param side        The direction to attack in.
     * @param sourceState The state of the calling tile entity, so we don't click
     *                    ourselves.
     * @return The remainder of whatever the player was holding. This should be set
     * back into the tile's stack handler or similar.
     */
    public static ItemStack leftClickInDirection(FakeServerPlayer player, World world, BlockPos pos, Direction side,
                                                 BlockState sourceState) {
        Vec3d base = new Vec3d(player.getPosX(), player.getPosY(), player.getPosZ());
        Vec3d look = player.getLookVec();
        Vec3d target = base.add(look.x * 5, look.y * 5, look.z * 5);
        RayTraceResult trace = world
                .rayTraceBlocks(new RayTraceContext(base, target, BlockMode.OUTLINE, FluidMode.NONE, player));
        RayTraceResult traceEntity = traceEntities(player, base, target, world);
        RayTraceResult toUse = trace == null ? traceEntity : trace;

        if (trace != null && traceEntity != null) {
            double d1 = trace.getHitVec().distanceTo(base);
            double d2 = traceEntity.getHitVec().distanceTo(base);
            toUse = traceEntity.getType() == RayTraceResult.Type.ENTITY && d1 > d2 ? traceEntity : trace;
        }

        if (toUse == null)
            return player.getHeldItemMainhand();

        ItemStack itemstack = player.getHeldItemMainhand();
        if (toUse.getType() == RayTraceResult.Type.ENTITY) {
            if (processUseEntity(player, world, ((EntityRayTraceResult) toUse).getEntity(), null,
                    CUseEntityPacket.Action.ATTACK))
                return player.getHeldItemMainhand();
        } else if (toUse.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockRayTraceResult) toUse).getPos();
            BlockState state = world.getBlockState(blockpos);
            if (state != sourceState && state.getMaterial() != Material.AIR) {
                player.interactionManager.func_225416_a(blockpos, Action.START_DESTROY_BLOCK,
                        ((BlockRayTraceResult) toUse).getFace(), 1);
                return player.getHeldItemMainhand();
            }
        }

        if (toUse.getType() == RayTraceResult.Type.MISS) {
            for (int i = 1; i <= 5; i++) {
                BlockState state = world.getBlockState(pos.offset(side, i));
                if (state != sourceState && state.getMaterial() != Material.AIR) {
                    player.interactionManager.func_225416_a(pos.offset(side, i), Action.ABORT_DESTROY_BLOCK,
                            side.getOpposite(), 1);
                    return player.getHeldItemMainhand();
                }
            }
        }

        if (itemstack.isEmpty() && toUse.getType() == RayTraceResult.Type.MISS)
            ForgeHooks.onEmptyLeftClick(player);
        return player.getHeldItemMainhand();
    }

    /**
     * Traces for an entity.
     *
     * @param player The player.
     * @param world  The world of the calling tile entity.
     * @return A ray trace result that will likely be of type entity, but may be
     * type block, or null.
     */
    public static RayTraceResult traceEntities(FakeServerPlayer player, Vec3d base, Vec3d target, World world) {
        Entity pointedEntity = null;
        RayTraceResult result = null;
        Vec3d vec3d3 = null;
        AxisAlignedBB search = new AxisAlignedBB(base.x, base.y, base.z, target.x, target.y, target.z).grow(.5, .5, .5);
        List<Entity> list = world.getEntitiesInAABBexcluding(player, search,
                Predicates.and(entity -> entity != null && entity.canBeCollidedWith()));
        double d2 = 5;

        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = list.get(j);

            AxisAlignedBB aabb = entity1.getBoundingBox().grow(entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = AxisAlignedBB.rayTrace(Arrays.asList(aabb), base, target, entity1.getPosition());

            if (aabb.contains(base)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    vec3d3 = raytraceresult == null ? base : raytraceresult.getHitVec();
                    d2 = 0.0D;
                }
            } else if (raytraceresult != null) {
                double d3 = base.distanceTo(raytraceresult.getHitVec());

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1.getLowestRidingEntity() == player.getLowestRidingEntity()
                            && !entity1.canRiderInteract()) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                            vec3d3 = raytraceresult.getHitVec();
                        }
                    } else {
                        pointedEntity = entity1;
                        vec3d3 = raytraceresult.getHitVec();
                        d2 = d3;
                    }
                }
            }
        }

        if (pointedEntity != null && base.distanceTo(vec3d3) > 5) {
            pointedEntity = null;
            result = BlockRayTraceResult.createMiss(vec3d3, null, new BlockPos(vec3d3));
        }

        if (pointedEntity != null) {
            result = new EntityRayTraceResult(pointedEntity, vec3d3);
        }

        return result;
    }

    /**
     * Processes the using of an entity from the server side.
     *
     * @param player The player.
     * @param world  The world of the calling tile entity.
     * @param entity The entity to interact with.
     * @param result The actual ray trace result, only necessary if using
     *               {@link CUseEntityPacket.Action#INTERACT_AT}
     * @param action The type of interaction to perform.
     * @return If the entity was used.
     */
    public static boolean processUseEntity(FakeServerPlayer player, World world, Entity entity,
                                           @Nullable RayTraceResult result, CUseEntityPacket.Action action) {
        if (entity != null) {
            boolean flag = player.canEntityBeSeen(entity);
            double d0 = 36.0D;

            if (!flag)
                d0 = 9.0D;

            if (player.getDistanceSq(entity) < d0) {
                if (action == CUseEntityPacket.Action.INTERACT) {
                    return player.interactOn(entity, Hand.MAIN_HAND) == ActionResultType.SUCCESS;
                } else if (action == CUseEntityPacket.Action.INTERACT_AT) {
                    if (ForgeHooks.onInteractEntityAt(player, entity, result.getHitVec(), Hand.MAIN_HAND) != null)
                        return false;
                    return entity.applyPlayerInteraction(player, result.getHitVec(),
                            Hand.MAIN_HAND) == ActionResultType.SUCCESS;
                } else if (action == CUseEntityPacket.Action.ATTACK) {
                    if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity
                            || entity instanceof ArrowEntity || entity == player)
                        return false;
                    player.attackTargetEntityWithCurrentItem(entity);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A copy-paste of the SideOnly rayTrace method.
     */
    public static RayTraceResult rayTrace(FakeServerPlayer player, World world, double reachDist, float partialTicks) {
        Vec3d vec3d = player.getEyePosition(partialTicks);
        Vec3d vec3d1 = player.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * reachDist, vec3d1.y * reachDist, vec3d1.z * reachDist);
        return world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d2, BlockMode.OUTLINE, FluidMode.NONE, player));
    }
}
