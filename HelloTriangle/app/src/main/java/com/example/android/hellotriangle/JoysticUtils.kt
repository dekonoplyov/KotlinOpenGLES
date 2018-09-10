package com.example.android.hellotriangle

import com.curiouscreature.kotlin.math.Float2

fun getXY(angle: Int): Float2 {
    return Float2(
            Math.cos(Math.toRadians(angle.toDouble())).toFloat(),
            Math.sin(Math.toRadians(angle.toDouble())).toFloat())
}
fun polarToXY(angle: Int, strength: Int): Float2 {
    return getXY(angle).times(strength / 100f)
}