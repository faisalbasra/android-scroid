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
package de.dan_nrw.android.util.ui;

import android.app.Dialog;
import android.content.Context;


/**
 * @author Daniel Czerwonk
 *
 */
public abstract class BaseDialog extends Dialog {

    /**
     * Method for creating a new instance of BaseDialog
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
	    super(context, cancelable, cancelListener);
    }

    /**
     * Method for creating a new instance of BaseDialog
     * @param context
     * @param theme
     */
    public BaseDialog(Context context, int theme) {
	    super(context, theme);
    }

	/**
	 * Method for creating a new instance of BaseDialog
	 * @param context
	 */
	public BaseDialog(Context context) {
	    super(context);
    }

	
	/**
	 * Method for finding view by id (casted)
	 * @param <T> Type of view
	 * @param id Id of view
	 * @return Casted view object
	 */
	@SuppressWarnings("unchecked")
    public <T> T findView(int id) {
		return (T)this.findViewById(id);
	}
}
