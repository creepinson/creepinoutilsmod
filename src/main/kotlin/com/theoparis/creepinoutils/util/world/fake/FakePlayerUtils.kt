package com.theoparis.creepinoutils.util.world.fake

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.item.ExperienceOrbEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.CPlayerDiggingPacket
import net.minecraft.network.play.client.CUseEntityPacket
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Direction.AxisDirection
import net.minecraft.util.Hand
import net.minecraft.util.math.*
import net.minecraft.util.math.RayTraceContext.BlockMode
import net.minecraft.util.math.RayTraceContext.FluidMode
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import java.util.*
import java.util.function.Consumer

object FakePlayerUtils {
    /**
     * Sets up for a fake player to be usable to right click things. This player
     * will be put at the center of the using side.
     *
     * @param player    The player.
     * @param pos       The position of the using tile entity.
     * @param direction The direction to use in.
     * @param toHold    The stack the player will be using. Should probably come
     * from an ItemStackHandler or similar.
     */
    fun setupFakePlayerForUse(
        player: FakeServerPlayer, pos: BlockPos, direction: Direction,
        toHold: ItemStack, sneaking: Boolean
    ) {
        player.inventory.mainInventory[player.inventory.currentItem] = toHold
        val pitch: Float = if (direction == Direction.UP) -90f else if (direction == Direction.DOWN) 90f else 0f
        val yaw: Float =
            if (direction == Direction.SOUTH) 0f else if (direction == Direction.WEST) 90f else if (direction == Direction.NORTH) 180f else -90f
        val sideVec = direction.directionVec
        val a = direction.axis
        val ad = direction.axisDirection
        val x = if (a === Direction.Axis.X && ad == AxisDirection.NEGATIVE) -.5 else .5 + sideVec.x / 1.9
        val y = 0.5 + sideVec.y / 1.9
        val z = if (a === Direction.Axis.Z && ad == AxisDirection.NEGATIVE) -.5 else .5 + sideVec.z / 1.9
        player.setLocationAndAngles(pos.x + x, pos.y + y, pos.z + z, yaw, pitch)
        if (!toHold.isEmpty) player.attributeManager.reapplyModifiers(toHold.getAttributeModifiers(EquipmentSlotType.MAINHAND))
        player.isSneaking = sneaking
    }

    /**
     * Cleans up the fake player after use.
     *
     * @param player      The player.
     * @param resultStack The stack that was returned from
     * right/leftClickInDirection.
     * @param oldStack    The previous stack, from before use.
     */
    fun cleanupFakePlayerFromUse(
        player: FakeServerPlayer, resultStack: ItemStack?, oldStack: ItemStack,
        stackCallback: Consumer<ItemStack?>
    ) {
        if (!oldStack.isEmpty) player.attributeManager.removeModifiers(oldStack.getAttributeModifiers(EquipmentSlotType.MAINHAND))
        player.inventory.mainInventory[player.inventory.currentItem] = ItemStack.EMPTY
        stackCallback.accept(resultStack)
        if (!player.inventory.isEmpty) player.inventory.dropAllItems()
        player.isSneaking = false
    }

    /**
     * Uses whatever the player happens to be holding in the given direction.
     *
     * @param player      The player.
     * @param world       The world of the calling tile entity. It may be a bad idea
     * to use [FakeServerPlayer.getEntityWorld].
     * @param pos         The pos of the calling tile entity.
     * @param side        The direction to use in.
     * @param sourceState The state of the calling tile entity, so we don't click
     * ourselves.
     * @return The remainder of whatever the player was holding. This should be set
     * back into the tile's stack handler or similar.
     */
    fun rightClickInDirection(
        player: FakeServerPlayer, world: World, pos: BlockPos, side: Direction?,
        sourceState: BlockState
    ): ItemStack {
        val base = Vector3d(player.posX, player.posY, player.posZ)
        val look = player.lookVec
        val target = base.add(look.x * 5, look.y * 5, look.z * 5)
        val trace: RayTraceResult? = world
            .rayTraceBlocks(RayTraceContext(base, target, BlockMode.OUTLINE, FluidMode.NONE, player))
        val traceEntity = traceEntities(player, base, target, world)
        var toUse = trace ?: traceEntity
        if (trace != null && traceEntity != null) {
            val d1 = trace.hitVec.distanceTo(base)
            val d2 = traceEntity.hitVec.distanceTo(base)
            toUse = if (traceEntity.type == RayTraceResult.Type.ENTITY && d1 > d2) traceEntity else trace
        }
        if (toUse == null) return player.heldItemMainhand
        val itemstack = player.heldItemMainhand
        if (toUse.type == RayTraceResult.Type.ENTITY) {
            if (processUseEntity(
                    player, world, (toUse as EntityRayTraceResult).entity,
                    toUse, CUseEntityPacket.Action.INTERACT_AT
                )
            ) return player.heldItemMainhand else if (processUseEntity(
                    player, world, toUse.entity, null,
                    CUseEntityPacket.Action.INTERACT
                )
            ) return player.heldItemMainhand
        } else if (toUse.type == RayTraceResult.Type.BLOCK) {
            val blockpos = (toUse as BlockRayTraceResult).pos
            val state = world.getBlockState(blockpos)
            if (state !== sourceState && state.material != Material.AIR) {
                val f = (toUse.getHitVec().x - pos.x).toFloat()
                val f1 = (toUse.getHitVec().y - pos.y).toFloat()
                val f2 = (toUse.getHitVec().z - pos.z).toFloat()
                val enumactionresult = player.interactionManager.processRightClick(
                    player, world,
                    itemstack, Hand.MAIN_HAND
                )
                if (enumactionresult == ActionResultType.SUCCESS) return player.heldItemMainhand
            }
        }
        if (toUse.type == RayTraceResult.Type.MISS) {
            for (i in 1..5) {
                val state = world.getBlockState(pos.offset(side, i))
                if (state !== sourceState && state.material != Material.AIR) {
                    player.interactionManager.processRightClick(player, world, itemstack, Hand.MAIN_HAND)
                    return player.heldItemMainhand
                }
            }
        }
        if (itemstack.isEmpty && toUse.type == RayTraceResult.Type.MISS) ForgeHooks.onEmptyClick(player, Hand.MAIN_HAND)
        if (!itemstack.isEmpty) player.interactionManager.processRightClick(player, world, itemstack, Hand.MAIN_HAND)
        return player.heldItemMainhand
    }

    /**
     * Attacks with whatever the player happens to be holding in the given
     * direction.
     *
     * @param player      The player.
     * @param world       The world of the calling tile entity. It may be a bad idea
     * to use [FakeServerPlayer.getEntityWorld].
     * @param pos         The pos of the calling tile entity.
     * @param side        The direction to attack in.
     * @param sourceState The state of the calling tile entity, so we don't click
     * ourselves.
     * @return The remainder of whatever the player was holding. This should be set
     * back into the tile's stack handler or similar.
     */
    fun leftClickInDirection(
        player: FakeServerPlayer, world: World, pos: BlockPos, side: Direction,
        sourceState: BlockState
    ): ItemStack {
        val base = Vector3d(player.posX, player.posY, player.posZ)
        val look = player.lookVec
        val target = base.add(look.x * 5, look.y * 5, look.z * 5)
        val trace: RayTraceResult? = world
            .rayTraceBlocks(RayTraceContext(base, target, BlockMode.OUTLINE, FluidMode.NONE, player))
        val traceEntity = traceEntities(player, base, target, world)
        var toUse = trace ?: traceEntity
        if (trace != null && traceEntity != null) {
            val d1 = trace.hitVec.distanceTo(base)
            val d2 = traceEntity.hitVec.distanceTo(base)
            toUse = if (traceEntity.type == RayTraceResult.Type.ENTITY && d1 > d2) traceEntity else trace
        }
        if (toUse == null) return player.heldItemMainhand
        val itemstack = player.heldItemMainhand
        if (toUse.type == RayTraceResult.Type.ENTITY) {
            if (processUseEntity(
                    player, world, (toUse as EntityRayTraceResult).entity, null,
                    CUseEntityPacket.Action.ATTACK
                )
            ) return player.heldItemMainhand
        } else if (toUse.type == RayTraceResult.Type.BLOCK) {
            val blockpos = (toUse as BlockRayTraceResult).pos
            val state = world.getBlockState(blockpos)
            if (state !== sourceState && state.material != Material.AIR) {
                player.interactionManager.func_225416_a(
                    blockpos, CPlayerDiggingPacket.Action.START_DESTROY_BLOCK,
                    toUse.face, 1
                )
                return player.heldItemMainhand
            }
        }
        if (toUse.type == RayTraceResult.Type.MISS) {
            for (i in 1..5) {
                val state = world.getBlockState(pos.offset(side, i))
                if (state !== sourceState && state.material != Material.AIR) {
                    player.interactionManager.func_225416_a(
                        pos.offset(side, i), CPlayerDiggingPacket.Action.ABORT_DESTROY_BLOCK,
                        side.opposite, 1
                    )
                    return player.heldItemMainhand
                }
            }
        }
        if (itemstack.isEmpty && toUse.type == RayTraceResult.Type.MISS) ForgeHooks.onEmptyLeftClick(player)
        return player.heldItemMainhand
    }

    /**
     * Traces for an entity.
     *
     * @param player The player.
     * @param world  The world of the calling tile entity.
     * @return A ray trace result that will likely be of type entity, but may be
     * type block, or null.
     */
    fun traceEntities(player: FakeServerPlayer, base: Vector3d, target: Vector3d, world: World): RayTraceResult? {
        var pointedEntity: Entity? = null
        var result: RayTraceResult? = null
        var vec3d3: Vector3d? = null
        val search = AxisAlignedBB(base.x, base.y, base.z, target.x, target.y, target.z).grow(.5, .5, .5)
        val list = world.getEntitiesInAABBexcluding(
            player, search,
            Predicates.and(Predicate { entity: Entity? -> entity != null && entity.canBeCollidedWith() })
        )
        var d2 = 5.0
        for (j in list.indices) {
            val entity1 = list[j]
            val aabb = entity1.boundingBox.grow(entity1.collisionBorderSize.toDouble())
            val raytraceresult: RayTraceResult? =
                AxisAlignedBB.rayTrace(Arrays.asList(aabb), base, target, entity1.position)
            if (aabb.contains(base)) {
                if (d2 >= 0.0) {
                    pointedEntity = entity1
                    vec3d3 = if (raytraceresult == null) base else raytraceresult.hitVec
                    d2 = 0.0
                }
            } else if (raytraceresult != null) {
                val d3 = base.distanceTo(raytraceresult.hitVec)
                if (d3 < d2 || d2 == 0.0) {
                    if (entity1.lowestRidingEntity === player.lowestRidingEntity
                        && !entity1.canRiderInteract()
                    ) {
                        if (d2 == 0.0) {
                            pointedEntity = entity1
                            vec3d3 = raytraceresult.hitVec
                        }
                    } else {
                        pointedEntity = entity1
                        vec3d3 = raytraceresult.hitVec
                        d2 = d3
                    }
                }
            }
        }
        if (pointedEntity != null && base.distanceTo(vec3d3) > 5) {
            pointedEntity = null
            result = BlockRayTraceResult.createMiss(vec3d3, null, BlockPos(vec3d3))
        }
        if (pointedEntity != null) {
            result = EntityRayTraceResult(pointedEntity, vec3d3)
        }
        return result
    }

    /**
     * Processes the using of an entity from the server side.
     *
     * @param player The player.
     * @param world  The world of the calling tile entity.
     * @param entity The entity to interact with.
     * @param result The actual ray trace result, only necessary if using
     * [CUseEntityPacket.Action.INTERACT_AT]
     * @param action The type of interaction to perform.
     * @return If the entity was used.
     */
    fun processUseEntity(
        player: FakeServerPlayer, world: World?, entity: Entity?,
        result: RayTraceResult?, action: CUseEntityPacket.Action
    ): Boolean {
        if (entity != null) {
            val flag = player.canEntityBeSeen(entity)
            var d0 = 36.0
            if (!flag) d0 = 9.0
            if (player.getDistanceSq(entity) < d0) {
                if (action == CUseEntityPacket.Action.INTERACT) {
                    return player.interactOn(entity, Hand.MAIN_HAND) == ActionResultType.SUCCESS
                } else if (action == CUseEntityPacket.Action.INTERACT_AT) {
                    return if (ForgeHooks.onInteractEntityAt(
                            player,
                            entity,
                            result!!.hitVec,
                            Hand.MAIN_HAND
                        ) != null
                    ) false else entity.applyPlayerInteraction(
                        player, result.hitVec,
                        Hand.MAIN_HAND
                    ) == ActionResultType.SUCCESS
                } else if (action == CUseEntityPacket.Action.ATTACK) {
                    if (entity is ItemEntity || entity is ExperienceOrbEntity
                        || entity is ArrowEntity || entity === player
                    ) return false
                    player.attackTargetEntityWithCurrentItem(entity)
                    return true
                }
            }
        }
        return false
    }

    /**
     * A copy-paste of the SideOnly rayTrace method.
     */
    fun rayTrace(player: FakeServerPlayer, world: World, reachDist: Double, partialTicks: Float): RayTraceResult {
        val vec3d = player.getEyePosition(partialTicks)
        val vec3d1 = player.getLook(partialTicks)
        val vec3d2 = vec3d.add(vec3d1.x * reachDist, vec3d1.y * reachDist, vec3d1.z * reachDist)
        return world.rayTraceBlocks(RayTraceContext(vec3d, vec3d2, BlockMode.OUTLINE, FluidMode.NONE, player))
    }
}