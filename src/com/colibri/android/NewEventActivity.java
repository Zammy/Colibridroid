package com.colibri.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.colibri.android.data.ColibriEvent;
import com.colibri.android.data.ColibriEvent.Type;
import com.colibri.android.maps.MapLocationDataHolder;
import com.colibri.util.AlertDialogHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.ptashek.widgets.datetimepicker.DateTimePicker;

public class NewEventActivity extends MapActivity {
	
	private double longitude;
	private double latitude;
	
	private Calendar startTime;
	private Calendar endTime;
	
	//private String address;
	
	private int indexOfEvent;
	
	public void onCreate(Bundle savedInstanceState) {
		ColibriActivity.currentActivity = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neweventactivity);
		
		ColibriEvent.Type[] types = ColibriEvent.Type.values();
		ArrayList<String> categories = new ArrayList<String>();
		for (ColibriEvent.Type type : types) {
			categories.add(type.toString());
		}
		
		Spinner spinner = (Spinner) this.findViewById(R.id.categorySpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categories);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		Intent i = this.getIntent();
		indexOfEvent = i.getIntExtra("event", -1);

		this.initFields();
		this.initMap();
		this.initTime();
		
		if (indexOfEvent != -1) {
			ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
			
			int index = Arrays.asList(types).indexOf(event.type);
			spinner.setSelection(index);

			CheckBox checkBox = (CheckBox) this.findViewById(R.id.makePrivate);
			checkBox.setChecked( event.isPrivate );
			
			Button createEventButton = (Button) this.findViewById(R.id.createEventButton);
			createEventButton.setText(R.string.EditEvent);
		}
	}

	private void initFields() {
		if (indexOfEvent != -1) {
			ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
			TextView name = (TextView) this.findViewById(R.id.nameNewEvent);
			name.setText(event.name);
			TextView description = (TextView) this.findViewById(R.id.descriptionNewEvent);
			description.setText(event.description);
			TextView address = (TextView) this.findViewById(R.id.addressNewEvent);
			address.setText(event.address);
		}
	}

	private void initMap() {
		MapView miniMap = (MapView) this.findViewById(R.id.minimap);
		MapController controller = miniMap.getController();
		if (MapLocationDataHolder.currentGeoLocation != null && indexOfEvent == -1) {
			controller.setCenter(MapLocationDataHolder.currentGeoLocation);
			controller.setZoom(17);
			
			GeoPoint p = MapLocationDataHolder.currentGeoLocation;
			if ( p != null) {
				this.longitude =  (double) (p.getLongitudeE6() / 1E6);
				this.latitude =  (double) (p.getLatitudeE6() / 1E6);
			}
			
		} else if (indexOfEvent != -1) {
			ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
			this.longitude = event.longitude;
			this.latitude = event.latitude;
			
			controller.setCenter(new GeoPoint(
		            (int) (this.latitude * 1E6), 
		            (int) (this.longitude * 1E6)));
			controller.setZoom(17);
		}
	}
	

	private void initTime() {
		if (indexOfEvent == -1) {
			this.startTime = new GregorianCalendar();
			this.endTime =  new GregorianCalendar();
			this.endTime.add(Calendar.HOUR_OF_DAY, 3);
		} else {
			ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
			this.startTime = event.startTime;
			this.endTime = event.endTime;
		}
		
		Button button = (Button) this.findViewById(R.id.buttonStartTime);
		this.setTimeOnButton(startTime,button);
		
		button = (Button) this.findViewById(R.id.buttonEndTime);
		this.setTimeOnButton(endTime,button);
		
	}

	private void setTimeOnButton(Calendar cal,Button button) {
		
		int years = cal.get(Calendar.YEAR);
		int months = cal.get(Calendar.MONTH);
		int days = cal.get(Calendar.DAY_OF_MONTH);
		int hours =  cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		button.setText(this.constructTimeString(years, months, days, hours, minutes));
		
	}
	
	private void setTimeOnDateTimePicker(Calendar cal, DateTimePicker dateTimePicker) {
		
		int years = cal.get(Calendar.YEAR);
		int months = cal.get(Calendar.MONTH);
		int days = cal.get(Calendar.DAY_OF_MONTH);
		int hours =  cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		dateTimePicker.updateDate(years, months, days);
		dateTimePicker.updateTime(hours, minutes);
	}
	
	public void onButtonTimeClicked(View sender) {
		showDateTimeDialogForButton((Button) sender);
	}
	
	public void onButtonCreateEventClicked(View sender) {
		ColibriEvent event;
		if (this.indexOfEvent == -1) {
			event = new ColibriEvent();
		} else {
			event = ColibriEvent.events.get(indexOfEvent);
		}
		
		EditText textField = (EditText)this.findViewById(R.id.nameNewEvent);
		event.name = textField.getText().toString();
		textField = (EditText)this.findViewById(R.id.descriptionNewEvent);
		event.description = textField.getText().toString();
		event.longitude = this.longitude;
		event.latitude = this.latitude;
		textField = (EditText)this.findViewById(R.id.addressNewEvent);
		event.address = textField.getText().toString();
		event.startTime = this.startTime;
		event.endTime = this.endTime;
		event.isOwnedByMe = true;
		Spinner spinner = (Spinner) this.findViewById(R.id.categorySpinner);
		event.type = ColibriEvent.Type.valueOf( (String) spinner.getSelectedItem() );
		
		CheckBox checkBox = (CheckBox) this.findViewById(R.id.makePrivate);
		event.isPrivate = checkBox.isChecked();
		
		if (this.indexOfEvent == -1) {
			ColibriEvent.addNewEvent(event);
		} else {
			ColibriEvent.editEvent(event);
		}
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
		ColibriActivity.currentActivity = this;
		super.onActivityResult(requestCode, resultCode, data);
		
		//here we get longitude and latitude data
		this.latitude = data.getExtras().getDouble("latitude"); 
		this.longitude = data.getExtras().getDouble("longitude");
		Parcelable p = data.getExtras().getParcelable("address");
		if (p != null) {
			Address a = (Address) p;
			int i = 0;
			String address = "";
			while(true) {
				String addressLine = a.getAddressLine(i);
				if (addressLine == null)
					break;
				address += addressLine;
				i++;
				if (i > 0 ) {
					address += ", ";
				}
			}
			EditText editText = (EditText)this.findViewById(R.id.addressNewEvent);
			editText.setText(address);
		}
	}
	
	private String constructTimeString(int years, int months, int days, int hours, int minutes) {
		return String.format("%04d/%02d/%02d %02d:%02d", years,months+1,days,hours,minutes);
	}

	private void showDateTimeDialogForButton(final Button button) {
		final String buttonTag = (String) button.getTag();
		
		final Dialog mDateTimeDialog = new Dialog(this);
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
	
		if (buttonTag.equalsIgnoreCase("startTime")) {
			this.setTimeOnDateTimePicker(this.startTime, mDateTimePicker);
		} else {
			this.setTimeOnDateTimePicker(this.endTime, mDateTimePicker);
		}
		
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
					NewEventActivity.this.startTime = cal;
					
					if (NewEventActivity.this.startTime.after(NewEventActivity.this.endTime)) {
						NewEventActivity.this.endTime = (Calendar) startTime.clone();
						NewEventActivity.this.endTime.add(Calendar.HOUR, 3);
						
						Button button = (Button) NewEventActivity.this.findViewById(R.id.buttonEndTime);
						NewEventActivity.this.setTimeOnButton(NewEventActivity.this.endTime,button);
					}
					
				} else {
					if (cal.before(NewEventActivity.this.startTime)) {
						AlertDialogHelper.ShowDialog(NewEventActivity.this, R.string.Error, "End time cannot be befer begin time.", R.string.OK	, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {}
						});
						return;
					}
					Calendar fourMonthsAfterBeginTime = (Calendar) NewEventActivity.this.startTime.clone();
					fourMonthsAfterBeginTime.add(Calendar.MONTH, 4);
					if (cal.after(fourMonthsAfterBeginTime)) {
						AlertDialogHelper.ShowDialog(NewEventActivity.this, R.string.Error, "End time cannot be four months after begin time.", R.string.OK	, new DialogInterface.OnClickListener() {
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
