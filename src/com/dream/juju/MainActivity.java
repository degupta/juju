package com.dream.juju;

import java.util.List;

import com.dream.juju.CircularLayout.CircularLayoutListener;
import com.dream.juju.CircularLayout.CircularLayoutNode;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class MainActivity extends Activity implements CircularLayoutListener {

	public static final int FACE_ID = 10;
	public static final int STATS_ID = 20;
	public static final int SEARCH_ID = 30;
	public static final int FEATURED_ID = 40;
	public static final int LIGHTBULB_ID = 50;
	public static final int FRIENDS_ID = 60;
	public static final int COMMUNITY_ID = 70;

	CircularLayout circularLayout;
	CircularLayoutNode mainNode;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		circularLayout = (CircularLayout) findViewById(R.id.circular_layout);

		mainNode = new CircularLayoutNode(newImageView(FACE_ID,
				R.drawable.face, 161), null, 2, 1);

		mainNode.children.add(new CircularLayoutNode(newImageView(COMMUNITY_ID,
				R.drawable.community, 94), mainNode, 3.0f, 1.5f));

		mainNode.children.add(new CircularLayoutNode(newImageView(FRIENDS_ID,
				R.drawable.friends, 105), mainNode, 4, 2));

		mainNode.children.add(new CircularLayoutNode(newImageView(FEATURED_ID,
				R.drawable.featuredstar, 77), mainNode, 3, 1.5f));

		mainNode.children.add(new CircularLayoutNode(newImageView(STATS_ID,
				R.drawable.stats3, 73), mainNode, 3, 1.5f));

		mainNode.children.add(new CircularLayoutNode(newImageView(LIGHTBULB_ID,
				R.drawable.lightbulb, 73), mainNode, 5, 2.5f));

		mainNode.children.add(new CircularLayoutNode(newImageView(SEARCH_ID,
				R.drawable.search, 60), mainNode, 3, 1.5f));

		circularLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					boolean done = false;

					@Override
					public void onGlobalLayout() {
						if (!done) {
							circularLayout.listener = MainActivity.this;
							circularLayout.initWithNode(mainNode, 325.0f);
							done = true;
						}
					}
				});

		findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						FriendsActivity.class);
				startActivity(intent);
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
		Intent intent = new Intent(this, UserProfileActivity.class);
		switch (node.view.getId()) {
		case FACE_ID:
			break;
		case SEARCH_ID:
			intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, 0);
			break;
		case STATS_ID:
			intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, 1);
			break;
		case COMMUNITY_ID:
			intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, 2);
			break;
		case LIGHTBULB_ID:
			intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, 0);
			break;
		case FRIENDS_ID:
			intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, 1);
			break;
		case FEATURED_ID:
			intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, 2);
			break;
		}

		startActivity(intent);
	}

	@Override
	public void onRootClicked(CircularLayoutNode node) {
		Intent intent = new Intent(this, UserProfileActivity.class);
		startActivity(intent);
	}

	public ImageView newImageView(int id, int resId, int size) {
		ImageView imageView = new ImageView(this);
		imageView.setId(id);
		imageView.setImageResource(resId);
		circularLayout.addChildView(imageView, size);
		return imageView;
	}
}
