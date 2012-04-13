package com.colibri.android.maps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class ChooseLocationOverlay extends com.google.android.maps.Overlay {
	private GeoPoint location;
	private Bitmap eventLocationBitmap;
	private Context context;
	
	public ChooseLocationOverlay(Context context) {
		this.context = context;
		this.eventLocationBitmap = BitmapFactory.decodeResource(ColibriActivity.instance.getResources(), R.drawable.yrhere);  
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		this.location = p;
		mapView.getController().animateTo(p);
		
		double latitude = (double) (p.getLatitudeE6() / 1E6);
		double longitude = (double) (p.getLongitudeE6() / 1E6);
		
		Geocoder gcd = new Geocoder(this.context, Locale.getDefault());
		try {
			List<Address> addresses = gcd.getFromLocation(latitude, longitude, 5);
			CharSequence[] addresses_charSequence = new CharSequence[addresses.size()];
			for(int i=0; i < addresses.size(); i++) {
				addresses_charSequence[i] = (CharSequence) addresses.get(i).toString();
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
			builder.setTitle(R.string.ChooseAddress);
			builder.setItems(addresses_charSequence, new DialogInterface.OnClickListener() {
				
			    public void onClick(DialogInterface dialog, int item) {
			       // Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
			    }
			    
			});
			
			AlertDialog alert = builder.create();
			alert.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
