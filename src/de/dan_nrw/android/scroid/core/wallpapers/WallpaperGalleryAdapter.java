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

	private final Context context;
	private final Wallpaper[] wallpapers;
	private final WallpaperManager wallpaperManager;
	

	/**
	 * Creates a new instance of WallpaperGalleryAdapter.
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