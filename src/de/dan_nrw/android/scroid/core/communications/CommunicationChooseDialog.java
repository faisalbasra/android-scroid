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
package de.dan_nrw.android.scroid.core.communications;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import de.dan_nrw.android.scroid.Communication;
import de.dan_nrw.android.scroid.R;


/**
 * @author Daniel Czerwonk
 *
 */
public class CommunicationChooseDialog extends Dialog {

	private final Communication[] communications;
	private final CommunicationChosenListener listener;

	
	/**
	 * Creates a new instance of CommunicationChooseDialog.
	 * @param context
	 * @param communications
	 * @param listener
	 */
	public CommunicationChooseDialog(Context context, Communication[] communications, CommunicationChosenListener listener) {
	    super(context);
	    
	    this.communications = communications;
	    this.listener = listener;
    }
    
	
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
    
    
    public static abstract class CommunicationChosenListener {
    	
    	public abstract void onCommunicationChosen(Communication communication);
    }
}