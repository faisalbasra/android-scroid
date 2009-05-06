package de.dan_nrw.android.util.ui;

import android.app.Dialog;
import android.content.Context;


/**
 * @author Daniel Czerwonk
 *
 */
public abstract class BaseDialog extends Dialog {

	// Constructors
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

	// Methods
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
