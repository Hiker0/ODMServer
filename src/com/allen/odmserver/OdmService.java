package com.allen.odmserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.allen.odmserver.ScreenProtector.ScreenProtector;
import com.allen.odmserver.ballmenu.BallMenuManager;

public class OdmService extends Service {
	final static String TAG = "odmserver/OdmService";
	UpdateMonitor mMonitor;
	SettingManager mSettingManager;
	ScreenProtector mScreenProtector;
	BallMenuManager mBallMenuManager;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG,"onCreate");
		
		mMonitor = UpdateMonitor.getInstance(this);
		mSettingManager = SettingManager.getInstance(this);
		
		
		if(ODMServer.BALL_MENU_SUPPORT){
			mBallMenuManager= new BallMenuManager(this);
		}
		
		if(ODMServer.SCREEN_PROTECTOR_SUPPORT){
			mScreenProtector = new ScreenProtector(this); 
		}
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onStartCommand");
		if(ODMServer.BALL_MENU_SUPPORT){
			mBallMenuManager.showUI();
		}
		
		return super.onStartCommand(intent, flags, startId);
		
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		if(ODMServer.BALL_MENU_SUPPORT){
			mBallMenuManager.hideUI();
		}
		
		super.onDestroy();
		Log.d(TAG,"onDestroy");
		
		
	}

	IODMserver.Stub mIbinder = new IODMserver.Stub(){

		@Override
		public void disableMenus() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void closeMenus() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispathPreferenceChanged(String key) {
			// TODO Auto-generated method stub
			mMonitor.dispatchPreferenceChange(key);
		}
		
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mIbinder;
	}

}
