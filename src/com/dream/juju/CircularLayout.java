package com.dream.juju;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class CircularLayout extends FrameLayout {

	public static final float PI_2 = (float) (Math.PI * 2.0f);
	public static final float CHILD_SCALE = 0.5f;
	public static final float CENTER_SCALE = 1.0f;

	public static class CircularLayoutNode {
		public ArrayList<CircularLayoutNode> children = new ArrayList<CircularLayoutNode>();
		public View view;
		public CircularLayoutNode parent;

		public CircularLayoutNode(View view, CircularLayoutNode parent) {
			this.view = view;
			this.parent = parent;
		}
	}

	public class CircularLayoutNodeOnClickListener implements
			View.OnClickListener {
		CircularLayoutNode node;

		public CircularLayoutNodeOnClickListener(CircularLayoutNode node) {
			this.node = node;
		}

		@Override
		public void onClick(View v) {
			if (node.parent == null) {
				return;
			} else if (currentNode == node) {
				animateToParent(node);
			} else if (node.children.size() > 0) {
				bringToCenter(node);
			}
		}
	}

	float radius = 0.0f;
	CircularLayoutNode currentNode;

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

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void centerAndReset(View view, boolean visible) {
		view.setX(centerX - view.getWidth() / 2);
		view.setY(centerY - view.getHeight() / 2);
		view.setAlpha(0.0f);
		view.setScaleX(0.0f);
		view.setScaleY(0.0f);
		view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}

	public void initAllChildren(CircularLayoutNode node) {
		View view = node.view;
		ArrayList<CircularLayoutNode> children = node.children;
		int size = children.size();
		if (size > 0) {
			view.setOnClickListener(new CircularLayoutNodeOnClickListener(node));
		}
		centerAndReset(view, false);
		for (int i = 0; i < size; i++) {
			initAllChildren(children.get(i));
		}
	}

	public void initWithNode(CircularLayoutNode node, float radius) {
		setVisibility(View.VISIBLE);
		width = getWidth() - getPaddingLeft() - getPaddingRight();
		height = getHeight() - getPaddingTop() - getPaddingBottom();
		centerX = width / 2.0f;
		centerY = height / 2.0f;
		this.radius = radius;
		currentNode = node;

		initAllChildren(node);
		initialAnimation();
	}

	public void initialAnimation() {
		int centerAnimTime = 1000;
		int childAnimTime = 2000;
		View view = currentNode.view;
		centerAndReset(view, true);
		view.animate().alpha(1.0f).scaleX(CENTER_SCALE).scaleY(CENTER_SCALE)
				.setDuration(centerAnimTime)
				.setInterpolator(new DecelerateInterpolator(1.0f)).start();
		circularAnimation(currentNode.children, childAnimTime, centerAnimTime);
	}

	public void animateToParent(CircularLayoutNode node) {
		ArrayList<CircularLayoutNode> children = node.children;
		int size = children.size();
		for (int i = 0; i < size; i++) {
			centerAndReset(children.get(i).view, false);
		}

		View view = node.view;
		int centerAnimTime = 1500;
		int otherAnimTime = 1000;
		int otherAnimTimeDelay = 1500;
		CircularLayoutNode parent = node.parent;
		View parentView = parent.view;
		ArrayList<CircularLayoutNode> siblings = parent.children;
		size = siblings.size();
		for (int i = 0; i < size; i++) {
			View child = siblings.get(i).view;
			child.setVisibility(View.VISIBLE);
			float currentDegree = PI_2 / size * i;
			float x = (float) (radius * Math.cos(currentDegree)) + centerX
					- child.getWidth() / 2;
			float y = (float) (-radius * Math.sin(currentDegree)) + centerY
					- child.getHeight() / 2;
			if (child == view) {
				child.animate().scaleX(CHILD_SCALE).x(x).y(y)
						.scaleY(CHILD_SCALE).setDuration(centerAnimTime)
						.setInterpolator(new DecelerateInterpolator(1.0f))
						.start();
			} else {
				child.setX(x);
				child.setY(y);
				child.setAlpha(0.0f);
				child.setScaleX(CHILD_SCALE);
				child.setScaleY(CHILD_SCALE);
				child.animate().alpha(1.0f)
						.setInterpolator(new DecelerateInterpolator(1.0f))
						.setStartDelay(otherAnimTimeDelay)
						.setDuration(otherAnimTime).start();
			}
		}

		parentView.setVisibility(View.VISIBLE);
		parentView.setX(centerX - parentView.getWidth() / 2);
		parentView.setY(centerY - parentView.getHeight() / 2);
		parentView.setAlpha(0.0f);
		parentView.setScaleX(CENTER_SCALE);
		parentView.setScaleY(CENTER_SCALE);
		parentView.animate().alpha(1.0f)
				.setInterpolator(new DecelerateInterpolator(1.0f))
				.setStartDelay(otherAnimTimeDelay).setDuration(otherAnimTime)
				.start();
		currentNode = node.parent;
	}

	public void bringToCenter(CircularLayoutNode node) {
		ArrayList<CircularLayoutNode> children = node.parent.children;
		int size = children.size();
		for (int i = 0; i < size; i++) {
			if (children.get(i) != node) {
				centerAndReset(children.get(i).view, false);
			}
		}
		centerAndReset(node.parent.view, false);
		int centerAnimTime = 1000;
		int childAnimTime = 2000;
		View view = node.view;
		float x = centerX - view.getWidth() / 2;
		float y = centerY - view.getHeight() / 2;
		view.animate().scaleX(CENTER_SCALE).scaleY(CENTER_SCALE).x(x).y(y)
				.setDuration(centerAnimTime)
				.setInterpolator(new DecelerateInterpolator(1.0f)).start();
		circularAnimation(node.children, childAnimTime, centerAnimTime);
		currentNode = node;
	}

	public void circularAnimation(ArrayList<CircularLayoutNode> nodes,
			int animTime, int delay) {
		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			View child = nodes.get(i).view;
			centerAndReset(child, true);
			float currentDegree = PI_2 / size * i;
			float x = (float) (radius * Math.cos(currentDegree)) + centerX
					- child.getWidth() / 2;
			float y = (float) (-radius * Math.sin(currentDegree)) + centerY
					- child.getHeight() / 2;
			child.animate().alpha(1.0f).x(x).y(y).setDuration(animTime)
					.setStartDelay(delay).scaleX(CHILD_SCALE)
					.scaleY(CHILD_SCALE)
					.setInterpolator(new DecelerateInterpolator(1.0f)).start();
		}
	}

	public boolean onBackPressed() {
		if (currentNode == null) {
			return false;
		} else if (currentNode.parent != null) {
			animateToParent(currentNode);
			return true;
		} else {
			return false;
		}
	}
}
