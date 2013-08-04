layout(location = 0) in vec4 p;
layout(location = 1) in vec4 n;
layout(location = 2) in vec4 t;
varying vec4 gl_TexCoord[];

varying smooth double d;
void main() {
  gl_Position = gl_ModelViewProjectionMatrix*p;
  vec4 e = gl_ModelViewProjectionMatrixInverse*vec4(0, 0, -1, 0);
  d = abs(0.1 / dot(normalize(p - e), normalize(n)));
  gl_TexCoord[0] = t;
}