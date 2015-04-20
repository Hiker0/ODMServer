package com.allen.odmserver.ScreenProtector;

import com.allen.odmserver.SettingManager;
import com.allen.odmserver.UpdateMonitor;
import com.allen.odmserver.UpdateMonitorCallback;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenProtector {
	final static String TAG = "odmserver/ScreenProtector";
	
	final static String ACTION = "android.intent.action.PROTECTOR";
	final static String ACTION_HORIZONTAL = "android.intent.action.PROTECTOR_HORIZONTAL";
	
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
					if(mSettingManager.getProtectorHorizontal()){
						intent.setAction(ACTION_HORIZONTAL); 
					}else{
						intent.setAction(ACTION);
					}
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
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
		public void onPreferenceChanged(String key) {
			// TODO Auto-generated method stub
			Log.d(TAG,"onPreferenceChanged:"+key);
			if(key.equals(SettingManager.ENABLE_PROTECTOR_KEY)){
			enabled = mSettingManager.getProtectorEnabled();
			Log.d(TAG,"enabled="+enabled);
			}
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
