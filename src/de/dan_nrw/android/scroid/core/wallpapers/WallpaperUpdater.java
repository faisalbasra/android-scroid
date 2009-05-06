package de.dan_nrw.android.scroid.core.wallpapers;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperUpdater implements IWallpaperUpdater {

	// Fields
	private final Context context;
	
	/**
	 * Method for creating a new instance of WallpaperUpdater
	 * @param context
	 */
	public WallpaperUpdater(Context context) {
	    super();
	    
	    this.context = context;
    }

    // Methods
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.scroid.core.wallpapers.IWallpaperUpdater#setWallpaper(android.graphics.Bitmap)
	 */
	public void setWallpaper(Bitmap bitmap) throws IOException {		
        this.context.setWallpaper(bitmap);
	} 
}
