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

import android.net.Uri;

import de.dan_nrw.android.scroid.Communication;


/**
 * @author Daniel Czerwonk
 *
 */
public interface ICommunicationDAO {

	/**
	 * Gets communications for person specified by personUri.
	 * @param personUri Content-URI of owning person
	 * @return Array of found communications
	 */
	Communication[] getCommunications(Uri personUri);
}