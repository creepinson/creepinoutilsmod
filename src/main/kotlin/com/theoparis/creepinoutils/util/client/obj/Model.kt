package com.theoparis.creepinoutils.util.client.obj

import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f

abstract class Model {
    abstract fun render(vertexBuilder: IVertexBuilder?, matrix3f: Matrix3f?, matrix4f: Matrix4f?, brightness: Int)
    abstract fun renderGroups(
        vertexBuilder: IVertexBuilder?,
        matrix3f: Matrix3f?,
        matrix4f: Matrix4f?,
        brightness: Int,
        s: String?
    )
}