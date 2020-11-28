package com.theoparis.creepinoutils.util

import net.minecraft.entity.Entity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3i
import kotlin.math.max
import kotlin.math.sqrt

object VelocityUtil {
    const val MIN_SPEED = 0.1

    fun accelerate(entity: Entity, v: BlockPos) {
        accelerate(entity, v.toVector3d(), MIN_SPEED)
    }

    fun accelerate(entity: Entity, v: Direction, speed: Double) {
        accelerate(entity, v.directionVec.toVector3d(), speed)
    }

    fun accelerate(entity: Entity, pos: Vector3d, speed: Double) {
        val v = pos.mul(speed, speed, speed)
        entity.addVelocity(v.x, v.y, v.z)
    }

    fun limitEntitySpeed(entity: Entity, limit: Double) {
        entity.setMotion(
            MathHelper.clamp(entity.motion.x, -limit, limit),
            MathHelper.clamp(entity.motion.y, -limit, limit),
            MathHelper.clamp(entity.motion.z, -limit, limit)
        )
    }

    fun calculateParabolicVelocity(from: Vector3d, to: Vector3d, heightGain: Int): Vector3d {
        // Gravity of a potion
        val gravity = 0.115

        // Block locations
        val endGain = (to.y - from.y).toInt()
        val horizDist = sqrt(distanceSquared(from, to))

        // Height gain
        val maxGain = max(heightGain, endGain + heightGain).toDouble()

        // Solve quadratic equation for velocity
        val a = -horizDist * horizDist / (4 * maxGain)
        val c = -endGain.toDouble()
        val slope = -horizDist / (2 * a) - sqrt(horizDist * horizDist - 4 * a * c) / (2 * a)

        // Vertical velocity
        val vy = sqrt(maxGain * gravity)

        // Horizontal velocity
        val vh = vy / slope

        // Calculate horizontal direction
        val dx = (to.x - from.x).toInt()
        val dz = (to.z - from.z).toInt()
        val mag = sqrt(dx * dx + dz * dz.toDouble())
        val dirx = dx / mag
        val dirz = dz / mag

        // Horizontal velocity components
        val vx = vh * dirx
        val vz = vh * dirz
        return Vector3d(vx, vy, vz)
    }

    private fun distanceSquared(from: Vector3d, to: Vector3d): Double {
        val dx = to.x - from.x
        val dz = to.z - from.z
        return dx * dx + dz * dz
    }
}