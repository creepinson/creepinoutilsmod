package com.theoparis.creepinoutils.util.client.obj

import java.util.*

class HashMapWithDefault<K, V> : HashMap<K?, V?>() {
    var default: V? = null
        private set

    fun setDefault(value: V) {
        default = value
    }

    override operator fun get(key: K?): V? {
        return if (this.containsKey(key)) super.get(key) else default
    }

    companion object {
        private const val serialVersionUID = 5995791692010816132L
    }
}