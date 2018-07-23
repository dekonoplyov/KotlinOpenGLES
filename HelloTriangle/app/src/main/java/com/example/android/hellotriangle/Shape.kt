package com.example.android.hellotriangle

import android.content.Context
import android.opengl.GLES30.*
import java.nio.Buffer


class Shape(val context: Context?) {
    val vertices = floatArrayOf(
            -0.9f, -0.9f, 0f, 1f, 0f, 0f, 0f, 0f,
             0.9f, -0.9f, 0f, 0f, 1f, 0f, 1f, 0f,
             0.9f,  0.9f, 0f, 0f, 0f, 1f, 1f, 1f,
            -0.9f,  0.9f, 0f, 1f, 0f, 1f, 0f, 1f
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

    val vertexShader =
            "attribute vec4 a_Position;" +
//            "attribute vec3 a_Color;" +
            "attribute vec2 a_TextureCoordinates;" +
//            "varying lowp vec4 v_Color;" +
            "varying mediump vec2 v_TextureCoordinates;" +
            "void main()" +
            "{" +
            "    v_TextureCoordinates = a_TextureCoordinates;" +
//            "    v_Color = vec4(a_Color, 1);" +
            "    gl_Position = a_Position;" +
            "}"
    val fragmentShader =
//            "varying lowp vec4 v_Color;" +
            "varying mediump vec2 v_TextureCoordinates;" +
            "uniform sampler2D u_TextureUnit0;" +
            "uniform sampler2D u_TextureUnit1;" +
            "void main()" +
            "{" +
                "    gl_FragColor = mix(texture2D(u_TextureUnit0, v_TextureCoordinates), texture2D(u_TextureUnit1, v_TextureCoordinates), 0.4);" +
            "}"

    val shader = ShaderProgram(vertexShader, fragmentShader)
    val textures = TextureManager(context)

    init {
        verticesVAOId = beginVAO()

        setVertexBuffer(vertices.size * 4, vertexBuffer)
        setIndexBuffer()

        endVAO()

        textures.load(R.drawable.tyrin, true)
        textures.load(R.drawable.dark_forest, true)
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

//        val colorOffset = 3 * 4
//        val colorHandle = shader.getAttributeLocation("a_Color")
//        glVertexAttribPointer(colorHandle, 3, GL_FLOAT, false, stride, colorOffset)
//        glEnableVertexAttribArray(colorHandle)

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


    fun endVAO() {
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun draw() {
        shader.startUse()

        shader.bindTexture("u_TextureUnit0", GL_TEXTURE0,
                textures.getTextureId(R.drawable.tyrin))
        shader.bindTexture("u_TextureUnit1", GL_TEXTURE1,
                textures.getTextureId(R.drawable.dark_forest))

        glBindVertexArray(verticesVAOId)
        glDrawElements(GL_TRIANGLES, indexes.size, GL_UNSIGNED_SHORT, 0)

        glBindVertexArray(0)

        shader.stopUse()
    }
}