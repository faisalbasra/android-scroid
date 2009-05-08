package de.dan_nrw.android.scroid.core.caching;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dan_nrw.android.scroid.core.settings.ISettingsProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;


/**
 * @author Daniel Czerwonk
 *
 */
public class FileSystemCachingProvider implements IPersistentCache {

	// Fields
	private static final Pattern URI_FILENAME_PATTERN = Pattern.compile("[^/]+$");
	private final Context context;
	private final ISettingsProvider settingsProvider;
	
	// Constructors
	/**
	 * Method for creating a new instance of FileSystemCachingProvider
	 * @param context
	 * @param settingsProvider
	 */
	public FileSystemCachingProvider(Context context, ISettingsProvider settingsProvider) {
	    super();
	    
	    this.context = context;
	    this.settingsProvider = settingsProvider;
    }	
	
    // Methods
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.IPersistentCache#isInCache(java.net.URI)
	 */
	@Override
	public boolean isInCache(URI uri, String prefix) {
		File file = this.context.getFileStreamPath(this.getFileNameForUri(uri, prefix));
        
        return file.exists();
	}
	
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.IPersistentCache#get(java.net.URI)
	 */
	@Override
	public Bitmap get(URI uri, String prefix) throws IOException {
		InputStream fileInputStream = this.context.openFileInput(this.getFileNameForUri(uri, prefix));
		
		return BitmapFactory.decodeStream(fileInputStream);
	}

	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.IPersistentCache#put(android.graphics.Bitmap, java.net.URI)
	 */
	@Override
	public void put(Bitmap bitmap, URI uri, String prefix) throws IOException {
		if (this.isInCache(uri, prefix)) {
			return;
		}
		
		OutputStream stream = this.context.openFileOutput(this.getFileNameForUri(uri, prefix), Context.MODE_PRIVATE); 
		
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		
		long cacheSize = this.settingsProvider.getCacheSize() * 1024; 
		
		if (this.getDirectorySize() > cacheSize) {
			// if used space is larger than configured -> clean up
			this.cleanUp(cacheSize);
		}
	}
	
	private String getFileNameForUri(URI uri, String prefix) {
		Matcher matcher = URI_FILENAME_PATTERN.matcher(uri.getPath());
		
		if (!matcher.find()) {
			throw new IllegalArgumentException("uri");
		}
		
		if (prefix != null) {
			return prefix.concat(matcher.group());
		}
		
		return matcher.group();
	}
	
	private long getDirectorySize() {
		long size = 0;
		
		for (File file : this.context.getFilesDir().listFiles()) {
			size += file.length();
		}
		
		return size;
	}
	
	private void cleanUp(long cacheSize) throws IOException {
		List<File> files = new ArrayList<File>();
		
		for (File file : this.context.getFilesDir().listFiles()) {
			files.add(file);
		}
		
		Collections.sort(files, new Comparator<File>() {

			/* (non-Javadoc)
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(File object1, File object2) {
	            return Long.valueOf(object1.lastModified()).compareTo(object2.lastModified());
            }
		});
		
		long remaining = (this.getDirectorySize() - cacheSize);

		for (int i = 0; (i < files.size()) && (remaining > 0); i++) {
			File file = files.get(i);
			
			remaining -= file.length();
			
			if (!this.context.deleteFile(file.getName())) {
				throw new IOException("Can not delete file!");
			}
		}
	}
}