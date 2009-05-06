package de.dan_nrw.android.scroid.dao.communications;

import java.util.ArrayList;
import java.util.List;

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
public class CommunicationDAO implements ICommunicationDAO {
	
	// Fields
	private final Context context;
	
	// Constructors
	/**
	 * Method for creating a new instance of CommunicationDAO
	 * @param context
	 */
	public CommunicationDAO(Context context) {
	    super();
	    
	    this.context = context;
    }	
	
    // Methods
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