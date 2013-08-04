varying vec4 dx;
varying vec4 dy;
varying vec4 offset;

uniform vec4 eye;

void main() {    
  vec4 a[3] = { vec4(0, 0, 0, 1), vec4(1, 1, 0, 1), vec4(10, 10, 10, 1) };

  vec4 p = offset + gl_TexCoord[0].x * dx + gl_TexCoord[0].y * dy;
  vec4 e = eye;
  vec4 r = normalize(p - e);

//  double t = dot(a[1], r) / dot(a[0], r); 
//  vec4 gamma = (t * t * a[0]) + (t * a[1]) + a[2]; 
//	double phi = dot(r, gamma) - dot(e, r);
//	double d = length(e + phi*r - gamma);
//  double w = 1.0 - step(10, d);

  vec4 n = normalize(vec4(0, 1, 0, 1));
  double d = 40;
  double phi = (d - dot(n, e)) / dot(n, r);
  vec4 g = e + phi*r;	
	double w = (1 - step(20, abs(g.x - 10))) * (1 - step(20, abs(g.z - 10)));
  gl_FragColor = vec4(w, w, w, 1.0);
}
