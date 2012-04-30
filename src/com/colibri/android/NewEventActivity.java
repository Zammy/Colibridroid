package com.colibri.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.colibri.android.data.ColibriEvent;
import com.colibri.android.maps.MapLocationDataHolder;
import com.colibri.util.AlertDialogHelper;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.ptashek.widgets.datetimepicker.DateTimePicker;

public class NewEventActivity extends MapActivity {
	
	@SuppressWarnings("unused")
	private double longitude;
	@SuppressWarnings("unused")
	private double latitude;
	
	private Calendar startTime;
	private Calendar endTime;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neweventactivity);
		
		this.findViewById(R.id.buttonStartTime);
		
		ColibriEvent.Type[] types = ColibriEvent.Type.values();
		ArrayList<String> categories = new ArrayList<String>();
		for (ColibriEvent.Type type : types) {
			categories.add(type.toString());
		}
		
		Spinner spinner = (Spinner) this.findViewById(R.id.categorySpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categories);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		MapView miniMap = (MapView) this.findViewById(R.id.minimap);
		MapController controller = miniMap.getController();
		if (MapLocationDataHolder.currentGeoLocation != null) {
			controller.setCenter(MapLocationDataHolder.currentGeoLocation);
			controller.setZoom(17);
		}
		
		this.startTime = new GregorianCalendar();
		this.endTime =  new GregorianCalendar();
		this.endTime.add(Calendar.HOUR_OF_DAY, 3);

		int years = startTime.get(Calendar.YEAR);
		int months = startTime.get(Calendar.MONTH);
		int days = startTime.get(Calendar.DAY_OF_MONTH);
		int hours =  startTime.get(Calendar.HOUR_OF_DAY);
		int minutes = startTime.get(Calendar.MINUTE);
		
		Button button = (Button) this.findViewById(R.id.buttonStartTime);
		button.setText(this.constructTimeString(years, months, days, hours, minutes));

		years = endTime.get(Calendar.YEAR);
		months = endTime.get(Calendar.MONTH);
		days = endTime.get(Calendar.DAY_OF_MONTH);
		hours =  endTime.get(Calendar.HOUR_OF_DAY);
		minutes = endTime.get(Calendar.MINUTE);
		
		button = (Button) this.findViewById(R.id.buttonEndTime);
		button.setText(this.constructTimeString(years, months, days, hours, minutes));
	}
	
	public void onButtonTimeClicked(View sender) {
		showDateTimeDialogForButton((Button) sender);
	}
	
	public void onButtonCreateEventClicked(View sender) {
		
	}
	
	public void onButtonSelectLocationClicked(View sender) {
		Intent i = new Intent(this,ChooseLocationActivity.class);
		this.startActivityForResult(i, 0);
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		//here we get longitude and latitude data
		this.latitude = data.getExtras().getDouble("latitude"); 
		this.longitude = data.getExtras().getDouble("longitude");
	}
	
	private String constructTimeString(int years, int months, int days, int hours, int minutes) {
		return String.format("%04d/%02d/%02d %02d:%02d", years,months+1,days,hours,minutes);
		//return years + "/" + ( months+1 ) + "/" + days + " " + hours + ":" + minutes;
	}

	private void showDateTimeDialogForButton(final Button button) {
		final String buttonTag = (String) button.getTag();
		
		final Dialog mDateTimeDialog = new Dialog(this);
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
		
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDateTimePicker.clearFocus();
				
				int years = mDateTimePicker.get(Calendar.YEAR);
				int months = mDateTimePicker.get(Calendar.MONTH);
				int days = mDateTimePicker.get(Calendar.DAY_OF_MONTH);
				int hours =  mDateTimePicker.get(Calendar.HOUR_OF_DAY);
				int minutes = mDateTimePicker.get(Calendar.MINUTE);
				
				Calendar cal = new GregorianCalendar(years,months,days,hours,minutes);
				
				if (buttonTag.equalsIgnoreCase("startTime")) {
					if (cal.after(NewEventActivity.this.endTime)) {
						AlertDialogHelper.ShowDialog(NewEventActivity.this, R.string.Error, "Begin time cannot be after end time.", R.string.OK	, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {}
						});
						return;
					}
					
					NewEventActivity.this.startTime = cal;
				} else {
					if (cal.before(NewEventActivity.this.startTime)) {
						AlertDialogHelper.ShowDialog(NewEventActivity.this, R.string.Error, "End time cannot be befer begin time.", R.string.OK	, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {}
						});
						return;
					}
					
					NewEventActivity.this.endTime = cal;
				}

				button.setText(NewEventActivity.this.constructTimeString(years, months, days, hours, minutes));
				mDateTimeDialog.dismiss();
			}
		});

		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDateTimeDialog.cancel();
			}
		});

		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mDateTimePicker.reset();
			}
		});
		
		mDateTimePicker.setIs24HourView(true);
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		mDateTimeDialog.show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
