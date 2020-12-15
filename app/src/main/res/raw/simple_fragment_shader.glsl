// 片段着色器
// 图形光栅化后，形成的每一个片段都会片段着色器确定最终的颜色

/*
 ↓ 所有浮点数据类型的默认精度
    - lowp    低
    - mediump 中
    - highp   高
*/
precision mediump float;

uniform vec4 u_Color;

void main() {
    gl_FragColor = u_Color;
}
