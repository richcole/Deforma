#version 130

uniform mat4 viewTr;
uniform mat4 modelTr;

in vec3 vert;
in vec3 normal;
in vec2 texCoords;

out vec2 fragTexCoords;
out mat4 fragTr;
out float fragLight;

void main() {
    fragTr = viewTr * modelTr;
    gl_Position = fragTr * vec4(vert, 1);
    fragTexCoords = texCoords;
    fragLight = 0.2 + (abs(dot(vec3(1,1,1),normal)) * 0.8);
    
    
}