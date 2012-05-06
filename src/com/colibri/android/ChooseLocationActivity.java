package com.colibri.android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Address;
import android.os.Bundle;

import com.colibri.android.maps.ChooseLocationOverlay;
import com.colibri.android.maps.MapLocationDataHolder;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ChooseLocationActivity extends MapActivity {
	
	ChooseLocationOverlay overlay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ColibriActivity.currentActivity = this;
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooselocationactivity);
        
        MapView mapView = (MapView) this.findViewById(R.id.map);
        mapView.setBuiltInZoomControls(true);
        
        this.overlay = new ChooseLocationOverlay(this);
        mapView.getOverlays().add(this.overlay);
        
        GeoPoint p = MapLocationDataHolder.currentGeoLocation;
        if (p != null) {
        	MapController mapController = mapView.getController();
        	mapController.animateTo(p);
        	mapController.setZoom(17);
        }
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public void LocationSelected(Address address) {
		Intent intent = this.getIntent();
		intent.putExtra("longitude", this.overlay.getLongitude());
		intent.putExtra("latitude", this.overlay.getLatitude());
		if (address != null) {
			intent.putExtra("address", address);
		}
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
