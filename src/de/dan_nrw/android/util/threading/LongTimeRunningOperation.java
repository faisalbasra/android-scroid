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

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;


/**
 * @author Daniel Czerwonk
 * @deprecated This class will be replaced by AsyncTask (which is part of Android SDK 1.5)
 * @see android.os.AsyncTask 
 */
public abstract class LongTimeRunningOperation {

	private final Dialog progressDialog;
	private final Thread thread;
	private final Handler handler;

	
	/**
	 * Creates a new instance of LongTimeRunningOperation.
	 * @param progressDialog
	 */
	public LongTimeRunningOperation(final Dialog progressDialog) {
	    super();
	    
	    this.progressDialog = progressDialog;
	    
	    this.handler = new Handler() {

			/* (non-Javadoc)
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg) {
	            if (msg.obj != null && msg.obj instanceof Throwable) {
	            	handleUncaughtException(thread, (Throwable)msg.obj);
	            }
	            else {
	            	afterOperationSuccessfullyCompleted(msg);
	            }
            }
	    };
	    
	    this.thread = new Thread(new Runnable() {
	    	
	    	/* (non-Javadoc)
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
	            Message message = new Message();
            	
            	try {
	            	onRun(message);
	            }
	            catch (Throwable ex) {
	            	message.obj = ex;
	            }
	            finally {
	            	progressDialog.dismiss();
	            }
	            
	            handler.sendMessage(message);
            }
	    });
	    
	    this.thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			/* (non-Javadoc)
             * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
             */
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
            	uncaughtException(thread, ex);
            }
	    });
    }
    
    /**
     * Method handling uncaught exceptions (marshalled in UI thread)
     * @param thread
     * @param ex
     */
    public abstract void handleUncaughtException(Thread thread, Throwable ex);
    
    /**
     * Method executed after child thread completed successfully
     * @param message Message sent by child thread
     */
    public void afterOperationSuccessfullyCompleted(Message message) {
    	// HOOK
    }
    
    /**
     * Method executed in child thread
     * @param message
     * @throws Exception
     */
    public abstract void onRun(Message message) throws Exception;
    
    /**
     * Method for starting operation 
     */
    public final void start() {
    	this.thread.start();
    	
    	this.progressDialog.show();
    }
}