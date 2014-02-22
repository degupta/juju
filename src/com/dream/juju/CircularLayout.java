package com.dream.juju;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class CircularLayout extends FrameLayout {
	public static final float PI_2 = (float) (Math.PI * 2.0f);

	float radius = 0.0f;
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

	public void init() {
		width = getWidth() - getPaddingLeft() - getPaddingRight();
		height = getHeight() - getPaddingTop() - getPaddingBottom();
		centerX = width / 2.0f;
		centerY = height / 2.0f;

		this.radius = Math.min(centerX, centerY)
				- Math.max(getChildAt(0).getWidth(), getChildAt(0).getHeight());
		numChildren = getChildCount();
		centerView = null;
		if (getChildAt(numChildren - 1).getId() == R.id.center) {
			centerView = getChildAt(numChildren - 1);
		}
		hasCenter = centerView != null;
		if (hasCenter) {
			numChildren -= 1;
		}
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void hideAllChildren() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).setVisibility(View.INVISIBLE);
		}
	}

	public void animateCircular() {
		setVisibility(View.VISIBLE);
		hideAllChildren();
		int centerAnimTime = 1000;
		int childAnimTime = 2000;
		int delay = hasCenter ? centerAnimTime : 0;

		for (int i = 0; i < numChildren; i++) {
			View child = getChildAt(i);
			child.setVisibility(View.VISIBLE);
			child.setX(centerX - child.getWidth() / 2);
			child.setY(centerY - child.getHeight() / 2);
			child.setAlpha(0.0f);
			child.setScaleX(0.0f);
			child.setScaleY(0.0f);
			float currentDegree = PI_2 / numChildren * i;
			float x = (float) (radius * Math.cos(currentDegree)) + centerX
					- child.getWidth() / 2;
			float y = (float) (radius * Math.sin(currentDegree)) + centerY
					- child.getHeight() / 2;
			child.animate().alpha(1.0f).x(x).y(y).setDuration(childAnimTime)
					.setStartDelay(delay).scaleX(1.0f).scaleY(1.0f)
					.setInterpolator(new DecelerateInterpolator(1.0f)).start();
		}

		if (hasCenter) {
			centerView.setVisibility(View.VISIBLE);
			centerView.setAlpha(0.0f);
			centerView.setScaleX(0.0f);
			centerView.setScaleY(0.0f);
			centerView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f)
					.setDuration(centerAnimTime)
					.setInterpolator(new DecelerateInterpolator(1.0f)).start();
		}
	}
}
