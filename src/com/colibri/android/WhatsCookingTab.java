package com.colibri.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.colibri.android.Server.Server;
import com.colibri.android.whatscooking.EventAdapter;

public class WhatsCookingTab extends Activity {

	private EventAdapter adapter;
	
  public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whatscooking);
		
		this.adapter = new EventAdapter(Server.getEvents());
		GridView gridView = (GridView) this.findViewById(R.id.gridView);
		gridView.setAdapter(this.adapter);

	}

}
