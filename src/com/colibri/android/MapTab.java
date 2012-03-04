package com.colibri.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.colibri.android.Server.Server;
import com.colibri.android.data.ColibriEvent;
import com.colibri.android.maps.EventOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapTab extends MapActivity {
	private MapTab.GeoLocListener locationListener;
	private MapView mapView;
	private MapTab.TargetOverlay targetOverlay;
	private EventOverlay eventOverlay;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptab);
        
        this.mapView = (MapView)this.findViewById(R.id.map);
        this.targetOverlay = new TargetOverlay();
		
		Drawable eventDrawble = ColibriActivity.instance.getResources().getDrawable(R.drawable.icon_event);
		//eventDrawble.setBounds(0, 0, eventDrawble.getIntrinsicWidth(), eventDrawble.getIntrinsicHeight());
        this.eventOverlay = new EventOverlay(eventDrawble);

        List<Overlay> listOfOverlays = this.mapView.getOverlays();
        listOfOverlays.add(targetOverlay);
        listOfOverlays.add(eventOverlay);

        this.populateEvents();

        this.locationListener = new GeoLocListener();
        this.locationListener.start();
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.locationListener);
    }
    
    private void populateEvents() {
		ArrayList<ColibriEvent> events = Server.getEvents();
		for(ColibriEvent event : events) {
			GeoPoint p = new GeoPoint((int)(event.Latitude * 1E6),(int)(event.Longitude * 1E6));
			this.eventOverlay.addOverlay(new OverlayItem(p,event.type.toString(),event.Description));
		}
		this.eventOverlay.refresh();
		
	}

 
	public void setLocation(Location l) {
    	
    	double lat = l.getLatitude();
    	double lng = l.getLongitude();
    	
		MapController controller = this.mapView.getController();
		controller.setZoom(13);
        GeoPoint p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
        
        controller.animateTo(p);
        
        this.targetOverlay.setGeoLocation(p);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class TargetOverlay extends com.google.android.maps.Overlay {
		private GeoPoint location;
		
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
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yrhere);    
            int halfWidth = bmp.getWidth()/2;
            canvas.drawBitmap(bmp, screenPts.x-halfWidth , screenPts.y-halfWidth , null);         
            return true;
        }
	}
	
	private class GeoLocListener implements LocationListener {
		private Timer timer = new Timer();
		
		public void start() {
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					GeoLocListener.this.stop();
				}
				
			}, 10000);
		}
		
		private void stop() {
			LocationManager locationManager = (LocationManager)MapTab.this.getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(this);
		}

		public void onLocationChanged(Location location) {
			MapTab.this.setLocation(location);
			if (location.getAccuracy() <= 20) {
				this.timer.cancel();
				this.stop();
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}
