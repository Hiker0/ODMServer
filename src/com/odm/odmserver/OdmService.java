package com.odm.odmserver;

import com.odm.odmserver.ScreenProtector.ScreenProtector;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class OdmService extends Service {
	final static String TAG = "odmserver/OdmService";
	UpdateMonitor mMonitor;
	SettingManager mSettingManager;
	ScreenProtector mScreenProtector;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG,"onCreate");
		
		mMonitor = UpdateMonitor.getInstance(this);
		mSettingManager = SettingManager.getInstance(this);
		mScreenProtector = new ScreenProtector(this); 
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onStartCommand");
		return super.onStartCommand(intent, flags, startId);
		
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
