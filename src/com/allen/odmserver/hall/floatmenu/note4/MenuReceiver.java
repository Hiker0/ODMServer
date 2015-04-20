/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.allen.odmserver.hall.floatmenu.note4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.FileReader;
import android.util.Log;

public class MenuReceiver extends BroadcastReceiver {
	static final String TAG = "FloatMenu";
    static final String SPEN_ACTION = "android.intent.action.HALL_SPEN";
    static final String SPEN_LAUNCH_ACTION = "android.intent.action.floatmenu_s";
    static final String SPEN_KEY = "hall_spen_state";
    static final int SPEN_PUSH = 0;
    static final int SPEN_PULL = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(TAG,">>>FloatMenuReceiver onReceive");
        Intent floatMenuIntent= new Intent(SPEN_LAUNCH_ACTION);

        if (intent != null && intent.getAction().equals(SPEN_ACTION)) {
            if (getHallPenState() == SPEN_PUSH) {
                context.stopService(floatMenuIntent);
            } else if (getHallPenState() == SPEN_PULL) {
                context.startService(floatMenuIntent);
            }
        }
   
    }
    private int getHallPenState() {
	  int state = 0;
	  String pinStateFilePath = String.format("/sys/devices/platform/Accdet_Driver/driver/tablet_state");
		try{
			FileReader fw = new FileReader(pinStateFilePath);
			state = fw.read();
			int pin_state = Integer.valueOf(state);
			fw.close();
			pin_state = pin_state%2;
			Log.d(TAG, "getHallPenState PIN state for Hall is " + pin_state);
			return pin_state;
		} catch (Exception e) {
			Log.d(TAG, "getHallPenState PIN state for Hall is default");
		}	
		return 0;

    }

}
