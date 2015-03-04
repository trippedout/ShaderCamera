#extension GL_OES_EGL_image_external : require

//necessary
precision mediump float;
uniform samplerExternalOES texture;
uniform float aspectRatio;
varying vec2 v_TexCoordinate;

//test
uniform sampler2D image;
varying vec2 v_Tex2Coordinate;

//added new ones
uniform float circleSize;
uniform float percentage;

bool in_circle(float center_x,float  center_y,float radius,float x,float y) {
    float square_dist = pow(center_x - x, 2.0) + pow(center_y - y, 2.0);
    return square_dist <= pow(radius, 2.0);
}

void main () {
    //set initial color
    vec4 color = texture2D(texture, v_TexCoordinate * vec2(aspectRatio, 1.0));

    //colored circles
    //vec4 purple = vec4(2.46, 0.43, 0.43, 1.0) * .5; //.5 adds darkness
    //vec4 newPurple = clamp(percentage + purple, 0.0, 1.0);
    //if(in_circle(0.5, 0.5, circleSize, v_TexCoordinate.x, v_TexCoordinate.y))
    //    gl_FragColor = color;// * newPurple;


    vec4 face = texture2D(image, v_Tex2Coordinate);
    gl_FragColor = face;

    if(face.a < 1.0 && face.a > 0.0)
    {
        vec4 mx = mix(color, face, face.a);
        gl_FragColor = mx + vec4(face.rgb * (1. - face.a), 1.0);
    }
    else if(face.a == 0.0)
        gl_FragColor = color;

}