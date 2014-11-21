package com.odm.odmserver.hall.floatmenu.note4;

import com.odm.odmserver.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class StartActivity extends Activity {
	final static String TAG = "samsungbar";
	final static String START = "android.intent.action.HALL_SPEN";
	final static String STOP = "com.allen.samsungbar.STOP";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		
		this.setContentView(R.layout.start_activity);
		
		Button mStart=(Button) this.findViewById(R.id.id_start);
		Button mStop=(Button) this.findViewById(R.id.id_stop);
		
		mStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mstartIntent = new Intent();
				
				mstartIntent.setAction(START);
				StartActivity.this.sendBroadcast(mstartIntent);
			}
		});
		
		mStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mstopIntent = new Intent();
				mstopIntent.setAction(STOP);
				StartActivity.this.sendBroadcast(mstopIntent);
			}
		});
		
	}
	

}
