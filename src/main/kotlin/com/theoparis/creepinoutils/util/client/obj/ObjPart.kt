package com.theoparis.creepinoutils.util.client.obj

import net.minecraft.util.math.vector.Vector3f

class ObjPart(val name: String) {
    var mesh: Mesh? = null
    var material: Material? = null
    var center: Vector3f? = null
}