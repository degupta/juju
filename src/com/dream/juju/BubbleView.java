package com.dream.juju;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BubbleView extends View {
	private static class Circle {
		float x, y;
		float radius;
		float speed;
		int color;
	}

	public static final int NUM_CIRCLES = 40;
	public static final float MIN_RADIUS = 0.05f;
	public static final float MAX_RADIUS = 0.25f;
	public static final float MIN_SPEED = 0.05f;
	public static final float MAX_SPEED = 0.15f;
	public static final float MAX_BELOW = 1000.0f;

	public static final float SPEED_DIFF = MAX_SPEED - MIN_SPEED;
	public static final float RADIUS_DIFF = MAX_RADIUS - MIN_RADIUS;

	public static final Circle[] CIRCLES = new Circle[NUM_CIRCLES];
	public static final Paint PAINT = new Paint();

	Runnable animation = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < NUM_CIRCLES; i++) {
				updateCircle(CIRCLES[i], 50);
			}
			invalidate();
			postDelayed(this, 50);
		}
	};

	boolean inited = false;

	public BubbleView(Context context) {
		super(context);
	}

	public BubbleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void init() {
		for (int i = 0; i < NUM_CIRCLES; i++) {
			CIRCLES[i] = new Circle();
			initRandomly(CIRCLES[i]);
		}
		inited = true;
	}

	public void initRandomly(Circle c) {
		float halfWidth = getWidth() / 2.0f;
		c.radius = (float) (halfWidth * Math.random() * RADIUS_DIFF + MIN_RADIUS);
		c.x = (float) ((2 * Math.random() - 1) * halfWidth * MAX_RADIUS + halfWidth);
		c.y = getHeight() + (float) (Math.random() * MAX_BELOW) + c.radius;
		c.speed = -(float) (Math.random() * SPEED_DIFF + MIN_SPEED);
		c.color = Color.argb((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255),
				(int) (Math.random() * 255));
	}

	public void updateCircle(Circle c, float deltaTime) {
		c.y += c.speed * deltaTime;
		if (c.y + c.radius <= 0) {
			initRandomly(c);
		} else {
			c.x += (2 * Math.random() - 1) * getWidth() * 0.001f;
		}
	}

	public void startAnimation() {
		post(animation);
	}

	public void stopAnimation() {
		removeCallbacks(animation);
		setVisibility(View.GONE);
	}

	protected void onDraw(Canvas canvas) {
		if (!inited || getVisibility() != View.VISIBLE) {
			return;
		}
		Circle c;
		int height = getHeight();
		for (int i = 0; i < NUM_CIRCLES; i++) {
			c = CIRCLES[i];
			if (c.y - c.radius >= height) {
				continue;
			}
			PAINT.setColor(c.color);
			canvas.drawCircle(c.x, c.y, c.radius, PAINT);
		}
	}
}
