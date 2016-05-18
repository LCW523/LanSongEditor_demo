
package com.example.lansongeditordemo;


import java.util.Locale;

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


public class VideoFilterDemoActivity extends Activity implements OnClickListener {
    private static final String TAG = "VideoActivity";

    private String mVideoPath;

    private VideoPlayView mVideoView;
    private TextView tvDuration;
    
    private boolean mBackPressed;
    private SeekBar skbarFilterAdjuster;

    public static Intent newIntent(Context context, String videoPath, String videoTitle) {
        Intent intent = new Intent(context, VideoFilterDemoActivity.class);
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
        setContentView(R.layout.filter_player_layout);
        
        mVideoPath = getIntent().getStringExtra("videoPath");
        mVideoView = (VideoPlayView) findViewById(R.id.video_view);
        
        findViewById(R.id.id_player_start).setOnClickListener(this);
        findViewById(R.id.id_player_pause).setOnClickListener(this);
        findViewById(R.id.id_player_btnselectfilter).setOnClickListener(this);
        
        tvDuration=(TextView)findViewById(R.id.id_player_duration);
        
        skbarFilterAdjuster=(SeekBar)findViewById(R.id.id_player_seekbar1);
        skbarFilterAdjuster.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				  if (mFilterAdjuster != null) {
			            mFilterAdjuster.adjust(progress);
			        }
			}
		});
        skbarFilterAdjuster.setMax(100);
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
	    		mVideoView.start();
				break;
	    	case R.id.id_player_pause:
	    		mVideoView.pause();
				break;
	    	case R.id.id_player_btnselectfilter:
	    		selectFilter();
	    		break;
			default:
				break;
		}
    }
    private void startPlayVideo()
    {
          if (mVideoPath != null){
        	  mVideoView.enableFilterEffect(true);
        	  mVideoView.setVideoPath(mVideoPath);
          }
          else {
              Log.e(TAG, "Null Data Source\n");
              finish();
              return;
          }
          
          mVideoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(IMediaPlayer mp) {
				// TODO Auto-generated method stub
					tvDuration.setText("视频时长:"+Utils.buildTimeMilli(mVideoView.getDuration()));
					mVideoView.start();
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
            mVideoView.stopPlayback();
            mVideoView.release();
        }
    }
    private FilterAdjuster mFilterAdjuster;
    private void selectFilter()
    {
    	GPUImageFilterTools.showDialog(this, new OnGpuImageFilterChosenListener() {

            @Override
            public void onGpuImageFilterChosenListener(final GPUImageFilter filter) {
         	   if(mVideoView.switchFilterTo(filter)){
         		   mFilterAdjuster = new FilterAdjuster(filter);

         		    findViewById(R.id.id_player_seekbar1).setVisibility(
         		            mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
         	   }
            }
        });
    }
    
}
