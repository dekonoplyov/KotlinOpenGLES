package com.example.android.hellotriangle

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30.*
import android.opengl.GLUtils.texImage2D
import java.nio.Buffer
import android.opengl.GLES20.glUniform1i



class Shape(val context: Context?) {
    val vertices = floatArrayOf(
            -0.5f, -0.5f, 0f, 1f, 0f, 0f, 0f, 0f,
             0.5f, -0.5f, 0f, 0f, 1f, 0f, 1f, 0f,
             0.5f,  0.5f, 0f, 0f, 0f, 1f, 1f, 1f,
            -0.5f,  0.5f, 0f, 1f, 0f, 1f, 0f, 1f
    )

    val vertexBuffer = floatBuffer(vertices.size)
            .put(vertices).position(0)

    val indexes = shortArrayOf(
            0, 2, 3,
            0, 1, 2
    )

    val indexBuffer = shortBuffer(indexes.size)
            .put(indexes).position(0)

    var verticesVAOId = -1

    var textureId = -1

    val vertexShader =
            "attribute vec4 a_Position;" +
            "attribute vec3 a_Color;" +
            "attribute vec2 a_TextureCoordinates;" +
            "varying lowp vec4 v_Color;" +
            "varying mediump vec2 v_TextureCoordinates;" +
            "void main()" +
            "{" +
            "    v_TextureCoordinates = a_TextureCoordinates;" +
            "    v_Color = vec4(a_Color, 1);" +
            "    gl_Position = a_Position;" +
            "}"
    val fragmentShader =
            "varying lowp vec4 v_Color;" +
            "varying mediump vec2 v_TextureCoordinates;" +
            "uniform sampler2D u_TextureUnit;" +
            "void main()" +
            "{" +
                "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates) * v_Color;" +
            "}"

    val shader = ShaderProgram(vertexShader, fragmentShader)

    init {
        verticesVAOId = beginVAO()

        setVertexBuffer(vertices.size * 4, vertexBuffer)
        setIndexBuffer()
        loadTexture()

        endVAO()
    }

    fun beginVAO(): Int {
        val vaoBufferIds = IntArray(1)
        glGenVertexArrays(1, vaoBufferIds, 0)
        glBindVertexArray(vaoBufferIds[0])
        return vaoBufferIds[0]
    }

    fun setVertexBuffer(size: Int, buffer: Buffer) {
        val vboBufferIds = IntArray(1)
        glGenBuffers(1, vboBufferIds, 0)

        glBindBuffer(GL_ARRAY_BUFFER, vboBufferIds[0])
        glBufferData(GL_ARRAY_BUFFER, size, buffer, GL_STATIC_DRAW)

        val stride = (3 + 3 + 2) * 4

        val positionHandle = shader.getAttributeLocation("a_Position")
        glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, stride, 0)
        glEnableVertexAttribArray(positionHandle)

        val colorOffset = 3 * 4
        val colorHandle = shader.getAttributeLocation("a_Color")
        glVertexAttribPointer(colorHandle, 3, GL_FLOAT, false, stride, colorOffset)
        glEnableVertexAttribArray(colorHandle)

        val textureOffset = (3 + 3) * 4
        val textureHandle = shader.getAttributeLocation("a_TextureCoordinates")
        glVertexAttribPointer(textureHandle, 2, GL_FLOAT, false, stride, textureOffset)
        glEnableVertexAttribArray(textureHandle)
    }

    fun setIndexBuffer() {
        val iboBufferIds = IntArray(1)
        glGenBuffers(1, iboBufferIds, 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboBufferIds[0])
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexes.size * 2, indexBuffer, GL_STATIC_DRAW)
    }

    fun loadTexture() {
        val textureIds = IntArray(1)
        glGenTextures(1, textureIds, 0)
        textureId = textureIds[0]

        val options = BitmapFactory.Options()
        options.inScaled = false

        val bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.tyrin, options)
        glBindTexture(GL_TEXTURE_2D, textureId)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        glGenerateMipmap(GL_TEXTURE_2D)

        bitmap.recycle()
        glBindTexture(GL_TEXTURE_2D, 0)

        // before setting uniform always call glUseProgram
        shader.startUse()
        glUniform1i(shader.getAttributeLocation("u_TextureUnit"), /*GL_TEXTURE0*/ 0)
        shader.stopUse()
    }

    fun endVAO() {
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun draw() {

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)

        shader.startUse()

        glBindVertexArray(verticesVAOId)
        glDrawElements(GL_TRIANGLES, indexes.size, GL_UNSIGNED_SHORT, 0)

        glBindVertexArray(0)

        shader.stopUse()
    }
}