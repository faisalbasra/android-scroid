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
package de.dan_nrw.android.scroid.dao.communications;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People;

import de.dan_nrw.android.scroid.Communication;


/**
 * @author Daniel Czerwonk
 *
 */
public final class CommunicationDAO implements ICommunicationDAO {
	
	private final Context context;

	
	/**
	 * Creates a new instance of CommunicationDAO.
	 * @param context
	 */
	@Inject
	CommunicationDAO(Context context) {
	    super();
	    
	    this.context = context;
    }	
	
	
	/* (non-Javadoc)
	 * @see de.dan_nrw.android.boobleftboobright.dao.ICommunicationDAO#getCommunications(android.net.Uri)
	 */
	public Communication[] getCommunications(Uri personUri) {
		List<Communication> communications = new ArrayList<Communication>();
	    
		ContentResolver contentResolver = this.context.getContentResolver();
		
	    Uri phonesUri = Uri.withAppendedPath(personUri, People.Phones.CONTENT_DIRECTORY); 
	    
	    Cursor phonesCursor = contentResolver.query(phonesUri, 
	    											new String[] { People.Phones.NUMBER }, 
	    											String.format("%s = ?", People.Phones.TYPE), 
	    											new String[] { Integer.toString(People.Phones.TYPE_MOBILE) }, 
	    											null);
	    
	    int numberColumnId = phonesCursor.getColumnIndex(People.Phones.NUMBER);
	    
	    while (phonesCursor.moveToNext()) {
	    	communications.add(new Communication(Communication.Type.Mobile, phonesCursor.getString(numberColumnId)));
	    }
	    
	    Uri contactMethodsUri = Uri.withAppendedPath(personUri, People.ContactMethods.CONTENT_DIRECTORY);
	    
	    Cursor contactMethodsCursor = contentResolver.query(contactMethodsUri,  
	    													new String[] { People.ContactMethods.DATA }, 
	    													String.format("%s = ?", People.ContactMethods.KIND),
	    													new String[] { Integer.toString(Contacts.KIND_EMAIL) }, 
	    													null);
	    
	    int dataColumnId = contactMethodsCursor.getColumnIndex(People.ContactMethods.DATA);
	    
	    while (contactMethodsCursor.moveToNext()) {
	    	communications.add(new Communication(Communication.Type.Email, contactMethodsCursor.getString(dataColumnId)));
	    }
	    
	    return communications.toArray(new Communication[communications.size()]);
	}
}