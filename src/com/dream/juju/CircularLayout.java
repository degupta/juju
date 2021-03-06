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

	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
			(int) getResources()
					.getDimension(R.dimen.circular_layout_node_size),
			(int) getResources()
					.getDimension(R.dimen.circular_layout_node_size));

	public static interface CircularLayoutListener {
		public void onRootClicked(CircularLayoutNode node);

		public void onLeafClicked(CircularLayoutNode node);
	}

	public static class CircularLayoutNode {
		public ArrayList<CircularLayoutNode> children = new ArrayList<CircularLayoutNode>();
		public View view;
		public CircularLayoutNode parent;
		public float centerScale, childScale;

		public CircularLayoutNode(View view, CircularLayoutNode parent) {
			this(view, parent, CENTER_SCALE, CHILD_SCALE);
		}

		public CircularLayoutNode(View view, CircularLayoutNode parent,
				float centerScale, float childScale) {
			this.view = view;
			this.parent = parent;
			this.centerScale = centerScale;
			this.childScale = childScale;
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
				if (listener != null) {
					listener.onRootClicked(node);
				}
			} else if (currentNode == node) {
				animateToParent(node);
			} else if (node.children.size() > 0) {
				bringToCenter(node);
			} else if (listener != null) {
				listener.onLeafClicked(node);
			}
		}
	}

	float radius = 0.0f;
	CircularLayoutNode currentNode;
	boolean animating = false;

	int width, height;
	float centerX, centerY;
	CircularLayoutListener listener;

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
		view.setOnClickListener(new CircularLayoutNodeOnClickListener(node));
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

	public void addChildView(View view) {
		addView(view, params);
	}

	public void addChildView(View view, int size) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size,
				size);
		addView(view, params);
	}

	public void initialAnimation() {
		if (animating) {
			return;
		}
		animating = true;
		final int centerAnimTime = 1000;
		final int childAnimTime = 2000;
		View view = currentNode.view;
		centerAndReset(view, true);
		view.animate().alpha(1.0f).scaleX(currentNode.centerScale)
				.scaleY(currentNode.centerScale).setDuration(centerAnimTime)
				.setInterpolator(new DecelerateInterpolator(1.0f))
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						circularAnimation(currentNode.children, childAnimTime,
								true);
					}
				}).start();
	}

	public void animateToParent(CircularLayoutNode node) {
		if (animating) {
			return;
		}
		animating = true;
		final View view = node.view;
		final int centerAnimTime = 1500;
		final int otherAnimTime = 1000;
		final CircularLayoutNode parent = node.parent;
		final View parentView = parent.view;
		final ArrayList<CircularLayoutNode> siblings = parent.children;
		final int siblingsSize = siblings.size();
		final ArrayList<CircularLayoutNode> children = node.children;
		final int size = children.size();

		for (int i = 0; i < size; i++) {
			centerAndReset(children.get(i).view, false);
		}

		for (int i = 0; i < siblingsSize; i++) {
			CircularLayoutNode sibling = siblings.get(i);
			View child = sibling.view;
			if (child == view) {
				float currentDegree = PI_2 / siblingsSize * i;
				float x = getX(currentDegree, child);
				float y = getY(currentDegree, child);
				child.animate().scaleX(sibling.childScale).x(x).y(y)
						.scaleY(sibling.childScale).setDuration(centerAnimTime)
						.setInterpolator(new DecelerateInterpolator(1.0f))
						.withEndAction(new Runnable() {

							@Override
							public void run() {
								if (siblingsSize < 2) {
									animating = false;
									return;
								}
								// -1 for view, +1 for parent
								final int[] animationsToGo = new int[] { siblingsSize };
								Runnable endAnimation = new Runnable() {
									@Override
									public void run() {
										animationsToGo[0]--;
										if (animationsToGo[0] <= 0) {
											animating = false;
										}
									}
								};
								int num = 0;
								for (int i = 0; i < siblingsSize; i++) {
									CircularLayoutNode sibling = siblings
											.get(i);
									View child = sibling.view;
									child.setVisibility(View.VISIBLE);
									float currentDegree = PI_2 / siblingsSize
											* i;
									float x = getX(currentDegree, child);
									float y = getY(currentDegree, child);
									if (child != view) {
										child.setX(x);
										child.setY(y);
										child.setAlpha(0.0f);
										child.setScaleX(sibling.childScale);
										child.setScaleY(sibling.childScale);
										child.animate()
												.alpha(1.0f)
												.setInterpolator(
														new DecelerateInterpolator(
																1.0f))
												.setDuration(otherAnimTime)
												.withEndAction(endAnimation)
												.setStartDelay(num * 200)
												.start();
										num++;
									}
								}

								parentView.setVisibility(View.VISIBLE);
								parentView.setX(centerX - parentView.getWidth()
										/ 2);
								parentView.setY(centerY
										- parentView.getHeight() / 2);
								parentView.setAlpha(0.0f);
								parentView.setScaleX(parent.centerScale);
								parentView.setScaleY(parent.centerScale);
								parentView
										.animate()
										.alpha(1.0f)
										.setInterpolator(
												new DecelerateInterpolator(1.0f))
										.withEndAction(endAnimation)
										.setDuration(otherAnimTime).start();
							}
						}).start();
				break;
			}
		}
		currentNode = node.parent;
	}

	public void bringToCenter(final CircularLayoutNode node) {
		if (animating) {
			return;
		}
		animating = true;
		ArrayList<CircularLayoutNode> children = node.parent.children;
		int size = children.size();
		for (int i = 0; i < size; i++) {
			if (children.get(i) != node) {
				centerAndReset(children.get(i).view, false);
			}
		}
		centerAndReset(node.parent.view, false);
		final int centerAnimTime = 1000;
		final int childAnimTime = 2000;
		View view = node.view;
		float x = centerX - view.getWidth() / 2;
		float y = centerY - view.getHeight() / 2;
		view.animate().scaleX(node.centerScale).scaleY(node.centerScale).x(x)
				.y(y).setDuration(centerAnimTime)
				.setInterpolator(new DecelerateInterpolator(1.0f))
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						circularAnimation(node.children, childAnimTime, true);
					}
				}).start();
		currentNode = node;
	}

	public void circularAnimation(ArrayList<CircularLayoutNode> nodes,
			int animTime, final boolean finalAnimation) {
		int size = nodes.size();
		if (size == 0) {
			if (finalAnimation) {
				animating = false;
			}
			return;
		}
		animating = true;
		final int[] animationsToGo = new int[] { size };
		for (int i = 0; i < size; i++) {
			CircularLayoutNode node = nodes.get(i);
			View child = node.view;
			centerAndReset(child, true);
			float currentDegree = PI_2 / size * i;
			float x = getX(currentDegree, child);
			float y = getY(currentDegree, child);
			child.animate().alpha(1.0f).x(x).y(y).setDuration(animTime)
					.scaleX(node.childScale).scaleY(node.childScale)
					.setInterpolator(new DecelerateInterpolator(1.0f))
					.setStartDelay(i * 200).withEndAction(new Runnable() {

						@Override
						public void run() {
							animationsToGo[0]--;
							if (finalAnimation && animationsToGo[0] <= 0) {
								animating = false;
							}
						}
					}).start();
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

	public float getX(float currentDegree, View view) {
		return (float) (radius * Math.cos(currentDegree)) + centerX
				- view.getWidth() / 2;
	}

	public float getY(float currentDegree, View view) {
		return (float) (-radius * Math.sin(currentDegree)) + centerY
				- view.getHeight() / 2;
	}
}
