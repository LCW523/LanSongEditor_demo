package org.insta;


import com.example.lansongeditordemo.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class IF1977Filter extends InstaFilter {

    public static final String SHADER = 
   		"#extension GL_OES_EGL_image_external : require\n" +	
   		"precision mediump float;\n" +
        " varying vec2 textureCoordinate;\n" +
        " \n" +
        " uniform samplerExternalOES inputImageTexture;\n" +
        " uniform sampler2D inputImageTexture2;\n" +
        " \n" +
        " void main()\n" +
        " {\n" +
        "     \n" +
        "     vec3 texel = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
        "     \n" +
        "     texel = vec3(\n" +
        "                  texture2D(inputImageTexture2, vec2(texel.r, .16666)).r,\n" +
        "                  texture2D(inputImageTexture2, vec2(texel.g, .5)).g,\n" +
        "                  texture2D(inputImageTexture2, vec2(texel.b, .83333)).b);\n" +
        "     \n" +
        "     gl_FragColor = vec4(texel, 1.0);\n" +
        " }";

    public IF1977Filter(Context context) {
        super(SHADER, 2);
        bitmaps[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.map_1977);
        bitmaps[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blowout_1977);
    }

}
