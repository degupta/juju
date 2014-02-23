package com.dream.juju;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BubbleView extends View {
	private static class Circle {
		float x, aX, y;
		float radius;
		int color;
	}

	private static class CircleGroup {
		Circle[] circles;
		float x, y;
	}

	public static final int TIME_STEP = 50;
	public static final int NUM_CIRCLE_GROUPS = 3;
	public static final int NUM_CIRCLES_PER_GROUP = 30;
	public static final int NUM_SUB_GROUPS = 5;
	public static final float MIN_RADIUS = 50f;
	public static final float MAX_RADIUS = 150f;
	// public static final float MIN_ALPHA = 0.1f;
	// public static final float MAX_ALPHA = 0.75f;
	public static final float SPEED = -0.1f;
	public static final float GROUP_HEIGHT = 1000.0f;
	public static final float GROUP_OVERLAP = 100.0f;
	public static final int START_DREAMING_NO = 10000;
	public static final int DREAMING_NO_SPEED = 1;

	public static final float RADIUS_DIFF = MAX_RADIUS - MIN_RADIUS;
	// public static final float ALPHA_DIFF = MAX_ALPHA - MIN_ALPHA;

	public static final CircleGroup[] CIRCLE_GROUPS = new CircleGroup[NUM_CIRCLE_GROUPS];
	public static final Paint PAINT = new Paint();
	public static final DecimalFormat FORMATTER = new DecimalFormat("#,###,###");

	Runnable animation = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
				updateGroup(CIRCLE_GROUPS[i], TIME_STEP);
			}
			numPeople += DREAMING_NO_SPEED;
			invalidate();
			postDelayed(this, TIME_STEP);
		}
	};

	boolean inited = false;
	CircleGroup bottomMostGroup = null;
	public int numPeople = 0;

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
		numPeople = START_DREAMING_NO;
		inited = true;
	}

	public void resetGroup(CircleGroup g, float y) {
		float halfWidth = getWidth() / 2.0f;
		g.x = halfWidth;
		g.y = y;
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int color = 0x80000000 | (red << 16) | (green << 8) | blue;

		Circle[] circles = g.circles;
		int len = circles.length;
		for (int i = 0; i < len; i++) {
			Circle c = circles[i];
			if (c == null) {
				circles[i] = new Circle();
				c = circles[i];
			}
			// int alpha = (int) ((Math.random() * ALPHA_DIFF + MIN_ALPHA) *
			// 255.0f);
			// c.color = color | ((alpha << 24) & 0xFF000000);
			c.color = color;
			c.radius = (float) (Math.random() * RADIUS_DIFF + MIN_RADIUS);
			c.x = (float) ((2 * Math.random() - 1) * halfWidth * 0.5f);
			c.aX = 0.0f;
			c.y = GROUP_HEIGHT / len * i;
			if (c.y + c.radius > GROUP_HEIGHT + GROUP_OVERLAP) {
				c.y = GROUP_HEIGHT + GROUP_OVERLAP - c.radius;
			}
			if (c.y - c.radius < -GROUP_OVERLAP) {
				c.y = c.radius - GROUP_OVERLAP;
			}
		}
	}

	public void updateGroup(CircleGroup g, float deltaTime) {
		g.y += SPEED * deltaTime;
		if (g.y + GROUP_HEIGHT <= 0) {
			resetGroup(g, bottomMostGroup.y + GROUP_HEIGHT);
			bottomMostGroup = g;
		} else {
			float width = getWidth();
			float amplitude = -width / 6.0f;
			float height = getHeight();
			Circle[] circles = g.circles;
			int len = circles.length;
			float x, y, ordinate;
			for (int i = 0; i < len; i++) {
				y = -(circles[i].y + g.y) + height;
				ordinate = y / height * (CircularLayout.PI_2)
						- CircularLayout.PI_2 / 2.0f;
				x = (float) Math.sin(ordinate) * amplitude;
				x += (2 * Math.random() - 1) * width * 0.0015f;
				circles[i].aX = x;
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
		float height = getHeight();
		float width = getWidth();
		for (int i = 0; i < NUM_CIRCLE_GROUPS; i++) {
			g = CIRCLE_GROUPS[i];
			Circle[] circles = g.circles;
			int len = circles.length;
			for (int j = 0; j < len; j++) {
				c = circles[j];
				float x = g.x + c.x + c.aX;
				float y = g.y + c.y;
				float radius = c.radius;
				if (y - radius >= height || y + radius <= 0
						|| x - radius >= width || x + radius <= 0) {
					continue;
				}
				PAINT.setColor(c.color);
				canvas.drawCircle(x, y, radius, PAINT);
			}
		}
		PAINT.setTextSize(200.0f);
		PAINT.setColor(Color.BLACK);
		canvas.drawText(FORMATTER.format(numPeople), 60, 350, PAINT);
		PAINT.setTextSize(50.0f);
		canvas.drawText("PEOPLE DREAMING", 100, 450, PAINT);
	}
}
