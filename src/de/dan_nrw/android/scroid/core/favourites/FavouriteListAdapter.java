package de.dan_nrw.android.scroid.core.favourites;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.dan_nrw.android.scroid.Favourite;
import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperManager;
import de.dan_nrw.android.util.ui.AlertDialogFactory;

/**
 * @author Daniel Czerwonk
 *
 */
public class FavouriteListAdapter extends BaseAdapter {

	// Fields
	private final List<Favourite> favourites;
	private final Context context;
	private final WallpaperManager wallpaperManager;

	// Constructors
	/**
     * Method for creating a new instance of FavouriteListAdapter
     * @param favourites
     * @param context
     * @param wallpaperManager
     */
    public FavouriteListAdapter(List<Favourite> favourites, Context context, WallpaperManager wallpaperManager) {
	    super();
	    
	    this.favourites = favourites;
	    this.context = context;
	    this.wallpaperManager = wallpaperManager;
    }
	
	// Methods
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.favourites.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return this.favourites.get(position);
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
		Favourite favourite = this.favourites.get(position);
		
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(this.context);
			
	    	convertView = layoutInflater.inflate(R.layout.favourite, null);	
	    }
		
		TextView textView = (TextView)convertView.findViewById(R.id.favouriteTextBox);
		
		textView.setText(favourite.getWallpaper().getTitle());
		
		ImageView imageView = (ImageView)convertView.findViewById(R.id.favouriteImageView);
		
		try {
			imageView.setImageBitmap(this.wallpaperManager.getThumbImage(favourite.getWallpaper()));
        }
        catch (ClientProtocolException ex) {
        	AlertDialogFactory.showErrorMessage(this.context, R.string.errorText, R.string.downloadException);
        }
        catch (IOException ex) {
        	AlertDialogFactory.showErrorMessage(this.context, R.string.errorText, R.string.downloadException);
        }
		
		return convertView;
	}
}