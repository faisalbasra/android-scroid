package de.dan_nrw.android.scroid.core.communications;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import de.dan_nrw.android.scroid.Communication;
import de.dan_nrw.android.scroid.R;
import de.dan_nrw.android.util.ui.BaseDialog;


/**
 * @author Daniel Czerwonk
 *
 */
public class CommunicationChooseDialog extends BaseDialog {

	// Fields
	private final Communication[] communications;
	private final CommunicationChosenListener listener;
	
	// Constructors
	/**
	 * Method for creating a new instance of CommunicationChooseDialog
	 * @param context
	 * @param communications
	 * @param listener
	 */
	public CommunicationChooseDialog(Context context, Communication[] communications, CommunicationChosenListener listener) {
	    super(context);
	    
	    this.communications = communications;
	    this.listener = listener;
    }
    
    // Methods
    /* (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setTitle(this.getContext().getText(R.string.recommendToTitleText));
        
        ListView listView = new ListView(this.getContext());
        
        listView.setAdapter(new CommunicationListAdapter(this.communications, this.getContext()));
        listView.setOnItemClickListener(new OnItemClickListener() {

			/* (non-Javadoc)
             * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	listener.onCommunicationChosen(communications[position]);
            	
            	dismiss();
            }
        });
        
        this.setContentView(listView);
    }
    
    // Inner-Classes
    public static abstract class CommunicationChosenListener {
    	
    	public abstract void onCommunicationChosen(Communication communication);
    }
}