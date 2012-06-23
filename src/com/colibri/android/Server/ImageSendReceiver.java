package com.colibri.android.Server;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ImageView;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.android.ViewEventActivity;
import com.colibri.android.data.ColibriEvent;
import com.colibri.util.AlertDialogHelper;

public class ImageSendReceiver implements ISendReceiver {
	
	ProgressDialog dialog;
	ColibriEvent event;
	ViewEventActivity activity;
	
	public ImageSendReceiver(ViewEventActivity viewEventActivity, ColibriEvent event, ProgressDialog dialog) {
		this.dialog = dialog;
		this.event = event;
		this.activity = viewEventActivity;
	}

	public void parse(String result) {
		String error;
		try {
			JSONObject resultJson = new JSONObject(result);
			if (resultJson.getString("result").equalsIgnoreCase("success")) {
				try {
					this.event.thumbImageUrl = new URL(resultJson.getString("pic_thumb"));
					this.event.imageUrl = new URL(resultJson.getString("pic"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Success, R.string.SuccessfulEventEdit, R.string.OK, new OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						ImageView imageView = (ImageView) activity.findViewById(R.id.viewEventImage);
						DrawableManager.getInstance().fetchDrawableOnThread(event.thumbImageUrl.toString(),imageView);
						ImageSendReceiver.this.dialog.dismiss();
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

		AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, error, R.string.OK, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				ImageSendReceiver.this.dialog.dismiss();
			}
		});
	}

	public void error(String error) {
		AlertDialogHelper.ShowDialog(ColibriActivity.currentActivity, R.string.Error, error , R.string.OK, new OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				ImageSendReceiver.this.dialog.dismiss();
			}
		});
		
	}

}
