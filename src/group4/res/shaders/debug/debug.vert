uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 md_matrix;

void main()
{
    gl_Position = pr_matrix * vw_matrix * md_matrix * position;
    gl_FrontColor = gl_Color;
}