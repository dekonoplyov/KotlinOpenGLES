package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30.*
import com.example.android.hellotriangle.util.floatBuffer
import java.nio.Buffer

class Shape(context: Context?) {
    private val vertices = floatArrayOf(
            -0.3f, -0.3f, -0.3f,  0.0f, 0.0f,
            0.3f, -0.3f, -0.3f,  1.0f, 0.0f,
            0.3f,  0.3f, -0.3f,  1.0f, 1.0f,
            0.3f,  0.3f, -0.3f,  1.0f, 1.0f,
            -0.3f,  0.3f, -0.3f,  0.0f, 1.0f,
            -0.3f, -0.3f, -0.3f,  0.0f, 0.0f,

            -0.3f, -0.3f,  0.3f,  0.0f, 0.0f,
            0.3f, -0.3f,  0.3f,  1.0f, 0.0f,
            0.3f,  0.3f,  0.3f,  1.0f, 1.0f,
            0.3f,  0.3f,  0.3f,  1.0f, 1.0f,
            -0.3f,  0.3f,  0.3f,  0.0f, 1.0f,
            -0.3f, -0.3f,  0.3f,  0.0f, 0.0f,

            -0.3f,  0.3f,  0.3f,  1.0f, 0.0f,
            -0.3f,  0.3f, -0.3f,  1.0f, 1.0f,
            -0.3f, -0.3f, -0.3f,  0.0f, 1.0f,
            -0.3f, -0.3f, -0.3f,  0.0f, 1.0f,
            -0.3f, -0.3f,  0.3f,  0.0f, 0.0f,
            -0.3f,  0.3f,  0.3f,  1.0f, 0.0f,

            0.3f,  0.3f,  0.3f,  1.0f, 0.0f,
            0.3f,  0.3f, -0.3f,  1.0f, 1.0f,
            0.3f, -0.3f, -0.3f,  0.0f, 1.0f,
            0.3f, -0.3f, -0.3f,  0.0f, 1.0f,
            0.3f, -0.3f,  0.3f,  0.0f, 0.0f,
            0.3f,  0.3f,  0.3f,  1.0f, 0.0f,

            -0.3f, -0.3f, -0.3f,  0.0f, 1.0f,
            0.3f, -0.3f, -0.3f,  1.0f, 1.0f,
            0.3f, -0.3f,  0.3f,  1.0f, 0.0f,
            0.3f, -0.3f,  0.3f,  1.0f, 0.0f,
            -0.3f, -0.3f,  0.3f,  0.0f, 0.0f,
            -0.3f, -0.3f, -0.3f,  0.0f, 1.0f,

            -0.3f,  0.3f, -0.3f,  0.0f, 1.0f,
            0.3f,  0.3f, -0.3f,  1.0f, 1.0f,
            0.3f,  0.3f,  0.3f,  1.0f, 0.0f,
            0.3f,  0.3f,  0.3f,  1.0f, 0.0f,
            -0.3f,  0.3f,  0.3f,  0.0f, 0.0f,
            -0.3f,  0.3f, -0.3f,  0.0f, 1.0f
    )

    private val vertexBuffer = floatBuffer(vertices.size)
            .put(vertices).position(0)

    private var verticesVAOId = -1

    private val vertexShader =
            "uniform mat4 u_Matrix;" +
            "attribute vec4 a_Position;" +
            "attribute vec2 a_TextureCoordinates;" +
            "varying mediump vec2 v_TextureCoordinates;" +
            "void main()" +
            "{" +
            "    v_TextureCoordinates = a_TextureCoordinates;" +
            "    gl_Position = u_Matrix * a_Position;" +
            "}"
    private val fragmentShader =
            "varying mediump vec2 v_TextureCoordinates;" +
            "uniform sampler2D u_TextureUnit0;" +
            "void main()" +
            "{" +
            "    gl_FragColor = texture2D(u_TextureUnit0, v_TextureCoordinates);" +
            "}"

    private val shader = ShaderProgram(vertexShader, fragmentShader)
    private val textures = TextureManager(context)

    init {
        verticesVAOId = beginVAO()

        setVertexBuffer(vertices.size * 4, vertexBuffer)

        endVAO()

        textures.load(R.drawable.tyrin, true)

        setTexture()
    }

    private fun beginVAO(): Int {
        val vaoBufferIds = IntArray(1)
        glGenVertexArrays(1, vaoBufferIds, 0)
        glBindVertexArray(vaoBufferIds[0])
        return vaoBufferIds[0]
    }

    private fun setVertexBuffer(size: Int, buffer: Buffer) {
        val vboBufferIds = IntArray(1)
        glGenBuffers(1, vboBufferIds, 0)

        glBindBuffer(GL_ARRAY_BUFFER, vboBufferIds[0])
        glBufferData(GL_ARRAY_BUFFER, size, buffer, GL_STATIC_DRAW)

        val stride = (3 + 2) * 4

        val positionHandle = shader.getAttributeLocation("a_Position")
        glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, stride, 0)
        glEnableVertexAttribArray(positionHandle)

        val textureOffset = (3) * 4
        val textureHandle = shader.getAttributeLocation("a_TextureCoordinates")
        glVertexAttribPointer(textureHandle, 2, GL_FLOAT, false, stride, textureOffset)
        glEnableVertexAttribArray(textureHandle)
    }

    private fun endVAO() {
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun setMatrix(viewPort: FloatArray) {
        shader.startUse()

        glUniformMatrix4fv(shader.getUniformLocation("u_Matrix"), 1, false, viewPort, 0)

        shader.stopUse()
    }

    private fun setTexture() {
        shader.startUse()

        // glUniform1i(samplerHandle, 0) means take textures from GL_TEXTURE0
        glUniform1i(shader.getUniformLocation("u_TextureUnit0"), GL_TEXTURE0 - GL_TEXTURE0)

        shader.stopUse()
    }

    private fun bindTexture(textureUnit: Int, textureId: Int) {
        glActiveTexture(textureUnit)
        glBindTexture(GL_TEXTURE_2D, textureId)
    }

    fun draw() {
        shader.startUse()

        bindTexture(GL_TEXTURE0, textures.getTextureId(R.drawable.tyrin))

        glBindVertexArray(verticesVAOId)
        GLES20.glDrawArrays(GL_TRIANGLES, 0, 36)

        glBindVertexArray(0)

        shader.stopUse()
    }
}