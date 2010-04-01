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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import de.dan_nrw.android.scroid.DependencyInjector;
import de.dan_nrw.android.scroid.R;


/**
 * @author Daniel Czerwonk
 *
 */
public class SettingsActivity extends Activity {
	
	private final ISettingsProvider settingsProvider;
	
	
	/**
	 * Creates a new instance of SettingsActivity.
	 */
	public SettingsActivity() {
	    super();
	    
	    this.settingsProvider = DependencyInjector.getInstance(ISettingsProvider.class);
    }
    
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.setTitle(String.format("%s: %s", 
	    							this.getString(R.string.applicationName),
	    							this.getString(R.string.settingsTitle)));
	    this.setContentView(R.layout.settings);
	    
	    this.refreshCacheEditText();
    }
	
	public void onOkButtonClicked(View parent) {
	    this.updateConfig();
	    this.finish();
	}
	
	public void onCancelButtonClicked(View parent) {
	    this.finish();
	}
    
    private void updateConfig() {
    	EditText cacheSizeTextBox = (EditText)this.findViewById(R.id.settingsCacheSizeTextBox);
    	this.settingsProvider.setCacheSize(Long.parseLong(cacheSizeTextBox.getText().toString()));
    }
    
    private void refreshCacheEditText() {
    	EditText cacheSizeTextBox = (EditText)this.findViewById(R.id.settingsCacheSizeTextBox);
	    cacheSizeTextBox.setText(Long.toString(this.settingsProvider.getCacheSize()));
    }
}