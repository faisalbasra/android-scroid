package de.dan_nrw.android.util.net;

import java.io.IOException;

import org.apache.http.HttpResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * @author Daniel Czerwonk
 *
 */
public class BitmapHttpResponseHandler extends BaseHttpResponseHandler<Bitmap> {

    /* (non-Javadoc)
     * @see de.dan_nrw.android.util.net.BaseHttpResponseHandler#handleResponseInternal(org.apache.http.HttpResponse)
     */
    @Override
    protected Bitmap handleResponseInternal(HttpResponse response) throws IOException {
    	return BitmapFactory.decodeStream(response.getEntity().getContent());
    }
}