package com.theoparis.creepinoutils.util.client.obj

import net.minecraft.util.math.vector.Vector3f

class Material(val name: String) {
    var diffuseColor: Vector3f? = null
    var ambientColor: Vector3f? = null
    var ambientTexture = 0
    var diffuseTexture = 0
    var transparency = 1f
}