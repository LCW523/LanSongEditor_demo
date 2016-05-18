package com.lansosdk.videoeditor;

import android.util.Log;


public class LanSoEditor {

	private static boolean isLoaded=false;
	  
		static synchronized void loadLibraries() {
	        if (isLoaded)
	            return;
	        Log.d("lansoeditor","load libraries......");
	    	System.loadLibrary("ffmpegeditor");
    	    System.loadLibrary("lsdisplay");
    	    System.loadLibrary("lsplayer");
    	    isLoaded=true;
	  }
		
		  public static void initSo()
		  {
			  		loadLibraries();
		    	    nativeInit();
		  }
	    public static void unInitSo()
	    {
	    	nativeUninit();
	    }
	    public static native void nativeInit();
	    public static native void nativeUninit();
	    
}
