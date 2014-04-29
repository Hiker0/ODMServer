package com.odm.odmserver.hall.floatmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.odm.odmserver.R;

public class AirAnimationBgView extends FrameLayout {
	
	Context mContext = null;
	  private int CENTER_IMAGE_DURATION = 600;
	  private int DESCRIPTION_DURATION = 150;
	  private int FOCUS_DURATION = 200;
	  private int MENU_ICON_DURATION = 120;
	  private int MENU_ICON_INTERVAL = 50;
	  private int START_DELAY = 0;
	  
		protected AirButtonGlobalMenuBgImageView bgView = null;
		protected ImageView selectView = null;
		protected ImageView centerView = null;
		protected ImageView shadowView = null;

		private ObjectAnimator mMoveFocusAnimation = new ObjectAnimator();
	Handler mHandler = new Handler();

	public AirAnimationBgView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mContext = context;
	}
	
	public AirAnimationBgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}



	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		initViews();
	
	}
	
	void initViews(){
		
		bgView = (AirButtonGlobalMenuBgImageView) this.findViewById(R.id.air_bg);
		selectView=(ImageView) this.findViewById(R.id.air_select);
		centerView = (ImageView) this.findViewById(R.id.air_center);
		shadowView =(ImageView) this.findViewById(R.id.air_shadow);
		/*
		bgView= new AirButtonGlobalMenuBgImageView(mContext);
		bgView.setImageResource(R.drawable.airbutton_global_bg);
		
		centerView=new ImageView(mContext);
		centerView.setImageResource(R.drawable.airbutton_global_center);
		shadowView=new ImageView(mContext);
		shadowView.setImageResource(R.drawable.airbutton_global_center_shadow);
		
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;

		bgView.setLayoutParams(params);
		
		this.addView(bgView);	
		
		centerView.setLayoutParams(params);
		
		this.addView(centerView);	
		
		shadowView.setLayoutParams(params);
		
		this.addView(shadowView);*/
	}
	

	
//	public void setSelect(int resid){
//		selectView.setBackgroundResource(resid);
//	}
	
	public void playSelect(int select) {

		if(select < 1){
			selectView.setVisibility(View.GONE);
			return;
		}
		float rotationFloat = -2.7F;
		switch (select) {
		case -1:
			break;
		case 0:
			break ;
		case 1:
			rotationFloat = -2.7F;
			break;
		case 2:
			rotationFloat = 40.299999F;
			break;
		case 3:
			rotationFloat = 83.800003F;
			break;
		case 4:
			rotationFloat = 126.0F;
			break;
		case 5:
			rotationFloat = 168.8F;
			break;
		default:
			break;
		}
		selectView.setRotation(rotationFloat);
		selectView.setVisibility(View.VISIBLE);
		/*
		if ((mMoveFocusAnimation != null) && (mMoveFocusAnimation.isRunning())){
			mMoveFocusAnimation.cancel();
		}
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = selectView.getRotation();
		arrayOfFloat[1] = rotationFloat;
		mMoveFocusAnimation = ObjectAnimator.ofFloat(selectView, "rotation",
				arrayOfFloat);
		mMoveFocusAnimation.setDuration(-50 + this.FOCUS_DURATION);
		mMoveFocusAnimation.start();*/
	}
	
	
	void startOpenAnimation(){

		
		selectView.setVisibility(View.GONE);
		bgView.startOpenAnimation();
		
		float[] arrayOfFloat1 = new float[2];
	    arrayOfFloat1[0] = 0.0F;
	    arrayOfFloat1[1] = 1.0F;
	    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(shadowView, "scaleX", arrayOfFloat1);

	    float[] arrayOfFloat2 = new float[2];
	    arrayOfFloat2[0] = 0.0F;
	    arrayOfFloat2[1] = 1.0F;
	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(shadowView, "scaleY", arrayOfFloat2);

	    float[] arrayOfFloat3 = new float[2];
	    arrayOfFloat3[0] = 0.0F;
	    arrayOfFloat3[1] = 1.0F;
	    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(shadowView, "alpha", arrayOfFloat3);
	    
	    AnimatorSet localAnimatorSet1 = new AnimatorSet();
	    Animator[] arrayOfAnimator1 = new Animator[3];
	    arrayOfAnimator1[0] = localObjectAnimator1;
	    arrayOfAnimator1[1] = localObjectAnimator2;
	    arrayOfAnimator1[2] = localObjectAnimator3;
	    localAnimatorSet1.playTogether(arrayOfAnimator1);
	    localAnimatorSet1.setStartDelay(this.START_DELAY);
	    localAnimatorSet1.setDuration(this.CENTER_IMAGE_DURATION);
	   // localAnimatorSet1.addListener(this.mStartAnimationListener);
	    localAnimatorSet1.start();
	    shadowView.setVisibility(View.VISIBLE);
	    

	    float[] arrayOfFloat4 = new float[2];
	    arrayOfFloat4[0] = 0.0F;
	    arrayOfFloat4[1] = 1.0F;
	    ObjectAnimator localObjectAnimator4 = ObjectAnimator.ofFloat(centerView, "scaleX", arrayOfFloat4);
	    float[] arrayOfFloat5 = new float[2];
	    arrayOfFloat5[0] = 0.0F;
	    arrayOfFloat5[1] = 1.0F;
	    ObjectAnimator localObjectAnimator5 = ObjectAnimator.ofFloat(centerView, "scaleY", arrayOfFloat5);
	    float[] arrayOfFloat6 = new float[2];
	    arrayOfFloat6[0] = 0.0F;
	    arrayOfFloat6[1] = 1.0F;
	    ObjectAnimator localObjectAnimator6 = ObjectAnimator.ofFloat(centerView, "alpha", arrayOfFloat6);
	    AnimatorSet localAnimatorSet2 = new AnimatorSet();
	    Animator[] arrayOfAnimator2 = new Animator[3];
	    arrayOfAnimator2[0] = localObjectAnimator4;
	    arrayOfAnimator2[1] = localObjectAnimator5;
	    arrayOfAnimator2[2] = localObjectAnimator6;
	    localAnimatorSet2.playTogether(arrayOfAnimator2);
	    localAnimatorSet2.setStartDelay(this.START_DELAY);
	    localAnimatorSet2.setDuration(this.CENTER_IMAGE_DURATION);
	    //localAnimatorSet2.addListener(this.mStartAnimationListener);
	    localAnimatorSet2.start();
	    centerView.setVisibility(View.VISIBLE);
	
	}
	
	void startCloseAnimation(){
		
		selectView.setVisibility(View.GONE);
	
	    float[] arrayOfFloat1 = new float[2];
	    arrayOfFloat1[0] = 1.0F;
	    arrayOfFloat1[1] = 0.0F;
	    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(shadowView, "scaleX", arrayOfFloat1);

	    float[] arrayOfFloat2 = new float[2];
	    arrayOfFloat2[0] = 1.0F;
	    arrayOfFloat2[1] = 0.0F;
	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(shadowView, "scaleY", arrayOfFloat2);

	    float[] arrayOfFloat3 = new float[2];
	    arrayOfFloat3[0] = 1.0F;
	    arrayOfFloat3[1] = 0.0F;
	    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(shadowView, "alpha", arrayOfFloat3);
	    
	    AnimatorSet localAnimatorSet1 = new AnimatorSet();
	    Animator[] arrayOfAnimator1 = new Animator[3];
	    arrayOfAnimator1[0] = localObjectAnimator1;
	    arrayOfAnimator1[1] = localObjectAnimator2;
	    arrayOfAnimator1[2] = localObjectAnimator3;
	    localAnimatorSet1.playTogether(arrayOfAnimator1);
	    localAnimatorSet1.setDuration(this.CENTER_IMAGE_DURATION);
	    localAnimatorSet1.start();

	    float[] arrayOfFloat4 = new float[2];
	    arrayOfFloat4[0] = 1.0F;
	    arrayOfFloat4[1] = 0.0F;
	    ObjectAnimator localObjectAnimator4 = ObjectAnimator.ofFloat(centerView, "scaleX", arrayOfFloat4);

	    float[] arrayOfFloat5 = new float[2];
	    arrayOfFloat5[0] = 1.0F;
	    arrayOfFloat5[1] = 0.0F;
	    ObjectAnimator localObjectAnimator5 = ObjectAnimator.ofFloat(centerView, "scaleY", arrayOfFloat5);

	    float[] arrayOfFloat6 = new float[2];
	    arrayOfFloat6[0] = 1.0F;
	    arrayOfFloat6[1] = 0.0F;
	    ObjectAnimator localObjectAnimator6 = ObjectAnimator.ofFloat(centerView, "alpha", arrayOfFloat6);
	    
	    AnimatorSet localAnimatorSet2 = new AnimatorSet();
	    Animator[] arrayOfAnimator2 = new Animator[3];
	    arrayOfAnimator2[0] = localObjectAnimator4;
	    arrayOfAnimator2[1] = localObjectAnimator5;
	    arrayOfAnimator2[2] = localObjectAnimator6;
	    localAnimatorSet2.playTogether(arrayOfAnimator2);
	    localAnimatorSet2.setDuration(this.CENTER_IMAGE_DURATION);
	    localAnimatorSet2.start();
	    
		bgView.startCloseAnimation(this.START_DELAY / 2,null );
	}
}
