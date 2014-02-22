package com.dream.juju;

import com.dream.juju.CircularLayout.CircularLayoutNode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	CircularLayout circularLayout;
	CircularLayoutNode mainNode;
	BubbleView bubbleView;

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

		bubbleView = (BubbleView) findViewById(R.id.bubble_view);
		bubbleView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bubbleView.stopAnimation();
				circularLayout.initWithNode(mainNode, 300.0f);
			}
		});
		bubbleView.postDelayed(new Runnable() {

			@Override
			public void run() {
				bubbleView.init();
				bubbleView.startAnimation();
			}
		}, 1500);
	}
	
	public void onBackPressed() {
		if (!circularLayout.onBackPressed()) {
			super.onBackPressed();
		}
	}
}
