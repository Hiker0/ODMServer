package com.allen.odmserver.hall.floatmenu.note4;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.allen.odmserver.R;

public class AirButtonGlobalMenuBgImageView extends ImageView
{


private final int ANIMATION_DURATION = 1000;
  private final float ARC_BASE_ANGLE = 80.0F;
  private final float ARC_MAX_ANGLE = -223.0F;
  private final String TAG = "AirButtonGlobalMenuBgImageView";
  private ObjectAnimator mArcAnimator = null;
  private Path mArcPath = null;
  private Context mContext = null;
  private float mCurrentAnimatingArc =ARC_MAX_ANGLE;
  private RectF mImageBoundary = null;
  private float mImageCenterX = 0.0F;
  private float mImageCenterY = 0.0F;


  public AirButtonGlobalMenuBgImageView(Context context)
  {
    super(context);
    this.mContext = context;
    super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    this.mArcAnimator = new ObjectAnimator();
    this.mArcAnimator.setTarget(this);
    this.mArcAnimator.setPropertyName("animationArc");
  }
  
  public AirButtonGlobalMenuBgImageView(Context context, AttributeSet attrs) {
	  

		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	    this.mContext = context;
	    super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	    super.setImageResource(R.drawable.airbutton_global_bg);
	    this.mArcAnimator = new ObjectAnimator();
	    this.mArcAnimator.setTarget(this);
	    this.mArcAnimator.setPropertyName("animationArc");
	}
  
  public AirButtonGlobalMenuBgImageView(Context context, AttributeSet attrs,int x) {
	  

		super(context, attrs,x);
		// TODO Auto-generated constructor stub
		
	    this.mContext = context;
	    super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	    super.setImageResource(R.drawable.airbutton_global_bg);
	    this.mArcAnimator = new ObjectAnimator();
	    this.mArcAnimator.setTarget(this);
	    this.mArcAnimator.setPropertyName("animationArc");
	}
  

  private void initVariables()
  {
    if (this.mImageBoundary == null)
      this.mImageBoundary = new RectF(-100.0F, -100.0F, 100 + super.getWidth(), 100 + super.getWidth());
    float f = super.getWidth() / 2;
    this.mImageCenterY = f;
    this.mImageCenterX = f;
    this.mArcPath = new Path();
    Log.v("airbutton", "AirButtonGlobalMenuBgImageView initVariables mImageCenterY=" + this.mImageCenterY + ", mImageCenterX=" + this.mImageCenterX);
    Log.v("airbutton", "AirButtonGlobalMenuBgImageView initVariables super.getWidth()=" + super.getWidth());
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if ((this.mImageBoundary == null) || (this.mArcPath == null)){
    	return;
    }
      
      this.mArcPath.reset();
      this.mArcPath.moveTo(this.mImageCenterX, this.mImageCenterY);
      this.mArcPath.arcTo(this.mImageBoundary, ARC_BASE_ANGLE, this.mCurrentAnimatingArc);
      this.mArcPath.close();
      paramCanvas.clipPath(this.mArcPath, Region.Op.DIFFERENCE);
      super.onDraw(paramCanvas);

  }

  public void setAnimationArc(float paramFloat)
  {
    this.mCurrentAnimatingArc = paramFloat;
    super.invalidate();
  }

  public void startCloseAnimation(int paramInt, Animator.AnimatorListener paramAnimatorListener)
  {
    initVariables();
    Log.v("airbutton", "startCloseAnimation");
    if (this.mArcAnimator.isRunning())
      this.mArcAnimator.cancel();
    this.mArcAnimator.setStartDelay(paramInt);
    this.mArcAnimator.setDuration(ANIMATION_DURATION);
    ObjectAnimator localObjectAnimator = this.mArcAnimator;
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = 0.0F;
    arrayOfFloat[1] = -303.0F;
    localObjectAnimator.setFloatValues(arrayOfFloat);
    this.mArcAnimator.removeAllListeners();
    if (paramAnimatorListener != null)
      this.mArcAnimator.addListener(paramAnimatorListener);
    this.mArcAnimator.start();
  }

  public void startOpenAnimation()
  {
    initVariables();
    if (this.mArcAnimator.isRunning())
      this.mArcAnimator.cancel();
    this.mArcAnimator.setStartDelay(0L);
    this.mArcAnimator.setDuration(ANIMATION_DURATION);
    ObjectAnimator localObjectAnimator = this.mArcAnimator;
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = -303.0F;
    arrayOfFloat[1] = 0.0F;
    localObjectAnimator.setFloatValues(arrayOfFloat);
    this.mArcAnimator.start();
  }
}

