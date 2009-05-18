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
package de.dan_nrw.android.scroid.core.settings;


/**
 * @author Daniel Czerwonk
 *
 */
public interface ISettingsProvider {

	/**
	 * Gets size of persistent cache.
	 * @return Max size allowed for persistent cache
	 */
	public long getCacheSize();
	
	/**
	 * Sets size of persistent cache.
	 * @param cacheSize Max size allowed for persistent cache
	 */
	public void setCacheSize(long cacheSize);
}
