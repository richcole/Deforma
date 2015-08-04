#version 150

uniform mat4 viewTr;
uniform mat4 modelTr;

in vec3 vert;
in vec2 texCoords;

out vec2 fragTexCoords;
out mat4 fragTr;

void main() {
	fragTr = viewTr * modelTr;
    gl_Position = fragTr * vec4(vert, 1);
    fragTexCoords = texCoords;
}