/**
 * Profile page fragment
 */
package com.dream.juju;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author denvo
 *
 */
public class ProfileSlidePageFragment extends Fragment {
	
	 /**
     * Pager adapter for gallery images
     */
    private class GallerySlidePagerAdapter extends FragmentStatePagerAdapter {
    	private final int[] imageList;
    	
        public GallerySlidePagerAdapter(FragmentManager fm, int[] list) {
            super(fm);
            imageList = list;
        	Log.d(LOG_TAG, "Creating adapter for " + imageList);
        }

        @Override
        public Fragment getItem(int position) {
        	Log.d(LOG_TAG, "Creating gallery fragment for position " + position);
            return GallerySlidePageFragment.create(imageList[position], position);
        }

        @Override
        public int getCount() {
            return imageList.length;
        }
    } 
    
	private final static String LOG_TAG = "ProfileSlidePageFragment";
	
	public final static String ARG_PROFILE = "profile";
	
	private UserProfileModel.ProfileModel profile;

	private View storyView; // blogView;
	private LinearLayout galleryContentView;
	private SlidingUpPanelLayout slidingPanelLayoutMain, slidingPanelLayoutGallery;
	
	private GestureDetector gestureDetectorMain, gestureDetectorGallery;
	
	// Gallery pager
	// private ViewPager galleryView; 
	private PagerAdapter pagerAdapter; 

	
	public static Fragment create(UserProfileModel.ProfileModel profile) {
		ProfileSlidePageFragment fragment = new ProfileSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE, profile);
        fragment.setArguments(args);
        return fragment; 		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = (UserProfileModel.ProfileModel)getArguments().getSerializable(ARG_PROFILE);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	// Inflate the layout 
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.user_profile_page, container, false);
        
        // Set basic content (images, text)
        ImageView profileImage = (ImageView)rootView.findViewById(R.id.profile_image);
        //ImageView dreamImage = (ImageView)rootView.findViewById(R.id.dream_image);
        //TextView dreamText = (TextView)rootView.findViewById(R.id.dream_text);
        ImageView blogImage = (ImageView)rootView.findViewById(R.id.blog_image);
        ImageView galleryImage = (ImageView)rootView.findViewById(R.id.gallery_image);
        
		// storyView = profileImage;// rootView.findViewById(R.id.story);
		// galleryView = (ViewPager)rootView.findViewById(R.id.gallery);
		//galleryContentView = (LinearLayout) rootView.findViewById(R.id.gallery_content);
		//blogView = rootView.findViewById(R.id.blog);
		
        Log.d(LOG_TAG, "onCreateView, profileImage = " + profileImage);
        
        if(profileImage != null && profile != null) {
        
	        profileImage.setImageResource(profile.profilePictureId);
	        //dreamImage.setImageResource(profile.dreamTitleId);
	        //dreamText.setText(profile.dreamStoryId);
	        blogImage.setImageResource(profile.blogPictureId);
	        galleryImage.setImageResource(profile.galleryPictureId);
	
	        // Set up vertical sliding stuff
			slidingPanelLayoutMain = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout_main);
			slidingPanelLayoutMain.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
			slidingPanelLayoutMain.setEnableDragViewTouchEvents(true);
			//slidingPanelLayoutMain.setAnchorPoint(0.5f);
	
			slidingPanelLayoutGallery = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout_gallery);
			slidingPanelLayoutGallery.setEnableDragViewTouchEvents(true);
			slidingPanelLayoutGallery.setAnchorPoint(0.01f);
			slidingPanelLayoutGallery.setPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() {
	            boolean isOpen = false;
				
				@Override
	            public void onPanelExpanded(View panel) {
	                Log.i(LOG_TAG, "Blog panel expanded");
	                slidingPanelLayoutMain.expandPane();
	                rootView.post(new Runnable() {
						
						@Override
						public void run() {
							slidingPanelLayoutGallery.collapsePane();
							
						}
					});
	                
	            }
	            
	            @Override
	            public void onPanelAnchored(View panel) {
	            	if(!isOpen) {
	            		//createGalleryImages(profile.galleryImageList, galleryContentView);
	            		isOpen = true;
	            		Log.i(LOG_TAG, "Gallery panel opened");
	            	}
	            }
	            
	            @Override
	            public void onPanelCollapsed(View panel) {
	            	//galleryContentView.removeAllViews();
	            	isOpen = false;
	            	Log.i(LOG_TAG, "Gallery panel closed");
	            }
			});
			
			slidingPanelLayoutMain.setPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() { 
				@Override
				public void onPanelCollapsed(View panel) {
					//slidingPanelLayoutGallery.expandPane(0.01f);
				}
			});
	
			
			// Create gallery images, put into galleryContentView
			
			// Detect scrolling up to show sliding panel for Gallery
			gestureDetectorGallery = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
						float distanceY) {
					Log.d(LOG_TAG, "onScroll " + e1 + e2 + distanceX + ", " + distanceY);
					if(distanceY > 0 && Math.abs(distanceX) < distanceY / 4) {
						slidingPanelLayoutGallery.expandPane(0.01f);
		                Log.i(LOG_TAG, "Gallery panel expanded");
						return true;
					}
					return false;
				}
				
				@Override
				public void onLongPress(MotionEvent e) {
					Activity a = getActivity();
					a.finish();
					a.overridePendingTransition(0, R.anim.activity_exit);
				}
			});
			profileImage.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return gestureDetectorGallery.onTouchEvent(event);
				}
			});
			
			// Set up gallery pager
			//pagerAdapter = new GallerySlidePagerAdapter(getFragmentManager(), profile.galleryImageList); 
			//galleryView.setAdapter(pagerAdapter);
			
        	Log.d(LOG_TAG, "Profile view has been set up for profile " + profile);

        }
        
        return rootView;
    }
    
    private void createGalleryImages(int[] imgList, ViewGroup container) {
		for(int imgId: imgList) {
			if(imgId != 0) {
				ImageView imgView = new ImageView(getActivity());
				imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imgView.setImageResource(imgId);
				imgView.setBackgroundColor(0xFF8800);
				container.addView(imgView);
				Log.d(LOG_TAG, "Image " + imgView + " added, resId=" + imgId);
			}
		}
    	
    }
 
}
