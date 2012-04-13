package com.colibri.android;

import android.os.Bundle;

import com.colibri.android.maps.ChooseLocationOverlay;
import com.colibri.android.maps.MapLocationDataHolder;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ChooseLocationActivity extends MapActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooselocationactivity);
        
        MapView mapView = (MapView) this.findViewById(R.id.map);
        mapView.setBuiltInZoomControls(true);
        
        mapView.getOverlays().add(new ChooseLocationOverlay(this));
        
        GeoPoint p = MapLocationDataHolder.currentGeoLocation;
        if (p != null) {
        	MapController mapController = mapView.getController();
        	mapController.animateTo(p);
        	mapController.setZoom(17);
        }
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
