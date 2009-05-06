package de.dan_nrw.android.scroid.core.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import de.dan_nrw.android.scroid.R;


/**
 * @author Daniel Czerwonk
 *
 */
public class SharedPreferencesSettingsProvider implements ISettingsProvider {

	// Fields
	private final Context context;
	private SharedPreferences sharedPreferences;
	private static final String CACHE_SIZE_KEY = "DiskCacheSize";
	
	// Constructors
	/**
     * Method for creating a new instance of SharedPreferencesSettingsProvider
     */
    public SharedPreferencesSettingsProvider(Context context) {
	    super();
	    
	    this.context = context;
    }
	
	// Methods
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.ISettingsProvider#getCacheSize()
	 */
	@Override
	public long getCacheSize() {
		return this.getSharedPreferences().getLong(CACHE_SIZE_KEY, 2048);
	}

	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.ISettingsProvider#setCacheSize(long)
	 */
	@Override
	public synchronized void setCacheSize(long cacheSize) {
		Editor editor = this.getSharedPreferences().edit();
		
		editor.putLong(CACHE_SIZE_KEY, cacheSize);
		
		editor.commit();
	}
	
	private SharedPreferences getSharedPreferences() {
		if (this.sharedPreferences == null) {
			this.sharedPreferences = this.context.getSharedPreferences(context.getString(R.string.applicationName), 0);
		}
		
		return this.sharedPreferences;
	}
}