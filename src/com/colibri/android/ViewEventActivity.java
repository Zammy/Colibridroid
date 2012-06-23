package com.colibri.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.colibri.android.Server.DrawableManager;
import com.colibri.android.data.ColibriEvent;
import com.colibri.android.maps.MapLocationDataHolder;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ViewEventActivity extends MapActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ColibriActivity.currentActivity = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vieweventactivity);
	}
	
	@Override
	protected void onResume() {
		super.onRestart();
		
		Intent i = this.getIntent();
		final int indexOfEvent = i.getIntExtra("event", -1);
		
		ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
		
		ImageView imageView = (ImageView) this.findViewById(R.id.viewEventImage);
		DrawableManager.getInstance().fetchDrawableOnThread(event.thumbImageUrl.toString(),imageView);
		
		TextView textView = (TextView) this.findViewById(R.id.nameEvent);
		textView.setText(event.name);
		
		textView = (TextView) this.findViewById(R.id.eventDescription);
		textView.setText(event.description);
		
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
		
		Button editEventButton = (Button)this.findViewById(R.id.editEventButton);
		if(event.isOwnedByMe) {
		
			editEventButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					 Intent intent = new Intent().setClass(ColibriActivity.currentActivity, NewEventActivity.class);
					 intent.putExtra("event", indexOfEvent);
					 ColibriActivity.currentActivity.startActivity(intent);
				}
				
			});
		} else {
			editEventButton.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
