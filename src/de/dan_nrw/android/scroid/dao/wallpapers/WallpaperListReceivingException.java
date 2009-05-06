package de.dan_nrw.android.scroid.dao.wallpapers;

/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperListReceivingException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Method for creating a new instance of WallpaperListReceivingException
	 */
	public WallpaperListReceivingException() {
	    super();
    }

	/**
	 * Method for creating a new instance of WallpaperListReceivingException
	 * @param detailMessage
	 * @param throwable
	 */
	public WallpaperListReceivingException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
    }

	/**
	 * Method for creating a new instance of WallpaperListReceivingException
	 * @param detailMessage
	 */
	public WallpaperListReceivingException(String detailMessage) {
	    super(detailMessage);
    }

	/**
	 * Method for creating a new instance of WallpaperListReceivingException
	 * @param throwable
	 */
	public WallpaperListReceivingException(Throwable throwable) {
	    super(throwable);
    }
}