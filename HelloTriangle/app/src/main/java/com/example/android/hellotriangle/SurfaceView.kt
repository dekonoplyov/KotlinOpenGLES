package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class TriangleSurfaceView(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {
    val renderer = TriangleRenderer(context)

    init {
        setEGLContextClientVersion(3)
        setRenderer(renderer)
    }
}