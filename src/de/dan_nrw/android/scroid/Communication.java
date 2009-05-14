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
package de.dan_nrw.android.scroid;


/**
 * @author Daniel Czerwonk
 *
 */
public class Communication {

	private final Type type;
	private final String value;

	
	/**
	 * Method for creating a new instance of Communication
	 * @param type
	 * @param value
	 */
	public Communication(Type type, String value) {
	    super();
	    
	    this.type = type;
	    this.value = value;
    }
    
	
    /**
     * @return Type (Mobile, E-Mail)
     */
    public Type getType() {
	    return type;
    }

	/**
     * @return Address / Number
     */
    public String getValue() {
	    return value;
    }

	public enum Type {
		Mobile,
		Email
	}
}
