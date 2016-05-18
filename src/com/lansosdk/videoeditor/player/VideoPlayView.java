
package com.lansosdk.videoeditor.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TableLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.lansosdk.box.VideoTextureRenderer;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;



public class VideoPlayView extends FrameLayout {
    private String TAG = "LanSoSDK";
    private Uri mUri;
    private Map<String, String> mHeaders;

    // all possible internal states
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;   //没有stop类型,因为stop就是release
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private int mCurrentState = STATE_IDLE;

    // All the stuff we need for playing and showing a video
    private IMediaPlayer mMediaPlayer = null;
//    private MediaPlayer player2 = null;
    // private int         mAudioSession;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mVideoRotationDegree;
    
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private int mCurrentBufferPercentage;
    
    
    private int mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;

    private Context mAppContext;
    private TextureRenderView mTextureRenderView;
    private int mVideoSarNum;
    private int mVideoSarDen;


    public VideoPlayView(Context context) {
        super(context);
        initVideoView(context);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVideoView(context);
    }


    private void initVideoView(Context context) {
        mAppContext = context.getApplicationContext();
        initBackground();
        setTextureView();

        mVideoWidth = 0;
        mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
    }
    private void setTextureView() {
                TextureRenderView renderView = new TextureRenderView(getContext());
                renderView.setSurfaceTextureListener(new SurfaceCallback());
                if (mMediaPlayer != null) {
                    renderView.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
                    renderView.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
                    renderView.setAspectRatio(mCurrentAspectRatio);
                }
                setTextureRenderView(renderView);
    }
    private void setTextureRenderView(TextureRenderView renderView) {
        if (mTextureRenderView != null) {
            View renderUIView = mTextureRenderView.getView();
            mTextureRenderView = null;
            removeView(renderUIView);
        }

        if (renderView == null)
            return;

        mTextureRenderView = renderView;
        renderView.setAspectRatio(mCurrentAspectRatio);
        if (mVideoWidth > 0 && mVideoHeight > 0)
            renderView.setVideoSize(mVideoWidth, mVideoHeight);
        if (mVideoSarNum > 0 && mVideoSarDen > 0)
            renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);

        View renderUIView = mTextureRenderView.getView();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,//<--------------需要调整这里视频的宽度
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        renderUIView.setLayoutParams(lp);
        addView(renderUIView);
        mTextureRenderView.setVideoRotation(mVideoRotationDegree);
    }
	
    /**
     * 
     * TextureView的回调,当SurfaceTexture 有效时的一些动作,主要是renderer是否可以.
     *
     */
	private class SurfaceCallback implements SurfaceTextureListener {
    			
    	        @Override
    	        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    	            mSurfaceTexture = surface;
    	        	if(filterEnabled && renderer==null){
//    	        		if(filterEncodeEnable && filterEncodePath!=null){
//
//    	        			if(mEncoderConfig!=null)
//    	        				renderer = new VideoTextureRenderer(getContext(),surface, mSurfaceWidth, mSurfaceHeight,mEncoderConfig);
//    	        		}
//    	        		else
    	        			renderer = new VideoTextureRenderer(getContext(),surface, mSurfaceWidth, mSurfaceHeight);
//    	        		renderer = new VideoTextureRenderGLCORE(getContext(),surface, mSurfaceWidth, mSurfaceHeight,null);
    	        		
    	        	}
    	        	mSurfaceWidth=width;
    	        	mSurfaceHeight=height;
    	        }
    	
    	        @Override
    	        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    	            mSurfaceTexture = surface;
    	            if(filterEnabled && renderer!=null)
    	            	renderer.setViewPortSize(width, height);
    	        }
    	
    	        @Override
    	        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    	            mSurfaceTexture = null;
    	            return false;
    	        }
    	
    	        @Override
    	        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    	        }
    }
    
    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
    	if(mCurrentState == STATE_IDLE){
            setVideoURI(Uri.parse(path),null);
    	}
    }
    public void enableFilterEffect(boolean en){
    	filterEnabled=en;
    	if(en && mSurfaceTexture!=null){
    		 if(renderer==null)
    			 renderer = new VideoTextureRenderer(getContext(),mSurfaceTexture, mSurfaceWidth, mSurfaceHeight);
//    			 renderer = new VideoTextureRenderGLCORE(getContext(),mSurfaceTexture, mSurfaceWidth, mSurfaceHeight,null);
    	}
    }
    /**
     * 使能 滤镜效果,并且同步对画面编码.
     * @param encodeOutputPath
     */
//    public void enableFilterEffectAndEncode(String encodeOutputPath){
//    	filterEnabled=true;
//    	filterEncodeEnable=true;
//    	filterEncodePath=encodeOutputPath;
//    }
    public boolean  switchFilterTo(final GPUImageFilter filter) {
    	if(renderer!=null)
    		return renderer.switchFilterTo(filter);
    	else
    		return false;
    }
    
    private VideoTextureRenderer renderer;
//    private VideoTextureRenderGLCORE renderer;
	private SurfaceTexture mSurfaceTexture=null;
	private boolean filterEnabled=false;
	
    private void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    private void openVideo() {
        if (mUri == null) {
        	Log.e(TAG,"mUri==mull, open video error.");
            return;
        }
        if (filterEnabled==false && mSurfaceTexture == null) {
        	Log.e(TAG,"surface view is not ready, open video error!");
            return;
        }
        if(filterEnabled && renderer==null){
        	Log.e(TAG,"opengl render is not ready, open video error!");
            return;
        }
        	
        AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        try {
            mMediaPlayer = createPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            
                mMediaPlayer.setDataSource(mAppContext, mUri);
//            
            if(filterEnabled  && renderer!=null)  
            	mMediaPlayer.setSurface(new Surface(renderer.getVideoTexture()));
            else if(!filterEnabled)
            	mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
//            
            //只是测试.
//            player2=new MediaPlayer();
//            player2.setDataSource("/sdcard/miaopai_baojing.mp4");
//            player2.prepare();
//            player2.setSurface(new Surface(renderer.getVideoTexture1()));
          
            
            
            	
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }

    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new IMediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    mVideoSarNum = mp.getVideoSarNum();
                    mVideoSarDen = mp.getVideoSarDen();
                    if (mVideoWidth != 0 && mVideoHeight != 0) {
                        if (mTextureRenderView != null) {
                            mTextureRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                            mTextureRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                        }
                        requestLayout();
                    }
                }
            };

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            mCurrentState = STATE_PREPARED;

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            
            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            
            if (mVideoWidth != 0 && mVideoHeight != 0) {
            	//在这里调整大小.
                if (mTextureRenderView != null) {
                    mTextureRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                    mTextureRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                }
            }
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                }
   };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                            mVideoRotationDegree = arg2;
                            Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                            if (mTextureRenderView != null)
                                mTextureRenderView.setVideoRotation(arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                            Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    Log.d(TAG, "Error: " + framework_err + "," + impl_err);
                    mCurrentState = STATE_ERROR;
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };

  
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
//        if(player2!=null){
//        	player2.stop();
//        	player2.release();
//        	player2=null;
//        }
        
        if (renderer != null)
            renderer.onPause();
        
          filterEnabled=false;
    }
    public void start() {
        if (isInPlaybackState()) {
            
        	mMediaPlayer.start();
//            player2.start();
            
            mCurrentState = STATE_PLAYING;
        }else if(mUri!=null && mCurrentState==STATE_IDLE){
        	setVideoURI(mUri, null);
        }
    }

    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
    }
    
    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }
    public void setLooping(boolean looping){
    	if(mMediaPlayer!=null)
    		mMediaPlayer.setLooping(looping);
    }
    
    public   boolean isLooping(){
    	 return (mMediaPlayer!=null) ? mMediaPlayer.isLooping(): false;
    }
    
    public void setVolume(float leftVolume, float rightVolume){
    	if(mMediaPlayer!=null)
    		mMediaPlayer.setVolume(leftVolume, rightVolume);
    }
    
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }

        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }
    public int getVideoWidth()
    {
    	return mMediaPlayer!=null? mMediaPlayer.getVideoWidth():0;
    }
    public int getVideoHeight()
    {
    	return mMediaPlayer!=null? mMediaPlayer.getVideoHeight():0;
    }

    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public boolean canPause() {
        return mCanPause;
    }

    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }
    private static final int[] s_allAspectRatio = {
            IRenderView.AR_ASPECT_FIT_PARENT,
            IRenderView.AR_ASPECT_FILL_PARENT,
            IRenderView.AR_ASPECT_WRAP_CONTENT,
            IRenderView.AR_16_9_FIT_PARENT,
            IRenderView.AR_4_3_FIT_PARENT};
    private int mCurrentAspectRatioIndex = 0;
    private int mCurrentAspectRatio = s_allAspectRatio[0];

    public int toggleAspectRatio() {
        mCurrentAspectRatioIndex++;
        mCurrentAspectRatioIndex %= s_allAspectRatio.length;

        mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
        if (mTextureRenderView != null)
            mTextureRenderView.setAspectRatio(mCurrentAspectRatio);
        return mCurrentAspectRatio;
    }

    private IMediaPlayer createPlayer() {
        IMediaPlayer mediaPlayer = null;


        VideoPlayer retPlayer = null;
                if (mUri != null) {
                    retPlayer = new VideoPlayer();

                        retPlayer.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                            retPlayer.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
                    
                        retPlayer.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
                                            retPlayer.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "overlay-format", VideoPlayer.SDL_FCC_RV32);
                   
                                            retPlayer.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
                    retPlayer.setOption(VideoPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
                    retPlayer.setOption(VideoPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
                    retPlayer.setOption(VideoPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
                }
                mediaPlayer = retPlayer;
        return mediaPlayer;
    }

    //-------------------------
    // Extend: Background
    //-------------------------

    private boolean mEnableBackgroundPlay = false;

    private void initBackground() {
//        mEnableBackgroundPlay = mSettings.getEnableBackgroundPlay();
//        if (mEnableBackgroundPlay) {
//            MediaPlayerService.intentToStart(getContext());
//            mMediaPlayer = MediaPlayerService.getMediaPlayer();
//        }
    }

    public boolean isBackgroundPlayEnabled() {
        return mEnableBackgroundPlay;
    }

    /**
     * 可以获取mediaPlayer,然后如果后台操作,则可以把MediaPlayer放到service中进行.
     * @return
     */
    public IMediaPlayer getMediaPlayer()
    {
    	return mMediaPlayer;
    }

    private String buildResolution(int width, int height, int sarNum, int sarDen) {
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append(" x ");
        sb.append(height);

        if (sarNum > 1 || sarDen > 1) {
            sb.append("[");
            sb.append(sarNum);
            sb.append(":");
            sb.append(sarDen);
            sb.append("]");
        }

        return sb.toString();
    }

    private String buildTimeMilli(long duration) {
        long total_seconds = duration / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
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
