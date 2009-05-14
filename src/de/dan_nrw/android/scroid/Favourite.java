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

import java.util.Date;



/**
 * @author Daniel Czerwonk
 *
 */
public class Favourite {

	private final Date creationDate;
	private final String wallpaperId;
	private Wallpaper wallpaper;

	
	/**
     * Method for creating a new instance of Favourite
     * @param wallpaperId
     * @param creationDate
     */
    public Favourite(String wallpaperId, Date creationDate) {
	    super();
	    
	    this.wallpaperId = wallpaperId;
	    this.creationDate = creationDate;
    }	

    
    /**
     * @return Wallpaper assigned to favourite
     */
    public Wallpaper getWallpaper() {
    	return this.wallpaper;
    }
    
    /**
     * @param wallpaper Wallpaper to assign to favourite
     */
    public void setWallpaper(Wallpaper wallpaper) {
    	this.wallpaper = wallpaper;
    }

    /**
     * @return Id of wallpaper assigned to favourite
     */
    public String getWallpaperId() {
    	return this.wallpaperId;
    }

	/**
     * @return Date of creation
     */
    public Date getCreationDate() {
    	return this.creationDate;
    }	
}