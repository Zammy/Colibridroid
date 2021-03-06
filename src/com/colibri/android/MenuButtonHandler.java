package com.colibri.android;

import com.colibri.android.Server.EventsReceiver;
import com.colibri.android.Server.Server;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;

public class MenuButtonHandler {
	
	private static MenuButtonHandler instance;
	public static MenuButtonHandler getInstance() {
		if (instance == null) {
			instance = new MenuButtonHandler();
		}	
		return instance;
	}
	
	public void addHandlers(View view) {
		Button newEventButton = (Button)view.findViewById(R.id.newEventButton);
		newEventButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 Intent intent = new Intent().setClass(ColibriActivity.currentActivity, NewEventActivity.class);
				 ColibriActivity.currentActivity.startActivity(intent);
			}
			
		});
		
		Button refresh = (Button) view.findViewById(R.id.refreshEvents);
		refresh.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
	            Server.getEvents(ColibriActivity.accessToken, new EventsReceiver());
			}
		});
	}
}
