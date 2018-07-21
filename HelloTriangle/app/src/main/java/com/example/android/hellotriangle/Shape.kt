package com.example.android.hellotriangle

import android.opengl.GLES20
import android.opengl.GLES30.*
import java.nio.Buffer
import java.nio.FloatBuffer

class Shape {
    val vertices = floatArrayOf(
            -0.5f, -0.5f, 0f,
             0.5f, -0.5f, 0f,
             0.5f,  0.5f, 0f
    )

    val vert1 = floatArrayOf(
            0.5f, 0.7f, 0f,
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
            "void main()" +
            "{" +
            "    gl_Position = a_Position;" +
            "}"
    val fragmentShader =
            "precision mediump float;" +
            "uniform vec4 u_Color;" +
            "void main()" +
            "{" +
            "    gl_FragColor = u_Color;" +
            "}"

    var programId = -1

    init {
        compileProgram()

        vaoId = beginVAO()

        setVertexBuffer(vertices.size * 4, verticesBuffer)

        endVAO()

        vaoId1 = beginVAO()

        setVertexBuffer(vert1.size * 4, verticesBuffer1)

        endVAO()
    }



    fun compileProgram() {
        val vertexShaderId = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vertexShaderId, vertexShader)
        glCompileShader(vertexShaderId)

        val fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fragmentShaderId, fragmentShader)
        glCompileShader(fragmentShaderId)

        programId = glCreateProgram()
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)
        glLinkProgram(programId)
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

        val positionHandle = glGetAttribLocation(programId, "a_Position")
        val vertexStride = 3 * 4
        glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, vertexStride, 0)
        glEnableVertexAttribArray(positionHandle)
    }

    fun endVAO() {
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun draw() {
        glUseProgram(programId)

        val colorHandle = glGetUniformLocation(programId, "u_Color")

        glUniform4f(colorHandle, 1f, 0f, 0f, 1f)
        glBindVertexArray(vaoId)
        glDrawArrays(GL_TRIANGLES, 0, 3)

        glUniform4f(colorHandle, 1f, 0f, 1f, 1f)
        glBindVertexArray(vaoId1)
        glDrawArrays(GL_TRIANGLES, 0, 3)

        glBindVertexArray(0)

    }
}