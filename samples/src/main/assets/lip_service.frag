#extension GL_OES_EGL_image_external : require

//necessary
precision mediump float;
uniform samplerExternalOES camTexture;
uniform float aspectRatio;

varying vec2 v_CamTexCoordinate;
varying vec2 v_TexCoordinate;

//THIS NEEDS TO MATCH STRING IN LipServiceRenderer
uniform sampler2D mouth;

void main () {
    //set initial color of camera
    vec4 cameraColor = texture2D(camTexture, v_CamTexCoordinate * vec2(aspectRatio, 1.0));

    vec4 face = texture2D(mouth, v_TexCoordinate);
    gl_FragColor = face;

    if(face.a < 1.0 && face.a > 0.0)
    {
        //if image is part of alpha fade of the mouth, mix the camera and the face
        //pixels together and then add a bit extra from the face to make sure its still solid
        vec4 mx = mix(cameraColor, face, face.a);
        gl_FragColor = mx + vec4(face.rgb * (1. - face.a), 1.0);
    }
    else if(face.a == 0.0)
        gl_FragColor = cameraColor;
}