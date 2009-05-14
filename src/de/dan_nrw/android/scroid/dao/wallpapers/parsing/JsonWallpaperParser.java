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