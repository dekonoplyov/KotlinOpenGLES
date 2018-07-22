package com.example.android.hellotriangle

import android.opengl.GLES30.*
import java.nio.Buffer

class Shape {
    val vertices = floatArrayOf(
            -0.5f, -0.5f, 0f,
             0.5f, -0.5f, 0f,
             0.5f,  0.5f, 0f
    )

    val vert1 = floatArrayOf(
            0.5f, 0.7f, 1f,
            -0.5f, 0.7f, 0f,
            -0.5f,  0f, 0f
    )

    val verticesBuffer = floatBuffer(vertices.size)
            .put(vertices).position(0)

    val verticesBuffer1 = floatBuffer(vert1.size)
            .put(vert1).position(0)

    var vaoId = -1
    var vaoId1 = -1

    val vertexShader =
            "attribute vec4 a_Position;" +
            "varying vec4 v_Color;" +
            "void main()" +
            "{" +
            "    v_Color = a_Position;" +
            "    gl_Position = a_Position;" +
            "}"
    val fragmentShader =
            "precision mediump float;" +
            "varying vec4 v_Color;" +
            "void main()" +
            "{" +
            "    gl_FragColor = v_Color;" +
            "}"

    val shader = ShaderProgram(vertexShader, fragmentShader)

    init {
        vaoId = beginVAO()

        setVertexBuffer(vertices.size * 4, verticesBuffer)

        endVAO()

        vaoId1 = beginVAO()

        setVertexBuffer(vert1.size * 4, verticesBuffer1)

        endVAO()
    }

    fun beginVAO(): Int {
        val vaoBuffer = intBuffer(1)
        glGenVertexArrays(1, vaoBuffer)
        glBindVertexArray(vaoBuffer.get(0))
        return vaoBuffer.get(0)
    }

    fun setVertexBuffer(size: Int, buffer: Buffer) {
        val vboBuffer = intBuffer(1)
        glGenBuffers(1, vboBuffer)

        glBindBuffer(GL_ARRAY_BUFFER, vboBuffer.get(0))
        glBufferData(GL_ARRAY_BUFFER, size, buffer, GL_STATIC_DRAW)

        val positionHandle = glGetAttribLocation(shader.programId, "a_Position")
        val vertexStride = 3 * 4
        glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, vertexStride, 0)
        glEnableVertexAttribArray(positionHandle)
    }

    fun endVAO() {
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun draw() {
        shader.startUse()

        glBindVertexArray(vaoId)
        glDrawArrays(GL_TRIANGLES, 0, 3)

        glBindVertexArray(vaoId1)
        glDrawArrays(GL_TRIANGLES, 0, 3)

        glBindVertexArray(0)

        shader.stopUse()
    }
}