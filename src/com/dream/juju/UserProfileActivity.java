package com.dream.juju;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * UserProfileActivity usage:
 * 
 * Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
 * 
 * To show friends instead of "My profile" add:
 * intent.putExtra(UserProfileActivity.CURRENT_PROFILE_EXTRA, profileIndex);
 *  
 * startActivity(intent)
 *
 */
public class UserProfileActivity extends Activity {
	
	/**
	 * Images config is here
	 */
	private static final UserProfileModel.ProfileModel[] FRIEND_PROFILES = new UserProfileModel.ProfileModel[] {
		new UserProfileModel.ProfileModel(R.drawable.profile_1_1, R.drawable.gallery_1_1_01, R.drawable.gallery_1_1_02_trim),
		new UserProfileModel.ProfileModel(R.drawable.profile_1_1, R.drawable.gallery_1_1_03, R.drawable.gallery_1_1_04_trim),
		new UserProfileModel.ProfileModel(R.drawable.profile_1_1, R.drawable.gallery_1_1_05, R.drawable.gallery_1_1_06_trim)
	};
	
	private static final UserProfileModel.ProfileModel MY_PROFILE = 
		new UserProfileModel.ProfileModel(R.drawable.profile_1_1_cr, R.drawable.gallery_1_1crop, R.drawable.gallery_1_1_02_trim);
	
	
	 /**
     * Pager adapter for profiles
     */
    private class ProfileSlidePagerAdapter extends FragmentStatePagerAdapter {
    	private final UserProfileModel profiles;
    	
        public ProfileSlidePagerAdapter(FragmentManager fm, UserProfileModel p) {
            super(fm);
            profiles = p;
        }

        @Override
        public Fragment getItem(int position) {
        	Log.d(LOG_TAG, "Creating profile fragment for position " + position);
            return ProfileSlidePageFragment.create(profiles.profiles[position]);
        }

        @Override
        public int getCount() {
            return profiles.profiles.length;
        }
    } 
    
    private final static String LOG_TAG = "UserProfileActivity";
	
	//public final static String PROFILES_EXTRA = "profiles";
    
    
	public final static String CURRENT_PROFILE_EXTRA = "current_profile_index";
	//public final static String SINGLE_PROFILE_EXTRA = "is_single_profile";
	

	private ViewPager viewPager; 
	private PagerAdapter pagerAdapter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		
		// Read the data
		Intent intent = getIntent();
		UserProfileModel profiles = (UserProfileModel)intent.getSerializableExtra(PROFILES_EXTRA);
		
		// For tests:
		if(profiles == null) {
			profiles = new UserProfileModel();
			profiles.profiles = new UserProfileModel.ProfileModel[2];
			
			profiles.profiles[0] = new UserProfileModel.ProfileModel();
			profiles.profiles[0].profilePictureId = R.drawable.profile_1_1;
			profiles.profiles[0].dreamTitleId = R.drawable.headline_1_1;
			profiles.profiles[0].dreamStoryId = R.string.dream_01_01;
			profiles.profiles[0].galleryImageList = new int[] {
				//R.drawable.gallery_1_1_01, 
				//R.drawable.gallery_1_1_02_trim,
				//R.drawable.gallery_1_1_03,
				//R.drawable.gallery_1_1_04_trim,
				R.drawable.gallery_1_1_05,
				R.drawable.gallery_1_1_06_trim
			};
			profiles.profiles[0].blogImageId = R.drawable.gallery_1_1_02;		// TODO - replace with the actual blog image
			
			profiles.profiles[1] = new UserProfileModel.ProfileModel();
			profiles.profiles[1].profilePictureId = R.drawable.profile_1_1;
			profiles.profiles[1].dreamTitleId = R.drawable.headline_1_1;
			profiles.profiles[1].dreamStoryId = R.string.dream_01_01;
			profiles.profiles[1].galleryImageList = new int[] {
				R.drawable.gallery_1_1_01, 
				R.drawable.gallery_1_1_02_trim,
				//R.drawable.gallery_1_1_03,
				//R.drawable.gallery_1_1_04_trim,
				//R.drawable.gallery_1_1_05,
				R.drawable.gallery_1_1_06_trim
			};
			profiles.profiles[1].blogImageId = R.drawable.gallery_1_1_02;		// TODO - replace with the actual blog image
		}
*/
		// Get setup from intent
		Intent intent = getIntent();
		int curItem = intent.getIntExtra(CURRENT_PROFILE_EXTRA, -1);
		// boolean isSingleProfileMode = intent.getBooleanExtra(SINGLE_PROFILE_EXTRA, false);
		
		
		UserProfileModel profiles = new UserProfileModel();
		if(curItem < 0) {
			profiles.profiles = new UserProfileModel.ProfileModel[] { MY_PROFILE };
		} else {
			profiles.profiles = FRIEND_PROFILES;
		}
		
		setContentView(R.layout.activity_user_profile);
		
		viewPager = (ViewPager)findViewById(R.id.profile_pager);
		pagerAdapter = new ProfileSlidePagerAdapter(getFragmentManager(), profiles); 
		viewPager.setAdapter(pagerAdapter);
		
		if(curItem > -1) {
			viewPager.setCurrentItem(curItem);
		}
		
		//Fragment profileFragment = ProfileSlidePageFragment.create(profiles.profiles[0]);
		//getFragmentManager().beginTransaction().add(R.id.test_activity, profileFragment, "1").commit();
		
	}
	
}
