#version 410

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;
uniform mat4 md_matrix;
uniform vec4 color_mask;

out DATA
{
    vec2 tc;
    vec3 position;
} vs_out;

void main()
{
    gl_Position = pr_matrix * vw_matrix * md_matrix * position;
    vs_out.tc = tc;
    vs_out.position = vec3(vw_matrix * md_matrix * position);
}