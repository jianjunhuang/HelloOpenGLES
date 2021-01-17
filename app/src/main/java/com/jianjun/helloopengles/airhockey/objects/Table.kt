package com.jianjun.helloopengles.airhockey.objects

import android.opengl.GLES20
import com.jianjun.helloopengles.utils.VertexArray

class Table {

    private val vertexArray = VertexArray(VERTEX_DTA)

    fun bindData() {
        
    }

    companion object {
        //分量计算
        private const val POSITION_COMPONENT_COUNT = 2

        //纹理坐标分量计数
        private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2

        //跨距
        private const val STRIDE =
            (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * VertexArray.BYTES_PER_FLOAT

        //顶点数据
        private val VERTEX_DTA = floatArrayOf(
            //X , Y , S , T ,T 分量是 y 的相反反向
            //Triangle Fan
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
        )
    }
}