#version 410

layout (location = 0) out vec4 color;

in DATA
{
    vec2 tc;
    vec3 position;
} fs_in;

uniform vec2 bird;
uniform sampler2D tex;

void main()
{
    color = vec4(1.0, 1.0, 1.0, 0.5);
//    color *= 2.0 / (length(bird - fs_in.position.xy) + 2.5) + 0.5;
}