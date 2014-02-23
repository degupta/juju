package com.dream.juju;

import java.util.Arrays;
import java.util.List;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils.Permissions;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;

public class FacebookLoginActivity extends Activity {

	private final static String LOG_TAG = "FacebookLoginActivity";

	public static final List<String> FB_PERMISSIONS = Arrays
			.asList(new String[] { Permissions.User.ABOUT_ME,
					Permissions.User.EMAIL, Permissions.User.PHOTOS });

	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_login);

		loginButton = (Button) findViewById(R.id.buttonFacebookLogin);
		loginButton.setOnClickListener(loginButtonOnClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook_login, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private OnClickListener loginButtonOnClick = new OnClickListener() {
		public void onClick(View view) {
			ParseFacebookUtils.logIn(FB_PERMISSIONS,
					FacebookLoginActivity.this, new LogInCallback() {
						@Override
						public void done(ParseUser user, ParseException err) {
							if (user == null) {
								Log.d(LOG_TAG,
										"Uh oh. The user cancelled the Facebook login.");
								return;
							}
							JujuApplication.INSTANCE.user.parseUser = user;
							Request request = Request.newMeRequest(
									ParseFacebookUtils.getSession(),
									new Request.GraphUserCallback() {
										@Override
										public void onCompleted(GraphUser user,
												Response response) {
											JujuApplication.INSTANCE.user.graphUser = user;
											startActivity(new Intent(getApplicationContext(), MainActivity.class));
											finish();
										}
									});
							request.executeAsync();
						}
					});
		}
	};

}
