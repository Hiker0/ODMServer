package com.odm.odmserver.ScreenProtector;

import com.odm.odmserver.SettingManager;
import com.odm.odmserver.UpdateMonitor;
import com.odm.odmserver.UpdateMonitorCallback;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenProtector {
	final static String TAG = "odmserver/ScreenProtector";
	
	final static String ACTION = "android.intent.action.PROTECTOR";
	
	Context mContext;
	UpdateMonitor mMonitor;
	SettingManager mSettingManager;
	
	boolean enabled = false;
	
	UpdateMonitorCallback callback = new UpdateMonitorCallback(){

		@Override
		public void onScreenTurnedOff() {
			// TODO Auto-generated method stub
			 Log.d(TAG,"onScreenTurnedOff");
			if(enabled){
				try{
					Intent intent = new Intent();
					intent.setAction(ACTION);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}catch(ActivityNotFoundException e){
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onScreenTurnedOn() {
			// TODO Auto-generated method stub
			super.onScreenTurnedOn();
		}
		
		@Override
		public void onPreferenceChanged() {
			// TODO Auto-generated method stub
			Log.d(TAG,"onPreferenceChanged");
			enabled = mSettingManager.getProtectorEnabled();
			Log.d(TAG,"enabled="+enabled);
		}
		
	};
	
	public ScreenProtector(Context context){
		mContext = context;
		mMonitor = UpdateMonitor.getInstance(context);
		mSettingManager = SettingManager.getInstance(context);
		enabled = mSettingManager.getProtectorEnabled();
		mMonitor.registerCallback(callback);
		
	}
	
	
	
	
}
