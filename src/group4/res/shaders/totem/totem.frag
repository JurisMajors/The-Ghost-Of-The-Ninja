#version 410

layout (location = 0) out vec4 color;

in DATA
{
    vec2 tc;
    vec3 position;
} fs_in;

uniform sampler2D tex;
uniform vec3 color_mask;

void main()
{
    color = texture(tex, fs_in.tc);
    color += vec4(color_mask, 0);
    if(color.a < 0.05)
        discard;
}