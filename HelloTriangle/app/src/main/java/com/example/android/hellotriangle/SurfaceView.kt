package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLSurfaceView

class TriangleSurfaceView(context: Context?) : GLSurfaceView(context) {
    init {
        setEGLContextClientVersion(3)
        setRenderer(TriangleRenderer())
    }
}