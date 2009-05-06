package de.dan_nrw.android.scroid.core.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.util.ui.BaseDialog;


/**
 * @author Daniel Czerwonk
 *
 */
public class SettingsDialog extends BaseDialog {

	// Fields
	private long currentCacheSize;
	
	// Constructors
	/**
	 * Method for creating a new instance of SettingsDialog
	 * @param context
	 * @param currentCacheSize
	 */
	public SettingsDialog(Context context, long currentCacheSize) {
	    super(context);
	    
	    this.currentCacheSize = currentCacheSize;
    }
    
    // Methods
	/* (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setContentView(R.layout.settings);
	    
	    this.setTitle(R.string.settingsTitle);
	    
	    Button okButton = this.findView(R.id.settingsOkButton);
	    
	    okButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	dismiss();
            }
	    });
	    
	    Button cancelButton = this.findView(R.id.settingsCancelButton);
	    
	    cancelButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	cancel();
            }
	    });
	    
	    EditText cacheSizeTextBox = this.findView(R.id.settingsCacheSizeTextBox);
	    
	    cacheSizeTextBox.setText(Long.toString(this.currentCacheSize));
    }
    
    public long getCacheSize() {
    	EditText cacheSizeTextBox = this.findView(R.id.settingsCacheSizeTextBox);
    	
    	if (cacheSizeTextBox.getText().toString().equals("")) {
    		return 0;
    	}
    	
    	return Long.parseLong(cacheSizeTextBox.getText().toString());
    }
}