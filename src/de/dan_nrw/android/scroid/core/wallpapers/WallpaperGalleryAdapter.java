package de.dan_nrw.android.scroid.core.wallpapers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.Wallpaper;
import de.dan_nrw.android.util.ui.AlertDialogFactory;


/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperGalleryAdapter extends BaseAdapter {

	// Fields
	private final Context context;
	private final Wallpaper[] wallpapers;
	private final WallpaperManager wallpaperManager;
	
	// Constructors
	/**
	 * Method for creating a new instance of WallpaperGalleryAdapter
	 * @param context
	 * @param wallpapers
	 * @param wallpaperManager
	 */
	public WallpaperGalleryAdapter(Context context, Wallpaper[] wallpapers, WallpaperManager wallpaperManager) {
	    super();
	    
	    this.context = context;
	    this.wallpapers = wallpapers;
	    this.wallpaperManager = wallpaperManager;
    }

    // Methods
    /**
     * @return the wallpapers
     */
    public Wallpaper[] getWallpapers() {
    	return this.wallpapers;
    }    
    
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.wallpapers.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return this.wallpapers[position];
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Wallpaper wallpaper = this.wallpapers[position];
		
		ImageView imageView = new ImageView(this.context);
		
		try {
			imageView.setImageBitmap(this.wallpaperManager.getThumbImage(wallpaper));
        }
        catch (ClientProtocolException ex) {
        	AlertDialogFactory.showErrorMessage(this.context, R.string.errorText, R.string.downloadException);
        }
        catch (IOException ex) {
        	AlertDialogFactory.showErrorMessage(this.context, R.string.errorText, R.string.downloadException);
        }
		
		return imageView;
	}
}