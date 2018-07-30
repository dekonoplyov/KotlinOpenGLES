package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.opengl.Matrix.orthoM
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleRenderer(val context: Context?) : GLSurfaceView.Renderer {
    val duration = 3600.0
    var shape: Shape? = null
    val translate = FloatArray(16)
    val rotate = FloatArray(16)
    val transform = FloatArray(16)
    val temp = FloatArray(16)
    val viewPort = FloatArray(16)

    override fun onDrawFrame(p0: GL10?) {

        glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)

        val angle = 0.1f * (System.currentTimeMillis() % duration).toFloat()

        Matrix.setIdentityM(rotate, 0)
        Matrix.setRotateM(rotate, 0, angle, 0f, 0f, -1f)
        Matrix.setIdentityM(translate, 0)
        Matrix.translateM(translate, 0, 0.5f, 0.0f, 0f)
        Matrix.multiplyMM(transform, 0, translate, 0,  rotate, 0)

        Matrix.multiplyMM(temp, 0, viewPort, 0, transform, 0)

        shape?.setMatrix(temp)

        shape?.draw()

        val scale = Math.sin(0.01 * System.currentTimeMillis() % duration).toFloat() + 1f
        Matrix.setIdentityM(rotate, 0)
        Matrix.scaleM(rotate, 0, scale, scale, 0f)
        Matrix.setIdentityM(translate, 0)
        Matrix.translateM(translate, 0, 0.0f, 0.5f, 0f)
        Matrix.multiplyMM(transform, 0, translate, 0,  rotate, 0)

        Matrix.multiplyMM(temp, 0, viewPort, 0, transform, 0)

        shape?.setMatrix(temp)

        shape?.draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        if (width > height) {
            val aspectRatio = width.toFloat() / height.toFloat()
            orthoM(viewPort, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            val aspectRatio = height.toFloat() / width.toFloat()
            orthoM(viewPort, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)


        shape = Shape(context)
    }

}