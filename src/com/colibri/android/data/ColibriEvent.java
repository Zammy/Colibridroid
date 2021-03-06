package com.colibri.android.data;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import com.colibri.android.ColibriActivity;
import com.colibri.android.Server.EditEventReceiver;
import com.colibri.android.Server.NewEventReceiver;
import com.colibri.android.Server.Server;


public class ColibriEvent {
	public double longitude;
	public double latitude;
	public String name;
	public String description;
	public String address;
	public ColibriEvent.Type type;
	public URL imageUrl;
	public URL thumbImageUrl;
	public Calendar startTime;
	public Calendar endTime;
	public boolean isPrivate;
	public String fbPointer;
	public boolean isOwnedByMe;
	
	public enum Type {
		Drinking,
		Concert,
		Movie
	}
	
	public static ArrayList<ColibriEvent> events;
	
	static {
		ColibriEvent.events = new ArrayList<ColibriEvent>();
	}
	public static void addNewEvent(ColibriEvent event) {
		events.add(event);
		
		Server.newEvent(ColibriActivity.accessToken, event, new NewEventReceiver(event));
	}
	
	public static void editEvent(ColibriEvent event) {
		Server.newEvent(ColibriActivity.accessToken, event, new EditEventReceiver());
	}
	
	public String startTimeAsString() {
		return String.format("%04d-%02d-%02d %02d:%02d:00", 
				this.startTime.get(Calendar.YEAR), 
				this.startTime.get(Calendar.MONTH)+1, 
				this.startTime.get(Calendar.DAY_OF_MONTH),
				this.startTime.get(Calendar.HOUR_OF_DAY),
				this.startTime.get(Calendar.MINUTE));
	}
	
	public String endTimeAsString() {
		return String.format("%04d-%02d-%02d %02d:%02d:00", 
				this.endTime.get(Calendar.YEAR), 
				this.endTime.get(Calendar.MONTH)+1, 
				this.endTime.get(Calendar.DAY_OF_MONTH),
				this.endTime.get(Calendar.HOUR_OF_DAY),
				this.endTime.get(Calendar.MINUTE));
	}
	

}
