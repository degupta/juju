package com.dream.juju;

import java.util.List;

import com.dream.juju.CircularLayout.CircularLayoutListener;
import com.dream.juju.CircularLayout.CircularLayoutNode;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class MainActivity extends Activity implements CircularLayoutListener {

	CircularLayout circularLayout;
	CircularLayoutNode mainNode;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		circularLayout = (CircularLayout) findViewById(R.id.circular_layout);

		mainNode = new CircularLayoutNode(newImageView(), null);
		for (int i = 0; i < 6; i++) {
			CircularLayoutNode child = new CircularLayoutNode(newImageView(),
					mainNode);
			mainNode.children.add(child);

			for (int j = 0; j < 6; j++) {
				child.children
						.add(new CircularLayoutNode(newImageView(), child));
				child.children
						.add(new CircularLayoutNode(newImageView(), child));
			}
		}

		User user = JujuApplication.INSTANCE.user;
		String userImage = user.getImageUrl();
		if (userImage != null) {
			JujuApplication.INSTANCE.imageLoader.displayImage(userImage,
					(ImageView) circularLayout.getChildAt(0),
					JujuApplication.ROUNDER);
		}

		user.getFriends(new Callback<List<GraphUser>>() {
			@Override
			public void callback(List<GraphUser> friends) {
				int size = Math.min(friends.size(),
						circularLayout.getChildCount() - 1);
				for (int i = 0; i < size; i++) {
					JujuApplication.INSTANCE.imageLoader.displayImage(
							User.getFbImageUrl(friends.get(i).getId()),
							(ImageView) circularLayout.getChildAt(i + 1),
							JujuApplication.ROUNDER);
				}
			}
		});

		circularLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					boolean done = false;

					@Override
					public void onGlobalLayout() {
						if (!done) {
							circularLayout.listener = MainActivity.this;
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

	@Override
	public void onRootClicked(CircularLayoutNode node) {
		startActivity(new Intent(this, BubbleActivity.class));
	}

	public ImageView newImageView() {
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.ic_launcher);
		circularLayout.addChildView(imageView);
		return imageView;
	}
}
