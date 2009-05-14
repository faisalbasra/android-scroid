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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;


/**
 * @author Daniel Czerwonk
 *
 */
public class TextFileHttpResponseHandler extends BaseHttpResponseHandler<String> {

    /* (non-Javadoc)
     * @see de.dan_nrw.android.util.net.BaseHttpResponseHandler#handleResponseInternal(org.apache.http.HttpResponse)
     */
	@Override
    protected String handleResponseInternal(HttpResponse response) throws IOException {
        BufferedReader reader = null;
        
        try {
        	reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        	
        	StringBuilder stringBuilder = new StringBuilder();
        	
        	char[] buffer = new char[1024];
        	int charsRead = 0;
        	
        	while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
        		stringBuilder.append(buffer, 0, charsRead);
        	}
        	
        	return stringBuilder.toString();
        }
        finally {
        	if (reader != null) {
        		reader.close();
        	}
        }
    }
}