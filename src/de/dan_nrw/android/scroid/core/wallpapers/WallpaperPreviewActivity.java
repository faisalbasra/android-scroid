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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.dan_nrw.android.scroid.DependencyInjector;
import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.Wallpaper;
import de.dan_nrw.android.util.threading.LongTimeRunningOperation;
import de.dan_nrw.android.util.ui.AlertDialogFactory;


/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperPreviewActivity extends Activity {

	private Wallpaper wallpaper;
	private final WallpaperManager wallpaperManager;
	private final IWallpaperUpdater wallpaperUpdater;

	
	/**
	 * Creates a new instance of WallpaperPreviewDialog.
	 * @param wallpaper
	 * @param bitmap
	 * @param context
	 */
	private WallpaperPreviewActivity() {
	    super();
	    
	    this.wallpaperManager = DependencyInjector.getInstance(WallpaperManager.class);
	    this.wallpaperUpdater = DependencyInjector.getInstance(IWallpaperUpdater.class);
    }
    
	
	public static void showPreviewActivity(Activity parent, Wallpaper wallpaper) {
		Intent intent = new Intent(parent, WallpaperPreviewActivity.class);
		intent.putExtra("WallpaperId", wallpaper.getId());
		
		parent.startActivity(intent);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setTitle(this.getString(R.string.previewTitle));
	    this.setContentView(R.layout.preview);
	    
	    String wallpaperId = this.getIntent().getExtras().getString("WallpaperId");
	    this.wallpaper = this.wallpaperManager.getWallpaperById(wallpaperId);
	    
	    if (this.wallpaper == null) {
	    	this.finish();
	    }
	    
	    TextView titleTextView = (TextView)this.findViewById(R.id.previewWallpaperTitle);
	    titleTextView.setText(this.wallpaper.getTitle());
	    
	    TextView textTextView = (TextView)this.findViewById(R.id.previewWallpaperText);
	    textTextView.setText(wallpaper.getText());
	    
	    this.loadImage();
    }
	
	public void onOkButtonClicked(View parent) {
	    this.downlaodAndSetWallpaper();
	}
	
	public void onCancelButtonClicked(View parent) {
        this.finish();
    }
	
	private void loadImage() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(this.getString(R.string.preparingText));
		
		new PreparePreviewTask(progressDialog, this).start();
	}
	
	/**
     * @param obj
     */
    private synchronized void setBitmap(Bitmap bitmap) {
	    ImageView imageView = (ImageView)this.findViewById(R.id.previewWallpaperImage);
	    imageView.setImageBitmap(bitmap);    	
    }
	
    private synchronized void downlaodAndSetWallpaper() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(this.getString(R.string.applyWallpaperText));

		new DownloadAndSetWallpaperTask(progressDialog, this).start();
    }
    
    
    private class PreparePreviewTask extends LongTimeRunningOperation<Bitmap> {

    	private final Context context;
    	
		/**
		 * Creates a new instance of ShowPreviewDialogTask.
		 * @param progressDialog
		 * @param context
		 */
		public PreparePreviewTask(Dialog progressDialog, Context context) {
	        super(progressDialog);
	        
	        this.context = context;
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(java.lang.Object)
         */
        @Override
        public void afterOperationSuccessfullyCompleted(Bitmap result) {
        	setBitmap(result);
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Throwable ex) {
        	if (ex instanceof IOException) {
        		AlertDialogFactory.showErrorMessage(context, R.string.errorText, R.string.downloadException);
        	}
        	else {
        		throw new RuntimeException(ex);
        	}
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#onRun()
         */
        @Override
        public Bitmap onRun() throws Exception {
        	return wallpaperManager.getPreviewImage(wallpaper);
        }
    }
    
    private class DownloadAndSetWallpaperTask extends LongTimeRunningOperation<Bitmap> {

    	private final Context context;

        /**
         * Creates a new instance of DownloadAndSetWallpaperTask.
         * @param progressDialog
         * @param context
         */
        public DownloadAndSetWallpaperTask(Dialog progressDialog, Context context) {
	        super(progressDialog);
	        
	        this.context = context;
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#afterOperationSuccessfullyCompleted(java.lang.Object)
         */
        @Override
        public void afterOperationSuccessfullyCompleted(Bitmap result) {
            try {
                wallpaperUpdater.setWallpaper(result);
                
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                
                startActivity(intent);
            }
            catch (IOException e) {
                AlertDialogFactory.showErrorMessage(context, R.string.errorText, R.string.wallpaperSetFailedErrorMessage);
            }
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#handleUncaughtException(java.lang.Throwable)
         */
        @Override
        public void handleUncaughtException(Throwable ex) {
        	if (ex instanceof IOException) {
            	AlertDialogFactory.showErrorMessage(context, R.string.errorText, R.string.downloadException);	
        	}
        	else {
        		throw new RuntimeException(ex);
        	}
        }

        /* (non-Javadoc)
         * @see de.dan_nrw.android.util.threading.LongTimeRunningOperation#onRun()
         */
        @Override
        public Bitmap onRun() throws Exception {
        	return wallpaperManager.getWallpaperImage(wallpaper);
        }
    }
}