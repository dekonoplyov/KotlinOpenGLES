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
    val projection = FloatArray(16)

    override fun onDrawFrame(p0: GL10?) {
        glEnable(GL_DEPTH_TEST)
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val angle = 0.1f * (System.currentTimeMillis() % duration).toFloat()

        val tr = arrayOf(
                floatArrayOf(-1.1f, 0f, -6f),
                floatArrayOf(0f, 1.2f, -4f),
                floatArrayOf(2.4f, 1.7f, -9f),
                floatArrayOf(1.4f, -2.7f, -8f),
                floatArrayOf(0f, 1.7f, -7f),
                floatArrayOf(2f, -1.2f, -5f),
                floatArrayOf(0f, -1.5f, -5f),
                floatArrayOf(-1.3f, 1.5f, -6f),
                floatArrayOf(1.1f, 2f, -4f),
                floatArrayOf(1.3f, 0f, -4f),
                floatArrayOf(-1f, -1f, -4f),
                floatArrayOf(-0.1f, -0.1f, -6f)
        )


        for (i in 0..11) {
            Matrix.setIdentityM(rotate, 0)
            if (i % 2 == 0 || i % 3 == 0) {
                Matrix.setRotateM(rotate, 0, i * 17f + angle, 1f, 0.2f, 0.3f)
            } else {
                Matrix.setRotateM(rotate, 0, 20f * i, 1f, 0.2f, 0.3f)
            }
            Matrix.setIdentityM(translate, 0)
            Matrix.translateM(translate, 0, tr[i][0], tr[i][1], tr[i][2])
            Matrix.multiplyMM(transform, 0, translate, 0,  rotate, 0)
            Matrix.multiplyMM(temp, 0, projection, 0,  transform, 0)

            shape?.setMatrix(temp)

            shape?.draw()
        }


    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        Matrix.perspectiveM(projection,0, 60f,
                width.toFloat() / height.toFloat(),
                1f, 10f)

    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        shape = Shape(context)
    }

}