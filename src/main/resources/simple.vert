#version 150

uniform mat4 viewTr;
uniform mat4 modelTr;
uniform float alpha;

in vec3    p1;
in vec3    p2;
in vec3    normal;
in vec2    texCoords;
in float   bone;

out vec2 fragTexCoords;
out mat4 fragTr;
out float fragLight;

void main() {
    fragTr = viewTr * modelTr;
    vec3 p = (1 - alpha) * p1 + alpha * p2;
    gl_Position = fragTr * vec4(p, 1);
    fragTexCoords = texCoords;
    fragLight = 0.2 + (abs(dot(vec3(1,1,1),normal)) * 0.8);
}