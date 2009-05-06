package de.dan_nrw.android.scroid.core.wallpapers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.Wallpaper;
import de.dan_nrw.android.util.ui.BaseDialog;


/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperPreviewDialog extends BaseDialog {

	// Fields
	private final Wallpaper wallpaper;
	private final Bitmap bitmap;
	public final static int DIALOG_RESULT_CANCEL = 0;
	public final static int DIALOG_RESULT_OK = 1;
	private int dialogResult;
	
	// Constructors
	/**
	 * Method for creating a new instance of WallpaperPreviewDialog
	 * @param wallpaper
	 * @param bitmap
	 * @param context
	 */
	public WallpaperPreviewDialog(Wallpaper wallpaper, Bitmap bitmap, Context context) {
	    super(context);
	    
	    this.wallpaper = wallpaper;
	    this.bitmap = bitmap;
    }
    
    // Methods
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
	    
	    TextView titleTextView = this.findView(R.id.previewWallpaperTitle);
	    
	    titleTextView.setText(this.wallpaper.getTitle());
	    
	    TextView textTextView = this.findView(R.id.previewWallpaperText);
	    
	    textTextView.setText(wallpaper.getText());
	    
	    Button cancelButton = this.findView(R.id.previewCancelButton);
	    
	    cancelButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	cancel();
            }
	    });
	    
	    Button okButton = this.findView(R.id.previewOkButton);
	    
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
	    
	    ImageView imageView = this.findView(R.id.previewWallpaperImage);

	    imageView.setImageBitmap(this.bitmap);
    }
}