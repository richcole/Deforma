varying vec4 dx;
varying vec4 dy;
varying vec4 offset;
varying vec4 normal;

uniform vec4 eye;

void main() {
  vec4 p = offset + gl_TexCoord[0].x * dx + gl_TexCoord[0].y * dy;
  vec4 e = eye;
  vec4 r = normalize(p - e);

  double c = 0.3 * abs(dot(normal, vec4(0, 1, 0, 1)));
	double v = gl_TexCoord[0].y;

  gl_FragColor = vec4(c, c, c, 1.0);
}
