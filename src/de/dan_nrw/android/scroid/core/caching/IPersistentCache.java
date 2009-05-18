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

import java.io.IOException;
import java.net.URI;

import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public interface IPersistentCache {

	/**
	 * Checks if image is already in cache.
	 * @param uri URI of image (key)
	 * @param prefix Prefix used for creating unique filenames
	 * @return true (cached) / false (not cached) 
	 */
	boolean isInCache(URI uri, String prefix);
	
	/**
	 * Gets image from cache.
	 * @param uri URI of image (key)
	 * @param prefix Prefix used for creating unique filenames
	 * @return Image loaded from cache
	 * @throws IOException
	 */
	Bitmap get(URI uri, String prefix) throws IOException;
	
	/**
	 * Adds image to cache.
	 * @param bitmap Image to cache
	 * @param uri URI of image (key)
	 * @param prefix Prefix used for creating unique filenames
	 * @throws IOException
	 */
	void put(Bitmap bitmap, URI uri, String prefix) throws IOException;
}