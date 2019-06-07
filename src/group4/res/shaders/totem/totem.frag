#version 410

layout (location = 0) out vec4 color;

in DATA
{
    vec2 tc;
    vec3 position;
} fs_in;

uniform sampler2D tex;
uniform vec4 color_mask;

void main()
{
    color = texture(tex, fs_in.tc);
    color += color_mask;
    if(color.a < 0.05)
        discard;
}