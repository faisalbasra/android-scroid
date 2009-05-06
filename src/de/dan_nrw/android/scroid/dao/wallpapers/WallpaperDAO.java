package de.dan_nrw.android.scroid.dao.wallpapers;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;

import android.content.Context;
import android.graphics.Bitmap;

import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.scroid.Wallpaper;
import de.dan_nrw.android.scroid.dao.wallpapers.parsing.WallpaperParserFactory;
import de.dan_nrw.android.util.net.BitmapHttpResponseHandler;
import de.dan_nrw.android.util.net.TextFileHttpResponseHandler;

/**
 * @author Daniel Czerwonk
 *
 */
public class WallpaperDAO implements IWallpaperDAO {

	// Fields
	private static final int MAX_DOWNLOAD_RETRIES = 10;
	
	// Constructors
	/**
	 * Method for creating instance of WallpaperDAO
	 * @param clientHelper
	 */
	public WallpaperDAO() {
	    super();
    }
	
	// Methods
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.scroid.dao.wallpapers.IWallpaperDAO#getAvailableWallpapers(android.content.Context)
	 */
	public Wallpaper[] getAvailableWallpapers(Context context) throws WallpaperListReceivingException {		
		try {
			String data = this.download(URI.create(context.getString(R.string.galleryUrl)), 
										new TextFileHttpResponseHandler(),
				    					MAX_DOWNLOAD_RETRIES);
            
			List<Wallpaper> wallpapers = WallpaperParserFactory.getInstance().parse(data);
			
			return wallpapers.toArray(new Wallpaper[wallpapers.size()]);
        }
        catch (IllegalStateException ex) {
        	throw new WallpaperListReceivingException(context.getString(R.string.receivingException), ex);
        }
        catch (IOException ex) {
        	throw new WallpaperListReceivingException(context.getString(R.string.receivingException), ex);
        }
        catch (ParseException ex) {
        	throw new WallpaperListReceivingException(context.getString(R.string.parseExceptionText), ex);
        }
	}

	/* (non-Javadoc)
     * @see de.dan_nrw.boobleftboobright.IWallpaperManager#downloadImage(java.net.URI)
     */
    @Override
    public Bitmap downloadImage(URI uri) throws ClientProtocolException, IOException {
	    return this.download(uri, new BitmapHttpResponseHandler(), MAX_DOWNLOAD_RETRIES);
    }
    
    /**
     * Method for downlaoding file
     * @param <T> Type of return
     * @param uri URI of file to download
     * @param responseHandler Response handler parsing the response
     * @param maxRetries Max count of retries before exception
     * @return Parsed object based on download data
     * @throws ClientProtocolException
     * @throws IOException
     */
    private <T> T download(URI uri, ResponseHandler<T> responseHandler, int maxRetries) throws ClientProtocolException, IOException {
    	try {
			DefaultHttpClient client = new DefaultHttpClient();
	    	
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(maxRetries, false));
			
			HttpRequest request = new HttpGet(uri);
			
			return client.execute((HttpUriRequest)request, responseHandler);
		}
		catch (IOException ex) {
			throw ex;
		}
    }
}