package com.theoparis.creepinoutils.util

import dev.throwouterror.util.math.Direction
import dev.throwouterror.util.math.Tensor
import net.minecraft.entity.Entity
import net.minecraft.util.math.MathHelper

object VelocityUtil {
    const val MIN_SPEED = 0.1
    fun accelerate(entity: Entity, v: Direction) {
        accelerate(entity, v.directionVec)
    }

    fun accelerate(entity: Entity, v: net.minecraft.util.Direction) {
        accelerate(entity, Direction.values()[v.ordinal])
    }

    fun accelerate(entity: Entity, v: Tensor) {
        accelerate(entity, v, MIN_SPEED)
    }

    fun accelerate(entity: Entity, v: Direction, speed: Double) {
        accelerate(entity, v.directionVec, speed)
    }

    fun accelerate(entity: Entity, v: net.minecraft.util.Direction, speed: Double) {
        accelerate(entity, Direction.values()[v.ordinal], speed)
    }

    fun accelerate(entity: Entity, Tensor: Tensor, speed: Double) {
        val v = Tensor.clone().mul(speed.toFloat())
        entity.addVelocity(v.x(), v.y(), v.z())
    }

    fun limitEntitySpeed(entity: Entity, limit: Double) {
        entity.setMotion(
            MathHelper.clamp(entity.motion.x, -limit, limit),
            MathHelper.clamp(entity.motion.y, -limit, limit),
            MathHelper.clamp(entity.motion.z, -limit, limit)
        )
    }

    fun calculateParabolicVelocity(from: Tensor, to: Tensor, heightGain: Int): Tensor {
        // Gravity of a potion
        val gravity = 0.115

        // Block locations
        val endGain = (to.y() - from.y()).toInt()
        val horizDist = Math.sqrt(distanceSquared(from, to))

        // Height gain
        val maxGain = Math.max(heightGain, endGain + heightGain).toDouble()

        // Solve quadratic equation for velocity
        val a = -horizDist * horizDist / (4 * maxGain)
        val c = -endGain.toDouble()
        val slope = -horizDist / (2 * a) - Math.sqrt(horizDist * horizDist - 4 * a * c) / (2 * a)

        // Vertical velocity
        val vy = Math.sqrt(maxGain * gravity)

        // Horizontal velocity
        val vh = vy / slope

        // Calculate horizontal direction
        val dx = (to.x() - from.x()).toInt()
        val dz = (to.z() - from.z()).toInt()
        val mag = Math.sqrt(dx * dx + dz * dz.toDouble())
        val dirx = dx / mag
        val dirz = dz / mag

        // Horizontal velocity components
        val vx = vh * dirx
        val vz = vh * dirz
        return Tensor(vx, vy, vz)
    }

    private fun distanceSquared(from: Tensor, to: Tensor): Double {
        val dx = to.x() - from.x()
        val dz = to.z() - from.z()
        return dx * dx + dz * dz
    }
}