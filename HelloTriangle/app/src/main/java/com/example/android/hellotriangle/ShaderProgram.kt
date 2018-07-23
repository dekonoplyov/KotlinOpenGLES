package com.example.android.hellotriangle

import android.opengl.GLES30.*
import android.util.Log

const val shaderLogTag = "ShaderLog"

class ShaderProgram(vertexShaderCode: String, fragmentShaderCode: String) {
    val vertexShaderId = glCreateShader(GL_VERTEX_SHADER)
    val fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER)
    val programId = glCreateProgram()
    
    init {
        if (vertexShaderId == 0) {
            Log.w(shaderLogTag, "Failed to create vertex shader")
        }
        compileShader(vertexShaderId, vertexShaderCode)

        if (fragmentShaderId == 0) {
            Log.w(shaderLogTag, "Failed to create fragment shader")
        }
        compileShader(fragmentShaderId, fragmentShaderCode)
        
        if (programId == 0) {
            Log.w(shaderLogTag, "Failed to create shader program")
        }
        linkProgram()
    }

    fun compileShader(shaderObjectId: Int, shaderCode: String) {
        glShaderSource(shaderObjectId, shaderCode)
        glCompileShader(shaderObjectId)

        val compileStatus = IntArray(1)
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)

        Log.v(shaderLogTag, "Results of compiling shader code:"
                + "\n" + shaderCode + "\n:"
                + glGetShaderInfoLog(shaderObjectId))

        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId)
            Log.w(shaderLogTag, "Compilation of shader failed")
        }

    }

    fun linkProgram() {
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)
        glLinkProgram(programId)

        val linkStatus = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0)

        Log.v(shaderLogTag, "Results of linking program:\n" + glGetProgramInfoLog(programId))

        if (linkStatus[0] == 0) {
            glDeleteProgram(programId)
            Log.w(shaderLogTag, "Linking of program failed.")
        }
    }

    fun startUse() {
        glUseProgram(programId)
    }

    fun stopUse() {
        glUseProgram(0)
    }

    fun getAttributeLocation(name: String): Int {
        val location = glGetAttribLocation(programId, name)
        if (location == -1) {
            Log.w(shaderLogTag, "Can't find attribute '$name' in program '$programId'")
        }
        return location
    }
}
