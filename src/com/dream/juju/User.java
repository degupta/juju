package com.dream.juju;

import java.util.ArrayList;
import java.util.List;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class User {
	public ParseUser parseUser;
	public GraphUser graphUser;
	public List<GraphUser> friends;

	public User() {
	}

	public String getImageUrl() {
		return getImageUrl(this);
	}

	public boolean isFBConnected() {
		return graphUser != null && ParseFacebookUtils.getSession() != null;
	}

	public void getFriends(final Callback<List<GraphUser>> callback) {
		if (!isFBConnected()) {
			friends = new ArrayList<GraphUser>();
			if (callback != null) {
				callback.callback(friends);
			}
			return;
		}
		Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {
						if (users != null) {
							friends = users;
						} else {
							friends = new ArrayList<GraphUser>();
						}
						if (callback != null) {
							callback.callback(friends);
						}
					}
				});
		request.executeAsync();
	}

	public static String getImageUrl(User user) {
		if (user.graphUser != null) {
			return getFbImageUrl(user.graphUser.getId());
		} else {
			return null;
		}
	}

	public static String getFbImageUrl(String userId) {
		return "http://graph.facebook.com/" + userId
				+ "/picture/width=256&height=256";
	}
}
