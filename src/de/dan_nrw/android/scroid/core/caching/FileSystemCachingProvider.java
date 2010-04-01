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

import com.google.inject.Inject;

import de.dan_nrw.android.scroid.core.settings.ISettingsProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;


/**
 * @author Daniel Czerwonk
 *
 */
public final class FileSystemCachingProvider implements IPersistentCache {

	private static final Pattern URI_FILENAME_PATTERN = Pattern.compile("[^/]+$");
	private final Context context;
	private final ISettingsProvider settingsProvider;

	
	/**
	 * Creates a new instance of FileSystemCachingProvider.
	 * @param context
	 * @param settingsProvider
	 */
	@Inject
	FileSystemCachingProvider(Context context, ISettingsProvider settingsProvider) {
	    super();
	    
	    this.context = context;
	    this.settingsProvider = settingsProvider;
    }	
	
	
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.scroid.core.caching.IPersistentCache#isInCache(java.net.URI, java.lang.String)
	 */
	@Override
	public boolean isInCache(URI uri, String prefix) {
		File file = this.context.getFileStreamPath(this.getFileNameForUri(uri, prefix));
        
        return file.exists();
	}
	
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.scroid.core.caching.IPersistentCache#get(java.net.URI, java.lang.String)
	 */
	@Override
	public Bitmap get(URI uri, String prefix) throws IOException {
		InputStream fileInputStream = null;
		
		try {
			fileInputStream = this.context.openFileInput(this.getFileNameForUri(uri, prefix));
			
			return BitmapFactory.decodeStream(fileInputStream);
		}
		finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();	
				}
				catch (IOException ex) {
					// nothing to do - exception would hide original exception thrown in try block 
				}	
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.dan_nrw.android.scroid.core.caching.IPersistentCache#put(android.graphics.Bitmap, java.net.URI, java.lang.String)
	 */
	@Override
	public void put(Bitmap bitmap, URI uri, String prefix) throws IOException {
		if (this.isInCache(uri, prefix)) {
			return;
		}
		
		OutputStream stream = null;
		
		try {
			stream = this.context.openFileOutput(this.getFileNameForUri(uri, prefix), Context.MODE_PRIVATE); 
			bitmap.compress(CompressFormat.JPEG, 100, stream);	
		}
		finally {
			if (stream != null) {
				try {
					stream.close();	
				}
				catch (IOException ex) {
					// nothing to do - exception would hide original exception thrown in try block
				}	
			}
		}
		
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
				throw new IOException(String.format("Can not delete file: %s!", file.getName()));
			}
		}
	}
}