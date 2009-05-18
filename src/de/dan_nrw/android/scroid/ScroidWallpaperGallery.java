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
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
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
import de.dan_nrw.android.scroid.core.caching.FileSystemCachingProvider;
import de.dan_nrw.android.scroid.core.communications.CommunicationChooseDialog;
import de.dan_nrw.android.scroid.core.communications.CommunicationChooseDialog.CommunicationChosenListener;
import de.dan_nrw.android.scroid.core.favourites.FavouriteListDialog;
import de.dan_nrw.android.scroid.core.favourites.FavouriteListDialog.FavouriteActionHandler;
import de.dan_nrw.android.scroid.core.settings.ISettingsProvider;
import de.dan_nrw.android.scroid.core.settings.SettingsDialog;
import de.dan_nrw.android.scroid.core.settings.SharedPreferencesSettingsProvider;
import de.dan_nrw.android.scroid.core.wallpapers.IWallpaperUpdater;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperGalleryAdapter;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperManager;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperPreviewDialog;
import de.dan_nrw.android.scroid.core.wallpapers.WallpaperUpdater;
import de.dan_nrw.android.scroid.dao.communications.CommunicationDAO;
import de.dan_nrw.android.scroid.dao.communications.ICommunicationDAO;
import de.dan_nrw.android.scroid.dao.favourites.FavouriteDAO;
import de.dan_nrw.android.scroid.dao.favourites.IFavouriteDAO;
import de.dan_nrw.android.scroid.dao.wallpapers.WallpaperDAO;
import de.dan_nrw.android.scroid.dao.wallpapers.WallpaperListReceivingException;
import de.dan_nrw.android.util.threading.LongTimeRunningOperation;
import de.dan_nrw.android.util.ui.AlertDialogFactory;

/**
 * @author Daniel Czerwonk
 *
 */
public class ScroidWallpaperGallery extends Activity {

	private static WallpaperGalleryAdapter wallpaperGalleryAdapter;
	private final WallpaperManager wallpaperManager;
	private final IWallpaperUpdater wallpaperUpdater;
	private final ICommunicationDAO communicationDAO;
	private final IFavouriteDAO favouriteDAO;
	private final ISettingsProvider settingsProvider;
	private final List<Integer> preloadedList;
	private Wallpaper selectedWallpaper;
	
	private static final int PICK_CONTACT = 0;

	
    /**
     * Creates a new instance of ScroidWallpaperGallery.
     */
    public ScroidWallpaperGallery() {
	    super();
	    
	    this.settingsProvider = new SharedPreferencesSettingsProvider(this);
	    this.wallpaperManager = new WallpaperManager(new WallpaperDAO(), new FileSystemCachingProvider(this, this.settingsProvider));
	    this.wallpaperUpdater = new WallpaperUpdater(this);
	    this.communicationDAO = new CommunicationDAO(this);
	    this.favouriteDAO = new FavouriteDAO(this);
	    this.preloadedList = new ArrayList<Integer>();
    }
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        this.setTheme(android.R.style.Theme_NoTitleBar);
        
        this.setContentView(R.layout.main);
        
        // initializing gallery
        Gallery gallery = (Gallery)this.findViewById(R.id.gallery);
        
        this.initGallery(gallery);
        
        // filling gallery
        ProgressDialog progressDialog = new ProgressDialog(this);
        
        progressDialog.setMessage(this.getString(R.string.loadingText));
        
        if (wallpaperGalleryAdapter != null) {
        	gallery.setAdapter(wallpaperGalleryAdapter);
        	
        	return;
        }
        
        new FillGalleryTask(progressDialog, this, gallery).start();
    }
	
	private void initGallery(Gallery gallery) {
		gallery.setOnItemClickListener(new OnItemClickListener() {

			/* (non-Javadoc)
             * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                	
            	showWallpaperPreview((Wallpaper)parent.getItemAtPosition(position));
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
	
	private void showWallpaperPreview(Wallpaper wallpaper) {
		ProgressDialog progressDialog = new ProgressDialog(this);
		
		progressDialog.setMessage(this.getString(R.string.preparingText));
		
		new ShowPreviewDialogTask(progressDialog, 
								  this, 
								  wallpaper, 
								  this.wallpaperUpdater, 
								  this.wallpaperManager).start();	
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
	    		this.showAboutDialog();
	    		return true;
	    		
	    	case R.id.settingsMenuItem:
	    		this.showSettingsDialog();
	    		return true;
	    		
	    	case R.id.recommendMenuItem:
	    		this.recommendWallpaper();
	    		return true;
	    		
	    	case R.id.favouritesMenuItem:
	    		this.showFavouritesDialog();
	    		return true;
	    		
	    	case R.id.closeMenuItem:
	    		this.finish();
	    		return true;
	    }
	    
	    return false;
    }

	private void showAboutDialog() {
    	AboutDialog aboutDialog = new AboutDialog(this);
    	
    	aboutDialog.show();
    }
	
    private void showSettingsDialog() {
    	final SettingsDialog settingsDialog = new SettingsDialog(this, this.settingsProvider.getCacheSize());
    	
    	settingsDialog.setOnDismissListener(new OnDismissListener() {

			/* (non-Javadoc)
             * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
             */
            @Override
            public void onDismiss(DialogInterface dialog) {
            	settingsProvider.setCacheSize(settingsDialog.getCacheSize());
            }
    	});
    	
    	settingsDialog.show();
    }

    private void showFavouritesDialog() {
    	ProgressDialog progressDialog = new ProgressDialog(this);
    	
    	progressDialog.setMessage(this.getString(R.string.loadingText));
    	
    	FavouriteActionHandler showPreviewHandler = new FavouriteActionHandler() {

			@Override
            public void handle(Favourite favourite) {
				showWallpaperPreview(favourite.getWallpaper());
            }
    	};
    	
    	new ShowFavouritesTask(progressDialog, 
    						   this, 
    						   this.favouriteDAO, 
    						   this.wallpaperManager, 
    						   wallpaperGalleryAdapter.getWallpapers(),
    						   showPreviewHandler).start();
    }
    
    private void recommendWallpaper() {
    	if (this.selectedWallpaper == null) {
    		return;
    	}

    	Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
    	
    	this.startActivityForResult(intent, 0);
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
             * @see de.dan_nrw.boobleftboobright.CommunicationChooseDialog.CommunicationChosenListener#onCommunicationChooen(de.dan_nrw.boobleftboobright.Communication)
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

    
    private class FillGalleryTask extends LongTimeRunningOperation {

    	private final Context context;
    	private final Gallery gallery;
    	
		/**
		 * Creates a new instance of FillGalleryTask.
		 * @param progressDialog
		 * @param context
		 * @param gallery
		 */
		public FillGalleryTask(Dialog progressDialog, Context context, Gallery gallery) {
	        super(progressDialog);
	        
	        this.context = context;
	        this.gallery = gallery;
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(android.os.Message)
         */
        @Override
        public void afterOperationSuccessfullyCompleted(Message message) {
        	if (message.obj == null || !(message.obj instanceof Wallpaper[])) {
        		return;
        	}
        	
        	wallpaperGalleryAdapter = new WallpaperGalleryAdapter(context, (Wallpaper[])message.obj, wallpaperManager);
        	
	        gallery.setAdapter(wallpaperGalleryAdapter);
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Thread, java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Thread thread, Throwable ex) {
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
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#onRun(android.os.Message)
         */
        @Override
        public void onRun(Message message) throws Exception {
        	// geting available wallpapers from server
            Wallpaper[] wallpapers = wallpaperManager.getAvailableWallpapers(getBaseContext());

            // preloading first 3 thumbs
            preloadThumbs(wallpapers, 0, 3);
            
            message.obj = wallpapers;
        }
    }
    
    private static class ShowPreviewDialogTask extends LongTimeRunningOperation {

    	private final Context context;
    	private final Wallpaper wallpaper;
    	private final IWallpaperUpdater wallpaperUpdater;
    	private final WallpaperManager wallpaperManager;
    	
		/**
		 * Creates a new instance of ShowPreviewDialogTask.
		 * @param progressDialog
		 * @param context
		 * @param wallpaper
		 * @param wallpaperUpdater
		 * @param wallpaperManager
		 */
		public ShowPreviewDialogTask(Dialog progressDialog, Context context, Wallpaper wallpaper, IWallpaperUpdater wallpaperUpdater, WallpaperManager wallpaperManager) {
	        super(progressDialog);
	        
	        this.context = context;
	        this.wallpaper = wallpaper;
	        this.wallpaperUpdater = wallpaperUpdater;
	        this.wallpaperManager = wallpaperManager;
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(android.os.Message)
         */
        @Override
        public void afterOperationSuccessfullyCompleted(Message message) {
        	final WallpaperPreviewDialog dialog = new WallpaperPreviewDialog(wallpaper, (Bitmap)message.obj, context);
			
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
	            public void onDismiss(DialogInterface sender) {
					if (dialog.getDialogResult() != WallpaperPreviewDialog.DIALOG_RESULT_OK) {
						return;
					}
					
					downlaodAndSetWallpaper();
	            }		
			});
			
			dialog.show();
        }
        
        private void downlaodAndSetWallpaper() {
			ProgressDialog progressDialog = new ProgressDialog(context);
			
			progressDialog.setMessage(context.getString(R.string.applyWallpaperText));

			new DownloadAndSetWallpaperTask(progressDialog, 
											this.context, 
											this.wallpaper, 
											this.wallpaperUpdater, 
											this.wallpaperManager).start();
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Thread, java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Thread thread, Throwable ex) {
        	if (ex instanceof IOException) {
        		AlertDialogFactory.showErrorMessage(context, R.string.errorText, R.string.downloadException);
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
        	message.obj = wallpaperManager.getPreviewImage(wallpaper);
        }
    }
    
    private static class DownloadAndSetWallpaperTask extends LongTimeRunningOperation {

    	private final Context context;
    	private final Wallpaper wallpaper;
    	private final IWallpaperUpdater wallpaperUpdater;
    	private final WallpaperManager wallpaperManager;

        /**
         * Creates a new instance of DownloadAndSetWallpaperTask.
         * @param progressDialog
         * @param context
         * @param wallpaper
         * @param wallpaperUpdater
         * @param wallpaperManager
         */
        public DownloadAndSetWallpaperTask(Dialog progressDialog, Context context, Wallpaper wallpaper, IWallpaperUpdater wallpaperUpdater, WallpaperManager wallpaperManager) {
	        super(progressDialog);
	        
	        this.context = context;
	        this.wallpaper = wallpaper;
	        this.wallpaperUpdater = wallpaperUpdater;
	        this.wallpaperManager = wallpaperManager;
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(android.os.Message)
         */
        @Override
        public void afterOperationSuccessfullyCompleted(Message message) {
        	Intent intent = new Intent(Intent.ACTION_MAIN);
            
            intent.addCategory(Intent.CATEGORY_HOME);
            
            this.context.startActivity(intent);
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Thread, java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Thread thread, Throwable ex) {
        	if (ex instanceof IOException) {
            	AlertDialogFactory.showErrorMessage(context, R.string.errorText, R.string.downloadException);	
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
        	Bitmap bitmap = wallpaperManager.getWallpaperImage(this.wallpaper);
			
			this.wallpaperUpdater.setWallpaper(bitmap);
        }
    }
    
    private static class ShowFavouritesTask extends LongTimeRunningOperation {

    	private final Context context;
    	private final IFavouriteDAO favouriteDAO;
    	private final WallpaperManager wallpaperManager;
    	private final Wallpaper[] wallpapers;
    	private final FavouriteActionHandler showPreviewHandler;
    	
		/**
         * Method for creating a new instance of ShowFavouritesTask
         * @param progressDialog
         * @param context
         * @param favouriteDAO
         * @param wallpaperManager
         * @param wallpapers
         * @param showPreviewHandler
         */
        public ShowFavouritesTask(Dialog progressDialog, Context context, IFavouriteDAO favouriteDAO, WallpaperManager wallpaperManager, Wallpaper[] wallpapers, FavouriteActionHandler showPreviewHandler) {
	        super(progressDialog);
	        
	        this.context = context;
	        this.favouriteDAO = favouriteDAO;
	        this.wallpaperManager = wallpaperManager;
	        this.wallpapers = wallpapers;
	        this.showPreviewHandler = showPreviewHandler;
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(android.os.Message)
         */
        @SuppressWarnings("unchecked")
        @Override
        public void afterOperationSuccessfullyCompleted(Message message) {
        	if (!(message.obj instanceof List)) {
        		return;
        	}
        	
        	List<Favourite> favourites = (List<Favourite>)message.obj;
        	
        	if (favourites.size() < 1) {
        		AlertDialogFactory.showInfoMessage(context, R.string.infoText, R.string.noFavouritesDefinedText);
        		
        		return;
        	}
        	
        	FavouriteActionHandler removeHandler = new FavouriteActionHandler() {

				@Override
                public void handle(Favourite favourite) {
					if (favourite.getWallpaper() != null) {
                		favouriteDAO.remove(favourite.getWallpaper().getId());	
                	}	
                }
        	};
        	
        	FavouriteListDialog dialog = new FavouriteListDialog(this.context, 
        														 favourites, 
        														 this.wallpaperManager, 
        														 removeHandler, 
        														 this.showPreviewHandler);
        	
        	dialog.show();
        }

		/* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Thread, java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Thread thread, Throwable ex) {
        	if (ex instanceof IOException) {
            	AlertDialogFactory.showErrorMessage(context, 
													R.string.errorText, 
													R.string.downloadException);	
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
        		
        		this.wallpaperManager.getThumbImage(wallpaper);
        	}
        	
        	message.obj = favourites;
        }
        
        private Wallpaper findWallpaperForFavourite(Favourite favourite) {
        	for (Wallpaper wallpaper : this.wallpapers) {
        		if (wallpaper.getId().equals(favourite.getWallpaperId())) {
        			return wallpaper;
        		}
        	}
        	
        	return null;
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