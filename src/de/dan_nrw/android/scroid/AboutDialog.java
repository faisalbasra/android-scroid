package de.dan_nrw.android.scroid;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.util.ui.BaseDialog;


/**
 * @author Daniel Czerwonk
 *
 */
class AboutDialog extends BaseDialog {

	// Constructors
	/**
	 * Method for creating a new instance of AboutDialog
	 * @param context
	 */
	public AboutDialog(Context context) {
	    super(context);
    }
    
    // Methods
	/* (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setTitle(String.format("%s - %s", 
	    							this.getContext().getString(R.string.applicationName), 
	    							this.getContext().getString(R.string.versionInfo)));
	    
	    this.setContentView(R.layout.about);
	    
	    Button closeButton = this.findView(R.id.aboutCloseButton);
	    
	    closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
            public void onClick(View v) {
				dismiss();
            }
	    });
    }
}