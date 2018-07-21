package com.example.android.hellotriangle

import java.nio.*

private fun buffer(size: Int, elementByteSize: Int): ByteBuffer {
    return ByteBuffer
            .allocateDirect(size * elementByteSize)
            .order(ByteOrder.nativeOrder())
}

fun byteBuffer(size: Int): ByteBuffer {
    return buffer(size, 1)
}

fun shortBuffer(size: Int): ShortBuffer {
    return buffer(size, 2).asShortBuffer()
}

fun intBuffer(size: Int): IntBuffer {
    return buffer(size, 4).asIntBuffer()
}

fun floatBuffer(size: Int): FloatBuffer {
    return buffer(size, 4).asFloatBuffer()
}
