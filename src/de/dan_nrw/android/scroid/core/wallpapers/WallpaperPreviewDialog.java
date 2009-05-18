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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.Wallpaper;


/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperPreviewDialog extends Dialog {

	private final Wallpaper wallpaper;
	private final Bitmap bitmap;
	public final static int DIALOG_RESULT_CANCEL = 0;
	public final static int DIALOG_RESULT_OK = 1;
	private int dialogResult;

	
	/**
	 * Creates a new instance of WallpaperPreviewDialog.
	 * @param wallpaper
	 * @param bitmap
	 * @param context
	 */
	public WallpaperPreviewDialog(Wallpaper wallpaper, Bitmap bitmap, Context context) {
	    super(context);
	    
	    this.wallpaper = wallpaper;
	    this.bitmap = bitmap;
    }
    

    public int getDialogResult() {
    	return this.dialogResult;
    }
    
	/* (non-Javadoc)
     * @see android.app.AlertDialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setTitle(this.getContext().getString(R.string.previewTitle));
	    this.setContentView(R.layout.preview);
	    
	    TextView titleTextView = (TextView)this.findViewById(R.id.previewWallpaperTitle);
	    
	    titleTextView.setText(this.wallpaper.getTitle());
	    
	    TextView textTextView = (TextView)this.findViewById(R.id.previewWallpaperText);
	    
	    textTextView.setText(wallpaper.getText());
	    
	    Button cancelButton = (Button)this.findViewById(R.id.previewCancelButton);
	    
	    cancelButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	cancel();
            }
	    });
	    
	    Button okButton = (Button)this.findViewById(R.id.previewOkButton);
	    
	    okButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	dialogResult = DIALOG_RESULT_OK;
            	
            	dismiss();
            }
	    });
	    
	    ImageView imageView = (ImageView)this.findViewById(R.id.previewWallpaperImage);

	    imageView.setImageBitmap(this.bitmap);
    }
}