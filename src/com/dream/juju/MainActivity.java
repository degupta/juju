package com.dream.juju;

import com.dream.juju.CircularLayout.CircularLayoutListener;
import com.dream.juju.CircularLayout.CircularLayoutNode;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;

public class MainActivity extends Activity implements CircularLayoutListener {

	CircularLayout circularLayout;
	CircularLayoutNode mainNode;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		circularLayout = (CircularLayout) findViewById(R.id.circular_layout);
		mainNode = new CircularLayoutNode(circularLayout.getChildAt(0), null);
		for (int i = 1; i < 7; i++) {
			mainNode.children.add(new CircularLayoutNode(circularLayout
					.getChildAt(i), mainNode));
		}

		for (int i = 7; i < 19; i += 2) {
			CircularLayoutNode parent = mainNode.children.get((i - 7) / 2);
			parent.children.add(new CircularLayoutNode(circularLayout
					.getChildAt(i), parent));
			parent.children.add(new CircularLayoutNode(circularLayout
					.getChildAt(i + 1), parent));
		}

		circularLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					boolean done = false;

					@Override
					public void onGlobalLayout() {
						if (!done) {
							circularLayout.initWithNode(mainNode, 300.0f);
							done = true;
						}
					}
				});
	}

	public void onBackPressed() {
		if (!circularLayout.onBackPressed()) {
			super.onBackPressed();
		}
	}

	@Override
	public void onLeafClicked(CircularLayoutNode node) {

	}
}
