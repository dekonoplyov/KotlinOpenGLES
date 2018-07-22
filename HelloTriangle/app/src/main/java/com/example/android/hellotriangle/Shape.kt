package com.example.android.hellotriangle

import android.opengl.GLES30.*
import java.nio.Buffer

class Shape {
    val vertices = floatArrayOf(
            -0.5f, -0.5f, 0f, 1f, 0f, 0f,
             0.5f, -0.5f, 0f, 0f, 1f, 0f,
             0.5f,  0.5f, 0f, 0f, 0f, 1f
    )

    val verticesBuffer = floatBuffer(vertices.size)
            .put(vertices).position(0)

    var verticesVAOId = -1

    val vertexShader =
            "attribute vec4 a_Position;" +
            "attribute vec3 a_Color;" +
            "varying lowp vec4 v_Color;" +
            "void main()" +
            "{" +
            "    v_Color = vec4(a_Color, 1);" +
            "    gl_Position = a_Position;" +
            "}"
    val fragmentShader =
            "varying lowp vec4 v_Color;" +
            "void main()" +
            "{" +
            "    gl_FragColor = v_Color;" +
            "}"

    val shader = ShaderProgram(vertexShader, fragmentShader)

    init {
        verticesVAOId = beginVAO()

        setVertexBuffer(vertices.size * 4, verticesBuffer)

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

        val vertexStride = (3 + 3) * 4

        val positionHandle = glGetAttribLocation(shader.programId, "a_Position")
        glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, vertexStride, 0)
        glEnableVertexAttribArray(positionHandle)

        val colorOffset = 3 * 4
        val colorHandle = glGetAttribLocation(shader.programId, "a_Color")
        glVertexAttribPointer(colorHandle, 3, GL_FLOAT, false, vertexStride, colorOffset)
        glEnableVertexAttribArray(colorHandle)
    }

    fun endVAO() {
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun draw() {
        shader.startUse()

        glBindVertexArray(verticesVAOId)
        glDrawArrays(GL_TRIANGLES, 0, 3)

        glBindVertexArray(0)

        shader.stopUse()
    }
}