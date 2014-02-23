package com.dream.juju;

import com.facebook.model.GraphUser;
import com.parse.ParseUser;

public class User {
	public ParseUser parseUser;
	public GraphUser graphUser;

	public User() {
	}

	public String getImageUrl() {
		if (graphUser != null) {
			return "http://graph.facebook.com/" + graphUser.getId()
					+ "/picture/width=256&height=256";
		} else {
			return null;
		}
	}
}
