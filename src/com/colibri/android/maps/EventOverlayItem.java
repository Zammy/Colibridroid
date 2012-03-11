package com.colibri.android.maps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.android.data.ColibriEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

public class EventOverlayItem {
	
	static Bitmap eventBitmap;
	static {
		eventBitmap = BitmapFactory.decodeResource(ColibriActivity.instance.getResources(), R.drawable.icon_event);
	}
	
	public ColibriEvent event;
	private Point screenPoint;
	
	public EventOverlayItem(ColibriEvent event) {
		this.event = event;
		this.screenPoint = new Point();
	}
	
	public GeoPoint getLocation() {
		return new GeoPoint((int)(event.Latitude * 1E6),(int)(event.Longitude * 1E6));
	}

	public Bitmap getMarker() {  
		return eventBitmap;
	}
	
	public void draw(Canvas canvas, Projection projection) {
        projection.toPixels(this.getLocation(), screenPoint);
        int halfWidth = this.getMarker().getWidth()/2;
        canvas.drawBitmap(this.getMarker(), screenPoint.x-halfWidth , screenPoint.y-halfWidth , null); 
	}
	
	public boolean hitTest(Point tapLocation) {
		int halfWidth = this.getMarker().getWidth()/2;
		Rect boundingBox = new Rect(screenPoint.x-halfWidth , screenPoint.y-halfWidth,screenPoint.x+halfWidth , screenPoint.y+halfWidth);
		return boundingBox.contains(tapLocation.x, tapLocation.y);
	}
}
