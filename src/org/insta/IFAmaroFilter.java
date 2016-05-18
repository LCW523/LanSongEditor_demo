package org.insta;


import com.example.lansongeditordemo.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class IFAmaroFilter extends InstaFilter {

    public static final String SHADER = 
    		 "#extension GL_OES_EGL_image_external : require\n" +	
    				 "precision mediump float;\n" +
            " varying vec2 textureCoordinate;\n" +
            " \n" +
            " uniform samplerExternalOES inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2; //blowout;\n" +
            " uniform sampler2D inputImageTexture3; //overlay;\n" +
            " uniform sampler2D inputImageTexture4; //map\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     \n" +
            "     vec4 texel = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     vec3 bbTexel = texture2D(inputImageTexture2, textureCoordinate).rgb;\n" +
            "     \n" +
            "     texel.r = texture2D(inputImageTexture3, vec2(bbTexel.r, texel.r)).r;\n" +
            "     texel.g = texture2D(inputImageTexture3, vec2(bbTexel.g, texel.g)).g;\n" +
            "     texel.b = texture2D(inputImageTexture3, vec2(bbTexel.b, texel.b)).b;\n" +
            "     \n" +
            "     vec4 mapped;\n" +
            "     mapped.r = texture2D(inputImageTexture4, vec2(texel.r, .16666)).r;\n" +
            "     mapped.g = texture2D(inputImageTexture4, vec2(texel.g, .5)).g;\n" +
            "     mapped.b = texture2D(inputImageTexture4, vec2(texel.b, .83333)).b;\n" +
            "     mapped.a = 1.0;\n" +
            "     \n" +
            "     gl_FragColor = mapped;\n" +
            " }";

    public IFAmaroFilter(Context context) {
        super(SHADER, 3);
        bitmaps[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blackboard_1024);
        bitmaps[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.overlay_map);
        bitmaps[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.amaro_map);
    }

}
