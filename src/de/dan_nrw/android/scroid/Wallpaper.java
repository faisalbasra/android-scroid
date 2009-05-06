package de.dan_nrw.android.scroid;

import java.net.URI;


/**
 * @author Daniel Czerwonk
 *
 */
public class Wallpaper {

	// Fields
	private final String id;
	private final String title;
	private final URI thumbUrl;
	private final URI previewUrl;
	private final URI wallpaperUrl;
	private final String text;
	
	// Constructors
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
	
	// Methods
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