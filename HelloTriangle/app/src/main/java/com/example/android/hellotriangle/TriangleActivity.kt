package com.example.android.hellotriangle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.github.controlwear.virtual.joystick.android.JoystickView


class TriangleActivity : AppCompatActivity() {
    var glSurfaceView: TriangleSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triangle)

        val leftJoystick = findViewById<JoystickView>(R.id.left_joystick)
        leftJoystick.setOnMoveListener { angle, strength ->
            Log.w("joystick", "left $angle, $strength")
        }

        val rightJoystick = findViewById<JoystickView>(R.id.right_joystick)
        rightJoystick.setOnMoveListener { angle, strength ->
            Log.w("joystick", "right $angle, $strength")
        }

        glSurfaceView = findViewById(R.id.surface)
    }
}
