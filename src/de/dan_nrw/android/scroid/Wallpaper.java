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

import java.net.URI;


/**
 * @author Daniel Czerwonk
 *
 */
public class Wallpaper {

	private final String id;
	private final String title;
	private final URI thumbUrl;
	private final URI previewUrl;
	private final URI wallpaperUrl;
	private final String text;

	
    /**
     * Method for creating a new instance of Wallpaper
     * @param id
     * @param title
     * @param thumbUrl
     * @param previewUrl
     * @param wallpaperUrl
     * @param text
     */
    public Wallpaper(String id, String title, URI thumbUrl, URI previewUrl, URI wallpaperUrl, String text) {
	    super();
	    
	    this.id = id;
	    this.title = title;
	    this.thumbUrl = thumbUrl;
	    this.previewUrl = previewUrl;
	    this.wallpaperUrl = wallpaperUrl;
	    this.text = text;
    }
	
    
    /**
     * @return Unique id of wallpaper
     */
    public String getId() {
    	return this.id;
    }
    
    /**
     * @return Title
     */
    public String getTitle() {
    	return this.title;
    }

	/**
     * @return Url to thumb image
     */
    public URI getThumbUrl() {
    	return this.thumbUrl;
    }
	
    /**
     * @return Url to preview image
     */
    public URI getPreviewUrl() {
    	return this.previewUrl;
    }
	
    /**
     * @return Url to image
     */
    public URI getWallpaperUrl() {
    	return this.wallpaperUrl;
    }
	
    /**
     * @return Text including copyright hints
     */
    public String getText() {
    	return this.text;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	    return this.title;
    }
}