#extension GL_OES_EGL_image_external : require

//necessary
precision mediump float;
uniform samplerExternalOES camTexture;
uniform float aspectRatio;

varying vec2 v_CamTexCoordinate;
varying vec2 v_TexCoordinate;

//test
uniform sampler2D mouth;

void main () {
    //set initial color
    vec4 cameraColor = texture2D(camTexture, v_CamTexCoordinate * vec2(aspectRatio, 1.0));


    vec4 face = texture2D(mouth, v_TexCoordinate);
    gl_FragColor = face;

    if(face.a < 1.0 && face.a > 0.0)
    {
        vec4 mx = mix(cameraColor, face, face.a);
        gl_FragColor = mx + vec4(face.rgb * (1. - face.a), 1.0);
    }
    else if(face.a == 0.0)
        gl_FragColor = cameraColor;

}