package de.dan_nrw.android.scroid.dao.communications;

import android.net.Uri;

import de.dan_nrw.android.scroid.Communication;


/**
 * @author Daniel Czerwonk
 *
 */
public interface ICommunicationDAO {

	/**
	 * Method for getting communications
	 * @param personUri Content-URI of owning person
	 * @return Array of found communications
	 */
	Communication[] getCommunications(Uri personUri);
}
