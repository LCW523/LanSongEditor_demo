package com.example.lansongeditordemo;

import java.io.IOException;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import com.lansosdk.box.ISprite;
import com.lansosdk.box.PlayBoxView;
import com.lansosdk.videoeditor.player.IMediaPlayer;
import com.lansosdk.videoeditor.player.VPlayer;
import com.lansosdk.videoeditor.player.VideoPlayView;
import com.lansosdk.videoeditor.player.IMediaPlayer.OnPreparedListener;
import com.lansosdk.videoeditor.utils.Utils;
import com.lansosdk.videoeditor.utils.snoCrashHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class PlayBoxDemo2Activity extends Activity implements OnClickListener,OnSeekBarChangeListener {
    private static final String TAG = "VideoActivity";

    private String mVideoPath;

    private PlayBoxView mPlayView;
    
    private VPlayer  mVPlayer;
    private VPlayer  mVPlayer2;
    private MediaPlayer mplayer2=null;
    
    private ISprite mAlphaVideoSprite=null;
    private ISprite  mSpriteMain=null;
    private ISprite mBitmapSprite=null;
    
    private TextView tvDuration;
    private boolean mBackPressed;
    
    private SeekBar  skbarRotate;
    private SeekBar  skbarScale;
    private SeekBar  skbarMove;

    public static void intentTo(Context context, String videoPath) {
    	  Intent intent = new Intent(context, PlayBoxDemo2Activity.class);
          intent.putExtra("videoPath", videoPath);
    	context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
		 Thread.setDefaultUncaughtExceptionHandler(new snoCrashHandler());
        setContentView(R.layout.playbox_layout);
        
        mVideoPath = getIntent().getStringExtra("videoPath");
        mPlayView = (PlayBoxView) findViewById(R.id.playbox_view);
        
        
        
        findViewById(R.id.id_playbox_start).setOnClickListener(this);
        findViewById(R.id.id_playbox_pause).setOnClickListener(this);
        
        tvDuration=(TextView)findViewById(R.id.id_playbox_duration);
        
        skbarRotate=(SeekBar)findViewById(R.id.id_playbox_skbar_rotate1);
        skbarRotate.setOnSeekBarChangeListener(this);
        skbarRotate.setMax(360);
      
        skbarMove=(SeekBar)findViewById(R.id.id_playbox_skbar_move1);
        skbarMove.setOnSeekBarChangeListener(this);
        skbarMove.setMax(100);
        
        skbarScale=(SeekBar)findViewById(R.id.id_playbox_skbar_scale1);
        skbarScale.setOnSeekBarChangeListener(this);
        skbarScale.setMax(100);
        
        
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 startPlayVideo();
			}
		}, 100);
    }
    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
	    	case R.id.id_player_start:
	    		mVPlayer.start();
				break;
	    	case R.id.id_player_pause:
	    		mVPlayer.pause();
				break;
			default:
				break;
		}
    }
    private void startPlayVideo()
    {
          if (mVideoPath != null){
        	  mVPlayer=new VPlayer(this);
        	  mVPlayer.setVideoPath(mVideoPath);
              mVPlayer.setOnPreparedListener(new OnPreparedListener() {
    			
    			@Override
    			public void onPrepared(IMediaPlayer mp) {
    				// TODO Auto-generated method stub
    					tvDuration.setText("视频时长:"+Utils.buildTimeMilli(mVPlayer.getDuration()));
    					
    					mSpriteMain=mPlayView.obtainMainVideoSprite(mp.getVideoWidth(),mp.getVideoHeight(),mp.getVideoSarNum(),mp.getVideoSarDen());
    					if(mSpriteMain!=null){
    						mVPlayer.setSurface(new Surface(mSpriteMain.getVideoTexture()));
    					}
    					mVPlayer.start();
    					addBitmapSprite();
    				}
    			});
        	  mVPlayer.prepareAsync();
          }
          else {
              Log.e(TAG, "Null Data Source\n");
              finish();
              return;
          }
    }
    private void addBitmapSprite()
    {
    	mBitmapSprite=mPlayView.obtainBitmapSprite(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
    }
    
    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (mBackPressed) {
        	if(mVPlayer!=null){
        		mVPlayer.stopPlayback();
            	mVPlayer.release();
            	mVPlayer=null;
        	}
            if(mVPlayer2!=null){
            	mVPlayer2.stopPlayback();
            	mVPlayer2.release();
            	mVPlayer2=null;
            }
        	if(mplayer2!=null){
        		mplayer2.stop();
        		mplayer2.release();
        		mplayer2=null;
        	}
        	if(mPlayView!=null){
        		mPlayView.release();
        		mPlayView=null;        		   
        	}
        }
    }
    private float xpos=0,ypos=0;
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(seekBar==skbarRotate){
			if(mBitmapSprite!=null)
				mBitmapSprite.setRotate(progress);
		}
		if(seekBar==skbarMove){
			if(mBitmapSprite!=null){
				 xpos+=10;
				 ypos+=10;
				 
				 if(xpos>mPlayView.getViewWidth())
					 xpos=0;
				 if(ypos>mPlayView.getViewWidth())
					 ypos=0;
				 
				 mBitmapSprite.setPosition(xpos, ypos);
			}
		}
		if(seekBar==skbarScale){
			if(mBitmapSprite!=null){
				mBitmapSprite.setScale(progress);
			}
		}
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
