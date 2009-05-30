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
package de.dan_nrw.android.scroid.core.favourites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import de.dan_nrw.android.scroid.DependencyInjector;
import de.dan_nrw.android.scroid.Favourite;
import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.Wallpaper;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperManager;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperPreviewActivity;
import de.dan_nrw.android.scroid.dao.favourites.IFavouriteDAO;
import de.dan_nrw.android.util.threading.LongTimeRunningOperation;
import de.dan_nrw.android.util.ui.AlertDialogFactory;


/**
 * @author Daniel Czerwonk
 *
 */
@SuppressWarnings("deprecation")	// TODO: should be removed when LongTimeRunningOperation is no longer used
public class FavouriteListActivity extends Activity {

	private static List<Favourite> favourites;
	private final WallpaperManager wallpaperManager;
	private final IFavouriteDAO favouriteDAO;
	private Favourite selectedFavourite;
	
	
	/**
	 * Creates a new instance of FavouriteListActivity.
	 * @param context
	 * @param favourites
	 * @param wallpaperManager
	 * @param removeHandler
	 * @param showHandler
	 */
	public FavouriteListActivity() {
	    super();
	    
	    this.favouriteDAO = DependencyInjector.getInstance(IFavouriteDAO.class);
	    this.wallpaperManager = DependencyInjector.getInstance(WallpaperManager.class);
    }
    
	
	public static void showFavouriteListActivity(Activity parent) {
		// reseting cache used for rotation 
		favourites = null;
		
		// starting activity
		parent.startActivity(new Intent(parent, FavouriteListActivity.class));
	}
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setTitle(String.format("%s: %s", 
	    							this.getString(R.string.applicationName), 
	    							this.getString(R.string.favouritesText)));
	    this.setContentView(R.layout.favourite_list);
	    
	    if (favourites != null) {
	    	this.updateFavouritesAdapter();
	    }
	    else {
	    	ProgressDialog progressDialog = new ProgressDialog(this);
	    	progressDialog.setMessage(this.getString(R.string.loadingText));

	    	new InitFavouriteListTask(progressDialog, this).start();
	    }
	    
	    this.initListView();
	    
	    this.findViewById(R.id.closeFavouriteListButton).setOnClickListener(new OnClickListener() {
	    	
			@Override
            public void onClick(View v) {
				finish();
            }
	    });
    }

    private void initListView() {
    	ListView listView = (ListView)this.findViewById(R.id.favouritesListView);
    	
	    final Activity parentActivity = this;
	    
	    listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			/* (non-Javadoc)
             * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	selectedFavourite = (Favourite)parent.getItemAtPosition(position);
            	
            	return false;
            }
	    });
	    listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Favourite favourite = (Favourite)parent.getItemAtPosition(position);
				
				WallpaperPreviewActivity.showPreviewActivity(parentActivity, favourite.getWallpaper());
            }	    	
	    });
	    
	    this.registerForContextMenu(listView);
    }

	/* (non-Javadoc)
     * @see android.app.Dialog#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (this.selectedFavourite == null) {
    		return;
    	}
    	
    	MenuInflater menuInflater = new MenuInflater(this);
    	
    	menuInflater.inflate(R.menu.favourites_context_menu, menu);
    }
    
	/* (non-Javadoc)
     * @see android.app.Dialog#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    if (this.selectedFavourite == null) {
	    	return false;
	    }
	    
	    switch (item.getItemId()) {
	    	case R.id.favouritesRemoveFavouriteMenuItem:
	    		this.removeFavourite();
	    		return true;
	    }
	    
	    return false;
    }

    private void removeFavourite() {
    	this.favouriteDAO.remove(this.selectedFavourite.getWallpaperId());
    	
		favourites.remove(this.selectedFavourite);
		
		this.updateFavouritesAdapter();
    }
    
    private synchronized void updateFavouritesAdapter() {
    	ListView listView = (ListView)this.findViewById(R.id.favouritesListView);
    	
    	listView.setAdapter(new FavouriteListAdapter(favourites, this, this.wallpaperManager));
    }
    
    
    private class InitFavouriteListTask extends LongTimeRunningOperation {
    	
    	private final Context context;
    	
		/**
		 * Creates a new instance of ShowFavouritesTask.
		 * @param progressDialog
		 * @param context
		 */
		public InitFavouriteListTask(Dialog progressDialog, Context context) {
	        super(progressDialog);
	        
	        this.context = context;
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(android.os.Message)
         */
        @Override
        @SuppressWarnings("unchecked")
        public void afterOperationSuccessfullyCompleted(Message message) {
        	if (!(message.obj instanceof List)) {
        		return;
        	}
        	
        	List<Favourite> tempFavourites = (List<Favourite>)message.obj;
        	
        	if (tempFavourites.size() < 1) {
        		AlertDialogFactory.showInfoMessage(this.context, R.string.infoText, R.string.noFavouritesDefinedText);
        		
        		finish();
        		
        		return;
        	}
        	
        	favourites = tempFavourites;
        	
        	updateFavouritesAdapter();
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Thread, java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Thread thread, Throwable ex) {
        	if (ex instanceof IOException) {
            	AlertDialogFactory.showErrorMessage(this.context, R.string.errorText, R.string.downloadException);
            	
            	finish();
        	}
        	else {
        		throw new RuntimeException(ex);
        	}
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#onRun(android.os.Message)
         */
        @Override
        public void onRun(Message message) throws Exception {
        	List<Favourite> favourites = new ArrayList<Favourite>();
        	
        	for (Favourite favourite : favouriteDAO.getAll()) {
        		Wallpaper wallpaper = this.findWallpaperForFavourite(favourite);
        		
        		if (wallpaper == null) {
        			continue;
        		}
        		
        		favourite.setWallpaper(wallpaper);
        		
        		favourites.add(favourite);
        		
        		wallpaperManager.getThumbImage(wallpaper);
        	}
        	
        	message.obj = favourites;
        }
        
        private Wallpaper findWallpaperForFavourite(Favourite favourite) {
        	for (Wallpaper wallpaper : wallpaperManager.getWallpapers()) {
        		if (wallpaper.getId().equals(favourite.getWallpaperId())) {
        			return wallpaper;
        		}
        	}
        	
        	return null;
        }
    }
}