package com.dream.juju;

import java.util.ArrayList;

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
		CircleGroup group;
		int colorIndex;
	}

	private static class CircleGroup {
		Circle[] circles;
		int order;
		ArrayList<Integer> colors;
	}

	public static final int TIME_STEP = 50;
	public static final int NUM_CIRCLE_GROUPS = 4;
	public static final int NUM_CIRCLES_PER_GROUP = 20;
	public static final float MIN_RADIUS = 50f;
	public static final float MAX_RADIUS = 150f;
	public static final float SPEED = 0.1f;
	public static final float GROUP_HEIGHT = 1000.0f;

	public static final float RADIUS_DIFF = MAX_RADIUS - MIN_RADIUS;

	public static final CircleGroup[] CIRCLE_GROUPS = new CircleGroup[NUM_CIRCLE_GROUPS];
	public static final Paint PAINT = new Paint();

	Runnable animation = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
				Circle[] circles = CIRCLE_GROUPS[i].circles;
				int len = circles.length;
				for (int j = 0; j < len; j++) {
					updateCircle(circles[j], TIME_STEP);
				}
			}
			invalidate();
			postDelayed(this, TIME_STEP);
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
		CircleGroup g;
		for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
			g = new CircleGroup();
			g.order = i;
			g.circles = new Circle[NUM_CIRCLES_PER_GROUP];
			g.colors = new ArrayList<Integer>(5);
			initCircleGroup(g);
			CIRCLE_GROUPS[i] = g;
		}
		inited = true;
	}

	public void initCircleGroup(CircleGroup group) {
		float halfWidth = getWidth() / 2.0f;
		Circle[] circles = group.circles;
		int length = circles.length;

		float startY = getHeight() + group.order * GROUP_HEIGHT;

		Circle c;
		for (int i = 0; i < length; i++) {
			c = new Circle();
			c.radius = (float) (Math.random() * RADIUS_DIFF + MIN_RADIUS);
			c.x = (float) ((2 * Math.random() - 1) * halfWidth * 0.1f + halfWidth);
			c.y = startY + (float) (Math.random() * GROUP_HEIGHT);
			c.speed = -SPEED;
			c.group = group;
			assignNewColor(c);
			circles[i] = c;
		}
	}

	public void assignNewColor(Circle c) {
		CircleGroup g = c.group;
		if (c.colorIndex >= g.colors.size()) {
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.colors.add(0x00000000 | (red << 16) | (green << 8) | blue);
		}
		int color = g.colors.get(c.colorIndex);
		c.color = color | ((((int) (Math.random() * 255)) << 24) & 0xFF000000);
		c.colorIndex++;
	}

	public void resetRandomly(Circle c) {
		float halfWidth = getWidth() / 2.0f;
		c.radius = (float) (Math.random() * RADIUS_DIFF + MIN_RADIUS);
		c.x = (float) ((2 * Math.random() - 1) * halfWidth * 0.1f + halfWidth);
		c.y = getHeight() + (float) (Math.random() * GROUP_HEIGHT);
		assignNewColor(c);
	}

	public void updateCircle(Circle c, float deltaTime) {
		c.y += c.speed * deltaTime;
		if (c.y + c.radius <= 0) {
			resetRandomly(c);
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
		for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
			Circle[] circles = CIRCLE_GROUPS[i].circles;
			int len = circles.length;
			for (int j = 0; j < len; j++) {
				c = circles[j];
				if (c.y - c.radius >= height) {
					continue;
				}
				PAINT.setColor(c.color);
				canvas.drawCircle(c.x, c.y, c.radius, PAINT);
			}
		}
	}
}
