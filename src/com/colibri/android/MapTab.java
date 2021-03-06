package com.colibri.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.colibri.android.Server.EventsReceiver;
import com.colibri.android.data.ColibriEvent;
import com.colibri.android.maps.EventOverlay;
import com.colibri.android.maps.EventOverlayItem;
import com.colibri.android.maps.IAmHereOverlay;
import com.colibri.android.maps.MapLocationDataHolder;
import com.colibri.util.IChangeListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapTab extends MapActivity
					implements IChangeListener {
	private MapTab.GeoLocListener locationListener;
	private MapView mapView;
	private IAmHereOverlay targetOverlay;
	private EventOverlay eventOverlay;
	
    public void onCreate(Bundle savedInstanceState) {
    	ColibriActivity.currentActivity = this;
    	EventsReceiver.listener = this;
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptab);
        
        this.mapView = (MapView)this.findViewById(R.id.map);
        this.targetOverlay = new IAmHereOverlay();
        this.eventOverlay = new EventOverlay();

        List<Overlay> listOfOverlays = this.mapView.getOverlays();
        listOfOverlays.add(targetOverlay);
        listOfOverlays.add(eventOverlay);

        this.locationListener = new GeoLocListener();
        this.locationListener.start();
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.locationListener);
		
		MenuButtonHandler.getInstance().addHandlers(this.findViewById(R.id.buttons));
    }
    
    @Override
    protected void onResume() {
        this.populateEvents();
    	super.onResume();
    }

	public void hasChanged() {
		this.populateEvents();
	}
    
    public void populateEvents() {
    	this.eventOverlay.clear();
		ArrayList<ColibriEvent> events = ColibriEvent.events;
	    synchronized(events) { 
			for(ColibriEvent event : events) {
				EventOverlayItem item = new EventOverlayItem(event);
				this.eventOverlay.addEvent(item);
			}
	    }
	}

	public void setLocation(Location l) {
		
		MapLocationDataHolder.currentLocation = l;
    	
    	double lat = l.getLatitude();
    	double lng = l.getLongitude();
    	
		MapController controller = this.mapView.getController();
		controller.setZoom(13);
        GeoPoint p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
        
        MapLocationDataHolder.currentGeoLocation = p;
        
        controller.animateTo(p);
        
        this.targetOverlay.setGeoLocation(p);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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
