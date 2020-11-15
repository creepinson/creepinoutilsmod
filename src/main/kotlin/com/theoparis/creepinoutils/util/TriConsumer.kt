package com.theoparis.creepinoutils.util

fun interface TriConsumer<A, B, C> {
    fun apply(a: A, b: B, c: C)
}