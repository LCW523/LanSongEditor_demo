package com.example.lansongeditordemo;

import java.io.File;


import com.lansosdk.videoeditor.LanSoEditor;
import com.lansosdk.videoeditor.player.VideoPlayer;
import com.lansosdk.videoeditor.utils.snoCrashHandler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{


	 private static final String TAG="MainActivity";
	 
	 
	EditText etVideoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		 Thread.setDefaultUncaughtExceptionHandler(new snoCrashHandler());
        setContentView(R.layout.activity_main);
        
        LanSoEditor.initSo();
        
        
        etVideoPath=(EditText)findViewById(R.id.id_main_etvideo);
        etVideoPath.setText("/sdcard/2x.mp4");
        
        findViewById(R.id.id_main_demofilter).setOnClickListener(this);
        findViewById(R.id.id_main_demoedit).setOnClickListener(this);
        findViewById(R.id.id_main_twovideooverlay).setOnClickListener(this);
        findViewById(R.id.id_main_videobitmapoverlay).setOnClickListener(this);
        findViewById(R.id.id_main_twovideoplay).setOnClickListener(this);
        
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showHintDialog();
			}
		}, 500);
    }
    
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	LanSoEditor.unInitSo();
    }
    
    
    private boolean checkPath(){
    	if(etVideoPath.getText()!=null && etVideoPath.getText().toString().isEmpty()){
    		Toast.makeText(MainActivity.this, "请输入视频地址", Toast.LENGTH_SHORT).show();
    		return false;
    	}	
    	else{
    		String path=etVideoPath.getText().toString();
    		if((new File(path)).exists()==false){
    			Toast.makeText(MainActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
    			return false;
    		}else{
    			return true;
    		}
    	}
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(checkPath()==false)
			return;
		switch (v.getId()) {
		case R.id.id_main_demofilter:
			startVideoFilterDemo();
			break;
		case R.id.id_main_demoedit:
			startVideoEditDemo();
			break;
		case R.id.id_main_twovideooverlay:
			startTwoVideoOverlayDemo();
			break;
		case R.id.id_main_videobitmapoverlay:
			startVideoBitmapDemo();
			break;
		case R.id.id_main_twovideoplay:
			startTwoVideoPlayDemo();
			break;
		default:
			break;
		}
	}
	   private void startVideoFilterDemo()
	    {
	    			String path=etVideoPath.getText().toString();
	    			VideoFilterDemoActivity.intentTo(MainActivity.this, path, path);
	    }
	   private void startTwoVideoPlayDemo()
	    {
	    			String path=etVideoPath.getText().toString();
	    			TwoVideoPlayActivity.intentTo(MainActivity.this, path, path);
	    }
		private void startTwoVideoOverlayDemo()
	    {
	    	String path=etVideoPath.getText().toString();
	    	Intent intent=new Intent(MainActivity.this,PlayBoxHintActivity.class);
	    	intent.putExtra("videopath", path);
	    	intent.putExtra("Activity_name", "PlayBoxDemoActivity");
	    	startActivity(intent);
	    }
		private void startVideoBitmapDemo()
	    {
	    	String path=etVideoPath.getText().toString();
	    	Intent intent=new Intent(MainActivity.this,PlayBoxHintActivity.class);
	    	intent.putExtra("videopath", path);
	    	intent.putExtra("Activity_name", "PlayBoxDemo2Activity");
	    	startActivity(intent);
	    }
	   	private void startVideoEditDemo()
	    {
	    	String path=etVideoPath.getText().toString();
	    	Intent intent=new Intent(MainActivity.this,VideoEditDemoActivity.class);
	    	intent.putExtra("videopath", path);
	    	startActivity(intent);
	    }
	   	
	   private void showHintDialog()
		{
			new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("本SDK版本是2016518, 不免费,大概每三天会更新一次,欢迎联系我们!!")
	        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
	        .show();
		}

}
