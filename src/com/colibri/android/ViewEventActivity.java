package com.colibri.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.colibri.android.data.ColibriEvent;
import com.colibri.android.maps.MapLocationDataHolder;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ViewEventActivity extends MapActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vieweventactivity);
		
		Intent i = this.getIntent();
		int indexOfEvent = i.getIntExtra("event", -1);
		
		ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
		
		TextView textView = (TextView) this.findViewById(R.id.nameEvent);
		textView.setText(event.Name);
		
		textView = (TextView) this.findViewById(R.id.eventDescription);
		textView.setText(event.Description);
		
		MapView miniMap = (MapView) this.findViewById(R.id.eventLocationMap);
		MapController controller = miniMap.getController();
		if (MapLocationDataHolder.currentGeoLocation != null) {
			controller.setCenter(MapLocationDataHolder.currentGeoLocation);
			controller.setZoom(17);
		}
		
		textView = (TextView) this.findViewById(R.id.startTimeDate);
		textView.setText("21.04.2012 @ 14:00");
		textView = (TextView) this.findViewById(R.id.endTimeDate);
		textView.setText("21.04.2012 @ 15:00");
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
