package de.dan_nrw.android.util.net;

import java.io.IOException;


/**
 * @author Daniel Czerwonk
 *
 */
public class HttpErrorException extends IOException {

    private static final long serialVersionUID = 1L;

	/**
     * Method for creating a new instance of HttpErrorException
     * @param detailMessage
     */
    public HttpErrorException(int errorCode) {
	    super(Integer.toString(errorCode));
    }
}
