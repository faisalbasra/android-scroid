package de.dan_nrw.android.scroid.core.favourites;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import de.dan_nrw.android.scroid.Favourite;
import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperManager;
import de.dan_nrw.android.util.ui.BaseDialog;


/**
 * @author Daniel Czerwonk
 *
 */
public class FavouriteListDialog extends BaseDialog {

	// Fields
	private final List<Favourite> favourites;
	private final WallpaperManager wallpaperManager;
	private final FavouriteActionHandler removeHandler;
	private final FavouriteActionHandler showHandler;
	private Favourite selectedFavourite;
	private ListView listView;
	
	// Constructors
	/**
     * Method for creating a new instance of FavouriteListDialog
     * @param context
     */
    public FavouriteListDialog(Context context, List<Favourite> favourites, WallpaperManager wallpaperManager, 
    						   FavouriteActionHandler removeHandler, FavouriteActionHandler showHandler) {
	    super(context);
	    
	    this.favourites = favourites;
	    this.wallpaperManager = wallpaperManager;
	    this.removeHandler = removeHandler;
	    this.showHandler = showHandler;
    }
    
    // Methods
	/* (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setTitle(this.getContext().getString(R.string.favouritesText));
	    
	    this.listView = new ListView(this.getContext());
	    
	    listView.setAdapter(new FavouriteListAdapter(this.favourites, this.getContext(), this.wallpaperManager));
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
				if (showHandler != null) {
					showHandler.handle((Favourite)parent.getItemAtPosition(position));
				}
            }	    	
	    });
	    
	    this.registerForContextMenu(listView);
	    
	    this.setContentView(listView);
    }
    
    /* (non-Javadoc)
     * @see android.app.Dialog#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (this.selectedFavourite == null) {
    		return;
    	}
    	
    	MenuInflater menuInflater = new MenuInflater(this.getContext());
    	
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
	    		if (this.removeHandler != null) {
	    			removeHandler.handle(this.selectedFavourite);
	    		}
	    		
	    		this.favourites.remove(this.selectedFavourite);
	    		
	    		listView.setAdapter(new FavouriteListAdapter(this.favourites, this.getContext(), this.wallpaperManager));
	    		return true;
	    }
	    
	    return false;
    }

    // Inner-Classes
	public static abstract class FavouriteActionHandler {
    	public abstract void handle(Favourite favourite);
    }
}