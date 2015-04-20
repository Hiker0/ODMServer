package com.allen.odmserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WelcomeActivity extends Activity {
	final static String TAG = "odmserver/WelcomeActivity";
	Handler mHandler = new Handler();
	Runnable mStartRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(UpdateMonitor.getInstance(WelcomeActivity.this) != null){
				Log.d(TAG,"Start OdmServiceSetting");
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, OdmServiceSetting.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}else{
				mHandler.post(mStartRunnable);
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent odmIntent = new Intent(this, OdmService.class);
        this.startService(odmIntent); 
		mHandler.post(mStartRunnable);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
