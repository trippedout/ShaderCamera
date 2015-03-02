#extension GL_OES_EGL_image_external : require

//necessary
precision mediump float;
uniform samplerExternalOES texture;
varying vec2 v_TexCoordinate;

//added new ones
uniform float circleSize;
uniform float percentage;
uniform float aspectRatio;

bool in_circle(float center_x,float  center_y,float radius,float x,float y) {
    float square_dist = pow(center_x - x, 2.0) + pow(center_y - y, 2.0);
    return square_dist <= pow(radius, 2.0);
}

void main () {
    //set initial color
    vec4 color = texture2D(texture, v_TexCoordinate * vec2(aspectRatio, 1.0));
    gl_FragColor = color;

    vec4 purple = vec4(2.46, 0.43, 0.43, 1.0) * .5; //.5 adds darkness
    vec4 newPurple = clamp(percentage + purple, 0.0, 1.0);

    if(in_circle(0.5, 0.5, circleSize, v_TexCoordinate.x, v_TexCoordinate.y))
        gl_FragColor = color * newPurple;
    else
        gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
}