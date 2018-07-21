package com.example.android.hellotriangle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TriangleActivity : AppCompatActivity() {
    var glSurfaceView: TriangleSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = TriangleSurfaceView(this)
        setContentView(glSurfaceView)
    }
}
