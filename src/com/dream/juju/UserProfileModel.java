package com.dream.juju;

import java.io.Serializable;

public class UserProfileModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7339472642029513425L;

	public static class ProfileModel implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1684462311629113396L;

		/**
		 * Set of public fields for data
		 */
		
		public int profilePictureId;	// Profile picture drawable id
		
		public int galleryPictureId;	// Profile picture drawable id

		//public int dreamTitleId;	// Dream title image id
		//public int dreamStoryId;	// Dream story string id
		
		//public int[] galleryImageList;	// List of gallery images (drawable id, 0 - no image)
		//public int[] galleryTextList;	// List of gallery texts (string id, 0 - no text)
		
		public int blogPictureId;		// Blog picture drawable id
		
		
		public ProfileModel(int p, int g, int b) {
			profilePictureId = p;
			galleryPictureId = g;
			blogPictureId = b;
		}
	}
	
	public ProfileModel[] profiles;
	
}
