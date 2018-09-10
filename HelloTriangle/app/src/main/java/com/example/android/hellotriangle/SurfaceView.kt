package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class TriangleSurfaceView(context: Context?) : GLSurfaceView(context) {
    init {
        setEGLContextClientVersion(3)
        setRenderer(TriangleRenderer(context))
    }

    constructor(context: Context?, attrs: AttributeSet) : this(context)
}