package com.colibri.android.maps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;


public class IAmHereOverlay extends com.google.android.maps.Overlay {
	private GeoPoint location;
	private Bitmap youAreHereBitmap;
	
	public IAmHereOverlay() {
		this.youAreHereBitmap = BitmapFactory.decodeResource(ColibriActivity.currentActivity.getResources(), R.drawable.yrhere);  
	}
	
	public void setGeoLocation(GeoPoint l) {
		this.location = l;
	}
	
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);         
        if (this.location == null)
        	return false;

        //---translate the GeoPoint to screen pixels---
        Point screenPts = new Point();
        mapView.getProjection().toPixels(this.location, screenPts);

        //---add the marker---
        int halfWidth = this.youAreHereBitmap.getWidth()/2;
        canvas.drawBitmap(this.youAreHereBitmap, screenPts.x-halfWidth , screenPts.y-halfWidth , null);         
        return true;
    }
}