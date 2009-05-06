package de.dan_nrw.android.util.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;


/**
 * @author Daniel Czerwonk
 *
 */
abstract class BaseHttpResponseHandler<T> implements ResponseHandler<T> {

	/* (non-Javadoc)
     * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
     */
    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
    	if (response.getStatusLine().getStatusCode() >= 400) {
    		throw new HttpErrorException(response.getStatusLine().getStatusCode());
    	}
    	
    	return this.handleResponseInternal(response);
    }
    
    protected abstract T handleResponseInternal(HttpResponse response) throws IOException;
}