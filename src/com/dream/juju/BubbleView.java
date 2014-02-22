package com.dream.juju;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BubbleView extends View {
	private static class Circle {
		float x, y;
		float radius;
		int color;
	}

	private static class CircleGroup {
		Circle[] circles;
		float y;
	}

	public static final int TIME_STEP = 50;
	public static final int NUM_CIRCLE_GROUPS = 4;
	public static final int NUM_CIRCLES_PER_GROUP = 30;
	public static final int NUM_SUB_GROUPS = 5;
	public static final float MIN_RADIUS = 50f;
	public static final float MAX_RADIUS = 150f;
	public static final float SPEED = -0.1f;
	public static final float GROUP_HEIGHT = 1000.0f;

	public static final float RADIUS_DIFF = MAX_RADIUS - MIN_RADIUS;

	public static final CircleGroup[] CIRCLE_GROUPS = new CircleGroup[NUM_CIRCLE_GROUPS];
	public static final Paint PAINT = new Paint();

	Runnable animation = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
				updateGroup(CIRCLE_GROUPS[i], TIME_STEP);
			}
			invalidate();
			postDelayed(this, TIME_STEP);
		}
	};

	boolean inited = false;

	CircleGroup bottomMostGroup = null;

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
		CircleGroup g;
		float height = getHeight();
		for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
			g = new CircleGroup();
			g.circles = new Circle[NUM_CIRCLES_PER_GROUP];
			resetGroup(g, height + i * GROUP_HEIGHT);
			CIRCLE_GROUPS[i] = g;
		}
		bottomMostGroup = CIRCLE_GROUPS[NUM_CIRCLE_GROUPS - 1];
		inited = true;
	}

	public void resetGroup(CircleGroup g, float y) {
		g.y = y;
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int color = 0x00000000 | (red << 16) | (green << 8) | blue;

		float halfWidth = getWidth() / 2.0f;
		Circle[] circles = g.circles;
		int len = circles.length;
		for (int i = 0; i < len; i++) {
			Circle c = circles[i];
			if (c == null) {
				circles[i] = new Circle();
				c = circles[i];
			}
			c.color = color
					| ((((int) (Math.random() * 255)) << 24) & 0xFF000000);
			c.radius = (float) (Math.random() * RADIUS_DIFF + MIN_RADIUS);
			c.x = (float) ((2 * Math.random() - 1) * halfWidth * 0.1f + halfWidth);
			c.y = (float) (Math.random() * GROUP_HEIGHT);
		}
	}

	public void updateGroup(CircleGroup g, float deltaTime) {
		g.y += SPEED * deltaTime;
		if (g.y + GROUP_HEIGHT <= 0) {
			resetGroup(g, bottomMostGroup.y + GROUP_HEIGHT);
			bottomMostGroup = g;
		} else {
			int width = getWidth();
			Circle[] circles = g.circles;
			int len = circles.length;
			for (int i = 0; i < len; i++) {
				circles[i].x += (2 * Math.random() - 1) * width * 0.001f;
			}
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
		CircleGroup g;
		int height = getHeight();
		for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
			g = CIRCLE_GROUPS[i];
			Circle[] circles = g.circles;
			int len = circles.length;
			for (int j = 0; j < len; j++) {
				c = circles[j];
				float y = g.y + c.y;
				if (y - c.radius >= height || y + c.radius <= 0) {
					continue;
				}
				PAINT.setColor(c.color);
				canvas.drawCircle(c.x, y, c.radius, PAINT);
			}
		}
	}
}
