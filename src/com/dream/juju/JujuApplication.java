/**
 * Main application class
 */
package com.dream.juju;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.app.Application;
import android.util.Log;

/**
 * @author denvo
 * 
 */
public class JujuApplication extends Application {
	public static JujuApplication INSTANCE;

	/**
	 * All global constants are here for now - it's bad but it's fast!
	 */
	public static final String PARSE_APP_ID = "i0DIscpQWHzowhiQt2zCmBN7BTvgAUHYFKskVkiW";
	public static final String PARSE_CLIENT_KEY = "KX8YXu759w3AdGhivVaFeizlCtSX2q3Yvohn9zDQ";

	public static final String FB_APP_ID = "723364517684172";
	public static final String FB_APP_SECRET = "666ddda4b7b257218e146272ed38e3e0";

	private final static String LOG_TAG = "JujuApplication";

	public User user;

	public ImageLoader imageLoader;

	public JujuApplication() {
		INSTANCE = this;
		user = new User();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY);

		ParseFacebookUtils.initialize(FB_APP_ID);

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Enable public read access.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);

		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);

		Log.d(LOG_TAG, "Init done");
	}

}
