package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.curiouscreature.kotlin.math.Float2
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleRenderer(val context: Context?) : GLSurfaceView.Renderer {
    private var shape: Shape? = null
    private val transform = FloatArray(16)
    private val temp = FloatArray(16)
    private val projection = FloatArray(16)
    private val camera = Camera()

    fun move(v: Float2) {
        camera.move(v)
    }

    fun look(v: Float2) {
        camera.look(v)
    }

    override fun onDrawFrame(p0: GL10?) {
        glEnable(GL_DEPTH_TEST)
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val tr = arrayOf(
                floatArrayOf(2f, 2f, -4f),
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

        for (i in 0 until tr.size) {
            // this order means -- rotate then translate
            Matrix.setIdentityM(transform, 0)
            Matrix.translateM(transform, 0, tr[i][0], tr[i][1], tr[i][2])
            Matrix.rotateM(transform, 0, 20f, 1f, 0.2f, 0.3f)

            Matrix.multiplyMM(temp, 0, camera.getLookAt(), 0,  transform, 0)
            // reuse transform matrix to eliminate allocation
            Matrix.multiplyMM(transform, 0, projection, 0,  temp, 0)

            shape?.setMatrix(transform)

            shape?.draw()
        }
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        Matrix.perspectiveM(projection,0, 60f,
                width.toFloat() / height.toFloat(),
                0.1f, 100f)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        shape = Shape(context)
    }
}