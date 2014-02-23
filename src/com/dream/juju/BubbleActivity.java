package com.dream.juju;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

public class BubbleActivity extends Activity {
	BubbleView bubbleView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bubble);
		bubbleView = (BubbleView) findViewById(R.id.bubble_view);

		bubbleView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					boolean done = false;

					@Override
					public void onGlobalLayout() {
						if (!done) {
							bubbleView.init();
							bubbleView.startAnimation();
							done = true;
						}
					}
				});

		bubbleView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				finish();
				overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
				return true;
			}
		});
	}
}
