package de.dan_nrw.android.util.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * @author Daniel Czerwonk
 *
 */
public final class AlertDialogFactory {

	public static void showErrorMessage(Context context, int titleResId, int messageResId) {
		showErrorMessage(context, titleResId, messageResId, null);
	}
	
	public static void showErrorMessage(Context context, int titleResId, String message) {
		showErrorMessage(context, titleResId, message, null);
	}
	
	public static void showErrorMessage(Context context, int titleResId, int messageResId, 
										DialogInterface.OnClickListener okButtonListener) {
		showErrorMessage(context, titleResId, context.getString(messageResId), okButtonListener);
	}
	
	public static void showErrorMessage(Context context, int titleResId, String message, 
										DialogInterface.OnClickListener okButtonListener) {
		showAlertDialog(context, titleResId, message, okButtonListener, android.R.drawable.ic_dialog_alert);
	}
	
	public static void showInfoMessage(Context context, int titleResId, int messageResId) {
		showInfoMessage(context, titleResId, messageResId, null);
	}
	
	public static void showInfoMessage(Context context, int titleResId, String message) {
		showInfoMessage(context, titleResId, message, null);
	}
	
	public static void showInfoMessage(Context context, int titleResId, int messageResId, 
									   DialogInterface.OnClickListener okButtonListener) {
		showInfoMessage(context, titleResId, context.getString(messageResId), okButtonListener);
	}
	
	public static void showInfoMessage(Context context, int titleResId, String message, 
									   DialogInterface.OnClickListener okButtonListener) {
		showAlertDialog(context, titleResId, message, okButtonListener, android.R.drawable.ic_dialog_info);
	}
	
	private static void showAlertDialog(Context context, int titleResId, String message, 
										DialogInterface.OnClickListener okButtonListener, int iconResId) {
		new AlertDialog.Builder(context)
		   			   .setPositiveButton("OK", ((okButtonListener != null) ? okButtonListener : new CloseOnClickListener()))
		   			   .setTitle(titleResId)
		   			   .setIcon(iconResId)
		   			   .setMessage(message)
		   			   .show();
	}

	static class CloseOnClickListener implements DialogInterface.OnClickListener {

		/* (non-Javadoc)
         * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
        	dialog.dismiss();
        }
	}
}