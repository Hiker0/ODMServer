package com.odm.odmserver;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class OdmServiceSetting extends PreferenceActivity {
	final static String TAG = "odmserver/OdmServiceSetting";
	
	final static String FILE_SELECTOR_ACTION =  "android.intent.action.FILE_SELECTOR";
	final static String ENABLE_PROTECTOR_KEY = "screen_protect_preference";
	final static String PROTECTOR_SOUND_KEY = "enable_sound_preference";
	final static String PROTECTOR_SOURCE_KEY = "protector_source_preference";
	
	final static String FILE_PATH = "file_path";
	final static int REQUEST_CODE = 100;
	
	 private SettingManager mSettingManager = null;
	 private CheckBoxPreference mProtectorPreference;
	 private CheckBoxPreference mSoundPreference;
	 private Preference mSourcePreference;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent odmIntent = new Intent(this, OdmService.class);
        this.startService(odmIntent); 
        
        mSettingManager = SettingManager.getInstance(this);
        addPreferencesFromResource(R.xml.setting_preference);
        
        mProtectorPreference = (CheckBoxPreference) findPreference(ENABLE_PROTECTOR_KEY);
        mSoundPreference = (CheckBoxPreference) findPreference(PROTECTOR_SOUND_KEY);
        mSourcePreference = findPreference(PROTECTOR_SOURCE_KEY);
        
        if(mSettingManager.getProtectorSourcePath() != null){
        	mSourcePreference.setSummary(mSettingManager.getProtectorSourcePath());
        }
        
        if(mSettingManager.getProtectorSoundEnabled()){
        	mSoundPreference.setChecked(true);
        }
        
        if(! mSettingManager.getProtectorEnabled()){
        	mProtectorPreference.setChecked(false);
        	mSoundPreference.setEnabled(false);
        	mSourcePreference.setEnabled(false);
        }else{
        	mProtectorPreference.setChecked(true);
        	mSourcePreference.setEnabled(true);
        }
        
        
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if(preference == mProtectorPreference){
			if(mProtectorPreference.isChecked()){
				mSoundPreference.setEnabled(true);
				mSourcePreference.setEnabled(true);
			}else{
				mSoundPreference.setEnabled(false);
				mSourcePreference.setEnabled(false);
			}
			
			mSettingManager.setProtectorEnabled(mProtectorPreference.isChecked());
			
		}else if(preference == mSoundPreference){
			mSettingManager.setProtectorSoundEnabled(mSoundPreference.isChecked());
		}else if(preference == mSourcePreference){
			Intent intent = new Intent();
			intent.setAction(FILE_SELECTOR_ACTION);
			this.startActivityForResult(intent, REQUEST_CODE);
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
    	   String path = data.getStringExtra(FILE_PATH);
    	   mSettingManager.setProtectorSource(path);
    	   mSourcePreference.setSummary(path);
       }
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
}
