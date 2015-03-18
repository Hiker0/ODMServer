package com.odm.odmserver;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.preference.PreferenceScreen;

public class OdmServiceSetting extends PreferenceActivity {
	final static String TAG = "odmserver/OdmServiceSetting";
	
	final static String ENABLE_PROTECTOR_KEY = "screen_protect_preference";
	final static String PROTECTOR_SOUND_KEY = "enable_sound_preference";
	
	 private SettingManager mSettingManager = null;
	 private CheckBoxPreference mProtectorPreference;
	 private CheckBoxPreference mSoundPreference;
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
        
        if(mSettingManager.getProtectorSoundEnabled()){
        	mSoundPreference.setChecked(true);
        }
        
        if(! mSettingManager.getProtectorEnabled()){
        	mProtectorPreference.setChecked(false);
        	mSoundPreference.setEnabled(false);
        }else{
        	mProtectorPreference.setChecked(true);
        }
        
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if(preference == mProtectorPreference){
			if(mProtectorPreference.isChecked()){
				mSoundPreference.setEnabled(true);
			}else{
				mSoundPreference.setEnabled(false);
			}
			
			mSettingManager.setProtectorEnabled(mProtectorPreference.isChecked());
			
		}else if(preference == mSoundPreference){
			mSettingManager.setProtectorSoundEnabled(mSoundPreference.isChecked());
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
}
