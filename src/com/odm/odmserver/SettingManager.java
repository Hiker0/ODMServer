package com.odm.odmserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SettingManager {
	final static String TAG = "odmserver/SettingManager";
	final static String PREFERENCE_FILE_NAME = "odmsettings_preference";
	final static String DEFAULT_VIDEO_PATH = "/sdcard/screen_protector.3gp";
	
	final static String FIRST_LAUNCH_KEY = "first_launch";
	final static String ENABLE_PROTECTOR_KEY = "enable_protector_key";
	final static String PROTECTOR_SOUND_KEY = "protector_sound_key";
	final static String PROTECTOR_SOURCE = "protector_source";
	
	final static String ENABLE_FILTER_KEY = "enable_screen_filter";
	final static String ENABLE_FILTER_STATUSBAR_KEY = "enable_filter_statusbar";
	final static String FILTER_LEVEL_KEY = "filter_levels";
	
	private Context mContext;
	private static SettingManager mInstance = null;
	private SharedPreferences   mSharedPreferences=null;
	private UpdateMonitor mMonitor;
	
	public SettingManager(Context context) {
		// TODO Auto-generated constructor stub
		Log.d(TAG, "On Create");
		
		mContext = context;
		mInstance = this;
		mMonitor = UpdateMonitor.getInstance(context);
		mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
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
			pEdit.putString(PROTECTOR_SOURCE, DEFAULT_VIDEO_PATH);
			
			pEdit.putBoolean(ENABLE_FILTER_KEY, false);
			pEdit.putBoolean(ENABLE_FILTER_STATUSBAR_KEY, false);
			pEdit.putInt(FILTER_LEVEL_KEY, 100);
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
	
	public String getProtectorSourcePath(){
		return mSharedPreferences.getString(PROTECTOR_SOURCE, DEFAULT_VIDEO_PATH);
	}
	public void setProtectorSource(String  path){
		Editor pEdit = mSharedPreferences.edit();
		pEdit.putString(PROTECTOR_SOURCE, path);
		pEdit.commit();
		notifyPreferencesChange();
	}	
	
	private void notifyPreferencesChange(){
		mMonitor.dispatchPreferenceChange();
	}

	public boolean getFilterEnable(){
		boolean enable = mSharedPreferences.getBoolean(ENABLE_FILTER_KEY, true);
		return enable;
	}
	public void setFilterEnabled(boolean enable){
		Editor pEdit = mSharedPreferences.edit();
		pEdit.putBoolean(ENABLE_FILTER_KEY, enable);
		pEdit.commit();
		notifyPreferencesChange();
	}	
	public boolean getFilterStatusbarEnable(){
		boolean enable = mSharedPreferences.getBoolean(ENABLE_FILTER_STATUSBAR_KEY, true);
		return enable;
	}
	public void setFilterStatusbarEnabled(boolean enable){
		Editor pEdit = mSharedPreferences.edit();
		pEdit.putBoolean(ENABLE_FILTER_STATUSBAR_KEY, enable);
		pEdit.commit();
		notifyPreferencesChange();
	}		
	public int getFilterLevel(){
		int level = mSharedPreferences.getInt(FILTER_LEVEL_KEY, 100);
		return level;
	}
	public void setFilterLevel(int level){
		Editor pEdit = mSharedPreferences.edit();
		pEdit.putInt(FILTER_LEVEL_KEY, level);
		pEdit.commit();
		notifyPreferencesChange();
	}		
}
