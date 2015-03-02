//all 4 of these are necessary
attribute vec4 vPosition;
attribute vec4 vTexCoordinate;
uniform mat4 textureTransform;
varying vec2 v_TexCoordinate;

//add new ones here

void main() {
    v_TexCoordinate = (textureTransform * vTexCoordinate).xy;
    gl_Position = vPosition;
}