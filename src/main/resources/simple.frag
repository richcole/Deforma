#version 130 

uniform sampler2D tex; 

in vec2 fragTexCoords;
in mat4 fragTr;
in float fragLight;

out vec4 finalColor; 

void main() {
    finalColor = texture(tex, fragTexCoords) * fragLight;
}
