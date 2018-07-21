package com.example.android.hellotriangle

import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleRenderer : GLSurfaceView.Renderer {
//    val duration = 1000.0
    var shape: Shape? = null

    override fun onDrawFrame(p0: GL10?) {
//        val intensity = Math.sin(System.currentTimeMillis() / duration).toFloat()

        glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)

        shape?.draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        shape = Shape()
    }

}