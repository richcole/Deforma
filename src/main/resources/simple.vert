#version 150

uniform mat4 tr;

in vec3 vert;
in vec2 texCoords;
out vec2 fragTexCoords;
out mat4 fragTr;

void main() {
    gl_Position = tr * vec4(vert, 1);
    fragTexCoords = texCoords;
    fragTr = tr;
}