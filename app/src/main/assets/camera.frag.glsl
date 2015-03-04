#extension GL_OES_EGL_image_external : require

precision mediump float;
uniform samplerExternalOES camTexture;
uniform float aspectRatio;

varying vec2 v_CamTexCoordinate;

void main () {
    vec4 color = texture2D(camTexture, v_CamTexCoordinate * vec2(aspectRatio, 1.0));
    gl_FragColor = color;
}