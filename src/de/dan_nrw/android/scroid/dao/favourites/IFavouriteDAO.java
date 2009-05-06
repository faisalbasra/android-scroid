package de.dan_nrw.android.scroid.dao.favourites;

import de.dan_nrw.android.scroid.Favourite;


/**
 * @author Daniel Czerwonk
 *
 */
public interface IFavouriteDAO {

	/**
	 * Method for getting all favourites
	 * @return Array of all favourites
	 */
	Favourite[] getAll();
	
	/**
	 * Method for adding wallpaper as favourite
	 * @param id Id of wallpaper
	 */
	void add(String id);
	
	/**
	 * Method for removing favourite
	 * @param id Id of wallpaper
	 */
	void remove(String id);
	
	/**
	 * Method for checking if wallpaper is favourite
	 * @param id Id of wallpaper
	 * @return true (yes) / false (no)
	 */
	boolean isFavourite(String id);
}
