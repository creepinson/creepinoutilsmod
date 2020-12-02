package com.theoparis.creepinoutils.util.client.obj

import com.theoparis.creepinoutils.util.client.obj.OBJLoader.OBJIndex
import net.minecraft.util.math.vector.Vector2f
import net.minecraft.util.math.vector.Vector3f
import java.util.*

class IndexedModel {
    val positions: ArrayList<Vector3f> = ArrayList()
    val texCoords: ArrayList<Vector2f> = ArrayList()
    val normals: ArrayList<Vector3f> = ArrayList()
    val tangents: ArrayList<Vector3f> = ArrayList()
    val indices: ArrayList<Int> = ArrayList()
    val objIndices: ArrayList<OBJIndex> = ArrayList()
    fun toMesh(mesh: Mesh) {
        val verticesList = ArrayList<Vertex>()
        val n = Math.min(positions.size, Math.min(texCoords.size, normals.size))
        for (i in 0 until n) {
            val vertex = Vertex(
                positions[i],
                texCoords[i],
                normals[i],
                Vector3f()
            )
            verticesList.add(vertex)
        }
        val indicesArray = indices.toTypedArray()
        val verticesArray: Array<Vertex?> = verticesList.toTypedArray()
        val indicesArrayInt = IntArray(indicesArray.size)
        for (i in indicesArray.indices) indicesArrayInt[i] = indicesArray[i]
        mesh.vertices = verticesArray
        mesh.indices = indicesArrayInt
    }

    fun computeNormals() {
        run {
            var i = 0
            while (i < indices.size) {
                val x = indices[i]
                val y = indices[i + 1]
                val z = indices[i + 2]

                // Per Vertex Normal:
                var v = positions[y].copy()
                v.sub(positions[x])
                val l0 = v
                v = positions[z].copy()
                v.sub(positions[x])
                val l1 = v
                v = l0.copy()
                v.cross(l1) //cross(l0, l1)
                val normal = v
                v = normals[x].copy()
                v.add(normal)
                normals[x] = v
                v = normals[y].copy()
                v.add(normal)
                normals[y] = v
                v = normals[z].copy()
                v.add(normal)
                normals[z] = v
                i += 3
            }
        }
        for (i in normals.indices) {
            normals[i].normalize()
        }
    }

    fun getFaceNormal(p1: Vector3f, p2: Vector3f, p3: Vector3f): Vector3f {
        val output = Vector3f()

        // Calculate Edges:
        val calU = Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)
        val calV = Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z)

        // Cross Edges:
        output[calU.y * calV.z - calU.z * calV.y, calU.z * calV.x - calU.x * calV.z] = calU.x * calV.y - calU.y * calV.x
        output.normalize() // normalize()
        return output
    }

    fun computeTangents() {
        tangents.clear()
        for (i in positions.indices) tangents.add(Vector3f())
        run {
            var i = 0
            while (i < indices.size) {
                val i0 = indices[i]
                val i1 = indices[i + 1]
                val i2 = indices[i + 2]
                var v = positions[i1].copy() as Vector3f // clone()
                v.sub(positions[i0])
                val edge1 = v
                v = positions[i2].copy() as Vector3f // clone()
                v.sub(positions[i0])
                val edge2 = v
                val deltaU1 = (texCoords[i1].x - texCoords[i0].x).toDouble()
                val deltaU2 = (texCoords[i2].x - texCoords[i0].x).toDouble()
                val deltaV1 = (texCoords[i1].y - texCoords[i0].y).toDouble()
                val deltaV2 = (texCoords[i2].y - texCoords[i0].y).toDouble()
                val dividend = deltaU1 * deltaV2 - deltaU2 * deltaV1
                val f: Float = if (dividend == 0.0) 0.0f else 1.0f / dividend.toFloat()
                val tangent = Vector3f(
                    (f * (deltaV2 * edge1.x - deltaV1 * edge2.x)).toFloat(),
                    (f * (deltaV2 * edge1.y - deltaV1 * edge2.y)).toFloat(),
                    (f * (deltaV2 * edge1.z - deltaV1 * edge2.z)).toFloat()
                )
                v = tangents[i0].copy() as Vector3f // clone()
                v.add(tangent) // add()
                tangents[i0] = v
                v = tangents[i1].copy() as Vector3f // clone()
                v.add(tangent)
                tangents[i1] = v
                v = tangents[i2].copy() as Vector3f // clone()
                v.add(tangent)
                tangents[i2] = v
                i += 3
            }
        }
        for (i in tangents.indices) tangents[i].normalize() // normalize()
    }

    fun computeCenter(): Vector3f {
        var x = 0f
        var y = 0f
        var z = 0f
        for (position in positions) {
            x += position.x
            y += position.y
            z += position.z
        }
        x /= positions.size.toFloat()
        y /= positions.size.toFloat()
        z /= positions.size.toFloat()
        return Vector3f(x, y, z)
    }

}