package com.colibri.android;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ButtonHandler {
	
	private static ButtonHandler instance;
	public static ButtonHandler getInstance() {
		if (instance == null) {
			instance = new ButtonHandler();
		}	
		return instance;
	}
	
	public void addHandlers(View view) {
		Button newEventButton = (Button)view.findViewById(R.id.newEventButton);
		newEventButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 Intent intent = new Intent().setClass(ColibriActivity.instance, NewEventActivity.class);
				 ColibriActivity.instance.startActivity(intent);
			}
			
		});
	}
}
