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
import java.nio.ByteBuffer
import java.nio.ByteOrder
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
        private var aColorLocation: Int = 0
        private var aPositionLocation: Int = 0

        val tableVertices = floatArrayOf(
            //三角扇形, 后三位 R,G,B
            0f, 0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.5f, 0f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0f, 0.7f, 0.7f, 0.7f,

            //line1
            -0.5f, 0f, 0f, 0f, 0f, 0f,
            0.5f, 0f, 0f, 0f, 0f, 0f,
            //point1
            0f, 0.25f, 0f, 0f, 0f, 0f,
            //point2
            0f, -0.25f, 0f, 0f, 0f, 0f,
            //border
            -0.55f, -0.55f, 0f, 0.52f, 0.73f, 0.94f,
            0.55f, 0.55f, 0f, 0.52f, 0.73f, 0.94f,
            -0.55f, 0.55f, 0f, 0.52f, 0.73f, 0.94f,
            -0.55f, -0.55f, 0f, 0.52f, 0.73f, 0.94f,
            0.55f, 0.55f, 0f, 0.52f, 0.73f, 0.94f,
            0.55f, -0.55f, 0f, 0.52f, 0.73f, 0.94f,
            //ice ball
            0f, 0f, 0f, 0.52f, 0.73f, 0.94f
        )
        val vertexData = ByteBuffer.allocateDirect(tableVertices.size * 4)//float 数字有四个字节
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(tableVertices)
                position(0)
            }

        companion object {
            const val TAG = "AirHockeyRender"

            //顶点的分量 x,y,z
            const val POSITION_COMPONENT_COUNT = 3
            const val COLOR_COMPONENT_COUNT = 3
            const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * 4
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
            aColorLocation = GLES20.glGetAttribLocation(program, "a_Color")
//            uColorLocation = GLES20.glGetUniformLocation(program, "u_Color")
            aPositionLocation = GLES20.glGetAttribLocation(program, "a_Position")

            //make sure read data from begin
            vertexData.position(0)
            GLES20.glVertexAttribPointer(
                aPositionLocation,
                POSITION_COMPONENT_COUNT,//分量size
                GLES20.GL_FLOAT,
                false,//数据整型时有用
                STRIDE,//多属性时有用
                vertexData
            )
            GLES20.glEnableVertexAttribArray(aPositionLocation)

            //link a_Color
            vertexData.position(POSITION_COMPONENT_COUNT)//跳过位置分量
            GLES20.glVertexAttribPointer(
                aColorLocation,
                COLOR_COMPONENT_COUNT,//分量size
                GLES20.GL_FLOAT,
                false,//数据整型时有用
                STRIDE,//读取下一个颜色需要跳过多少个字节
                vertexData
            )
            GLES20.glEnableVertexAttribArray(aColorLocation)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            //border
//            GLES20.glUniform4f(uColorLocation, 0.52f, 0.73f, 0.94f, 1f)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 10, 6)

            //与 shader 中的 vec4 匹配
            // ↓ 指定颜色
//            GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f)
            // ↓ 绘制 2 个三角形，从开头读取 6 个顶点
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)

            //draw line
//            GLES20.glUniform4f(uColorLocation, 0f, 0f, 0f, 1f)
            GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

            //draw point
//            GLES20.glUniform4f(uColorLocation, 0f, 0f, 0f, 1f)
            GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 2)

            //draw ice ball
//            GLES20.glUniform4f(uColorLocation, 0.52f, 0.73f, 0.94f, 1f)
            GLES20.glDrawArrays(GLES20.GL_POINTS, 16, 1)
        }
    }

}

