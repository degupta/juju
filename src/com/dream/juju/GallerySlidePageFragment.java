/**
 * 
 */
package com.dream.juju;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author denvo
 *
 */
public class GallerySlidePageFragment extends Fragment {
	
	public static final String ARG_IMG_ID = "img_id";
	public static final String ARG_PAGE_NO = "page_no";
	
	public static final String LOG_TAG = "GallerySlidePageFragment";
	
	private int imageId, pageNo;

	public static Fragment create(int img, int page) {
		ProfileSlidePageFragment fragment = new ProfileSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMG_ID, img);
        args.putSerializable(ARG_PAGE_NO, img);
        
        fragment.setArguments(args);
        return fragment; 		
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getInt(ARG_IMG_ID);
        pageNo = getArguments().getInt(ARG_PAGE_NO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
    	// Inflate the layout 
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.user_profile_page, container, false);
        
        ImageView img = (ImageView)rootView.findViewById(R.id.gallery_image);
        if(img != null && imageId != 0) {
        	img.setImageResource(imageId);
        }
        
        Log.d(LOG_TAG, "view created for image " + imageId);
        
        return rootView;
    }

    public int getPageNumber() {
        return pageNo;
    } 
    

}
