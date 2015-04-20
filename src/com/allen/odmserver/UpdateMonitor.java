package com.allen.odmserver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UpdateMonitor {
	final static String TAG = "odmserver/UpdateMonitor";
	
	
    private static final int MSG_TIME_UPDATE = 301;
    private static final int MSG_BATTERY_UPDATE = 302;
    private static final int MSG_BOOT_COMPLETED = 303;
    private static final int MSG_SCREEN_TURNED_ON = 304;   
    private static final int MSG_SCREEN_TURNED_OFF = 305;
    private static final int MSG_PREFENCE_CHANGE = 306;
    
    Context mContext;
    
    
	public static UpdateMonitor mInstance = null;
	private final ArrayList<WeakReference<UpdateMonitorCallback>> mCallbacks;
	
	
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCREEN_TURNED_OFF:
                    handleScreenTurnedOff();
                    break;
                case MSG_SCREEN_TURNED_ON:
                    handleScreenTurnedOn();
                    break;
                case MSG_PREFENCE_CHANGE:
                	String key = (String) msg.obj;
                	handlePreferenceChanged( key);
                default: break;
            }
        }
	};
	
	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			 final String action = intent.getAction();
			 Log.d(TAG,"onReceive action:"+action );
			 if (Intent.ACTION_SCREEN_OFF.equals(action)){
				 mHandler.sendEmptyMessage(MSG_SCREEN_TURNED_OFF);
			 }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
				 mHandler.sendEmptyMessage(MSG_SCREEN_TURNED_ON);
			 }
		}
		
	};
	
	public UpdateMonitor(Context context){
		
		 Log.d(TAG," create UpdateMonitor " );
		 
        if(mContext != null){
        	cleanUp();
        }     
        
		mContext = context;
		mInstance = this;
		mCallbacks = new ArrayList<WeakReference<UpdateMonitorCallback>>();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        

        context.registerReceiver(mBroadcastReceiver, filter);

	}
	
	public void cleanUp(){
		Log.d(TAG,"cleanUp " );
		mContext.unregisterReceiver(mBroadcastReceiver);
		 for (int i = mCallbacks.size() - 1; i >= 0; i--) {
			 	mCallbacks.remove(i);
	     }
	}
	
	public static UpdateMonitor getInstance(Context context){
		if(mInstance == null && context instanceof Service){
			mInstance= new UpdateMonitor(context);
		}
		
		return mInstance;
	}
    public void removeCallback(UpdateMonitorCallback callback) {
    	
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
            if (mCallbacks.get(i).get() == callback) {
                mCallbacks.remove(i);
            }
        }
    }
    
    public void registerCallback(UpdateMonitorCallback callback) {
        // Prevent adding duplicate callbacks
        for (int i = 0; i < mCallbacks.size(); i++) {
            if (mCallbacks.get(i).get() == callback) {
                return;
            }
        }
        mCallbacks.add(new WeakReference<UpdateMonitorCallback>(callback));
        removeCallback(null); 
        sendUpdates(callback);

    }
    
    public void dispatchPreferenceChange(String key){
    	Message msg = mHandler.obtainMessage(MSG_PREFENCE_CHANGE, key);
    	mHandler.sendMessage(msg);
    }
    
    private void sendUpdates(UpdateMonitorCallback callback) {
        // Notify listener of the current state
    }
    
    
	private void handleScreenTurnedOn(){
		 for (int i = 0; i < mCallbacks.size(); i++) {
			 UpdateMonitorCallback cb = mCallbacks.get(i).get();
	         if(cb != null){
	        	 cb.onScreenTurnedOn();
	         }
	     }
	}
	
	private void handleScreenTurnedOff(){
		 for (int i = 0; i < mCallbacks.size(); i++) {
			 UpdateMonitorCallback cb = mCallbacks.get(i).get();
	         if(cb != null){
	        	 cb.onScreenTurnedOff();
	         }
	     }
	}

	private void handlePreferenceChanged(String key){
		 for (int i = 0; i < mCallbacks.size(); i++) {
			 UpdateMonitorCallback cb = mCallbacks.get(i).get();
	         if(cb != null){
	        	 cb.onPreferenceChanged(key);
	         }
	     }
	}
	
}
