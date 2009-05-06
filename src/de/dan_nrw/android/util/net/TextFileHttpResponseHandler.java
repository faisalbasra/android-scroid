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