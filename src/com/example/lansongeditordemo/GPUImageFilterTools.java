/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lansongeditordemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;

import jp.co.cyberagent.android.gpuimage.*;

import java.util.LinkedList;
import java.util.List;

import org.insta.IF1977Filter;
import org.insta.IFAmaroFilter;
import org.insta.IFBrannanFilter;
import org.insta.IFEarlybirdFilter;
import org.insta.IFHefeFilter;
import org.insta.IFHudsonFilter;
import org.insta.IFInkwellFilter;
import org.insta.IFLomofiFilter;
import org.insta.IFLordKelvinFilter;
import org.insta.IFNashvilleFilter;
import org.insta.IFRiseFilter;
import org.insta.IFSierraFilter;
import org.insta.IFSutroFilter;
import org.insta.IFToasterFilter;
import org.insta.IFValenciaFilter;
import org.insta.IFWaldenFilter;
import org.insta.IFXproIIFilter;


public class GPUImageFilterTools {
    public static void showDialog(final Context context,
            final OnGpuImageFilterChosenListener listener) {
        final FilterList filters = new FilterList();
        
        filters.addFilter("Contrast", FilterType.CONTRAST);  //PASS
        filters.addFilter("Invert", FilterType.INVERT);  //PASS
        
//        filters.addFilter("Pixelation", FilterType.PIXELATION);   静止图片像素的大小.
        filters.addFilter("Hue", FilterType.HUE);  //PASS
        filters.addFilter("Gamma", FilterType.GAMMA);  //PASS
        
        filters.addFilter("Brightness", FilterType.BRIGHTNESS); //PASS
        filters.addFilter("Sepia", FilterType.SEPIA); //PASS
        filters.addFilter("Grayscale", FilterType.GRAYSCALE);  //PASS
        
        
        filters.addFilter("Posterize", FilterType.POSTERIZE);  //PASS
        filters.addFilter("Saturation", FilterType.SATURATION); //PASS
        
        filters.addFilter("Exposure", FilterType.EXPOSURE);  //PASS
        
        filters.addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW);  //PASS
        filters.addFilter("Monochrome", FilterType.MONOCHROME); //PASS
        
        filters.addFilter("Opacity", FilterType.OPACITY);    //PASS 设置视频的透明度,一般不需要.
        filters.addFilter("RGB", FilterType.RGB);  //PASS
        
        filters.addFilter("White Balance", FilterType.WHITE_BALANCE);  //PASS
        
        filters.addFilter("Vignette", FilterType.VIGNETTE);  //PASS
        filters.addFilter("ToneCurve", FilterType.TONE_CURVE);  //PASS 色调曲线

//        filters.addFilter("Blend (Difference)", FilterType.BLEND_DIFFERENCE);
//        filters.addFilter("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER);
//        filters.addFilter("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN);
//        filters.addFilter("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE);
//        filters.addFilter("Blend (Darken)", FilterType.BLEND_DARKEN);
//        filters.addFilter("Blend (Dissolve)", FilterType.BLEND_DISSOLVE);
//        filters.addFilter("Blend (Exclusion)", FilterType.BLEND_EXCLUSION);
//        filters.addFilter("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT);
//        filters.addFilter("Blend (Lighten)", FilterType.BLEND_LIGHTEN);
//        filters.addFilter("Blend (Add)", FilterType.BLEND_ADD);
//        filters.addFilter("Blend (Divide)", FilterType.BLEND_DIVIDE);
//        filters.addFilter("Blend (Multiply)", FilterType.BLEND_MULTIPLY);
//        filters.addFilter("Blend (Overlay)", FilterType.BLEND_OVERLAY);
//        filters.addFilter("Blend (Screen)", FilterType.BLEND_SCREEN);
//        filters.addFilter("Blend (Alpha)", FilterType.BLEND_ALPHA);
//        filters.addFilter("Blend (Color)", FilterType.BLEND_COLOR);
//        filters.addFilter("Blend (Hue)", FilterType.BLEND_HUE);
//        filters.addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION);
//        filters.addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY);
//        filters.addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN);
//        filters.addFilter("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT);
//        filters.addFilter("Blend (Subtract)", FilterType.BLEND_SUBTRACT);
//        filters.addFilter("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY);
//        filters.addFilter("Blend (Normal)", FilterType.BLEND_NORMAL);

//        filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA);
        filters.addFilter("Crosshatch", FilterType.CROSSHATCH);  //PASS

        filters.addFilter("CGA Color Space", FilterType.CGA_COLORSPACE);  //PASS
        filters.addFilter("Kuwahara", FilterType.KUWAHARA);  //PASS
//        filters.addFilter("Halftone", FilterType.HALFTONE);

        filters.addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION);  //PASS
//        filters.addFilter("Glass Sphere", FilterType.GLASS_SPHERE); 
        filters.addFilter("Haze", FilterType.HAZE);  //PASS
        filters.addFilter("Sphere Refraction", FilterType.SPHERE_REFRACTION); //PASS
        filters.addFilter("Swirl", FilterType.SWIRL);  //PASS
        filters.addFilter("False Color", FilterType.FALSE_COLOR);  //PASS

        filters.addFilter("Color Balance", FilterType.COLOR_BALANCE);  //PASS

        filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN);  //PASS

        //新增
        filters.addFilter("AMARO", FilterType.AMARO);   //PASS
        filters.addFilter("RISE", FilterType.RISE);   //PASS
        filters.addFilter("HUDSON", FilterType.HUDSON);   //PASS
        filters.addFilter("XPROII", FilterType.XPROII);   //PASS
        filters.addFilter("SIERRA", FilterType.SIERRA);   //PASS
        filters.addFilter("LOMOFI", FilterType.LOMOFI);   //PASS
        filters.addFilter("EARLYBIRD", FilterType.EARLYBIRD);   //PASS
        filters.addFilter("SUTRO", FilterType.SUTRO);   //PASS
        filters.addFilter("TOASTER", FilterType.TOASTER);   //PASS
        filters.addFilter("BRANNAN", FilterType.BRANNAN);   //PASS
        filters.addFilter("INKWELL", FilterType.INKWELL);   //PASS
        filters.addFilter("WALDEN", FilterType.WALDEN);   //PASS
        filters.addFilter("HEFE", FilterType.HEFE);   //PASS
        filters.addFilter("VALENCIA", FilterType.VALENCIA); //PASS  
        filters.addFilter("NASHVILLE", FilterType.NASHVILLE);   //PASS
        filters.addFilter("if1977", FilterType.IF1977);     //PASS
        filters.addFilter("LORDKELVIN", FilterType.LORDKELVIN);  //PASS		
          		
          		
          		
          		
        

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a filter(total:"+filters.names.size()+" )");
        builder.setItems(filters.names.toArray(new String[filters.names.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int item) {
                        listener.onGpuImageFilterChosenListener(
                                createFilterForType(context, filters.filters.get(item)));
                    }
                });
        builder.create().show();
    }

    private static GPUImageFilter createFilterForType(final Context context, final FilterType type) {
        switch (type) {
            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case GAMMA:
                return new GPUImageGammaFilter(2.0f);
            case INVERT:
                return new GPUImageColorInvertFilter();
            case PIXELATION:
                return new GPUImagePixelationFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
            case BRIGHTNESS:
                return new GPUImageBrightnessFilter(1.5f);
            case GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case SEPIA:
                return new GPUImageSepiaFilter();
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case SATURATION:
                return new GPUImageSaturationFilter(1.0f);
            case EXPOSURE:
                return new GPUImageExposureFilter(0.0f);
            case HIGHLIGHT_SHADOW:
            	return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
            case MONOCHROME:
            	return new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case OPACITY:
                return new GPUImageOpacityFilter(1.0f);
            case RGB:
                return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case WHITE_BALANCE:
                return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[] {0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case TONE_CURVE:
                GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
                toneCurveFilter.setFromCurveFileInputStream(
                        context.getResources().openRawResource(R.raw.tone_cuver_sample));
                return toneCurveFilter;
            case BLEND_DIFFERENCE:
                return createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case BLEND_SOURCE_OVER:
                return createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case BLEND_COLOR_BURN:
                return createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case BLEND_COLOR_DODGE:
                return createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case BLEND_DARKEN:
                return createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case BLEND_DISSOLVE:
                return createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case BLEND_EXCLUSION:
                return createBlendFilter(context, GPUImageExclusionBlendFilter.class);
            case BLEND_HARD_LIGHT:
                return createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case BLEND_LIGHTEN:
                return createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case BLEND_ADD:
                return createBlendFilter(context, GPUImageAddBlendFilter.class);
            case BLEND_DIVIDE:
                return createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case BLEND_MULTIPLY:
                return createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case BLEND_OVERLAY:
                return createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case BLEND_SCREEN:
                return createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case BLEND_ALPHA:
                return createBlendFilter(context, GPUImageAlphaBlendFilter.class);
            case BLEND_COLOR:
                return createBlendFilter(context, GPUImageColorBlendFilter.class);
            case BLEND_HUE:
                return createBlendFilter(context, GPUImageHueBlendFilter.class);
            case BLEND_SATURATION:
                return createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case BLEND_LUMINOSITY:
                return createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case BLEND_LINEAR_BURN:
                return createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case BLEND_SOFT_LIGHT:
                return createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case BLEND_SUBTRACT:
                return createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case BLEND_CHROMA_KEY:
                return createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case BLEND_NORMAL:
                return createBlendFilter(context, GPUImageNormalBlendFilter.class);

//            case LOOKUP_AMATORKA:
//                GPUImageLookupFilter amatorka = new GPUImageLookupFilter();
//                amatorka.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_amatorka));
//                return amatorka;
            case CROSSHATCH:
                return new GPUImageCrosshatchFilter();
            case CGA_COLORSPACE:
                return new GPUImageCGAColorspaceFilter();
            case KUWAHARA:
                return new GPUImageKuwaharaFilter();
            case BULGE_DISTORTION:
                return new GPUImageBulgeDistortionFilter();
            case GLASS_SPHERE:
                return new GPUImageGlassSphereFilter();
            case HAZE:
                return new GPUImageHazeFilter();
            case SPHERE_REFRACTION:
                return new GPUImageSphereRefractionFilter();
            case SWIRL:
                return new GPUImageSwirlFilter();
            case FALSE_COLOR:
                return new GPUImageFalseColorFilter();
            case COLOR_BALANCE:
                return new GPUImageColorBalanceFilter();
            case LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;
            case HALFTONE:
                return new GPUImageHalftoneFilter();
            
                //一下是新增的
            case AMARO:
            	 return new IFAmaroFilter(context);
            case RISE:
            	 return new IFRiseFilter(context);
            case HUDSON:
            		return new IFHudsonFilter(context);
           	case	XPROII:
           			return new IFXproIIFilter(context);
           	case	SIERRA:
           			return new IFSierraFilter(context);
           	case	LOMOFI:
           			return new IFLomofiFilter(context);
           	case 	EARLYBIRD:
           			return new IFEarlybirdFilter(context);
           	case 	SUTRO:
           			return new IFSutroFilter(context);
           	case  TOASTER:
           			return new IFToasterFilter(context);
           	case    BRANNAN:
           			return new IFBrannanFilter(context);
           	case     INKWELL:
           			return new IFInkwellFilter(context);
           	case      WALDEN:
           			return new IFWaldenFilter(context);
           	case	HEFE:
           			return new IFHefeFilter(context);
           	case	VALENCIA:
        	   		return new IFValenciaFilter(context);
           	case		NASHVILLE:
           			return new IFNashvilleFilter(context);
           	case		LORDKELVIN:
           			return new IFLordKelvinFilter(context);
           	case  IF1977:
           		return new IF1977Filter(context);
           		
            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }

    private static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    private enum FilterType {
        CONTRAST, GRAYSCALE,  SEPIA,  POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE,
        BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        CROSSHATCH, CGA_COLORSPACE, KUWAHARA,  BULGE_DISTORTION, GLASS_SPHERE, HAZE, SPHERE_REFRACTION, SWIRL, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, HALFTONE,
        //以下是新增的(共16个) . 来自https://github.com/beartung/insta-filter
        AMARO,RISE,HUDSON,XPROII,SIERRA,LOMOFI,EARLYBIRD,SUTRO,TOASTER,BRANNAN,INKWELL,WALDEN,HEFE,VALENCIA,NASHVILLE,IF1977,LORDKELVIN}

    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    public static class FilterAdjuster {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        public FilterAdjuster(final GPUImageFilter filter) {
            if (filter instanceof GPUImageSepiaFilter) {
                adjuster = new SepiaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageContrastFilter) {
                adjuster = new ContrastAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGammaFilter) {
                adjuster = new GammaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBrightnessFilter) {
                adjuster = new BrightnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHueFilter) {
                adjuster = new HueAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePosterizeFilter) {
                adjuster = new PosterizeAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePixelationFilter) {
                adjuster = new PixelationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSaturationFilter) {
                adjuster = new SaturationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageExposureFilter) {
                adjuster = new ExposureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHighlightShadowFilter) {
                adjuster = new HighlightShadowAdjuster().filter(filter);
            } else if (filter instanceof GPUImageMonochromeFilter) {
                adjuster = new MonochromeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageOpacityFilter) {
                adjuster = new OpacityAdjuster().filter(filter);
            } else if (filter instanceof GPUImageRGBFilter) {
                adjuster = new RGBAdjuster().filter(filter);
            } else if (filter instanceof GPUImageWhiteBalanceFilter) {
                adjuster = new WhiteBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageVignetteFilter) {
                adjuster = new VignetteAdjuster().filter(filter);
            } else if (filter instanceof GPUImageDissolveBlendFilter) {
                adjuster = new DissolveBlendAdjuster().filter(filter);
            } else if (filter instanceof GPUImageCrosshatchFilter) {
                adjuster = new CrosshatchBlurAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBulgeDistortionFilter) {
                adjuster = new BulgeDistortionAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGlassSphereFilter) {
                adjuster = new GlassSphereAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHazeFilter) {
                adjuster = new HazeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSphereRefractionFilter) {
                adjuster = new SphereRefractionAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSwirlFilter) {
                adjuster = new SwirlAdjuster().filter(filter);
            } else if (filter instanceof GPUImageColorBalanceFilter) {
                adjuster = new ColorBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageLevelsFilter) {
                adjuster = new LevelsMinMidAdjuster().filter(filter);
            }
            else {

                adjuster = null;
            }
        }

        public boolean canAdjust() {
            return adjuster != null;
        }

        public void adjust(final int percentage) {
            if (adjuster != null) {
                adjuster.adjust(percentage);
            }
        }

        private abstract class Adjuster<T extends GPUImageFilter> {
            private T filter;

            @SuppressWarnings("unchecked")
            public Adjuster<T> filter(final GPUImageFilter filter) {
                this.filter = (T) filter;
                return this;
            }

            public T getFilter() {
                return filter;
            }

            public abstract void adjust(int percentage);

            protected float range(final int percentage, final float start, final float end) {
                return (end - start) * percentage / 100.0f + start;
            }

            protected int range(final int percentage, final int start, final int end) {
                return (end - start) * percentage / 100 + start;
            }
        }
        private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
          @Override
          public void adjust(final int percentage) {
              getFilter().setPixel(range(percentage, 1.0f, 100.0f));
          }
        }

        private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
          @Override
          public void adjust(final int percentage) {
            getFilter().setHue(range(percentage, 0.0f, 360.0f));
          }
        }

        private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setContrast(range(percentage, 0.0f, 2.0f));
            }
        }

        private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setGamma(range(percentage, 0.0f, 3.0f));
            }
        }

        private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setBrightness(range(percentage, -1.0f, 1.0f));
            }
        }

        private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 2.0f));
            }
        }

    

        private class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
            @Override
            public void adjust(final int percentage) {
                // In theorie to 256, but only first 50 are interesting
                getFilter().setColorLevels(range(percentage, 1, 50));
            }
        }

       
        private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSaturation(range(percentage, 0.0f, 2.0f));
            }
        }

        private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setExposure(range(percentage, -10.0f, 10.0f));
            }
        }

        private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setShadows(range(percentage, 0.0f, 1.0f));
                getFilter().setHighlights(range(percentage, 0.0f, 1.0f));
            }
        }

        private class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
                //getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            }
        }

        private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setOpacity(range(percentage, 0.0f, 1.0f));
            }
        }

        private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRed(range(percentage, 0.0f, 1.0f));
                //getFilter().setGreen(range(percentage, 0.0f, 1.0f));
                //getFilter().setBlue(range(percentage, 0.0f, 1.0f));
            }
        }

        private class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setTemperature(range(percentage, 2000.0f, 8000.0f));
                //getFilter().setTint(range(percentage, -100.0f, 100.0f));
            }
        }

        private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setVignetteStart(range(percentage, 0.0f, 1.0f));
            }
        }

        private class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setMix(range(percentage, 0.0f, 1.0f));
            }
        }


        private class CrosshatchBlurAdjuster extends Adjuster<GPUImageCrosshatchFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setCrossHatchSpacing(range(percentage, 0.0f, 0.06f));
                getFilter().setLineWidth(range(percentage, 0.0f, 0.006f));
            }
        }

        private class BulgeDistortionAdjuster extends Adjuster<GPUImageBulgeDistortionFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRadius(range(percentage, 0.0f, 1.0f));
                getFilter().setScale(range(percentage, -1.0f, 1.0f));
            }
        }

        private class GlassSphereAdjuster extends Adjuster<GPUImageGlassSphereFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRadius(range(percentage, 0.0f, 1.0f));
            }
        }

        private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setDistance(range(percentage, -0.3f, 0.3f));
                getFilter().setSlope(range(percentage, -0.3f, 0.3f));
            }
        }

        private class SphereRefractionAdjuster extends Adjuster<GPUImageSphereRefractionFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRadius(range(percentage, 0.0f, 1.0f));
            }
        }

        private class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setAngle(range(percentage, 0.0f, 2.0f));
            }
        }

        private class ColorBalanceAdjuster extends Adjuster<GPUImageColorBalanceFilter> {

            @Override
            public void adjust(int percentage) {
                getFilter().setMidtones(new float[]{
                        range(percentage, 0.0f, 1.0f),
                        range(percentage / 2, 0.0f, 1.0f),
                        range(percentage / 3, 0.0f, 1.0f)});
            }
        }

        private class LevelsMinMidAdjuster extends Adjuster<GPUImageLevelsFilter> {
            @Override
            public void adjust(int percentage) {
                getFilter().setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f);
            }
        }
    }
}
