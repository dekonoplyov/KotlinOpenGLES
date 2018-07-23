package com.example.android.hellotriangle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.opengl.GLES30.*
import android.opengl.GLUtils.texImage2D
import android.util.Log

private fun Bitmap.flipVertical(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(1.0f, -1.0f)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

class TextureManager(val context: Context?) {
    val resourceToTexture = HashMap<Int, Int>()

    fun load(resourceId: Int, flipVertical: Boolean = false): Int {
        val textureIds = IntArray(1)
        glGenTextures(1, textureIds, 0)
        val textureId = textureIds[0]

        val options = BitmapFactory.Options()
        options.inScaled = false

        var bitmap = BitmapFactory.decodeResource(context?.resources, resourceId, options)

        if (flipVertical) {
            val original = bitmap
            bitmap = original.flipVertical()
            original.recycle()
        }

        glBindTexture(GL_TEXTURE_2D, textureId)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        glGenerateMipmap(GL_TEXTURE_2D)

        bitmap.recycle()
        glBindTexture(GL_TEXTURE_2D, 0)

        resourceToTexture.put(resourceId, textureId)

        return textureId
    }

    fun getTextureId(resourceId: Int): Int {
        if (resourceToTexture.containsKey(resourceId)) {
            return resourceToTexture[resourceId]!!
        }

        // TODO add logger
        Log.w("1", "")
        return -1
    }
}