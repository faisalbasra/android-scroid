package de.dan_nrw.android.scroid.core.caching;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public final class WallpaperCache {

	// Fields
	private static Map<URI, Bitmap> cache = new HashMap<URI, Bitmap>();
	
	// Methods
	/**
	 * Method for checking if image is in cache
	 * @param uri URI to image (key)
	 * @return true (cached) / false (not cached)
	 */
	public static boolean isInCache(URI uri) {
		return cache.containsKey(uri);
	}
	
	/**
	 * Method for getting image from cache
	 * @param uri URI to image (key)
	 * @return
	 */
	public static Bitmap get(URI uri) {
		return cache.get(uri);
	}
	
	/**
	 * Method for adding image to cache 
	 * @param uri URI to image (key)
	 * @param bitmap
	 */
	public static void put(URI uri, Bitmap bitmap) {
		synchronized(cache) {
			cache.put(uri, bitmap);   
        }
	}
}