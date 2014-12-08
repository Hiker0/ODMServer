/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Rewrote by zhouxf in 29/04 2014,
 * added hiden button,changed the position judegement method.
 */

package com.odm.odmserver.hall.floatmenu.note4;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odm.odmserver.R;

public class FloatMenuService extends Service {

	private String TAG = "FloatMenuService";
	
	private static final String SCREEN_SHOT = "android.intent.action.floatmenu.screenshot";
	private static final int ITEM_NUM = 4; 
	
	private static WindowManager.LayoutParams mWindowParams;
	private static WindowManager mWindowManager;

	final double cos220 = Math.cos(210.0f * 3.14f / 180.0f);
	final double cosn2p7 = Math.cos(-2.7f * 3.14f / 180.0f);
	final double cos43 = Math.cos(43.0f * 3.14f / 180.0f);
	final double cos83p8 = Math.cos(83.8f * 3.14f / 180.0f);
	final double cos126 = Math.cos(126.0f * 3.14f / 180.0f);
	final double cos168p8 = Math.cos(168.8f * 3.14f / 180.0f);;
	

	final double cos53 = Math.cos(53.0f * 3.14f / 180.0f);
	final double cos103p8 = Math.cos(103.8f * 3.14f / 180.0f);
	final double cos156 = Math.cos(156.0f * 3.14f / 180.0f);

	
	
	static final long SLIDE_TIME = 300;
	static final int CUSTOM_LOCATION_X = 150;
	static final int CUSTOM_LOCATION_Y = 600;
	private final int MENU_ICON_DURATION = 120;
	private final int MENU_ICON_INTERVAL = 50;  
	private final int START_DELAY = 200;

	protected SoundPool sound = null;
	private static int sCloseSoundId = -1;
	private static int sOpenSoundId = -1;
	private int mStreamSoundId = -1;

	private AbsoluteLayout.LayoutParams lp;

	private AbsoluteLayout contentView;
	private LinearLayout.LayoutParams linearLp;
	private FrameLayout parent;
	ImageView actionMemo = null;
	ImageView scrapBooker = null;
	ImageView screenWrite = null;
	ImageView penWindow = null;
	ImageView centerView = null;
	View[] targetViews;

	LinearLayout actionLabelParent = null;
	TextView actionLabel = null;
	FrameLayout bgView = null;

	private AirAnimationBgView mAirAnimationBgView = null;
	// screen size
	int screenWidth;
	int screenHeight;
	int statusBarHeight;
	int touchSlop;
	// touch point
	private float mLastX = CUSTOM_LOCATION_X;
	private float mLastY = CUSTOM_LOCATION_Y;
	private int bigRadius, smallRadius;
	int touchX;
	int touchY;
	// set below

	boolean isInCircle;
	boolean isDraging = true;
	int curPart = 0;

	static final int[] actionStringId = new int[] { R.string.air_command,
			R.string.action_memo, R.string.scrap_booker,
			R.string.screen_writer, R.string.s_finder, R.string.pen_window };
	Handler mHandler = new Handler();
               
	@Override
	public void onCreate() {
		super.onCreate();

		Log.w(TAG, "onCreate start");
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		statusBarHeight = getStatusBarHeight();
		initSoundPool();
		initExpandView();
      
		
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_LOCALE_CHANGED);
		registerReceiver(localChangedReceiver, intentFilter);

		addExpandView(CUSTOM_LOCATION_X, CUSTOM_LOCATION_Y);
		
		Log.w(TAG, "onCreate end ");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.w(TAG, "onStartCommand end ");
		if ( mAirAnimationBgView != null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playOpen();
				}
			}, 50);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		

		if (mWindowManager != null) {
			mWindowManager.removeView(bgView);

			mWindowManager.removeView(parent);
		}
		if (localChangedReceiver != null) {
			unregisterReceiver(localChangedReceiver);
		}


		if (sound != null) {
			sound.release();
		}
	}

	private void initSoundPool() {
		sound = new SoundPool(1, 1, 0);
		sOpenSoundId = sound.load(this, R.raw.airbutton_open, 1);
		sCloseSoundId = sound.load(this, R.raw.airbutton_close, 1);
	}

	private void initExpandView() {
		bgView = (FrameLayout) LayoutInflater.from(this).inflate(
				R.layout.bgview, null);
		bgView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					switchHiden();
				}

				return true;
			}
		});

		parent = (FrameLayout) LayoutInflater.from(this).inflate(
				R.layout.float_menu_note4, null);
		contentView = (AbsoluteLayout) parent.findViewById(R.id.sector_bg);
		contentView.setWillNotDraw(false);

		actionMemo = (ImageView) contentView.findViewById(R.id.action_memo);
		scrapBooker = (ImageView) contentView.findViewById(R.id.scrap_booker);
		screenWrite = (ImageView) contentView.findViewById(R.id.screen_write);

		penWindow = (ImageView) contentView.findViewById(R.id.pen_window);

		centerView = (ImageView) parent.findViewById(R.id.air_center);
		centerView.setOnTouchListener(innerViewListener);
		
		actionLabelParent = (LinearLayout) contentView
				.findViewById(R.id.circle_layout);

		actionLabel = (TextView) contentView.findViewById(R.id.sector_title);

		mAirAnimationBgView = (AirAnimationBgView) parent
				.findViewById(R.id.air_animate);

		targetViews = new View[] { actionMemo, scrapBooker, screenWrite,
		 penWindow };		
		recordCordinates();

		parent.setOnTouchListener(expandViewListener);
		mWindowParams = new WindowManager.LayoutParams();

	}

	private void recordCordinates() {

		final ViewConfiguration configuration = ViewConfiguration.get(this);
		touchSlop = configuration.getScaledTouchSlop();
		lp = (LayoutParams) actionLabelParent.getLayoutParams();

		linearLp = (android.widget.LinearLayout.LayoutParams) actionLabel
				.getLayoutParams();

	}

	private void switchHiden() {

		playClose();

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mWindowManager = (WindowManager) getApplicationContext()
						.getSystemService("window");

				stopSelf();
			}

		}, 500);
	}


	private void addExpandView(int x, int y) {
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = 40;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;

		mWindowParams.format = PixelFormat.RGBA_8888;

		mWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
		mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		mWindowParams.x = 0;
		mWindowParams.y = 0;
		mWindowManager.addView(bgView, mWindowParams);

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.x = x - parent.getWidth() / 2;
		mWindowParams.y = y - statusBarHeight - parent.getHeight() / 2;
		mWindowManager.addView(parent, mWindowParams);

	}


	private void updateExpandView(int x, int y) {
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");

		mWindowParams.x = x - parent.getWidth() / 2;
		mWindowParams.y = y - statusBarHeight - parent.getHeight() / 2;
		mWindowManager.updateViewLayout(parent, mWindowParams);
	}
	

	
	void playOpenSound() {
		sound.stop(this.mStreamSoundId);
		mStreamSoundId = sound.play(sOpenSoundId, 1.0F, 1.0F, 1, 0, 1.0F);

	}

	void playCloseSound() {
		sound.stop(this.mStreamSoundId);
		mStreamSoundId = sound.play(sCloseSoundId, 1.0F, 1.0F, 1, 0, 1.0F);
	}


	void playOpen() {
		
		actionLabel.setText(getResources().getString(
				actionStringId[0]));
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				playOpenSound();
			}
		});

		if (mAirAnimationBgView != null) {
			mAirAnimationBgView.startOpenAnimation();
		}
		
		for (int i = 0; i < ITEM_NUM; ++i) {
			final View localImageView7 = targetViews[i];
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					localImageView7.setVisibility(View.VISIBLE);
				}
			}, START_DELAY + i * this.MENU_ICON_INTERVAL);

			float[] arrayOfFloat7 = new float[2];
			arrayOfFloat7[0] = 0.0F;
			arrayOfFloat7[1] = 1.0F;
			ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(
					localImageView7, "alpha", arrayOfFloat7);
			localObjectAnimator7.setStartDelay(START_DELAY + i
					* this.MENU_ICON_INTERVAL);
			localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
			localObjectAnimator7.start();
		}
		{
			final LinearLayout localImageView7 = actionLabelParent;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					localImageView7.setVisibility(View.VISIBLE);
				}
			}, START_DELAY + 4 * this.MENU_ICON_INTERVAL);
		}

		{
			View localImageView7 = actionLabel;
			float[] arrayOfFloat7 = new float[2];
			arrayOfFloat7[0] = 0.0F;
			arrayOfFloat7[1] = 1.0F;
			ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(
					localImageView7, "alpha", arrayOfFloat7);
			localObjectAnimator7
					.setStartDelay(START_DELAY + MENU_ICON_INTERVAL);
			localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
			localObjectAnimator7.start();
		}
	}

	void playClose() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				playCloseSound();
			}
		});

		{
			View localImageView7 = actionLabel;
			float[] arrayOfFloat7 = new float[2];
			arrayOfFloat7[0] = 1.0F;
			arrayOfFloat7[1] = 0.0F;
			ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(
					localImageView7, "alpha", arrayOfFloat7);
			localObjectAnimator7.setStartDelay(4 * MENU_ICON_INTERVAL);
			localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
			localObjectAnimator7.start();
		}
		for (int i = ITEM_NUM-1; i >= 0; --i) {
			View localImageView7 = targetViews[i];
			float[] arrayOfFloat7 = new float[2];
			arrayOfFloat7[0] = 1.0F;
			arrayOfFloat7[1] = 0.0F;
			ObjectAnimator localObjectAnimator7 = ObjectAnimator.ofFloat(
					localImageView7, "alpha", arrayOfFloat7);
			localObjectAnimator7.setStartDelay((4 - i)
					* this.MENU_ICON_INTERVAL);
			localObjectAnimator7.setDuration(this.MENU_ICON_DURATION);
			localObjectAnimator7.start();
		}
		if (mAirAnimationBgView != null) {
			mAirAnimationBgView.startCloseAnimation();
		}

	}

	// zxf

	public int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getResources().getDimensionPixelSize(x);
			
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return statusBarHeight;
	}

	private BroadcastReceiver localChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			actionLabel.setText(getResources().getString(
					actionStringId[curPart]));
			mAirAnimationBgView.playSelect(curPart);
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Runnable innerLongClickListener = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			switchHiden();
		}
		
	};
	View.OnTouchListener innerViewListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int mRawX = (int) event.getRawX();
			int mRawY = (int) event.getRawY();

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mLastX = mRawX;
				mLastY = mRawY;
				isDraging = false;
				mHandler.postDelayed(innerLongClickListener, 200);
				break;
			case MotionEvent.ACTION_MOVE:
				if (isDraging || Math.abs(mRawX - mLastX) > touchSlop
						|| Math.abs(mRawY - mLastY) > touchSlop) {
					mLastX = mRawX;
					mLastY = mRawY;
					isDraging = true;
					updateExpandView(mRawX, mRawY);
					mHandler.removeCallbacks(innerLongClickListener);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mHandler.removeCallbacks(innerLongClickListener);
				if (!isDraging) {
					switchHiden();
				}
				isDraging = false;
				break;
			default:
				;
			}

			return true;
		}
	};
	

	View.OnTouchListener expandViewListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			int mRawX = (int) event.getRawX();
			int mRawY = (int) event.getRawY();

			touchX = (int) event.getX();
			touchY = (int) event.getY();

			curPart = witchPartAt(touchX, touchY);
			Log.d(TAG, "part=" + curPart);

			switch (event.getAction() & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_DOWN: {
				if (curPart == 0) {
					mLastX = mRawX;
					mLastY = mRawY;
					switchHiden();
				} else {
					actionLabel.setText(getResources().getString(
							actionStringId[curPart]));
					mAirAnimationBgView.playSelect(curPart);
					mAirAnimationBgView.updateHighlightCircleBg();
				}
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				actionLabel.setText(getResources().getString(
						actionStringId[curPart]));
				mAirAnimationBgView.playSelect(curPart);
				break;
			}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				launchActivity(curPart);
				mAirAnimationBgView.resetHighlightCircleBg();
				actionLabel.setText(getResources().getString(
						actionStringId[0]));
				break;
			default:
				break;
			}
			return true;
		}
	};
	

	private int witchPartAt(float x, float y) {
		bigRadius = parent.getWidth() / 2;

		smallRadius = centerView.getWidth() / 2;

		float dis2 = (x - bigRadius) * (x - bigRadius) + (y - bigRadius)
				* (y - bigRadius);
		float dis = (float) Math.sqrt(dis2);

		boolean isBigCircle = dis2 <= bigRadius * bigRadius;
		boolean isSmallCircle = dis2 <= smallRadius * smallRadius;

		if (!isBigCircle || isSmallCircle) {
			return 0;
		}

		float sinP = -(y - bigRadius) / dis;
		float cosP = -(x - bigRadius) / dis;

		if (sinP < 0) {
			if (cosP < cos220) {
				return 4;
			} else if (cosP > cosn2p7) {
				return 1;
			}
		} else {
			if (cosP > cos53) {
				return 1;
			} else if (cosP > cos103p8) {
				return 2;
			} else if (cosP > cos156) {
				return 3;
			} else {
				return 4;
			}
		}

		return 0;
	}

	private void launchActivity(int currentPoint) {
		Intent intent = null;
		switch (currentPoint) {
		case 1:
            stopSelf();
            intent = new Intent();
            intent.putExtra("location", "");
            intent.putExtra("folder", "");
            intent.putExtra("initpenmode", 1);
            intent.putExtra("isDesktop", true);
            ComponentName componentName = new ComponentName("com.android.smemo", "com.android.smemo.DesktopMemoActivity");
            intent.setComponent(componentName);
            startActivitySafely(intent);
            //TODO
            break;
		case 2:
		    stopSelf();
			screenShot(0);
            break;
		case 3:
		    stopSelf();
			screenShot(1);
            break;

		case 4:

			 stopSelf();
			screenShot(2);
            break;
		default:
			break;
		}
	}
    private void screenShot(int type) {

        Log.w(TAG, "screenShot =" +type);
        if(type == 0 || type == 1){
            Intent inentScrapper = new Intent("com.android.pic.floatwindow");
            inentScrapper.putExtra("pic_show_float", false);
            startService(inentScrapper);
        }
        final Intent intentScreenShot = new Intent(SCREEN_SHOT);
        intentScreenShot.putExtra("spen_cut_type", type);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendBroadcast(intentScreenShot);
            }
        }, 50);

         stopSelf();
    }


	void startActivitySafely(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.d(TAG, " Activity not found intent is " + intent);
		} catch (SecurityException e) {
			// ignore
		}
	}

}
