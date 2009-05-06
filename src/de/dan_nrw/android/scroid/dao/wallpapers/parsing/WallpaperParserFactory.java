package de.dan_nrw.android.scroid.dao.wallpapers.parsing;


/**
 * @author Daniel Czerwonk
 *
 */
public final class WallpaperParserFactory {

	public static IWallpaperParser getInstance() {
		return new JsonWallpaperParser();
	}
}
