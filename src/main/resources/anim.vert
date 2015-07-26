#version 150

uniform mat4 tr;
uniform int   animIndex;
uniform float alpha;

uniform int  numAnim;
uniform int  numBones;
uniform vec4 rotationTable[];
uniform vec4 translationTable[];
uniform int  boneParents[];

in vec3  vert;
in vec2  texCoords;
in float boneIndex;

out vec2 fragTexCoords;
out mat4 fragTr;

vec3 qrot(vec4 q, vec3 v) {
    return v + 2.0*cross(q.xyz, cross(q.xyz,v) + q.w*v);
}

vec4 qmul(vec4 a, vec4 b) {
	return vec4(cross(a.xyz,b.xyz) + a.xyz*b.w + b.xyz*a.w, a.w*b.w - dot(a.xyz,b.xyz));
}

vec4 slerp(vec4 a, vec4 b, float t) {
	float theta = acos(dot(a,b)/(length(a)*length(b)));
	(sin((1-t)*theta) / sin(theta)) * a + (sin(t*theta) / sin(theta)) * b;
}
	    

int getIndex(int boneIndex, int animIndex) {
  return ((animIndex % numAnim) * numBones) + boneIndex;
}

vec4 rotation(int boneIndex, int animIndex, float alpha) {
	r1 = rotationTable[getIndex(boneIndex, animIndex)];
	r2 = rotationTable[getIndex(boneIndex+1, animIndex)];
	return slerp(r1, r2, alpha);
}

int boneParent(int boneIndex) {
	return boneParents[boneIndex];
}

vec3 translation(int boneIndex, int animIndex) {
	t1 = translationTable[getIndex(boneIndex, animIndex)];
	t2 = translationTable[getIndex(boneIndex, animIndex)];
	return vlerp(t2, t2, alpha);
}


void main() {

	while(boneIndex != -1) {
	  vert = qrot(rotation(boneIndex, animIndex), vert);
	  vert = vert + translation(boneIndex, animIndex);
	  boneIndex = getParent(boneIndex);
	} 

    gl_Position = tr * vec4(vert, 1);
    fragTexCoords = texCoords;
    fragTr = tr;
}