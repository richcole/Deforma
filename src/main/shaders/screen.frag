varying vec4 dx;
varying vec4 dy;
varying vec4 offset;
varying vec4 normal;

uniform vec4 eye;

void main() {
  vec4 p = offset + gl_TexCoord[0].x * dx + gl_TexCoord[0].y * dy;
  vec4 e = eye;
  vec4 r = normalize(p - e);

  vec4 c = 0.3 * abs(dot(normal, vec4(0, 1, 0, 1)));
  gl_FragColor = vec4(c.x, c.y, c.z, 1.0);
}
