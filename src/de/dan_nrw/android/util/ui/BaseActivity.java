package de.dan_nrw.android.util.ui;

import android.app.Activity;


/**
 * @author Daniel Czerwonk
 *
 */
public abstract class BaseActivity extends Activity {

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
