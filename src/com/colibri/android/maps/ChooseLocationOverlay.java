package com.colibri.android.maps;

import java.io.IOException;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

import com.colibri.android.ChooseLocationActivity;
import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class ChooseLocationOverlay extends com.google.android.maps.Overlay {
	private GeoPoint location;
	private Bitmap eventLocationBitmap;
	private ChooseLocationActivity context;

	
	public ChooseLocationOverlay(ChooseLocationActivity context) {
		this.context = context;
		this.eventLocationBitmap = BitmapFactory.decodeResource(ColibriActivity.currentActivity.getResources(), R.drawable.yrhere);  
	}
	
	public double getLatitude() {
		return (double) (location.getLatitudeE6() / 1E6);
	}
	
	public double getLongitude() {
		return (double) (location.getLongitudeE6() / 1E6);
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		this.location = p;
		mapView.getController().animateTo(p);
		
		
		Address address = null;
		Geocoder gcd = new Geocoder(this.context, Locale.getDefault());
		try {
			address = gcd.getFromLocation(getLatitude(), getLongitude(), 1).get(0);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		context.LocationSelected(address);
		return true;
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
        int halfWidth = this.eventLocationBitmap.getWidth()/2;
        canvas.drawBitmap(this.eventLocationBitmap, screenPts.x-halfWidth , screenPts.y-halfWidth , null);         
        return true;
    }

}
