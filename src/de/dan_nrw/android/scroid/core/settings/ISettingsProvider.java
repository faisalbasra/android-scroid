package de.dan_nrw.android.scroid.core.settings;


/**
 * @author Daniel Czerwonk
 *
 */
public interface ISettingsProvider {

	
	/**
	 * Method for getting size of persistent cache
	 * @return Max size allowed for persistent cache
	 */
	public long getCacheSize();
	
	/**
	 * Method for setting size of persistent cache
	 * @param cacheSize Max size allowed for persistent cache
	 */
	public void setCacheSize(long cacheSize);
}
