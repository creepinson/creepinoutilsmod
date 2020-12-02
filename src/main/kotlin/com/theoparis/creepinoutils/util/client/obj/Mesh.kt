package com.theoparis.creepinoutils.util.client.obj

import net.minecraft.util.math.vector.Vector3f

class Mesh {
    var indices: IntArray = intArrayOf()
    var vertices: Array<Vertex?> = arrayOf()
    var normals: Array<Vector3f?> = arrayOf()
}