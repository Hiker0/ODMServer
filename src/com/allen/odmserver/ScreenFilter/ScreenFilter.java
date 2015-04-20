package com.allen.odmserver.ScreenFilter;

import com.allen.odmserver.SettingManager;
import com.allen.odmserver.UpdateMonitor;
import com.allen.odmserver.UpdateMonitorCallback;

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
		public void onPreferenceChanged(String key) {
			// TODO Auto-generated method stub
			Log.d(TAG,"onPreferenceChanged:"+key);
			if(key.equals(SettingManager.ENABLE_FILTER_KEY)){
				enabled = mSettingManager.getProtectorEnabled();
				Log.d(TAG,"enabled="+enabled);
			}
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
