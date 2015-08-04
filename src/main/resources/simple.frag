#version 150

uniform sampler2D tex; 

in vec2 fragTexCoords;
in mat4 fragTr;

out vec4 finalColor; 

void main() {
    finalColor = texture(tex, fragTexCoords);
}
