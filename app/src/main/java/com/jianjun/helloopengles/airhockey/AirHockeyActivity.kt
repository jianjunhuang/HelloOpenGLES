package com.jianjun.helloopengles.airhockey

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jianjun.helloopengles.R
import com.jianjun.helloopengles.utils.ResourceUtils
import com.jianjun.helloopengles.utils.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(AirHockeyRender(this))
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        setContentView(glSurfaceView)
    }

    class AirHockeyRender(private val context: Context) : GLSurfaceView.Renderer {

        private var uColorLocation: Int = 0
        private var aPositionLocation: Int = 0

        companion object {
            const val TAG = "AirHockeyRender"
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0f, 0f, 0f, 1f)
            val vertexShader = ShaderUtils.compileVertexShader(
                ResourceUtils.raw2String(
                    context,
                    R.raw.simple_vertex_shader
                )
            )
            val fragmentShader = ShaderUtils.compileFragmentShader(
                ResourceUtils.raw2String(
                    context,
                    R.raw.simple_fragment_shader
                )
            )

            if (vertexShader == 0 || fragmentShader == 0) {
                Log.e(TAG, "onSurfaceCreated: ")
                return
            }
            val program = ShaderUtils.linkProgram(vertexShader, fragmentShader)
            if (program != 0) {
                GLES20.glUseProgram(program)
            }
            uColorLocation = GLES20.glGetUniformLocation(program, "u_Color")
            aPositionLocation = GLES20.glGetAttribLocation(program, "a_Position")
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClearColor(0f, 0f, 0f, 1f)
        }

    }

}
