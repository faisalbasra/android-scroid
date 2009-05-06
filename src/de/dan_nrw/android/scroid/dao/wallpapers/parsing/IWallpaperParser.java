package de.dan_nrw.android.scroid.dao.wallpapers.parsing;

import java.text.ParseException;
import java.util.List;

import de.dan_nrw.android.scroid.Wallpaper;


/**
 * @author Daniel Czerwonk
 *
 */
public interface IWallpaperParser {

	/**
	 * Method for parsing input string
	 * @param data String containing wallpaper information 
	 * @return List of parsed wallpapers
	 * @throws ParseException
	 */
	List<Wallpaper> parse(String data) throws ParseException; 
}
