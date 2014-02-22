package com.dream.juju;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	CircularLayout circularLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		circularLayout = (CircularLayout) findViewById(R.id.circular_layout);
		circularLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				circularLayout.init();
				circularLayout.animateCircular();
			}
		}, 2000);
	}
}
