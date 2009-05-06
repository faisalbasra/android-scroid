package de.dan_nrw.android.scroid.core.wallpapers;

import java.io.IOException;

import android.graphics.Bitmap;


/**
 * @author Daniel Czerwonk
 *
 */
public interface IWallpaperUpdater {

	/**
	 * Method for setting new system wallpaper
	 * @param wallpaper Wallpaper to set as new system wallpaper
	 * @throws IOException
	 */
	void setWallpaper(Bitmap wallpaper) throws IOException;
}
