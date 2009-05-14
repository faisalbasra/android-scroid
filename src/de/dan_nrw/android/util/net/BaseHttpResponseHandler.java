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