package com.dream.juju;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class CircularLayout extends FrameLayout {
	public static final float PI_2 = (float) (Math.PI * 2.0f);
	
	float radius = 0.0f;
	float[] currentRadius;
	boolean hasCenter;
	View centerView;
	int numChildren = 0;
	
	int width, height;
	float centerX, centerY;

	public CircularLayout(Context context) {
		super(context);
	}

	public CircularLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setChildRadius(int child, float radius) {
		currentRadius[child] = radius;
		updatePositionOfChild(child);
	}

	public void resetChildRadius(int child) {
		currentRadius[child] = radius;
		updatePositionOfChild(child);
	}

	public void init() {
		width = getWidth() - getPaddingLeft() - getPaddingRight();
		height = getHeight() - getPaddingTop() - getPaddingBottom();
		centerX = width / 2.0f;
		centerY = height / 2.0f;
		
		this.radius = Math.min(centerX, centerY) * 0.8f;
		numChildren = getChildCount();
		centerView = null;
		if (getChildAt(numChildren - 1).getId() == R.id.center) {
			centerView = getChildAt(numChildren - 1);
		}
		hasCenter = centerView != null;
		if (hasCenter) {
			numChildren -= 1;
		}
		currentRadius = new float[numChildren];
		for (int i = 0; i < numChildren; i++) {
			currentRadius[i] = radius;
		}
		// Log.d("DEGUPTA", numChildren + "");
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void updatePositionOfAll() {
		for (int i = 0; i < numChildren; i++) {
			updatePositionOfChild(i);
		}
	}

	protected void updatePositionOfChild(int index) {
		View child = getChildAt(index);
		float currentDegree = PI_2 / numChildren * index;
		float x = (float) (currentRadius[index] * Math.cos(currentDegree)) + centerX;
		float y = (float) (-currentRadius[index] * Math.sin(currentDegree)) + centerY;
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) child.getLayoutParams();
		params.setMargins((int) (x - child.getWidth() / 2), (int) (y - child.getHeight() / 2), params.rightMargin, params.leftMargin);
		child.setLayoutParams(params);
		// Log.d("DEGUPTA", index + ":" + (currentDegree * 360.0f / PI_2) + "," + params.leftMargin + "," + params.topMargin);
	}
	
	public void hideAllChildren() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).setVisibility(View.INVISIBLE);
		}
	}
	
	public class IndexAnimationListener implements AnimatorUpdateListener {
		int index;
		public IndexAnimationListener(int index) {
			this.index = index;
		}
		
		public void onAnimationUpdate(ValueAnimator animation) {
			float value = (Float) animation.getAnimatedValue();
			getChildAt(index).setVisibility(View.VISIBLE);
			currentRadius[index] = radius * value;
			getChildAt(index).setAlpha(value);
			updatePositionOfChild(index);
		}
	}
	
	public void animateCircular() {
		hideAllChildren();
		int centerAnimTime = 1000;
		int childAnimTime = 1000;
		int childStaggerTime = 500;
		int delay = hasCenter ? centerAnimTime : 0;
		
		final ValueAnimator childAnims[] = new ValueAnimator[numChildren];

		for (int i = 0; i < numChildren; i++) {
			childAnims[i] = ValueAnimator.ofFloat(0.0f, 1.0f);
			ValueAnimator anim = childAnims[i];
			anim.setInterpolator(new DecelerateInterpolator(1.0f));
			anim.setDuration(childAnimTime);
			anim.addUpdateListener(new IndexAnimationListener(i));
			anim.setStartDelay(delay);
			anim.start();
			delay += childStaggerTime;
		}

		if (hasCenter) {
			ValueAnimator centerAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
			centerAnim.setInterpolator(new DecelerateInterpolator(1.0f));
			centerAnim.setDuration(centerAnimTime);
			centerAnim.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					centerView.setVisibility(View.VISIBLE);
					float value = (Float) animation.getAnimatedValue();
					centerView.setAlpha(value);
				}
			});
			centerAnim.start();
		}
	}
}
