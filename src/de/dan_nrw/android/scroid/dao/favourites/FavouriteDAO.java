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
package de.dan_nrw.android.scroid.dao.favourites;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.inject.Inject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.dan_nrw.android.scroid.Favourite;


/**
 * @author Daniel Czerwonk
 *
 */
public final class FavouriteDAO implements IFavouriteDAO {

	private final Context context;
	private SQLiteDatabase databaseInstance;
	
	private static String TABLE_NAME = "favourites";
	
	private static String WALLPAPER_ID = "WallpaperId";
	private static String DATE = "Date";

	
	/**
	 * Creates a new instance of FavouriteDAO.
	 * @param context
	 */
	@Inject
	FavouriteDAO(Context context) {
		super();
		
		this.context = context;
	}
	
	
	private SQLiteDatabase getDatabaseInstance() {
		if (databaseInstance == null) {
			this.databaseInstance = this.context.openOrCreateDatabase("blbr", SQLiteDatabase.CREATE_IF_NECESSARY, null);
			this.databaseInstance.execSQL("create table if not exists favourites ( WallpaperId VARCHAR(255) NOT NULL PRIMARY KEY, Date INTEGER NOT NULL );");
		}
		
		return databaseInstance;
	}
	
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.dao.IFavouriteDAO#getFavourites()
	 */
	@Override
	public Favourite[] getAll() {
		List<Favourite> favourites = new ArrayList<Favourite>();
		SQLiteDatabase database = this.getDatabaseInstance();
		Cursor cursor = null;
		
		try {
			cursor = database.query(TABLE_NAME, null, null, null, null, null, DATE);
			
			int wallpaperIdColumn = cursor.getColumnIndexOrThrow(WALLPAPER_ID);
			int dateColumn = cursor.getColumnIndexOrThrow(DATE);
			
			while (cursor.moveToNext()) {
				Calendar calendar = Calendar.getInstance();
	        	calendar.setTimeInMillis(cursor.getLong(dateColumn));
	        	
	        	Favourite favourite = new Favourite(cursor.getString(wallpaperIdColumn), calendar.getTime());
	            favourites.add(favourite);
			}
			
			return favourites.toArray(new Favourite[favourites.size()]);
		}
		finally {
			if (cursor != null) {
    			cursor.close();
    		}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.dao.IFavouriteDAO#add(java.lang.String)
	 */
	@Override
	public void add(String id) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(WALLPAPER_ID, id);
		contentValues.put(DATE, Calendar.getInstance().getTimeInMillis());
		
		SQLiteDatabase database = this.getDatabaseInstance();
		database.beginTransaction();

		try {
			database.insertOrThrow(TABLE_NAME, null, contentValues);
			database.setTransactionSuccessful();
		}
		finally {
			database.endTransaction();
		}
	}
	
	/* (non-Javadoc)
     * @see de.dan_nrw.android.boobleftboobright.dao.IFavouriteDAO#remove(java.lang.String)
     */
    @Override
    public void remove(String id) {
    	SQLiteDatabase database = this.getDatabaseInstance();
    	database.beginTransaction();
    	
    	try {
    		database.delete(TABLE_NAME, String.format("%s = ?", WALLPAPER_ID), new String[] { id });
    		database.setTransactionSuccessful();
    	}
    	finally {
    		database.endTransaction();
    	}
    }

	/* (non-Javadoc)
     * @see de.dan_nrw.android.boobleftboobright.dao.IFavouriteDAO#isFavourite(java.lang.String)
     */
    @Override
    public boolean isFavourite(String id) {
    	SQLiteDatabase database = this.getDatabaseInstance();
    	
    	Cursor cursor = null;
    	
    	try {
    		cursor = database.query(TABLE_NAME, 
					   				new String[] { WALLPAPER_ID }, 
					   				String.format("%s = ?", WALLPAPER_ID), 
					   				new String[] { id }, 
					   				null, 
					   				null, 
					   				null);

    		return cursor.moveToNext();	
    	}
    	finally {
    		if (cursor != null) {
    			cursor.close();
    		}
    	}
    }
}