package com.dream.juju;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FacebookLoginActivity extends Activity {

	private final static String LOG_TAG = "FacebookLoginActivity";

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
			ParseFacebookUtils.logIn(FacebookLoginActivity.this,
					new LogInCallback() {
						@Override
						public void done(ParseUser user, ParseException err) {
							if (user == null) {
								Log.d(LOG_TAG,
										"Uh oh. The user cancelled the Facebook login.");
							} else if (user.isNew()) {
								Log.d(LOG_TAG,
										"User signed up and logged in through Facebook!");
							} else {
								Log.d(LOG_TAG,
										"User logged in through Facebook!");
							}
						}
					});
		}
	};

}
