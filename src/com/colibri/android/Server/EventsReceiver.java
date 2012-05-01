package com.colibri.android.Server;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.android.data.ColibriEvent;
import com.colibri.util.AlertDialogHelper;

public class EventsReceiver implements ISendReceiver {

	public void error(String error) {
		
		AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, error , R.string.OK, new OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {}
		});
		
	}
	
	public void parse(String result) {
		try {
			JSONArray jsonEventsArray = new JSONArray(result);
			for (int i = 0; i < jsonEventsArray.length(); i++) {
				JSONObject jsonEvent = jsonEventsArray.getJSONObject(i);
				ColibriEvent event = new ColibriEvent();
				event.description = jsonEvent.getString("description");
				event.longitude = Double.parseDouble(jsonEvent.getString("lon"));
				event.latitude = Double.parseDouble(jsonEvent.getString("lat"));
				event.fbPointer = jsonEvent.getString("fb_event_id");

				String startTime = jsonEvent.getString("start_time");
				String endTime = jsonEvent.getString("end_time");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:", Locale.US);
				try {
					Date startDate = sdf.parse(startTime);
					event.startTime = new GregorianCalendar();
					event.startTime.setTime(startDate);
					
					Date endDate = sdf.parse(endTime);
					event.endTime = new GregorianCalendar();
					event.endTime.setTime(endDate);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}

				try {
					event.thumbImageUrl = new URL(jsonEvent.getString("pic_thumb"));
					event.imageUrl = new URL(jsonEvent.getString("pic"));
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				
				boolean ownerIsMe = Boolean.parseBoolean(jsonEvent.getString("owner"));
				event.isOwnedByMe = ownerIsMe;
				
				ColibriEvent.events.add(event);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			this.error("Error parsing json.");
		}
		
	}

}
