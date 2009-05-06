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
	 * Method for getting all available wallpapers
	 * @param context
	 * @return Array of all available wallpapers
	 * @throws WallpaperListReceivingException
	 */
	Wallpaper[] getAvailableWallpapers(Context context) throws WallpaperListReceivingException;
	
	/**
	 * Method for downloading image
	 * @param uri URI to image
	 * @return Image downloaded from uri
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	Bitmap downloadImage(URI uri) throws ClientProtocolException, IOException;
}