package de.dan_nrw.android.scroid.dao.wallpapers.parsing;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.dan_nrw.android.scroid.Wallpaper;


/**
 * @author Daniel Czerwonk
 *
 */
final class JsonWallpaperParser implements IWallpaperParser {

	/* (non-Javadoc)
	 * @see de.dan_nrw.boobleftboobright.IWallpaperParser#parse(java.lang.String)
	 */
	@Override
	public List<Wallpaper> parse(String data) throws ParseException {
        try {
	        JSONArray array = new JSONArray(data);
	        
			List<Wallpaper> wallpapers = new ArrayList<Wallpaper>();
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonWallpaper = array.getJSONObject(i);
				
				wallpapers.add(new Wallpaper(jsonWallpaper.getString("id"),
											 jsonWallpaper.getString("title"),
											 URI.create(jsonWallpaper.getString("thumburl")),
											 URI.create(jsonWallpaper.getString("previewurl")),
											 URI.create(jsonWallpaper.getString("url")),
											 jsonWallpaper.getString("text")));
			}
			
			return wallpapers;
        }
        catch (JSONException ex) {
        	throw new ParseException(ex.getMessage(), 0);
        }			
	}
}