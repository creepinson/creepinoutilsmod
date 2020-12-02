package com.theoparis.creepinoutils.util.client.obj

import com.mojang.blaze3d.vertex.IVertexBuilder
import com.theoparis.creepinoutils.CreepinoUtilsMod
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.*
import org.apache.commons.io.output.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

class ObjModel(resourceLocation: ResourceLocation) {
    var filename: String
    var objParts: MutableList<ObjPart> = ArrayList()

    @Throws(IOException::class)
    protected fun read(resource: InputStream): ByteArray {
        var i: Int
        val buffer = ByteArray(65565)
        val out = ByteArrayOutputStream()
        while (resource.read(buffer, 0, buffer.size).also { i = it } != -1) {
            out.write(buffer, 0, i)
        }
        out.flush()
        out.close()
        return out.toByteArray()
    }

    fun getNormal(p1: Vector3f, p2: Vector3f, p3: Vector3f): Vector3f {
        val output = Vector3f()

        // Calculate Edges:
        val calU = Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)
        val calV = Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z)

        // Cross Edges
        output.set(
            calU.y * calV.z - calU.z * calV.y,
            calU.z * calV.x - calU.x * calV.z,
            calU.x * calV.y - calU.y * calV.x
        )
        output.normalize() // normalize()
        return output
    }

    fun renderAll(
        vertexBuilder: IVertexBuilder,
        matrix3f: Matrix3f,
        matrix4f: Matrix4f,
        brightness: Int,
        fade: Int,
        color: Vector4f,
        textureOffset: Vector2f
    ) {
        objParts.sortWith { a: ObjPart?, b: ObjPart? ->
            val v: Vector3d = Minecraft.getInstance().getRenderViewEntity()!!.positionVec
            val aDist: Double =
                v.distanceTo(Vector3d(a?.center!!.x.toDouble(), a.center!!.y.toDouble(), a.center!!.z.toDouble()))
            val bDist: Double =
                v.distanceTo(Vector3d(b?.center!!.x.toDouble(), b.center!!.y.toDouble(), b.center!!.z.toDouble()))
            aDist.compareTo(bDist)
        }
        for (objPart in objParts) {
            renderPart(vertexBuilder, matrix3f, matrix4f, brightness, fade, objPart, color, textureOffset)
        }
    }

    fun renderPartGroup(
        vertexBuilder: IVertexBuilder,
        matrix3f: Matrix3f,
        matrix4f: Matrix4f,
        brightness: Int,
        fade: Int,
        color: Vector4f,
        textureOffset: Vector2f,
        group: String
    ) {
        for (objPart in objParts) {
            if (objPart.name == group) {
                renderPart(vertexBuilder, matrix3f, matrix4f, brightness, fade, objPart, color, textureOffset)
            }
        }
    }

    fun renderPart(
        vertexBuilder: IVertexBuilder,
        matrix3f: Matrix3f,
        matrix4f: Matrix4f,
        brightness: Int,
        fade: Int,
        objPart: ObjPart?,
        color: Vector4f,
        textureOffset: Vector2f
    ) {
        // Mesh data:
        if (objPart!!.mesh == null) {
            return
        }
        val indices = objPart.mesh!!.indices
        val vertices = objPart.mesh!!.vertices

        // Build:
        var i = 0
        while (i < indices.size) {


            // Normal:
            var normal: Vector3f? = objPart.mesh!!.normals[i]
            if (normal == null) {
                normal = getNormal(
                    vertices[indices[i]]!!.pos, vertices[indices[i + 1]]!!.pos, vertices[indices[i + 2]]!!.pos
                )
                objPart.mesh!!.normals[i] = normal
            }
            for (iv in 0..2) {
                val v = objPart.mesh!!.vertices[indices[i + iv]]!!
                vertexBuilder
                    .pos(matrix4f, v.pos.x, v.pos.y, v.pos.z)
                    .color(color.x, color.y, color.z, color.w)
                    .tex(v.texCoords.x + textureOffset.x * 0.01f, 1f - (v.texCoords.y + textureOffset.y * 0.01f))
                    .overlay(0, 10 - fade)
                    .lightmap(brightness)
                    .normal(matrix3f, normal.x, normal.y, normal.z)
                    .endVertex()
            }
            i += 3
        }
    }

    init {
        filename = resourceLocation.path
        val path: String = resourceLocation.toString()
        try {
            val inputStream: InputStream =
                Minecraft.getInstance().resourceManager.getResource(resourceLocation).inputStream
            val content = String(read(inputStream), Charset.forName("UTF-8"))
            val startPath = path.substring(0, path.lastIndexOf('/') + 1)
            val map = OBJLoader().loadModel(startPath, content)
            objParts.clear()
            val keys: Set<ObjPart?> = map.keys
            val it = keys.iterator()
            while (it.hasNext()) {
                val objPart = it.next()
                val mesh = Mesh()
                objPart!!.mesh = mesh
                objParts.add(objPart)
                map[objPart]!!.toMesh(mesh)
            }
        } catch (e: Exception) {
            CreepinoUtilsMod.logger.warn("", "Unable to load model: $resourceLocation")
            e.toString()
        }
    }
}