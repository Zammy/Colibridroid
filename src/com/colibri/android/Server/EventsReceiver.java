package com.colibri.android.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.util.AlertDialogHelper;

public class EventsReceiver implements ISendReceiver {

	public void error(String error) {
		
		AlertDialogHelper.ShowDialog(ColibriActivity.instance, R.string.Error, error , R.string.OK, new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
	}
	
	public void parse(String result) {
		//String newResult = result.substring(3);
		try {
			JSONArray jsonEventsArray = new JSONArray(result);
			for (int i = 0; i < jsonEventsArray.length(); i++) {
				JSONObject jsonEvent = jsonEventsArray.getJSONObject(i);
				
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			this.error("Error parsing json.");
		}
		
	}

}
