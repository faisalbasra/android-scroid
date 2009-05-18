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
package de.dan_nrw.android.scroid.dao.wallpapers;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.ClientProtocolException;

import de.dan_nrw.android.scroid.Wallpaper;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public interface IWallpaperDAO {

	/**
	 * Gets all available wallpapers.
	 * @param context
	 * @return Array of all available wallpapers
	 * @throws WallpaperListReceivingException
	 */
	Wallpaper[] getAvailableWallpapers(Context context) throws WallpaperListReceivingException;
	
	/**
	 * Downloads an image specified by uri.
	 * @param uri URI to image
	 * @return Image downloaded from uri
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	Bitmap downloadImage(URI uri) throws ClientProtocolException, IOException;
}