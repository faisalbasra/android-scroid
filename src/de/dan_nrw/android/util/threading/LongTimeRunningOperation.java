package de.dan_nrw.android.util.threading;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;


/**
 * @author Daniel Czerwonk
 *
 */
public abstract class LongTimeRunningOperation {

	// Fields
	private final Dialog progressDialog;
	private final Thread thread;
	private final Handler handler;
	
	// Constructors
	/**
	 * Method for creating a new instance of LongTimeRunningOperation
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
	    
	    this.thread = new Thread() {

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
	    };
	    
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