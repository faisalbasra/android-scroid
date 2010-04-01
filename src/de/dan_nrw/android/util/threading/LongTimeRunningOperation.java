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
package de.dan_nrw.android.util.threading;

import android.app.Dialog;
import android.os.AsyncTask;


/**
 * @author Daniel Czerwonk
 */
public abstract class LongTimeRunningOperation<T> {

	private final Dialog progressDialog;
	private final AsyncTask<Void, Void, T> asyncTask;
	
	
	/**
	 * Creates a new instance of LongTimeRunningOperation.
	 * @param progressDialog
	 */
	public LongTimeRunningOperation(final Dialog progressDialog) {
	    super();
	    
	    this.progressDialog = progressDialog;
	    this.asyncTask = new InnerAsyncTask();
    }

    /**
     * Method handling uncaught exceptions (marshaled in UI thread)
     * @param thread
     * @param ex
     */
    public abstract void handleUncaughtException(Throwable ex);
    
    /**
     * Method executed after child thread completed successfully
     * @param message Message sent by child thread
     */
    public void afterOperationSuccessfullyCompleted(T result) {
    	// HOOK
    }
    
    /**
     * Method executed in background thread
     * @param message
     * @throws Exception
     */
    public abstract T onRun() throws Exception;
    
    /**
     * Method for starting operation 
     */
    public final void start() {
    	this.asyncTask.execute();
    	
    	this.progressDialog.show();
    }
    
    private class InnerAsyncTask extends AsyncTask<Void, Void, T> {

        private Throwable exception;
        
        
        @Override
        protected T doInBackground(Void... params) {
            try {
                return onRun();
            }
            catch (Exception e) {
                this.exception = e;
            }
            
            return null;
        }

        @Override
        protected void onPostExecute(T result) {
            progressDialog.dismiss();
            
            if (this.exception != null) {
                handleUncaughtException(this.exception);
            }
            else {
                afterOperationSuccessfullyCompleted(result);
            }
        }
    }
}