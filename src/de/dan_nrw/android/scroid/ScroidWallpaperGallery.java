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
package de.dan_nrw.android.scroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.core.communications.CommunicationChooseDialog;
import de.dan_nrw.android.scroid.core.communications.CommunicationChooseDialog.CommunicationChosenListener;
import de.dan_nrw.android.scroid.core.favourites.FavouriteListActivity;
import de.dan_nrw.android.scroid.core.settings.SettingsActivity;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperGalleryAdapter;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperManager;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperPreviewActivity;
import de.dan_nrw.android.scroid.dao.communications.ICommunicationDAO;
import de.dan_nrw.android.scroid.dao.favourites.IFavouriteDAO;
import de.dan_nrw.android.scroid.dao.wallpapers.WallpaperListReceivingException;
import de.dan_nrw.android.util.threading.LongTimeRunningOperation;
import de.dan_nrw.android.util.ui.AlertDialogFactory;

/**
 * @author Daniel Czerwonk
 *
 */
public class ScroidWallpaperGallery extends Activity {

	private WallpaperGalleryAdapter wallpaperGalleryAdapter;
	private final WallpaperManager wallpaperManager;
	private final ICommunicationDAO communicationDAO;
	private final IFavouriteDAO favouriteDAO;
	private final List<Integer> preloadedList;
	private Wallpaper selectedWallpaper;
	
	private static final int PICK_CONTACT = 0;
	private static final int DIALOG_ABOUT = 0;

	
    /**
     * Creates a new instance of ScroidWallpaperGallery.
     */
    public ScroidWallpaperGallery() {
	    super();
	    
	    if (!DependencyInjector.isInitialized()) {
	    	DependencyInjector.init(this);	
	    }
	    
	    this.wallpaperManager = DependencyInjector.getInstance(WallpaperManager.class);
	    this.communicationDAO = DependencyInjector.getInstance(ICommunicationDAO.class);
	    this.favouriteDAO = DependencyInjector.getInstance(IFavouriteDAO.class);
	    
	    this.preloadedList = new ArrayList<Integer>();
    }
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        this.setTheme(android.R.style.Theme_NoTitleBar);
        this.setContentView(R.layout.main);
        
        // initializing gallery
        this.initGallery();
        
        // filling gallery
        ProgressDialog progressDialog = new ProgressDialog(this);     
        progressDialog.setMessage(this.getString(R.string.loadingText));
        
        if (this.wallpaperGalleryAdapter != null) {
        	this.updateGalleryAdapter();
        	
        	return;
        }
        
        new FillGalleryTask(progressDialog, this).start();
    }
	
	private void updateGalleryAdapter() {
		this.updateGalleryAdapter(this.wallpaperManager.getWallpapers());
	}
	
    private synchronized void updateGalleryAdapter(Wallpaper[] wallpapers) {
        this.wallpaperGalleryAdapter = new WallpaperGalleryAdapter(this, wallpapers, this.wallpaperManager);
        
        Gallery gallery = (Gallery)this.findViewById(R.id.gallery);
        gallery.setAdapter(this.wallpaperGalleryAdapter);
    }
	
	private void initGallery() {
		Gallery gallery = (Gallery)this.findViewById(R.id.gallery);
		
		gallery.setOnItemClickListener(new OnItemClickListener() {

			/* (non-Javadoc)
             * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                	
            	Wallpaper wallpaper = (Wallpaper)parent.getItemAtPosition(position);
            	
            	showPreviewActivity(wallpaper);
            }
        });
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			/* (non-Javadoc)
             * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
            	selectedWallpaper = (Wallpaper)wallpaperGalleryAdapter.getItem(position);
            	
            	new Thread(new Runnable() {
            		
            		@Override
                    public void run() {
                    	preloadThumbs(wallpaperGalleryAdapter.getWallpapers(), (position + 1), 3);
                    }
            	}).start();
            }

			/* (non-Javadoc)
             * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
             */
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            	selectedWallpaper = null;
            } 
        });
        
        this.registerForContextMenu(gallery);
	}
	
	private void showPreviewActivity(Wallpaper wallpaper) {
		WallpaperPreviewActivity.showPreviewActivity(this, wallpaper);
	}
	
	private void preloadThumbs(Wallpaper[] wallpapers, int index, int maxCount) {
		for (int i = index; (i < (index + maxCount)) && (i < wallpapers.length); i++) {
			if (this.preloadedList.contains(i)) {
				continue;
			}
			
			try {
                this.wallpaperManager.getThumbImage(wallpapers[i]);
                
                this.preloadedList.add(i);
            }
            catch (ClientProtocolException ex) {
                // nothing to do - image will be loaded on select
            }
            catch (IOException ex) {
            	// nothing to do - image will be loaded on select
            }
		}
	}

	/* (non-Javadoc)
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (this.selectedWallpaper == null
    			|| !(v instanceof Gallery)) {
    		return;
    	}
    	
    	MenuInflater menuInflater = new MenuInflater(this);
    	menuInflater.inflate(R.menu.gallery_context_menu, menu);
    	
    	if (this.favouriteDAO.isFavourite(this.selectedWallpaper.getId())) {
    		menu.findItem(R.id.galleryRemoveFavouriteMenuItem).setVisible(true);
    	}
    	else {
    		menu.findItem(R.id.galleryAddFavouriteMenuItem).setVisible(true);
    	}
    }

	/* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater menuInflater = new MenuInflater(this);
    	menuInflater.inflate(R.menu.main_menu, menu);
    	
	    return true;
    }

	/* (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	if (this.selectedWallpaper == null) {
    		return false;
    	}
    	
    	switch (item.getItemId()) {
	    	case R.id.galleryAddFavouriteMenuItem:
	    		this.favouriteDAO.add(this.selectedWallpaper.getId());
	    		return true;
	    		
	    	case R.id.galleryRemoveFavouriteMenuItem:
	    		this.favouriteDAO.remove(this.selectedWallpaper.getId());
	    		return true;
	    }
    	
		return false;
    }

	/* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case R.id.aboutMenuItem:
	    		this.showDialog(DIALOG_ABOUT);
	    		return true;
	    		
	    	case R.id.settingsMenuItem:
	    		this.startActivity(new Intent(this, SettingsActivity.class));
	    		return true;
	    		
	    	case R.id.recommendMenuItem:
	    		this.recommendWallpaper();
	    		return true;
	    		
	    	case R.id.favouritesMenuItem:
	    		FavouriteListActivity.showFavouriteListActivity(this);
	    		return true;
	    		
	    	case R.id.closeMenuItem:
	    		this.finish();
	    		return true;
	    }
	    
	    return false;
    }
    
	/* (non-Javadoc)
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    	case DIALOG_ABOUT:
	    		return new AboutDialog(this);
	    		
	    	default:
	    		return null;
	    }
    }
    
    private void recommendWallpaper() {
    	if (this.selectedWallpaper == null) {
    		return;
    	}

    	Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
    	this.startActivityForResult(intent, PICK_CONTACT);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    		case PICK_CONTACT:
    			this.onPickContactActivityResult(resultCode, data);
    			break;
    	}
    }
    
    private void onPickContactActivityResult(int resultCode, Intent data) {
	    if (resultCode == 0) {
	    	return;
	    }

	    Communication[] communications = this.communicationDAO.getCommunications(data.getData());
	    
	    if (communications.length < 1) {
	    	AlertDialogFactory.showInfoMessage(this, R.string.infoText, R.string.noCommunicationFoundInfoText);
	    	
	    	return;
	    }
	    
	    CommunicationChooseDialog dialog = new CommunicationChooseDialog(this, communications, new CommunicationChosenListener() {

            /* (non-Javadoc)
             * @see de.dan_nrw.android.scroid.core.communications.CommunicationChooseDialog.CommunicationChosenListener#onCommunicationChosen(de.dan_nrw.android.scroid.Communication)
             */
            @Override
            public void onCommunicationChosen(Communication communication) {
            	handleOnCommunicationChosen(communication);
            }
	    });
	    
	    dialog.show();
    }
    
    private void handleOnCommunicationChosen(Communication communication) {
    	Wallpaper wallpaper = this.selectedWallpaper;
    	
    	if (communication.getType().equals(Communication.Type.Email)) {
    		Intent intent = new Intent(Intent.ACTION_SEND);
    		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { communication.getValue() });
    		intent.putExtra(Intent.EXTRA_SUBJECT, getBaseContext().getString(R.string.applicationName));
    		intent.putExtra(Intent.EXTRA_TEXT, String.format(getBaseContext().getString(R.string.recommendEmailPattern), 
    														 wallpaper.getWallpaperUrl()));
    		intent.setType("message/rfc822");
    		
    		this.startActivity(intent);            		
    	}
    	else if (communication.getType().equals(Communication.Type.Mobile)) {
    		Intent intent = new Intent(Intent.ACTION_VIEW);
    		intent.putExtra("address", communication.getValue());
    		intent.putExtra("sms_body", String.format(getBaseContext().getString(R.string.recommendSmsPattern), 
    												  wallpaper.getWallpaperUrl()));
    		intent.setType("vnd.android-dir/mms-sms");
    		
    		this.startActivity(intent);
    	}
    }

    
    private class FillGalleryTask extends LongTimeRunningOperation<Wallpaper[]> {

    	private final Context context;
    	
		/**
		 * Creates a new instance of FillGalleryTask.
		 * @param progressDialog
		 * @param context
		 */
		public FillGalleryTask(Dialog progressDialog, Context context) {
	        super(progressDialog);
	        
	        this.context = context;
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(java.lang.Object)
         */
        @Override
        public void afterOperationSuccessfullyCompleted(Wallpaper[] result) {
        	updateGalleryAdapter(result);
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Throwable ex) {
        	if (ex instanceof WallpaperListReceivingException) {
        		AlertDialogFactory.showErrorMessage(this.context, 
        											R.string.errorText, 
        											ex.getMessage(), 
        											new ShutDownAlertDialogOnClickListener());
        	} 
        	else if (ex instanceof IOException) {
        		AlertDialogFactory.showErrorMessage(this.context,
        											R.string.errorText, 
        											R.string.downloadException, 
        											new ShutDownAlertDialogOnClickListener());
        	}
        	else {
        		throw new RuntimeException(ex);
        	}
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#onRun()
         */
        @Override
        public Wallpaper[] onRun() throws Exception {
        	// retrieving available wallpapers from server
        	wallpaperManager.loadAvailableWallpapers(getBaseContext());
        	
            Wallpaper[] wallpapers = wallpaperManager.getWallpapers();

            // preloading first 3 thumbs
            preloadThumbs(wallpapers, 0, 3);
            
            return wallpapers;
        }
    }

	private class ShutDownAlertDialogOnClickListener implements DialogInterface.OnClickListener {

		/* (non-Javadoc)
         * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
        	dialog.dismiss();
        	
        	finish();
        }
    }
}