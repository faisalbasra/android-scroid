package de.dan_nrw.android.scroid.core.communications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.dan_nrw.android.scroid.Communication;
import de.dan_nrw.android.scroid.R;

/**
 * @author Daniel Czerwonk
 *
 */
final class CommunicationListAdapter extends BaseAdapter {

	// Fields
	private final Context context;
	private final Communication[] communications;
	
	// Constructors
	/**
	 * Method for creating a new instance of CommunicationListAdapter
	 * @param communications
	 * @param context
	 */
	public CommunicationListAdapter(Communication[] communications, Context context) {
	    super();
	    
	    this.communications = communications;
	    this.context = context;
    }	
	
    // Methods
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.communications.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return this.communications[position];
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Communication communication = this.communications[position];
		
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(this.context);
			
	    	convertView = layoutInflater.inflate(R.layout.communication, null);	
	    }
		
		ImageView imageView = (ImageView)convertView.findViewById(R.id.communicationImageView);
		
		imageView.setImageResource(this.getImageResId(communication));
		
		TextView textView = (TextView)convertView.findViewById(R.id.communicationTextView);
		
		textView.setText(communication.getValue());
		
		return convertView;
	}
	
	private int getImageResId(Communication communication) {
		if (communication.getType().equals(Communication.Type.Email)) {
			return android.R.drawable.sym_action_email;
		}
		else {
			return android.R.drawable.sym_action_chat;
		}
	}
}