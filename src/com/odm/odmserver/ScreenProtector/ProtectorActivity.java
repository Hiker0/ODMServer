package com.odm.odmserver.ScreenProtector;

import com.odm.odmserver.R;
import com.odm.odmserver.SettingManager;

import android.app.Activity;
import android.graphics.Point;
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

public class ProtectorActivity extends Activity implements Callback,
		OnTouchListener {
	final static String TAG = "odmserver/ProtectorActivity";
	final static String VIDEO_PATH = "/sdcard/screen_protector.3gp";
	private WakeLock mWakeLock;
	private SurfaceView mSurfaceView;
	private MediaPlayer mMediaPlayer;
	private SurfaceHolder mHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setWindow();

		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		this.setContentView(R.layout.screen_protector_layout);
		
		
		initMedia();

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
		mWakeLock.acquire();
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

	private void initMedia() {
		Display display = this.getWindowManager().getDefaultDisplay();
		Point outSize = new Point();
		display.getSize(outSize);

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
		mSurfaceView.setOnTouchListener(this);
		mHolder = mSurfaceView.getHolder();

		mHolder.setFixedSize(outSize.x, outSize.y);
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mMediaPlayer = new MediaPlayer();

		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		mMediaPlayer.setLooping(true);
		
		SettingManager mSettingManager = SettingManager.getInstance(this);
		if(! mSettingManager.getProtectorSoundEnabled()){
			mMediaPlayer.setVolume(0, 0);
		}
	}

	private void playVideo() {
		Log.d(TAG, "playVideo");

		mMediaPlayer.setDisplay(mHolder);
		try {
			mMediaPlayer.setDataSource(VIDEO_PATH);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWakeLock.release();

		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
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
		finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
