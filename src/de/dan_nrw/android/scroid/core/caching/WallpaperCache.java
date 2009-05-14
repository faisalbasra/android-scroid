/* 
 * Android Scroid - Screen Android
 * 
 * Copyright (C) 2009  Daniel Czerwonk <d.czerwonk@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dan_nrw.android.scroid.core.caching;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public final class WallpaperCache {

	private static Map<URI, Bitmap> cache = new ConcurrentHashMap<URI, Bitmap>();

	
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
		cache.put(uri, bitmap);
	}
}