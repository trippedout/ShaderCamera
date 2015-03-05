#extension GL_OES_EGL_image_external : require

//necessary
precision mediump float;
uniform samplerExternalOES camTexture;
uniform float aspectRatio;

varying vec2 v_CamTexCoordinate;
varying vec2 v_TexCoordinate;

uniform highp vec2 blurCenter;
uniform highp float blurSize;

uniform sampler2D color;

void main ()
{
    //set initial color of camera
    //vec4 cameraColor = texture2D(camTexture, v_CamTexCoordinate * vec2(aspectRatio, 1.0));
    vec2 textureCoordinate = v_CamTexCoordinate * vec2(aspectRatio, 1.0);

    vec2 blurCenterOffset = blurCenter * vec2(aspectRatio, 1.0);

    highp vec2 samplingOffset = 1.0/100.0 * (blurCenterOffset - textureCoordinate) * blurSize;

    lowp vec4 fragmentColor = texture2D(camTexture, textureCoordinate) * 0.18;
    fragmentColor += texture2D(camTexture, textureCoordinate + samplingOffset) * 0.15;
    fragmentColor += texture2D(camTexture, textureCoordinate + (2.0 * samplingOffset)) *  0.12;
    fragmentColor += texture2D(camTexture, textureCoordinate + (3.0 * samplingOffset)) * 0.09;
    fragmentColor += texture2D(camTexture, textureCoordinate + (4.0 * samplingOffset)) * 0.05;
    fragmentColor += texture2D(camTexture, textureCoordinate - samplingOffset) * 0.15;
    fragmentColor += texture2D(camTexture, textureCoordinate - (2.0 * samplingOffset)) *  0.12;
    fragmentColor += texture2D(camTexture, textureCoordinate - (3.0 * samplingOffset)) * 0.09;
    fragmentColor += texture2D(camTexture, textureCoordinate - (4.0 * samplingOffset)) * 0.05;

    //color it weird
    vec4 color = texture2D(color, v_TexCoordinate);

    gl_FragColor = fragmentColor * color;
}