package com.colibri.android.Server;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.android.data.ColibriEvent;
import com.colibri.util.AlertDialogHelper;

public class NewEventReceiver implements ISendReceiver {

	ColibriEvent event;
	
	public NewEventReceiver(ColibriEvent event) {
		this.event = event;
	}

	public void parse(String result) {
		String error;
		try {
			JSONObject resultJson = new JSONObject(result);
			if (resultJson.getString("result").equalsIgnoreCase("success")) {
				this.event.fbPointer = resultJson.getString("fb_event_id");
				AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Success, R.string.SuccessfulEventCreat, R.string.OK, new OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						ColibriActivity.currentActivity.finish();
					}
				});
				return;
			} else {
				error = resultJson.getString("error");
			}
			
		} catch (JSONException e) {
			error = "Could not parse JSON.";
			e.printStackTrace();
		}

		AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, result, R.string.OK, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { }
		});
	}

	public void error(String error) {
		AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, error , R.string.OK, new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
		
	}

}
