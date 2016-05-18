package com.example.lansongeditordemo;


import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import com.example.lansongeditordemo.GPUImageFilterTools.FilterAdjuster;
import com.example.lansongeditordemo.GPUImageFilterTools.OnGpuImageFilterChosenListener;
import com.lansosdk.videoeditor.player.IMediaPlayer;
import com.lansosdk.videoeditor.player.VideoPlayView;
import com.lansosdk.videoeditor.player.IMediaPlayer.OnPreparedListener;
import com.lansosdk.videoeditor.utils.Utils;
import com.lansosdk.videoeditor.utils.snoCrashHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class TwoVideoPlayActivity extends Activity implements OnClickListener {
    private static final String TAG = "VideoActivity";

    private String mVideoPath;

    private VideoPlayView mVideoView1;
    private VideoPlayView mVideoView2;
//    private TextView tvDuration;
    
    private boolean mBackPressed;

    public static Intent newIntent(Context context, String videoPath, String videoTitle) {
        Intent intent = new Intent(context, TwoVideoPlayActivity.class);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoTitle", videoTitle);
        return intent;
    }
    public static void intentTo(Context context, String videoPath, String videoTitle) {
        context.startActivity(newIntent(context, videoPath, videoTitle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
		 Thread.setDefaultUncaughtExceptionHandler(new snoCrashHandler());
        setContentView(R.layout.two_video_play_layout);
        
        mVideoPath = getIntent().getStringExtra("videoPath");
        mVideoView1 = (VideoPlayView) findViewById(R.id.id_two_player_demo_view1);
        mVideoView2 = (VideoPlayView) findViewById(R.id.id_two_player_demo_view2);
        
//        findViewById(R.id.id_player_demo_start).setOnClickListener(this);
//        findViewById(R.id.id_player_demo_pause).setOnClickListener(this);
        
//        tvDuration=(TextView)findViewById(R.id.id_player_demo_duration);
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
	    		mVideoView1.start();
				break;
	    	case R.id.id_player_pause:
	    		mVideoView1.pause();
				break;
			default:
				break;
		}
    }
    private void startPlayVideo()
    {
          if (mVideoPath != null){
        	  mVideoView1.setVideoPath(mVideoPath);
          }
          else {
              Log.e(TAG, "Null Data Source\n");
              finish();
              return;
          }
          
          mVideoView1.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(IMediaPlayer mp) {
				// TODO Auto-generated method stub
					//tvDuration.setText("视频时长:"+Utils.buildTimeMilli(mVideoView1.getDuration()));
					mVideoView1.start();
				}
			});
          
          
          mVideoView2.setVideoPath(mVideoPath);
          mVideoView2.setOnPreparedListener(new OnPreparedListener() {
  			
  			@Override
  			public void onPrepared(IMediaPlayer mp) {
  				// TODO Auto-generated method stub
  					mVideoView2.start();
  				}
  			});
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
        	
        	if(mVideoView1!=null){
        		  mVideoView1.stopPlayback();
                  mVideoView1.release();
                  mVideoView1=null;
        	}
        	
        	if(mVideoView2!=null){
        		mVideoView2.stopPlayback();
                mVideoView2.release();
                mVideoView2=null;
        	}
        }
    }

    
}
