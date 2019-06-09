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
    vec4 sum = vec4(0.0);

    float blur = 5.0;

    float hstep = 1.0;
    float vstep = 0.0;

    sum += texture(tex, vec2(fs_in.tc.x - 4.0*blur*hstep, fs_in.tc.y - 4.0*blur*vstep)) * 0.0162162162;
    sum += texture(tex, vec2(fs_in.tc.x - 3.0*blur*hstep, fs_in.tc.y - 3.0*blur*vstep)) * 0.0540540541;
    sum += texture(tex, vec2(fs_in.tc.x - 2.0*blur*hstep, fs_in.tc.y - 2.0*blur*vstep)) * 0.1216216216;
    sum += texture(tex, vec2(fs_in.tc.x - 1.0*blur*hstep, fs_in.tc.y - 1.0*blur*vstep)) * 0.1945945946;

    sum += texture(tex, vec2(fs_in.tc.x, fs_in.tc.y)) * 0.2270270270;

    sum += texture(tex, vec2(fs_in.tc.x + 1.0*blur*hstep, fs_in.tc.y + 1.0*blur*vstep)) * 0.1945945946;
    sum += texture(tex, vec2(fs_in.tc.x + 2.0*blur*hstep, fs_in.tc.y + 2.0*blur*vstep)) * 0.1216216216;
    sum += texture(tex, vec2(fs_in.tc.x + 3.0*blur*hstep, fs_in.tc.y + 3.0*blur*vstep)) * 0.0540540541;
    sum += texture(tex, vec2(fs_in.tc.x + 4.0*blur*hstep, fs_in.tc.y + 4.0*blur*vstep)) * 0.0162162162;

//    color = texture(tex, fs_in.tc);
    color = vec4(sum.rgb, 1.0);
    color += vec4(color_mask, 0);
    if(color.a < 0.05)
        discard;
}