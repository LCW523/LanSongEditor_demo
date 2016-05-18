package org.insta;

import com.example.lansongeditordemo.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class IFValenciaFilter extends InstaFilter {

    public static final String SHADER = 
    		 "#extension GL_OES_EGL_image_external : require\n" +	
    		"precision lowp float;\n" +
            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform samplerExternalOES inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2; //map\n" +
            " uniform sampler2D inputImageTexture3; //gradMap\n" +
            " \n" +
            " mat3 saturateMatrix = mat3(\n" +
            "                            1.1402,\n" +
            "                            -0.0598,\n" +
            "                            -0.061,\n" +
            "                            -0.1174,\n" +
            "                            1.0826,\n" +
            "                            -0.1186,\n" +
            "                            -0.0228,\n" +
            "                            -0.0228,\n" +
            "                            1.1772);\n" +
            " \n" +
            " vec3 lumaCoeffs = vec3(.3, .59, .11);\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     vec3 texel = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "     \n" +
            "     texel = vec3(\n" +
            "                  texture2D(inputImageTexture2, vec2(texel.r, .1666666)).r,\n" +
            "                  texture2D(inputImageTexture2, vec2(texel.g, .5)).g,\n" +
            "                  texture2D(inputImageTexture2, vec2(texel.b, .8333333)).b\n" +
            "                  );\n" +
            "     \n" +
            "     texel = saturateMatrix * texel;\n" +
            "     float luma = dot(lumaCoeffs, texel);\n" +
            "     texel = vec3(\n" +
            "                  texture2D(inputImageTexture3, vec2(luma, texel.r)).r,\n" +
            "                  texture2D(inputImageTexture3, vec2(luma, texel.g)).g,\n" +
            "                  texture2D(inputImageTexture3, vec2(luma, texel.b)).b);\n" +
            "     \n" +
            "     gl_FragColor = vec4(texel, 1.0);\n" +
            " }";

    public IFValenciaFilter(Context context) {
        super(SHADER, 2);
        bitmaps[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.valencia_map);
        bitmaps[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.valencia_gradient_map);
    }

}
