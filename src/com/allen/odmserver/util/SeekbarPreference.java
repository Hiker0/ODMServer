package com.allen.odmserver.util;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.allen.odmserver.R;

public class SeekbarPreference extends Preference implements OnSeekBarChangeListener {
	TextView mTitle;
	TextView mContent;
	SeekBar mSeekbar;
	int mProcess=100;
	
	public SeekbarPreference(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public SeekbarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater  inflater=LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.seekbar_preference_layout, null);
		mTitle = (TextView) view.findViewById(R.id.preference_title);
		mContent= (TextView) view.findViewById(R.id.preference_content);
		mSeekbar = (SeekBar) view.findViewById(R.id.preference_seekbar);

		return view;
	}

	private void initViews(){
		mTitle.setText(getTitle());
		mSeekbar.setOnSeekBarChangeListener(this);
		updateProcess(mProcess);
	}
	
	private void updateProcess(int process){
		if(mSeekbar!=null){
			mSeekbar.setProgress(process);
		}
		String content=getContext().getString(R.string.percent, process);
		if(mContent!=null){
			mContent.setText(content);
		}
	}
	
	public void setValue(int process){
		mProcess = process;
		updateProcess(mProcess);
	}
	public int getValue(){
		
		return mProcess;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if(mTitle!=null){
			mTitle.setEnabled(enabled);
		}
		if(mContent!=null){
			mContent.setEnabled(enabled);
		}
		if(mSeekbar!=null){
			mSeekbar.setEnabled(enabled);
		}		
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
		if(mTitle!=null){
			mTitle.setText(getTitle());
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		mProcess=mSeekbar.getProgress();
		String content=getContext().getString(R.string.percent, mProcess);
		mContent.setText(content);
		callChangeListener(progress);
	}

	
	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		
		super.onBindView(view);
		initViews();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	

}
