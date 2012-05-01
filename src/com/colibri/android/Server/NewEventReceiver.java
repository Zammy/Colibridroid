package com.colibri.android.Server;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.util.AlertDialogHelper;

public class NewEventReceiver implements ISendReceiver {

	public void parse(String result) {
		if (result.equalsIgnoreCase("event created")) {
			AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Success, R.string.SuccessfulEventCreat, R.string.OK, new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					ColibriActivity.currentActivity.finish();
				}
			});
		} else {
			AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, result, R.string.OK, new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { }
			});
		}
	}

	public void error(String error) {
		AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, error , R.string.OK, new OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
		
	}

}
