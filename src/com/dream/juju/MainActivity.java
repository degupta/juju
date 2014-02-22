package com.dream.juju;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	CircularLayout circularLayout;
	BubbleView bubbleView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		circularLayout = (CircularLayout) findViewById(R.id.circular_layout);
		bubbleView = (BubbleView) findViewById(R.id.bubble_view);
		bubbleView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bubbleView.stopAnimation();
				circularLayout.init();
				circularLayout.animateCircular();
			}
		});
		bubbleView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				bubbleView.init();
				bubbleView.startAnimation();
			}
		}, 2000);
	}
}
