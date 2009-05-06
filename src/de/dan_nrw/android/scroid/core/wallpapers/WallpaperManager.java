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

	// Fields
	private final IWallpaperDAO wallpaperDAO;
	private final IPersistentCache persistentWallpaperCache;
	
	// Constructors
	/**
	 * Method for creating a new instance of WallpaperManager
	 * @param wallpaperDAO
	 */
	public WallpaperManager(IWallpaperDAO wallpaperDAO, IPersistentCache persistentWallpaperCache) {
	    super();
	    
	    this.wallpaperDAO = wallpaperDAO;
	    this.persistentWallpaperCache = persistentWallpaperCache;
    }	
	
    // Methods
    /**
     * Method for getting list of all available wallpapers
     * @param context
     * @return List of available wallpapers
     * @throws WallpaperListReceivingException
     */
    public Wallpaper[] getAvailableWallpapers(Context context) throws WallpaperListReceivingException {
    	return this.wallpaperDAO.getAvailableWallpapers(context);
    }
    
    /**
     * Method for getting thumb image for wallpaper
     * @param wallpaper Wallpaper containing URI of image
     * @return Image (read out from cache or downloaded)
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Bitmap getThumbImage(Wallpaper wallpaper) throws ClientProtocolException, IOException {
    	return this.getImage(wallpaper.getThumbUrl(), true, "thumb_");
    }
    
    /**
     * Method for getting preivew image for wallpaper
     * @param wallpaper Wallpaper containing URI of image
     * @return Image (read out from cache or downloaded)
     * @throws ClientProtocolException
     * @throws IOException
     */
    public Bitmap getPreviewImage(Wallpaper wallpaper) throws ClientProtocolException, IOException {
    	return this.getImage(wallpaper.getPreviewUrl(), true, "prev_");
    }
    
    /**
     * Method for getting image for wallpaper
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
			
			if (this.persistentWallpaperCache.isInCache(uri, fileNamePrefix)) {
				bitmap = this.persistentWallpaperCache.get(uri, fileNamePrefix);
			} 
			else {
				bitmap = this.wallpaperDAO.downloadImage(uri);
	        	
				this.persistentWallpaperCache.put(bitmap, uri, fileNamePrefix);
			}
			
			WallpaperCache.put(uri, bitmap);
			
			return bitmap;
		}
    }
}