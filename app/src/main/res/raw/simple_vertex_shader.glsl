// 顶点着色器
/*
 vec4      -> contain x,y,z,w (0,0,0,1)
 attribute -> 顶点属性
*/
attribute vec4 a_Position;

void main() {
    //must init the gl_Position
    gl_Position = a_Position;
    gl_PointSize = 10.0;//gl_Position为中心的四边形，四条边的长度等于 gl_PointSize
}