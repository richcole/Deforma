layout(location = 0) in vec4 pos;
layout(location = 1) in vec4 n;
layout(location = 2) in vec4 t;
layout(location = 3) in vec4 o;
layout(location = 4) in vec4 r;
layout(location = 5) in vec4 u;

varying vec4 offset;
varying vec4 dx;
varying vec4 dy;

uniform vec4 eye;

void main() {
  gl_Position = gl_ModelViewProjectionMatrix*pos;
  gl_TexCoord[0] = t;
  dx = r;
  dy = u;
  offset = o;
}
