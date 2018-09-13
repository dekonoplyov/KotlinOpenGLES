package com.example.android.hellotriangle

import android.opengl.Matrix
import com.curiouscreature.kotlin.math.Float2
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.cross
import com.curiouscreature.kotlin.math.normalize

class Camera {
    private val camera = FloatArray(16)
    private var cameraPos = Float3(0.0f, 0.0f,  3.0f)
    private var cameraDirection = Float3(0f, 0f, -1f)
    private var cameraUp = Float3(0.0f, 1.0f,  0.0f)

    private var pitch = 0.0
    private var yaw = -Math.PI / 2.0

    private fun getUp(direction: Float3): Float3 {
        val worldUp = Float3(0.0f, 1.0f,  0.0f)
        val xAxis = cross(worldUp, direction)
        return normalize(cross(direction, xAxis))
    }

    fun move(v: Float2) {
        cameraPos += cameraDirection.times(v.y) + cross(cameraDirection, cameraUp).times(v.x)
    }

    fun look(v: Float2) {
        pitch +=  Math.toRadians(4.0 * v.y).toFloat()
        yaw += Math.toRadians(4.0 * v.x).toFloat()

        cameraDirection.x = (Math.cos(pitch) * Math.cos(yaw)).toFloat()
        cameraDirection.y = Math.sin(pitch).toFloat()
        cameraDirection.z = (Math.cos(pitch) * Math.sin(yaw)).toFloat()

        cameraUp = getUp(cameraDirection)
    }

    fun getLookAt(): FloatArray {
        Matrix.setLookAtM(camera, 0,
                cameraPos.x, cameraPos.y, cameraPos.z,
                cameraPos.x + cameraDirection.x,
                cameraPos.y + cameraDirection.y,
                cameraPos.z + cameraDirection.z,
                cameraUp.x, cameraUp.y, cameraUp.z)
        return camera
    }
}