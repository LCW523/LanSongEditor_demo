package com.lansosdk.videoeditor;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.lansongeditordemo.WaterMarkConfig;
import com.lansosdk.videoeditor.utils.FileUtils;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;



public class VideoEditor {

	
	 private static final String TAG="VideoEditor";
	 
	  public static final int VIDEO_EDITOR_EXECUTE_SUCCESS1 =0;
	  public static final int VIDEO_EDITOR_EXECUTE_SUCCESS2 =1;
	  public static final int VIDEO_EDITOR_EXECUTE_FAILED =-1;
	  
	  private final int VIDEOEDITOR_HANDLER_PROGRESS=203;
		private final int VIDEOEDITOR_HANDLER_COMPLETED=204;
	public VideoEditor() {
		// TODO Auto-generated constructor stub
		Looper looper;
        if ((looper = Looper.myLooper()) != null) {
            mEventHandler = new EventHandler(this, looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            mEventHandler = new EventHandler(this, looper);
        } else {
            mEventHandler = null;
            Log.w(TAG,"cannot get Looper handler. may be cannot receive video editor progress!!");
        }
	}
	
	
	 public interface onProgressListener {
			/**
			 * 
			 * @param v
			 * @param currentTime  当前正在处理的视频帧的时间戳.即当前的位置.
			 */
	        void onProgress(VideoEditor v,int percent);
	    }
	    public onProgressListener mProgressListener=null;
	    /**
	     * 开始执行后,每秒钟更新一次. 
	     * @param listener
	     */
		public void setOnProgessListener(onProgressListener listener)
		{
			mProgressListener=listener;
		}
		private void doOnProgressListener(int timeMS)
		{
			if(mProgressListener!=null)
				mProgressListener.onProgress(this,timeMS);
		}
	 private EventHandler mEventHandler;
	 private  class EventHandler extends Handler {
	        private final WeakReference<VideoEditor> mWeakExtract;

	        public EventHandler(VideoEditor mp, Looper looper) {
	            super(looper);
	            mWeakExtract = new WeakReference<VideoEditor>(mp);
	        }

	        @Override
	        public void handleMessage(Message msg) {
	        	VideoEditor videoextract = mWeakExtract.get();
	        	if(videoextract==null){
	        		Log.e(TAG,"VideoExtractBitmap went away with unhandled events");
	        		return ;
	        	}
	        	switch (msg.what) {
				case VIDEOEDITOR_HANDLER_PROGRESS:
					videoextract.doOnProgressListener(msg.arg1);
					break;
				default:
					break;
				}
	        }
	   }
	   /**
	     * 异步线程执行的代码.
	     */
	    public int executeVideoEditor(String[] array)  {
	        return execute(array);
	    }
	    
	    @SuppressWarnings("unused") /* Used from JNI */
	    private void postEventFromNative(int what,int arg1, int arg2) {
	    	Log.i(TAG,"postEvent from native  is:"+what);
	    	
	    	  if(mEventHandler!=null){
              	  Message msg=mEventHandler.obtainMessage(VIDEOEDITOR_HANDLER_PROGRESS);
                  msg.arg1=what;
                  mEventHandler.sendMessage(msg);	
              }
	    }
	    
	    private native int execute(Object cmdArray);
	
	public int vAdjustSpped(String srcPath,MediaInfo media,String dstPath)
	{
		return videoAdjustSpeed(srcPath,media.vCodecName,2,dstPath);
	}
	    //2016年5月15日09:30:23,新增更多的效果. 
	//只是在PC端测试后,在android暂时没有测试.
	/**
	 * 把视频转换为mp4视频格式,并有淡入淡出的效果.
	 * 适用于视频转场的场合,比如两个视频AB之间需要转场,中间需要"五分钟过去了..."这样的文字,可以用这个命令来生成一个提示的视频,然后把这个视频和两个转场拼接起来即可.
	 * 
	 * @param srcPath
	 * @param totalTime
	 * @param fadeinstart
	 * @param fadeinCnt
	 * @param fadeoutstart
	 * @param fadeoutCnt
	 * @param dstPath
	 * @return
	 */
	public native int  pictureFadeInOut( String srcPath,int totalTime,int fadeinstart,int fadeinCnt,int fadeoutstart,int fadeoutCnt,
		   String dstPath);
	/**
	 * 
	 * @param srcPath
	 * @param totalTime
	 * @param fadeinstart
	 * @param fadeinCnt
	 * @param dstPath
	 * @return
	 */
	private native int  pictureFadeIn( String srcPath,int totalTime,int fadeinstart,int fadeinCnt,String dstPath);
	/**
	 * 
	 * @param srcPath
	 * @param totalTime
	 * @param fadeoutstart
	 * @param fadeoutCnt
	 * @param dstPath
	 * @return
	 */
	private native int  pictureFadeOut( String srcPath,int totalTime,int fadeoutstart,int fadeoutCnt,String dstPath);
	/**
	 * 
	 * @param srcVideoPath
	 * @param srcPngPath
	 * @param totalTime
	 * @param offsetTime
	 * @param fadeinStart
	 * @param fadeoutCnt
	 * @param x
	 * @param y
	 * @param dstPath
	 * @return
	 */
	private native int  pngFadeIn( String srcVideoPath,String srcPngPath,int totalTime,int offsetTime,int fadeinStart,int fadeoutCnt,int x,int y,String dstPath);
	/**
	 * 
	 * @param srcPath
	 * @param decoder
	 * @param angle
	 * @param dstPath
	 * @return
	 */
	private native int  videoRotateAngle( String srcPath,String decoder,float angle,String dstPath);
	/**
	 * 
	 * @param srcPath
	 * @param decoder
	 * @param speed
	 * @param dstPath
	 * @return
	 */
	private native int  videoAdjustSpeed( String srcPath,String decoder,float speed,String dstPath);
	/**
	 * 
	 * @param srcPath
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int  videoMirrorH( String srcPath,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param srcPath2
	 * @param vol1
	 * @param vol2
	 * @param dstPath
	 * @return
	 */
	private native int audioAdjustVolumeMix( String srcPath1,String srcPath2,float vol1,float vol2,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param srcPath2
	 * @param decoder1
	 * @param speed
	 * @param dstPath
	 * @return
	 */
	private native int videoTransferRight2Left( String srcPath1,String srcPath2,String decoder1,float speed,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int videoRotateVertically( String srcPath1,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int videoRotateHorizontally( String srcPath1,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int videoRotate90Clockwise( String srcPath1,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int videoRotate90CounterClockwise( String srcPath1,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int videoReverse( String srcPath1,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int audioReverse( String srcPath1,String decoder,String dstPath);
	/**
	 * 
	 * @param srcPath1
	 * @param decoder
	 * @param dstPath
	 * @return
	 */
	private native int avReverse( String srcPath1,String decoder,String dstPath);
	    //--------------------------------------------------------------------------
	    /**
		  * 删除多媒体文件中的音频
		  * @param srcFile  输入的MP4文件
		  * @param dstFile 输出文件的绝对路径,路径的文件名类型是.mp4
		  * @return 返回执行的结果.
		  */
		  public int executeDeleteAudio(String srcFile,String dstFile)
		  {
			  	if(FileUtils.fileExist(srcFile)){
				  	List<String> cmdList=new ArrayList<String>();
			    	cmdList.add("-i");
					cmdList.add(srcFile);
					cmdList.add("-vcodec");
					cmdList.add("copy");
					cmdList.add("-an");
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				     return executeVideoEditor(command);
			  	}else{
			  		return VIDEO_EDITOR_EXECUTE_FAILED;
			  	}
		  }
		  /**
		   * 把多媒体中的音频提取出来,保存到文件中.
		   *  
		   * @param srcFile  要处理的多媒体文件,里面需要有视频
		   * @param dstFile  删除视频部分后的音频保存绝对路径, 注意:如果多媒体中是音频是aac压缩,则后缀必须是aac. 如果是mp3压缩,则后缀必须是mp3,
		   * @return 返回执行的结果.
		   */
		  public int executeDeleteVideo(String srcFile,String dstFile)
		  {
			  	if(FileUtils.fileExist(srcFile)==false)
			  		return VIDEO_EDITOR_EXECUTE_FAILED;
			  	
			    if(srcFile.endsWith(".mp4")==false){
			    	return VIDEO_EDITOR_EXECUTE_FAILED;
			    }
			    
			  	List<String> cmdList=new ArrayList<String>();
		    	cmdList.add("-i");
				cmdList.add(srcFile);
				cmdList.add("-acodec");
				cmdList.add("copy");
				cmdList.add("-vn");
				cmdList.add("-y");
				cmdList.add(dstFile);
				String[] command=new String[cmdList.size()];  
			     for(int i=0;i<cmdList.size();i++){  
			    	 command[i]=(String)cmdList.get(i);  
			     }  
			    return  executeVideoEditor(command);
		  }
		  /**
		   * 音频和视频合成为多媒体格式.
		   * @param videoFile 输入的视频文件
		   * @param audioFile 输入的音频文件
		   * @param dstFile  合成后的输出
		   * @return 返回执行的结果.
		   * 
		   * 注意:如果合并的音频是aac格式,ffmpeg -i test.mp4 -i test.aac -vcodec copy -acodec copy -absf aac_adtstoasc shanchu4.mp4
		   */
		  public int executeVideoMergeAudio(String videoFile,String audioFile,String dstFile)
		  {
			  boolean isAAC=false;
			  if(dstFile.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  
			  if(FileUtils.fileExist(videoFile) && FileUtils.fileExist(audioFile)){
				  
					  if(audioFile.endsWith(".aac")){
						  isAAC=true;
					  }
				  
					List<String> cmdList=new ArrayList<String>();
			    	cmdList.add("-i");
					cmdList.add(videoFile);
					cmdList.add("-i");
					cmdList.add(audioFile);
					cmdList.add("-vcodec");
					cmdList.add("copy");
					cmdList.add("-acodec");
					cmdList.add("copy");
					if(isAAC){
						cmdList.add("-absf");
						cmdList.add("aac_adtstoasc");
					}
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
				  
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 
		   * @param videoFile
		   * @param audioFile
		   * @param dstFile
		   * @param audiostartS
		   * @return
		   */
		  public int executeVideoMergeAudio(String videoFile,String audioFile,String dstFile,float audiostartS)
		  {
			  boolean isAAC=false;
			  if(dstFile.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  if(FileUtils.fileExist(videoFile) && FileUtils.fileExist(audioFile)){
				  
				  if(audioFile.endsWith(".aac")){
					  isAAC=true;
				  }
					List<String> cmdList=new ArrayList<String>();
			    	cmdList.add("-i");
					cmdList.add(videoFile);
					
					cmdList.add("-ss");
					cmdList.add(String.valueOf(audiostartS));
					
					cmdList.add("-i");
					cmdList.add(audioFile);
					cmdList.add("-vcodec");
					cmdList.add("copy");
					cmdList.add("-acodec");
					cmdList.add("copy");
					if(isAAC){
						cmdList.add("-absf");
						cmdList.add("aac_adtstoasc");
					}
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
				  
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 输出文件后缀是.mp4格式.
		   * @param videoFile
		   * @param audioFile
		   * @param dstFile
		   * @param audiostartS  音频开始时间, 单位秒,可以有小数, 比如2.5秒
		   * @param audiodurationS 音频增加的总时长.
		   * @return
		   */
		  public int executeVideoMergeAudio(String videoFile,String audioFile,String dstFile,float audiostartS,float audiodurationS)
		  {
			  boolean isAAC=false;
			  if(dstFile.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  
			  
			  if(FileUtils.fileExist(videoFile) && FileUtils.fileExist(audioFile)){
				
				  if(audioFile.endsWith(".aac")){
					  isAAC=true;
				  }
					List<String> cmdList=new ArrayList<String>();
			    	cmdList.add("-i");
					cmdList.add(videoFile);
					
					cmdList.add("-ss");
					cmdList.add(String.valueOf(audiostartS));
					
					cmdList.add("-t");
					cmdList.add(String.valueOf(audiodurationS));
					
					cmdList.add("-i");
					cmdList.add(audioFile);
					
					cmdList.add("-vcodec");
					cmdList.add("copy");
					cmdList.add("-acodec");
					cmdList.add("copy");
					if(isAAC){
						cmdList.add("-absf");
						cmdList.add("aac_adtstoasc");
					}
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
				  
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 裁剪视频文件.(包括视频文件中的音频部分和视频部分)
		   * @param videoFile
		   * @param dstFile
		   * @param startS   开始位置
		   * @param durationS  裁剪时长.
		   * @return
		   */
		  public int executeVideoCutOut(String videoFile,String dstFile,float startS,float durationS)
		  {
			  if(dstFile.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  
			  if(FileUtils.fileExist(videoFile)){
				
					List<String> cmdList=new ArrayList<String>();
					
					cmdList.add("-ss");
					cmdList.add(String.valueOf(startS));
					
					
			    	cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-t");
					cmdList.add(String.valueOf(durationS));
					
					cmdList.add("-vcodec");
					cmdList.add("copy");
					cmdList.add("-acodec");
					cmdList.add("copy");
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
				  
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 获取视频的所有帧图片,并保存到指定路径.
		   * @param videoFile  
		   * @param dstDir  目标文件夹绝对路径.
		   * @param jpgPrefix   保存图片文件的前缀
		   * @return
		   * 
		   * ./ffmpeg -i tenSecond.mp4 -qscale:v 2 output_%03d.jpg
		   */
		  public int executeGetAllFrames(String videoFile,String dstDir,String jpgPrefix)
		  {
			  if(videoFile.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  
			  String dstPath=dstDir+jpgPrefix+"_%3d.jpeg";
			  if(FileUtils.fileExist(videoFile)){
				
					List<String> cmdList=new ArrayList<String>();
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					
			    	cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-qscale:v");
					cmdList.add("2");
					
					cmdList.add(dstPath);

					cmdList.add("-y");
					
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
				  
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 根据设定的采样,获取视频的几行图片.
		   * 假如视频时长是30秒,想平均取5张图片,则sampleRate=5/30;
		   * @param videoFile
		   * @param dstDir
		   * @param jpgPrefix
		   * @param sampeRate  一秒钟采样几张图片. 可以是小数.
		   * @return
		   * 
		   * ./ffmpeg -i 2x.mp4 -qscale:v 2 -vsync 1 -r 5/32 -f image2 r5r-%03d.jpeg
		   */
		  public int executeGetSomeFrames(String videoFile,String dstDir,String jpgPrefix,float sampeRate)
		  {
			  if(videoFile.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  
			  String dstPath=dstDir+jpgPrefix+"_%3d.jpeg";
			  if(FileUtils.fileExist(videoFile)){
				
					List<String> cmdList=new ArrayList<String>();
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					
			    	cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-qscale:v");
					cmdList.add("2");
					
					cmdList.add("-vsync");
					cmdList.add("1");
					
					cmdList.add("-r");
					cmdList.add(String.valueOf(sampeRate));
					
					cmdList.add("-f");
					cmdList.add("image2");
					
					cmdList.add("-y");
					
					cmdList.add(dstPath);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
				  
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  
//		  ./ffmpeg -i 0.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts ts0.ts
//		  ./ffmpeg -i 1.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts ts1.ts
//		  ./ffmpeg -i 2.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts ts2.ts
//		  ./ffmpeg -i 3.mp4 -c copy -bsf:v h264_mp4toannexb -f mpegts ts3.ts
//		  ./ffmpeg -i "concat:ts0.ts|ts1.ts|ts2.ts|ts3.ts" -c copy -bsf:a aac_adtstoasc out2.mp4
		  /**
		   * 
		   * @param mp4Path
		   * @param dstTs
		   * @return
		   */
		  public int executeConvertMp4toTs(String mp4Path,String dstTs)
		  {
			  if(mp4Path.endsWith(".mp4")==false){
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
			  if(FileUtils.fileExist(mp4Path)){
				
					List<String> cmdList=new ArrayList<String>();
					
			    	cmdList.add("-i");
					cmdList.add(mp4Path);

					cmdList.add("-c");
					cmdList.add("copy");
					
					cmdList.add("-bsf:v");
					cmdList.add("h264_mp4toannexb");
					
					cmdList.add("-f");
					cmdList.add("mpegts");
					
					cmdList.add("-y");
					cmdList.add(dstTs);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 注意:输入的各个流需要编码参数一致,适用于断点拍照,拍照多段视频,然后合并的场合.
		   * @param tsArray
		   * @param dstFile
		   * @return
		   * ./ffmpeg -i "concat:ts0.ts|ts1.ts|ts2.ts|ts3.ts" -c copy -bsf:a aac_adtstoasc out2.mp4
		   */
		  public int executeConvertTsToMp4(String[] tsArray,String dstFile)
		  {
			  if(FileUtils.filesExist(tsArray)){
				
				    String concat="concat:";
				    for(int i=0;i<tsArray.length-1;i++){
				    	concat+=tsArray[i];
				    	concat+="|";
				    }
				    concat+=tsArray[tsArray.length-1];
				    	
					List<String> cmdList=new ArrayList<String>();
					
			    	cmdList.add("-i");
					cmdList.add(concat);

					cmdList.add("-c");
					cmdList.add("copy");
					
					cmdList.add("-bsf:a");
					cmdList.add("aac_adtstoasc");
					
					cmdList.add("-y");
					
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  
		  /**
		   * 
		   * @param videoFile
		   * @param cropWidth
		   * @param cropHeight
		   * @param x
		   * @param y
		   * @param dstFile
		   * @return
		   *  ./ffmpeg -i test_720p.mp4 -vf crop=480:480:0:0 -acodec copy testcrop.mp4,暂时使用软编码,后面再优化硬编码
		   */
		  public int executeVideoFrameCrop(String videoFile,int cropWidth,int cropHeight,int x,int y,String dstFile,String codecname)
		  {
			  if(FileUtils.fileExist(videoFile)){
					
					List<String> cmdList=new ArrayList<String>();
					String cropcmd=String.format(Locale.getDefault(),"crop=%d:%d:%d:%d",cropWidth,cropHeight,x,y);
					
					cmdList.add("-vcodec");
					cmdList.add(codecname);
					
					cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-vf");
					cmdList.add(cropcmd);
					
					cmdList.add("-acodec");
					cmdList.add("copy");
					
					cmdList.add("-vcodec");
//					cmdList.add("libx264");
					cmdList.add("lansoh264_enc"); 
					
					cmdList.add("-pix_fmt");  //编码的时候,指定yuv420p
					cmdList.add("yuv420p");
					
					cmdList.add("-y");
					
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  
		 /**
		  * 视频画面缩放, 务必保持视频的缩放后的宽高比,等于原来视频的宽高比.
		  * @param videoFile
		  * @param scaleWidth
		  * @param scaleHeight
		  * @param dstFile
		  * @return
		  * ./ffmpeg -i test_720p.mp4 -vf scale=480:270 -acodec copy scale1.mp4  ,暂时使用软编码,后面再优化硬编码
		  */
		  public int executeVideoFrameScale(String videoFile,int scaleWidth,int scaleHeight,String dstFile){
			  if(FileUtils.fileExist(videoFile)){
					
					List<String> cmdList=new ArrayList<String>();
					String scalecmd=String.format(Locale.getDefault(),"scale=%d:%d",scaleWidth,scaleHeight);
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					
					cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-vf");
					cmdList.add(scalecmd);
					
					cmdList.add("-acodec");
					cmdList.add("copy");
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_enc"); 
					
					cmdList.add("-pix_fmt");  //编码的时候,指定yuv420p
					cmdList.add("yuv420p");
					
					cmdList.add("-y");
					
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 
		   * @param picDir
		   * @param jpgprefix
		   * @param framerate
		   * @param dstPath
		   * @return
		   */
		  //./ffmpeg -framerate 1 -i r5r-%03d.jpeg -c:v libx264 -r 25 -pix_fmt yuv420p out33.mp4
		  public int executeConvertPictureToVideo(String picDir,String jpgprefix,float framerate,String dstPath){
					
			  		String picSet=picDir+jpgprefix+"_%3d.jpeg";
			  
					List<String> cmdList=new ArrayList<String>();
					
					cmdList.add("-framerate");
					cmdList.add(String.valueOf(framerate));
					
					cmdList.add("-i");
					cmdList.add(picSet);

					cmdList.add("-c:v");
//					cmdList.add("libx264");
					cmdList.add("lansoh264_enc"); 
					
					cmdList.add("-r");
					cmdList.add("25");
					
					cmdList.add("-pix_fmt");
					cmdList.add("yuv420p"); 
					
					cmdList.add("-y");
					
					cmdList.add(dstPath);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
		  }
		  
		  //OK, PASS 2016年4月2日12:43:55
		  public int executeTestOverlay()
		  {
			  //./ffmpeg -i /sdcard/miaopai.mp4 -i /sdcard/miaopai_mv.ts -filter_complex 'overlay=main_w-overlay_w-10:main_h-overlay_h-10' -acodec copy -vcodec libx264 ou22.mp4
			  //  -  -  .mp4
				List<String> cmdList=new ArrayList<String>();
				cmdList.add("-vcodec");
				cmdList.add("lansoh264_dec");
				cmdList.add("-i");
				cmdList.add("/sdcard/miaopai.mp4");
				
				cmdList.add("-vcodec");
				cmdList.add("lansoh264_dec");
				
				cmdList.add("-i");
				cmdList.add("/sdcard/dream-480-480.mp4");

				cmdList.add("-filter_complex");
				cmdList.add("overlay=main_w-overlay_w-10:main_h-overlay_h-10");
				
				cmdList.add("-acodec");
				cmdList.add("copy");
				
				cmdList.add("-vcodec");
				cmdList.add("lansoh264_enc");
				
				cmdList.add("-y");
				
				cmdList.add("/sdcard/ou22.mp4");
				String[] command=new String[cmdList.size()];  
			     for(int i=0;i<cmdList.size();i++){  
			    	 command[i]=(String)cmdList.get(i);  
			     }  
			    return  executeVideoEditor(command);
		  }
		  /**
		   * 
		   * @param videoFile
		   * @param imagePngPath
		   * @param x
		   * @param y
		   * @param dstFile
		   * @return
		   */
		  public int executeAddWaterMark(String videoFile,String imagePngPath,int x,int y,String dstFile){
			  //./ffmpeg -i miaopai.mp4 -i watermark.png -filter_complex "overlay=0:0" -acodec copy out2.mp4  
			  //大概6秒钟.
			  //测试两个图片同时增加的版本!!!,一个右上角,另一个在文件最后2秒钟增加.
			  
			  if(FileUtils.fileExist(videoFile)){
					List<String> cmdList=new ArrayList<String>();
					String overlayXY=String.format(Locale.getDefault(),"overlay=%d:%d",x,y);
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					
					cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-i");
					cmdList.add(imagePngPath);

					cmdList.add("-filter_complex");
					cmdList.add(overlayXY);
					
					cmdList.add("-acodec");
					cmdList.add("copy");
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_enc"); 
					
					cmdList.add("-pix_fmt");  //编码的时候,指定yuv420p
					cmdList.add("yuv420p");
					
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /*
		   *测试发现,一个10秒的视频,做透明处理后,大概需要20秒的时间.
	*/
		  public int executeTestOverlay2()
		  {
			  //./ffmpeg -i /sdcard/miaopai.mp4 -i /sdcard/miaopai_mv.ts -filter_complex 'overlay=main_w-overlay_w-10:main_h-overlay_h-10' -acodec copy -vcodec libx264 ou22.mp4
			  //  -  -  .mp4
				List<String> cmdList=new ArrayList<String>();
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					cmdList.add("-i");
					cmdList.add("/sdcard/miaopai.mp4");
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					cmdList.add("-i");
					cmdList.add("/sdcard/dream-480-480.mp4");
					
			  	   cmdList.add("-filter_complex");
			  	   String filter="[1:v] format=rgba,colorchannelmixer=1:0:0:0:0:1:0:0:0:0:1:0:0:0:0:0.3 [a]; [0:v][a] overlay=0:0:eof_action=repeat";
				   cmdList.add(filter);
				   String filename = "/sdcard/VideoDemo1.mp4";
				   cmdList.add("-acodec");
				   cmdList.add("copy");
				   cmdList.add("-vcodec");
				   cmdList.add("lansoh264_enc");
				   cmdList.add("-y");
				   cmdList.add(filename);
				String[] command=new String[cmdList.size()];  
			     for(int i=0;i<cmdList.size();i++){  
			    	 command[i]=(String)cmdList.get(i);  
			     }  
			    return  executeVideoEditor(command);
		  }
		 /**
		  * 在某段时间区间内叠加.
		  * @param videoFile
		  * @param imagePngPath
		  * @param startTimeS
		  * @param endTimeS
		  * @param x
		  * @param y
		  * @param dstFile
		  * @return
		  */
		  public int executeAddWaterMark(String videoFile,String imagePngPath,float startTimeS,float endTimeS,int x,int y,String dstFile)
		  {
			// ./ffmpeg -i miaopai.mp4 -i test.png -filter_complex "[0:v][1:v] overlay=25:25:enable='between(t,0,5)'" -pix_fmt yuv420p -c:a copy output33.mp4
			  if(FileUtils.fileExist(videoFile)){
					List<String> cmdList=new ArrayList<String>();
					String overlayXY=String.format(Locale.getDefault(),"overlay=%d:%d:enable='between(t,%f,%f)",x,y,startTimeS,endTimeS);
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					
					cmdList.add("-i");
					cmdList.add(videoFile);

					cmdList.add("-i");
					cmdList.add(imagePngPath);

					cmdList.add("-filter_complex");
					cmdList.add(overlayXY);
					
					cmdList.add("-acodec");
					cmdList.add("copy");
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_enc"); 
					
					cmdList.add("-pix_fmt");  //编码的时候,指定yuv420p
					cmdList.add("yuv420p");
					
					cmdList.add("-y");
					cmdList.add(dstFile);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  }
		  /**
		   * 
		   * @param cfg
		   * @return
		   */
		  public int executeAddWaterMark(WaterMarkConfig cfg)
		  {
			//./ffmpeg -i miaopai.mp4 -i test.png -i testfade.png -filter_complex "overlay=25:25,overlay=x=if(gt(t\,10)\,0\,NAN ) :y=0" -pix_fmt yuv420p -c:a copy outt3.mp4
			  if(cfg!=null && FileUtils.fileExist(cfg.srcPath)){
					List<String> cmdList=new ArrayList<String>();
					String overlayXY=String.format(Locale.getDefault(),"overlay=%d:%d,overlay=%d:%d:enable='between(t,%f,%f)",
							cfg.logo1X,
							cfg.logo1Y,
							cfg.logo2X,
							cfg.logo2Y,
							cfg.logo2StartTimeS,
							cfg.logo2EndTimeS);
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_dec");
					
					cmdList.add("-i");
					cmdList.add(cfg.srcPath);

					cmdList.add("-i");
					cmdList.add(cfg.logo1);
					cmdList.add("-i");
					cmdList.add(cfg.logo2);

					cmdList.add("-filter_complex");
					cmdList.add(overlayXY);
					
					cmdList.add("-acodec");
					cmdList.add("copy");
					
					cmdList.add("-vcodec");
					cmdList.add("lansoh264_enc"); 
					
					cmdList.add("-pix_fmt");  //编码的时候,指定yuv420p
					cmdList.add("yuv420p");
					
					cmdList.add("-y");
					cmdList.add(cfg.dstPath);
					String[] command=new String[cmdList.size()];  
				     for(int i=0;i<cmdList.size();i++){  
				    	 command[i]=(String)cmdList.get(i);  
				     }  
				    return  executeVideoEditor(command);
			  }else{
				  return VIDEO_EDITOR_EXECUTE_FAILED;
			  }
		  } 
//		  private void demoVideoGray()
//		  {
//			  	List<String> cmdList=new ArrayList<String>();
//		    	cmdList.add("-vcodec");
//		    	cmdList.add("lansoh264_dec");  //使用我们的硬解码加速
//				cmdList.add("-i");
//				cmdList.add(videoPath);
//				cmdList.add("-vf");
//				cmdList.add("format=gray");
//				cmdList.add("-vcodec");
//				cmdList.add("lansoh264_enc"); //使用我们的硬编码加速
//				cmdList.add("-strict");
//				cmdList.add("-2");
//				cmdList.add("-y");
//				cmdList.add("/sdcard/video_demo_gray.mp4");
//				String[] command=new String[cmdList.size()];  
//			     for(int i=0;i<cmdList.size();i++){  
//			    	 command[i]=(String)cmdList.get(i);  
//			     }  
//			     mVideoEditor.executeVideoEditor(command);
//		  }
}