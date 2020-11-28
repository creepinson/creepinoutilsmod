package com.theoparis.creepinoutils.util.client

import com.theoparis.creepinoutils.util.ReflectionUtils.getField
import com.theoparis.creepinoutils.util.client.GeometryMasks.Quad
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11

class GeometryTessellator @JvmOverloads constructor(size: Int = 0x200000) : Tessellator(size) {
    private var delta = 0.0
    fun setTranslation(x: Double, y: Double, z: Double) {
        buffer.pos(x, y, z)
    }

    fun beginQuads() {
        begin(GL11.GL_QUADS)
    }

    fun beginLines() {
        begin(GL11.GL_LINES)
    }

    fun begin(mode: Int) {
        buffer.begin(mode, DefaultVertexFormats.POSITION_COLOR)
    }

    override fun draw() {
        super.draw()
    }

    fun setDelta(delta: Double) {
        this.delta = delta
    }

    fun drawCuboid(pos: BlockPos, sides: Int, argb: Int) {
        drawCuboid(pos, pos, sides, argb)
    }

    fun drawCuboid(begin: BlockPos, end: BlockPos, sides: Int, argb: Int) {
        drawCuboid(buffer, begin, end, sides, argb, delta)
    }


    companion object {
        var instance: GeometryTessellator? = null
            get() {
                if (field == null) {
                    field = GeometryTessellator()
                }
                return field
            }
            private set
        private var deltaS = 0.0
        fun setStaticDelta(delta: Double) {
            deltaS = delta
        }

        fun drawCuboid(buffer: BufferBuilder, pos: BlockPos, sides: Int, argb: Int) {
            drawCuboid(buffer, pos, pos, sides, argb)
        }

        fun drawCuboid(
            buffer: BufferBuilder, begin: BlockPos, end: BlockPos, sides: Int,
            argb: Int
        ) {
            drawCuboid(buffer, begin, end, sides, argb, deltaS)
        }

        private fun drawCuboid(
            buffer: BufferBuilder, begin: BlockPos, end: BlockPos,
            sides: Int, argb: Int, delta: Double
        ) {
            if (getDrawMode(buffer) == -1 || sides == 0) {
                return
            }
            val x0 = begin.x - delta
            val y0 = begin.y - delta
            val z0 = begin.z - delta
            val x1 = end.x + 1 + delta
            val y1 = end.y + 1 + delta
            val z1 = end.z + 1 + delta
            when (getDrawMode(buffer)) {
                GL11.GL_QUADS -> drawQuads(buffer, x0, y0, z0, x1, y1, z1, sides, argb)
                GL11.GL_LINES -> drawLines(buffer, x0, y0, z0, x1, y1, z1, sides, argb)
                else -> throw IllegalStateException("Unsupported mode!")
            }
        }

        fun getDrawMode(buffer: BufferBuilder?): Int {
            return getField("drawMode", buffer!!) as Int
        }

        fun drawQuads(
            buffer: BufferBuilder, x0: Double, y0: Double, z0: Double,
            x1: Double, y1: Double, z1: Double, sides: Int, argb: Int
        ) {
            val a = argb ushr 24 and 0xFF
            val r = argb ushr 16 and 0xFF
            val g = argb ushr 8 and 0xFF
            val b = argb and 0xFF
            drawQuads(buffer, x0, y0, z0, x1, y1, z1, sides, a, r, g, b)
        }

        fun drawQuads(
            buffer: BufferBuilder, x0: Double, y0: Double, z0: Double,
            x1: Double, y1: Double, z1: Double, sides: Int, a: Int, r: Int, g: Int,
            b: Int
        ) {
            if (sides and Quad.DOWN != 0) {
                buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex()
            }
            if (sides and Quad.UP != 0) {
                buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex()
            }
            if (sides and Quad.NORTH != 0) {
                buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex()
            }
            if (sides and Quad.SOUTH != 0) {
                buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex()
            }
            if (sides and Quad.WEST != 0) {
                buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex()
            }
            if (sides and Quad.EAST != 0) {
                buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex()
            }
        }

        fun drawLines(
            buffer: BufferBuilder, x0: Double, y0: Double, z0: Double,
            x1: Double, y1: Double, z1: Double, sides: Int, argb: Int
        ) {
            val a = argb ushr 24 and 0xFF
            val r = argb ushr 16 and 0xFF
            val g = argb ushr 8 and 0xFF
            val b = argb and 0xFF
            drawLines(buffer, x0, y0, z0, x1, y1, z1, sides, a, r, g, b)
        }

        fun drawLines(
            buffer: BufferBuilder, x0: Double, y0: Double, z0: Double,
            x1: Double, y1: Double, z1: Double, sides: Int, a: Int, r: Int, g: Int,
            b: Int
        ) {
            if (sides and GeometryMasks.Line.DOWN_WEST != 0) {
                buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.UP_WEST != 0) {
                buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.DOWN_EAST != 0) {
                buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.UP_EAST != 0) {
                buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.DOWN_NORTH != 0) {
                buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.UP_NORTH != 0) {
                buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.DOWN_SOUTH != 0) {
                buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.UP_SOUTH != 0) {
                buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.NORTH_WEST != 0) {
                buffer.pos(x0, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z0).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.NORTH_EAST != 0) {
                buffer.pos(x1, y0, z0).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z0).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.SOUTH_WEST != 0) {
                buffer.pos(x0, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x0, y1, z1).color(r, g, b, a).endVertex()
            }
            if (sides and GeometryMasks.Line.SOUTH_EAST != 0) {
                buffer.pos(x1, y0, z1).color(r, g, b, a).endVertex()
                buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex()
            }
        }
    }
}