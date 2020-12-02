package com.theoparis.creepinoutils.util.client.obj

import net.minecraft.util.math.vector.Vector2f
import net.minecraft.util.math.vector.Vector3f
import org.apache.commons.io.output.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

class OBJLoader {
    class OBJIndex {
        var positionIndex = 0
        var texCoordsIndex = 0
        var normalIndex = 0
        override fun equals(o: Any?): Boolean {
            if (o is OBJIndex) {
                val index = o
                return index.normalIndex == normalIndex && index.positionIndex == positionIndex && index.texCoordsIndex == texCoordsIndex
            }
            return false
        }

        override fun hashCode(): Int {
            val base = 17
            val multiplier = 31
            var result = base
            result = multiplier * result + positionIndex
            result = multiplier * result + texCoordsIndex
            result = multiplier * result + normalIndex
            return result
        }
    }

    private var hasNormals = false
    private var hasTexCoords = false

    @Throws(Exception::class)
    fun loadModel(startPath: String?, res: String): HashMap<ObjPart, IndexedModel> {
        return try {
            hasNormals = true
            hasTexCoords = true
            var result = IndexedModel()
            var normalModel = IndexedModel()
            val lines = res.split("\n|\r").toTypedArray()
            val posOffset = 0
            val indicesOffset = 0
            val texOffset = 0
            val normOffset = 0
            val positions = ArrayList<Vector3f>()
            val texCoords = ArrayList<Vector2f>()
            val normals = ArrayList<Vector3f>()
            var indices: ArrayList<OBJIndex> = ArrayList()
            val materials = ArrayList<Material>()
            val resultIndexMap = HashMapWithDefault<OBJIndex, Int>()
            val normalIndexMap = HashMapWithDefault<Int, Int>()
            val indexMap = HashMapWithDefault<Int, Int>()
            resultIndexMap.setDefault(-1)
            normalIndexMap.setDefault(-1)
            indexMap.setDefault(-1)
            val map = HashMap<ObjPart, IndexedModel>()
            var currentObject: ObjPart
            val objects = HashMap<ObjPart, Array<IndexedModel>>()
            objects[ObjPart("main").also { currentObject = it }] = arrayOf(result, normalModel)
            for (line in lines) {
                if (line.trim { it <= ' ' } != "") {
                    val parts = trim(line.split(" ").toTypedArray())
                    if (parts.isEmpty()) continue
                    if (parts[0] == COMMENT) {
                        continue
                    } else if (parts[0] == POSITION) {
                        positions.add(Vector3f(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat()))
                    } else if (parts[0] == FACE) {
                        for (i in 0 until parts.size - 3) {
                            indices!!.add(parseOBJIndex(parts[1], posOffset, texOffset, normOffset))
                            indices.add(parseOBJIndex(parts[2 + i], posOffset, texOffset, normOffset))
                            indices.add(parseOBJIndex(parts[3 + i], posOffset, texOffset, normOffset))
                        }
                    } else if (parts[0] == NORMAL) {
                        normals.add(Vector3f(parts[1].toFloat(), parts[2].toFloat(), parts[3].toFloat()))
                    } else if (parts[0] == TEX_COORDS) {
                        texCoords.add(Vector2f(parts[1].toFloat(), parts[2].toFloat()))
                    } else if (parts[0] == NEW_MATERIAL) {
                        // No need to load textures here.
                        /*String path = startPath+parts[1];
                        MtlMaterialLib material = new MtlMaterialLib(path);
                        ResourceLocation resourceLocation = new ResourceLocation(path);
                        InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();
                        material.parse(read(inputStream));
                        materials.addAll(material.getMaterials());*/
                    } else if (parts[0] == USE_MATERIAL) {
                        currentObject.material = getMaterial(materials, parts[1])
                    } else if (parts[0] == NEW_OBJECT || parts[0] == NEW_GROUP) {
                        result.objIndices.addAll(indices)
                        normalModel.objIndices.addAll(indices)
                        result = IndexedModel()
                        normalModel = IndexedModel()
                        indices!!.clear()
                        objects[ObjPart(parts[1]).also { currentObject = it }] = arrayOf(result, normalModel)
                    }
                }
            }
            result.objIndices.addAll(indices)
            normalModel.objIndices.addAll(indices)
            val it: Iterator<ObjPart> = objects.keys.iterator()
            while (it.hasNext()) {
                val `object` = it.next()
                result = objects[`object`]!![0]
                normalModel = objects[`object`]!![1]
                indices = result.objIndices
                map[`object`] = result
                `object`.center = result.computeCenter()
                for (i in indices.indices) {
                    val current = indices[i]
                    val pos = positions[current.positionIndex]
                    var texCoord: Vector2f
                    texCoord = if (hasTexCoords) {
                        texCoords[current.texCoordsIndex]
                    } else {
                        Vector2f(0f, 0f)
                    }
                    var normal: Vector3f
                    normal = if (hasNormals) {
                        try {
                            normals[current.normalIndex]
                        } catch (e: Exception) {
                            Vector3f()
                        }
                    } else {
                        Vector3f()
                    }
                    var modelVertexIndex = resultIndexMap[current]!!
                    if (modelVertexIndex == -1) {
                        resultIndexMap[current] = result.positions.size
                        modelVertexIndex = result.positions.size
                        result.positions.add(pos)
                        result.texCoords.add(texCoord)
                        if (hasNormals) result.normals.add(normal)
                        result.tangents.add(Vector3f())
                    }
                    var normalModelIndex = normalIndexMap[current.positionIndex]!!
                    if (normalModelIndex == -1) {
                        normalModelIndex = normalModel.positions.size
                        normalIndexMap[current.positionIndex] = normalModelIndex
                        normalModel.positions.add(pos)
                        normalModel.texCoords.add(texCoord)
                        normalModel.normals.add(normal)
                        normalModel.tangents.add(Vector3f())
                    }
                    result.indices.add(modelVertexIndex)
                    normalModel.indices.add(normalModelIndex)
                    indexMap[modelVertexIndex] = normalModelIndex
                }
                if (!hasNormals) {
                    normalModel.computeNormals()
                    for (i in result.normals.indices) {
                        indexMap[i]?.let { it1 -> normalModel.normals.get(it1) }?.let { it2 -> result.normals.add(it2) }
                    }
                }
            }
            map
        } catch (e: Exception) {
            throw e
        }
    }

    private fun getMaterial(materials: ArrayList<Material>, id: String): Material? {
        for (mat in materials) {
            if (mat.name == id) return mat
        }
        return null
    }

    @Throws(IOException::class)
    protected fun read(resource: InputStream): String {
        var i: Int
        val buffer = ByteArray(65565)
        val out = ByteArrayOutputStream()
        while (resource.read(buffer, 0, buffer.size).also { i = it } != -1) {
            out.write(buffer, 0, i)
        }
        out.flush()
        out.close()
        return String(out.toByteArray(), Charset.forName("UTF-8"))
    }

    fun parseOBJIndex(token: String, posOffset: Int, texCoordsOffset: Int, normalOffset: Int): OBJIndex {
        val index = OBJIndex()
        val values: Array<String?> = token.split("/").toTypedArray()
        index.positionIndex = values[0]!!.toInt() - 1 - posOffset
        if (values.size > 1) {
            if (values[1] != null && values[1] != "") {
                index.texCoordsIndex = values[1]!!.toInt() - 1 - texCoordsOffset
            }
            hasTexCoords = true
            if (values.size > 2) {
                index.normalIndex = values[2]!!.toInt() - 1 - normalOffset
                hasNormals = true
            }
        }
        return index
    }

    companion object {
        private const val COMMENT = "#"
        private const val FACE = "f"
        private const val POSITION = "v"
        private const val TEX_COORDS = "vt"
        private const val NORMAL = "vn"
        private const val NEW_OBJECT = "o"
        private const val NEW_GROUP = "g"
        private const val USE_MATERIAL = "usemtl"
        private const val NEW_MATERIAL = "mtllib"
        fun trim(split: Array<String?>): Array<String> {
            val strings = ArrayList<String>()
            for (s in split) if (s != null && s.trim { it <= ' ' } != "") strings.add(s)
            return strings.toTypedArray()
        }
    }
}