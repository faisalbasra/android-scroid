package de.dan_nrw.android.scroid.core.caching;

import java.io.IOException;
import java.net.URI;

import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public interface IPersistentCache {

	/**
	 * Method for checking if Image is already in cache
	 * @param uri URI of image (key)
	 * @param prefix Prefix used for creating unique filenames
	 * @return true (cached) / false (not cached) 
	 */
	boolean isInCache(URI uri, String prefix);
	
	/**
	 * Method for getting image from cache
	 * @param uri URI of image (key)
	 * @param prefix Prefix used for creating unique filenames
	 * @return Image loaded from cache
	 * @throws IOException
	 */
	Bitmap get(URI uri, String prefix) throws IOException;
	
	/**
	 * Method for adding image to cache
	 * @param bitmap Image to cache
	 * @param uri URI of image (key)
	 * @param prefix Prefix used for creating unique filenames
	 * @throws IOException
	 */
	void put(Bitmap bitmap, URI uri, String prefix) throws IOException;
}