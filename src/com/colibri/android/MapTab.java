package com.colibri.android;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapTab extends MapActivity {
	private MapTab.GeoLocListener locationListener;
	private MapView mapView;
	private MapTab.TargetOverlay mapOverlay;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptab);
        
        this.mapView = (MapView)this.findViewById(R.id.map);
        this.mapOverlay = new TargetOverlay();
        
        this.locationListener = new GeoLocListener();
        this.locationListener.start();
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.locationListener);
    }
    
    public void setLocation(Location l) {
    	Log.i("Colibri", "New location.");
    	
    	double lat = l.getLatitude();
    	double lng = l.getLongitude();
    	
		MapController controller = this.mapView.getController();
 
        GeoPoint p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
        
        controller.animateTo(p);
        this.mapOverlay.setGeoLocation(p);
        
        List<Overlay> listOfOverlays = mapView.getOverlays();
        if (!listOfOverlays.contains(this.mapOverlay)) {
	        listOfOverlays.clear();
	        listOfOverlays.add(mapOverlay);
	        mapView.invalidate();
        }
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
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(this.location, screenPts);
 
            //---add the marker---
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.target);    
//            int halfWidth = bmp.getWidth()/2;
//            canvas.drawBitmap(bmp, screenPts.x-halfWidth , screenPts.y-halfWidth , null);         
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
				
			}, 60000);
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
