/**
 * 
 */
package com.dream.juju;

import android.app.Fragment;
import android.os.Bundle;
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
	
	private int imageId;

	public static Fragment create(int img) {
		ProfileSlidePageFragment fragment = new ProfileSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMG_ID, img);
        fragment.setArguments(args);
        return fragment; 		
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getInt(ARG_IMG_ID);
        
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
        
        return rootView;
    }
}
