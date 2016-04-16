#version 150

uniform sampler2D tex; 

in vec2 fragTexCoords;
in mat4 fragTr;
in float fragLight;

out vec4 finalColor; 

void main() {
    vec4 color = texture(tex, fragTexCoords);
    finalColor = texture(tex, fragTexCoords) * fragLight;
}
