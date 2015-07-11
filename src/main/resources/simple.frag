#version 150
uniform sampler2D tex; 
in vec2 fragTexCoords;
in mat4 fragTr;
out vec4 finalColor; 

void main() {
    if (true) {
        finalColor = texture(tex, fragTexCoords);
    }
    else {
        finalColor = vec4(1.0, 0, 0, 1.0);
    }
}
