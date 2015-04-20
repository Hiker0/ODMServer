package com.allen.odmserver.ballmenu;

import com.allen.odmserver.SettingManager;
import com.allen.odmserver.UpdateMonitor;
import com.allen.odmserver.UpdateMonitorCallback;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BallMenuManager {
	final static String TAG = "odmserver/BallMenuManager";
	
	
	Context mContext;
	UpdateMonitor mMonitor;
	SettingManager mSettingManager;
	
	boolean enabled = false;
	boolean isShow = false;
	boolean expended = false;
	
	UpdateMonitorCallback callback = new UpdateMonitorCallback(){

		@Override
		public void onPreferenceChanged(String key) {
			// TODO Auto-generated method stub

		}
		
	};
	
	public BallMenuManager(Context context){
		mContext = context;
		mMonitor = UpdateMonitor.getInstance(context);
		mSettingManager = SettingManager.getInstance(context);
		enabled = mSettingManager.getBallMenuEnabled();
		mMonitor.registerCallback(callback);
		
	}
	
	public void showUI(){
		if(enabled && !isShow){
			isShow = true;
			
		}
	}
	
	public void hideUI(){
		if(enabled && isShow){
			isShow = false;
		}
	}
	
	public void expendMenu(){
		
	}
	
	public void hideMenu(){
		
	}
	
}
