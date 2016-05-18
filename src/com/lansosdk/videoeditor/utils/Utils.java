package com.lansosdk.videoeditor.utils;

import java.util.Locale;

public class Utils {
	
	  public static String buildTimeMilli(long duration) {
	        long total_seconds = duration / 1000;
	        long hours = total_seconds / 3600;
	        long minutes = (total_seconds % 3600) / 60;
	        long seconds = total_seconds % 60;
	        
	       // Log.i("sno","buildTimeMilli:"+duration);
	        
	        if (duration <= 0) {
	            return "--:--";
	        }
	       
	        
	        if (hours >= 100) {
	            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
	        } else if (hours > 0) {
	            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
	        } else {
	            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
	        }
	    }
}
