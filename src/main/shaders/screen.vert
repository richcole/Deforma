uniform mat4 tr;

void main(){
  gl_Position = gl_ModelViewProjectionMatrix*tr*gl_Vertex;
}