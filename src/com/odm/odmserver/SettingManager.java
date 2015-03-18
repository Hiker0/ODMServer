package com.odm.odmserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingManager {
	final static String TAG = "odmserver/SettingManager";
	final static String PREFERENCE_FILE_NAME = "odmsettings_preference";
	final static String FIRST_LAUNCH_KEY = "first_launch";
	final static String ENABLE_PROTECTOR_KEY = "enable_protector_key";
	final static String PROTECTOR_SOUND_KEY = "protector_sound_key";

	
	
	private Context mContext;
	private static SettingManager mInstance = null;
	private SharedPreferences   mSharedPreferences=null;
	private UpdateMonitor mMonitor;
	
	public SettingManager(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mMonitor = UpdateMonitor.getInstance(context);
		mSharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
		initPreferences();
	}

	public static SettingManager getInstance(Context context){
		if(mInstance == null){
			mInstance= new SettingManager(context);
		}
		
		return mInstance;
	}
	
	private void initPreferences(){
		boolean isfirst=mSharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true);
		if(isfirst){
			Editor pEdit = mSharedPreferences.edit();
			pEdit.putBoolean(FIRST_LAUNCH_KEY, false);
			pEdit.putBoolean(ENABLE_PROTECTOR_KEY, true);
			pEdit.putBoolean(PROTECTOR_SOUND_KEY, false);
			pEdit.commit();
		}
	}
	
	public boolean getProtectorEnabled(){
		boolean enable = mSharedPreferences.getBoolean(ENABLE_PROTECTOR_KEY, true);
		return enable;
	}
	public void setProtectorEnabled(boolean enable){
		Editor pEdit = mSharedPreferences.edit();
		pEdit.putBoolean(ENABLE_PROTECTOR_KEY, enable);
		pEdit.commit();
		notifyPreferencesChange();
	}	

	public boolean getProtectorSoundEnabled(){
		boolean enable = mSharedPreferences.getBoolean(PROTECTOR_SOUND_KEY, false);
		return enable;
	}
	public void setProtectorSoundEnabled(boolean enable){
		Editor pEdit = mSharedPreferences.edit();
		pEdit.putBoolean(PROTECTOR_SOUND_KEY, enable);
		pEdit.commit();
		notifyPreferencesChange();
	}
	
	private void notifyPreferencesChange(){
		mMonitor.dispatchPreferenceChange();
	}
	
	
}
