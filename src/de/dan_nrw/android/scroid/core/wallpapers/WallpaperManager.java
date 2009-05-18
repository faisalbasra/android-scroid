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
package de.dan_nrw.android.scroid.core.wallpapers;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.graphics.Bitmap;

import de.dan_nrw.android.scroid.Wallpaper;
import de.dan_nrw.android.scroid.core.caching.IPersistentCache;
import de.dan_nrw.android.scroid.core.caching.WallpaperCache;
import de.dan_nrw.android.scroid.dao.wallpapers.IWallpaperDAO;
import de.dan_nrw.android.scroid.dao.wallpapers.WallpaperListReceivingException;


/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperManager {

	private final IWallpaperDAO wallpaperDAO;
	private final IPersistentCache persistentWallpaperCache;
	
	
	/**
	 * Creates a new instance of WallpaperManager.
	 * @param wallpaperDAO
	 * @param persistentWallpaperCache
	 */
	public WallpaperManager(IWallpaperDAO wallpaperDAO, IPersistentCache persistentWallpaperCache) {
	    super();
	    
	    this.wallpaperDAO = wallpaperDAO;
	    this.persistentWallpaperCache = persistentWallpaperCache;
    }	
	
    
	/**
     * Gets list of all available wallpapers.
     * @param context
     * @return List of available wallpapers
     * @throws WallpaperListReceivingException
     */
    public Wallpaper[] getAvailableWallpapers(Context context) throws WallpaperListReceivingException {
    	return this.wallpaperDAO.getAvailableWallpapers(context);
    }
    
    /**
     * Gets thumb image for wallpaper.
     * @param wallpaper Wallpaper containing URI of image
     * @return Image (read out from cache or downloaded)
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Bitmap getThumbImage(Wallpaper wallpaper) throws ClientProtocolException, IOException {
    	return this.getImage(wallpaper.getThumbUrl(), true, "thumb_");
    }
    
    /**
     * Gets preivew image for wallpaper.
     * @param wallpaper Wallpaper containing URI of image
     * @return Image (read out from cache or downloaded)
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Bitmap getPreviewImage(Wallpaper wallpaper) throws ClientProtocolException, IOException {
    	return this.getImage(wallpaper.getPreviewUrl(), true, "prev_");
    }
    
    /**
     * Gets image for wallpaper.
     * @param wallpaper Wallpaper containing URI of image
     * @return Image (read out from cache or downloaded)
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Bitmap getWallpaperImage(Wallpaper wallpaper) throws ClientProtocolException, IOException {
    	return this.getImage(wallpaper.getWallpaperUrl(), false, null);
    }
    
	private Bitmap getImage(URI uri, boolean cachable, String fileNamePrefix) throws ClientProtocolException, IOException {
    	if (!cachable) {
    		return this.wallpaperDAO.downloadImage(uri);
    	}
    	
    	if (WallpaperCache.isInCache(uri)) {
    		return WallpaperCache.get(uri);
    	}
		else {
			Bitmap bitmap = null;
			
			synchronized(this.persistentWallpaperCache) {
				if (this.persistentWallpaperCache.isInCache(uri, fileNamePrefix)) {
					bitmap = this.persistentWallpaperCache.get(uri, fileNamePrefix);
				} 
				
				if (bitmap == null) {
					bitmap = this.wallpaperDAO.downloadImage(uri);
		        	
					this.persistentWallpaperCache.put(bitmap, uri, fileNamePrefix);
				}
			}
			
			WallpaperCache.put(uri, bitmap);
			
			return bitmap;
		}
    }
}