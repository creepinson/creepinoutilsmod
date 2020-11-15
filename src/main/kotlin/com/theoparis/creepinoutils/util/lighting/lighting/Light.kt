package com.theoparis.creepinoutils.util.lighting.lighting

import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d

class Light {
    var x: Float
    var y: Float
    var z: Float
    var r: Float
    var g: Float
    var b: Float
    var a: Float
    var rx: Float
    var ry: Float
    var rz: Float
    var angle = 0f

    constructor(x: Float, y: Float, z: Float, r: Float, g: Float, b: Float, a: Float, radius: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.r = r
        this.g = g
        this.b = b
        this.a = a
        rx = 0f
        ry = radius
        rz = 0f
    }

    constructor(
        x: Float,
        y: Float,
        z: Float,
        r: Float,
        g: Float,
        b: Float,
        a: Float,
        rx: Float,
        ry: Float,
        rz: Float,
        angle: Float
    ) {
        this.x = x
        this.y = y
        this.z = z
        this.r = r
        this.g = g
        this.b = b
        this.a = a
        this.rx = rx
        this.ry = ry
        this.rz = rz
        this.angle = angle
    }

    fun radius(): Float {
        return Math.sqrt(rx * rx + ry * ry + (rz * rz).toDouble()).toFloat()
    }

    class Builder {
        private var x = Float.NaN
        private var y = Float.NaN
        private var z = Float.NaN
        private var r = Float.NaN
        private var g = Float.NaN
        private var b = Float.NaN
        private var a = Float.NaN
        private var rx = Float.NaN
        private var ry = Float.NaN
        private var rz = Float.NaN
        private var angle = Float.NaN
        fun pos(pos: BlockPos): Builder {
            return pos(pos.x + 0.5f, pos.y + 0.5f, pos.z + 0.5f)
        }

        fun pos(pos: Vector3d): Builder {
            return pos(pos.x, pos.y, pos.z)
        }

        fun pos(e: Entity): Builder {
            return pos(e.posX, e.posY, e.posZ)
        }

        fun pos(x: Double, y: Double, z: Double): Builder {
            return pos(x.toFloat(), y.toFloat(), z.toFloat())
        }

        fun pos(x: Float, y: Float, z: Float): Builder {
            this.x = x
            this.y = y
            this.z = z
            return this
        }

        fun color(c: Int, hasAlpha: Boolean): Builder {
            return color(extract(c, 2), extract(c, 1), extract(c, 0), if (hasAlpha) extract(c, 3) else 1f)
        }

        private fun extract(i: Int, idx: Int): Float {
            return (i shr idx * 8 and 0xFF) / 255f
        }

        @JvmOverloads
        fun color(r: Float, g: Float, b: Float, a: Float = 1f): Builder {
            this.r = r
            this.g = g
            this.b = b
            this.a = a
            return this
        }

        fun radius(radius: Float): Builder {
            rx = 0f
            ry = radius
            rz = 0f
            angle = (Math.PI * 2.0).toFloat()
            return this
        }

        fun direction(x: Float, y: Float, z: Float, angle: Float): Builder {
            rx = x
            ry = y
            rz = z
            this.angle = angle
            return this
        }

        fun direction(vec: Vector3d, angle: Float): Builder {
            rx = vec.x.toFloat()
            ry = vec.y.toFloat()
            rz = vec.z.toFloat()
            this.angle = angle
            return this
        }

        fun build(): Light {
            return if (java.lang.Float.isFinite(x) && java.lang.Float.isFinite(y) && java.lang.Float.isFinite(z) &&
                java.lang.Float.isFinite(r) && java.lang.Float.isFinite(g) && java.lang.Float.isFinite(b) && java.lang.Float.isFinite(
                    a
                ) &&
                java.lang.Float.isFinite(rx) && java.lang.Float.isFinite(ry) && java.lang.Float.isFinite(rz) && java.lang.Float.isFinite(
                    angle
                )
            ) {
                Light(x, y, z, r, g, b, a, rx, ry, rz, angle)
            } else {
                throw IllegalArgumentException("Position, color, and radius must be set, and cannot be infinite")
            }
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}