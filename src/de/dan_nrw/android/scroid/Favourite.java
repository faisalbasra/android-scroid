package de.dan_nrw.android.scroid;

import java.util.Date;



/**
 * @author Daniel Czerwonk
 *
 */
public class Favourite {

	// Fields
	private final Date creationDate;
	private final String wallpaperId;
	private Wallpaper wallpaper;
	
	// Constructors
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

    // Methods
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