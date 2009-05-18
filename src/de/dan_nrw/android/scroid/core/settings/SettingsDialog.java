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
package de.dan_nrw.android.scroid.core.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.dan_nrw.android.scroid.R;


/**
 * @author Daniel Czerwonk
 *
 */
public class SettingsDialog extends Dialog {

	private long currentCacheSize;

	
	/**
	 * Creates a new instance of SettingsDialog.
	 * @param context
	 * @param currentCacheSize
	 */
	public SettingsDialog(Context context, long currentCacheSize) {
	    super(context);
	    
	    this.currentCacheSize = currentCacheSize;
    }
    
	
	/* (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setContentView(R.layout.settings);
	    
	    this.setTitle(R.string.settingsTitle);
	    
	    Button okButton = (Button)this.findViewById(R.id.settingsOkButton);
	    
	    okButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	dismiss();
            }
	    });
	    
	    Button cancelButton = (Button)this.findViewById(R.id.settingsCancelButton);
	    
	    cancelButton.setOnClickListener(new View.OnClickListener() {

			/* (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
            	cancel();
            }
	    });
	    
	    EditText cacheSizeTextBox = (EditText)this.findViewById(R.id.settingsCacheSizeTextBox);
	    
	    cacheSizeTextBox.setText(Long.toString(this.currentCacheSize));
    }
    
    public long getCacheSize() {
    	EditText cacheSizeTextBox = (EditText)this.findViewById(R.id.settingsCacheSizeTextBox);
    	
    	if (cacheSizeTextBox.getText().toString().equals("")) {
    		return 0;
    	}
    	
    	return Long.parseLong(cacheSizeTextBox.getText().toString());
    }
}