//position
attribute vec4 vPosition;

//camera transform and texture
uniform mat4 textureTransform;
attribute vec4 vTexCoordinate;
varying vec2 v_TexCoordinate;

//extra texture
attribute vec4 vTex2Coordinate;
varying vec2 v_Tex2Coordinate;


void main() {
    //camera texcoord needs to be manipulated by the transform given back
    //from the system
    v_TexCoordinate = (textureTransform * vTexCoordinate).xy;

    //other texcoords need to swap the y due to stupid issue in opengl with clockwise rendering
    v_Tex2Coordinate = vTex2Coordinate.xy * vec2(1.0, -1.);

    gl_Position = vPosition;
}