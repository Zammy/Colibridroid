package com.colibri.android.maps;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;

import com.colibri.android.ColibriActivity;
import com.colibri.android.ViewEventActivity;
import com.colibri.android.data.ColibriEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class EventOverlay extends com.google.android.maps.Overlay {
	ArrayList<EventOverlayItem> events;
	
	public EventOverlay() {
		this.events = new ArrayList<EventOverlayItem>();
	}
	
	public void addEvent(EventOverlayItem e) {
		this.events.add(e);
	}
	
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);    
        
        if (this.events.size() == 0)
        	return false;
        
        for(EventOverlayItem e : events) {
        	e.draw(canvas, mapView.getProjection());
        }
        
        return false;
    }
    
    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
    	Point tapLocation = new Point();
    	mapView.getProjection().toPixels(p, tapLocation);
        for(EventOverlayItem e : events) {
        	if (e.hitTest(tapLocation)) {
        		
        		Intent i = new Intent(ColibriActivity.currentActivity,ViewEventActivity.class);
        		i.putExtra("event", ColibriEvent.events.indexOf(e.event));
        		ColibriActivity.instance.startActivity(i);
        		
        	}
        }
    	return super.onTap(p, mapView);
    }
}
