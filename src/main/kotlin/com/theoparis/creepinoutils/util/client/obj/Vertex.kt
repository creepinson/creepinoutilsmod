package com.theoparis.creepinoutils.util.client.obj

import net.minecraft.util.math.vector.Vector2f
import net.minecraft.util.math.vector.Vector3f

class Vertex(
    val pos: Vector3f, val texCoords: Vector2f,
    /** Returns per vertex normal for smoother shading.  */
    val normal: Vector3f, val tangent: Vector3f
)