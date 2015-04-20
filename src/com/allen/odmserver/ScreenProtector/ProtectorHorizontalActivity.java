package com.allen.odmserver.ScreenProtector;

import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.allen.odmserver.R;
import com.allen.odmserver.SettingManager;
import com.allen.odmserver.util.FileType;

public class ProtectorHorizontalActivity extends Activity implements Callback,
		OnTouchListener {
	final static String TAG = "odmserver/ProtectorActivity";
	final static String VIDEO_PATH = "/sdcard/screen_protector.3gp";
	private WakeLock mWakeLock;
	private SurfaceView mSurfaceView;
	private MediaPlayer mMediaPlayer;
	private SurfaceHolder mHolder;
	private SettingManager mSettingManager;
	private String sourcePath = null;
	private ImageView photoView = null;
	private Bitmap bitmap = null;
	private Point outSize = new Point();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setWindow();

		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		this.setContentView(R.layout.screen_protector_layout);
		

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
		photoView = (ImageView) findViewById(R.id.photo_view);
		
		Display display = this.getWindowManager().getDefaultDisplay();
		display.getSize(outSize);
		
		mSettingManager = SettingManager.getInstance(this);
		sourcePath = mSettingManager.getProtectorSourcePath();
		
		showPictureOrVideo();
		
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
		mWakeLock.acquire();
	}

	private void showPictureOrVideo(){
		
		int type = FileType.getFileType(sourcePath);
		Log.d(TAG, "sourcePath:"+sourcePath+" type:"+type);
		
		if(type == FileType.FILE_TYPE_VIDEO){
			mSurfaceView.setVisibility(View.VISIBLE);
			photoView.setVisibility(View.GONE);
			initMedia();
		}else if(type == FileType.FILE_TYPE_IMAGE){
			mSurfaceView.setVisibility(View.GONE);
			photoView.setVisibility(View.VISIBLE);
			initPhoto();
		}else{
			mSurfaceView.setVisibility(View.GONE);
			photoView.setVisibility(View.VISIBLE);
		}
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	private void setWindow() {
		// getWindow().setType(
		// WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);

		int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_FULLSCREEN;

		final WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.flags |= flags;
		getWindow().setAttributes(lp);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	private void initPhoto(){
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(sourcePath, opts);
		
		int wRatio = (int)Math.ceil(opts.outWidth/outSize.x);
		int hRatio = (int)Math.ceil(opts.outHeight/outSize.y);
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				opts.inSampleSize = wRatio;
			} else {
				opts.inSampleSize = hRatio;
			}
		}
		opts.inJustDecodeBounds = false;
			  
		bitmap = BitmapFactory.decodeFile(sourcePath, opts);
		Drawable drawable = new BitmapDrawable(this.getResources(),bitmap);
		photoView.setImageDrawable(drawable);
		photoView.setFocusable(true);
		photoView.setOnTouchListener(this);
		
	}
	
	private void initMedia() {

		mMediaPlayer = new MediaPlayer();

		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		mMediaPlayer.setLooping(true);
		

		if(! mSettingManager.getProtectorSoundEnabled()){
			mMediaPlayer.setVolume(0, 0);
		}
		
		mSurfaceView.setOnTouchListener(this);
		mHolder = mSurfaceView.getHolder();
		mHolder.setFixedSize(outSize.x,outSize.y);
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}


	private void playVideo() {
		Log.d(TAG, "playVideo");

		mMediaPlayer.setDisplay(mHolder);
		try {
			mMediaPlayer.setDataSource(sourcePath);
			mMediaPlayer.prepare();;
			mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWakeLock.release();

		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		
		if(bitmap!=null){
			bitmap.recycle();
		}
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceCreated called");
		playVideo();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceChanged called");

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "surfaceDestroyed called");
		//finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
