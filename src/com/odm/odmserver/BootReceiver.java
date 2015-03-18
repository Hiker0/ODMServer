package com.odm.odmserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	 static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION))    
        {   
				Intent odmIntent = new Intent(context, OdmService.class);
                context.startService(odmIntent);  
        }
	}
}
