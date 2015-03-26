package com.odm.odmserver.ScreenFilter;

import com.odm.odmserver.SettingManager;
import com.odm.odmserver.UpdateMonitor;
import com.odm.odmserver.UpdateMonitorCallback;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenFilter {
	final static String TAG = "odmserver/ScreenProtector";
	
	final static String ACTION = "android.intent.action.PROTECTOR";
	
	Context mContext;
	UpdateMonitor mMonitor;
	SettingManager mSettingManager;
	
	boolean enabled = false;
	
	UpdateMonitorCallback callback = new UpdateMonitorCallback(){

		
		@Override
		public void onPreferenceChanged() {
			// TODO Auto-generated method stub
			Log.d(TAG,"onPreferenceChanged");
			enabled = mSettingManager.getProtectorEnabled();
			Log.d(TAG,"enabled="+enabled);
		}
		
	};
	
	public ScreenFilter(Context context){
		mContext = context;
		mMonitor = UpdateMonitor.getInstance(context);
		mSettingManager = SettingManager.getInstance(context);
		enabled = mSettingManager.getProtectorEnabled();
		mMonitor.registerCallback(callback);
		
	}
	
	
	
	
}
