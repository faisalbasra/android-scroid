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

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.dan_nrw.android.scroid.R;


/**
 * @author Daniel Czerwonk
 *
 */
class AboutDialog extends Dialog {

	/**
	 * Creates a new instance of AboutDialog.
	 * @param context
	 */
	public AboutDialog(Context context) {
	    super(context);
    }
	
    
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
	    
	    Button closeButton = (Button)this.findViewById(R.id.aboutCloseButton);
	    
	    closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
            public void onClick(View v) {
				dismiss();
            }
	    });
    }
}