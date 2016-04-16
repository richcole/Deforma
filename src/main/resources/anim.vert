#version 150

#define MAX_BONES 1000

uniform mat4 viewTr;
uniform mat4 modelTr;
uniform int numBones;
uniform int frame;
uniform Bones {
	mat4 tr[MAX_BONES];
} bones;

in vec3    vert;
in vec3    normal;
in vec2    texCoords;
in float   bone;

out vec2 fragTexCoords;
out mat4 fragTr;
out float fragLight;


void main() {
    int boneIndex = int(bone);
    mat4 btr;
    if (boneIndex < MAX_BONES) {
		btr = bones.tr[(frame * numBones) + boneIndex];
	}
    fragTr = viewTr * modelTr * btr;
    gl_Position = fragTr * vec4(vert, 1);
    fragTexCoords = texCoords;
    fragLight = 0.2 + (abs(dot(vec3(1,1,1),normal)) * 0.8);
}