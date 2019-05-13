uniform mat4 pr_matrix;
uniform mat4 vw_matrix;

void main()
{
    gl_Position = pr_matrix * vw_matrix * gl_Vertex;
    gl_FrontColor = gl_Color;
}