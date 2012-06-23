package com.colibri.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.colibri.android.Server.EventsReceiver;
import com.colibri.android.data.ColibriEvent;
import com.colibri.android.whatscooking.EventAdapter;
import com.colibri.util.IChangeListener;

public class WhatsCookingTab extends Activity
							 implements IChangeListener{

	private EventAdapter adapter;
	
  public void onCreate(Bundle savedInstanceState) {
	  	ColibriActivity.currentActivity = this;
    	EventsReceiver.listener = this;
    	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whatscooking);
		
		this.adapter = new EventAdapter(ColibriEvent.events);
		GridView gridView = (GridView) this.findViewById(R.id.gridView);
		gridView.setAdapter(this.adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				
        		Intent i = new Intent(ColibriActivity.currentActivity,ViewEventActivity.class);
        		i.putExtra("event", position);
        		ColibriActivity.currentActivity.startActivity(i);
			}
		});
		
		MenuButtonHandler.getInstance().addHandlers(this.findViewById(R.id.buttons));
	}
  
    @Override
    protected void onResume() {
    	super.onResume();
    	
		GridView gridView = (GridView) this.findViewById(R.id.gridView);
		adapter.notifyDataSetChanged();
		gridView.invalidateViews();
    }

	public void hasChanged() {
		GridView gridView = (GridView) this.findViewById(R.id.gridView);
		adapter.notifyDataSetChanged();
		gridView.invalidateViews();
	}

}
