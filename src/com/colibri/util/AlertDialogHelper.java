package com.colibri.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogHelper {
	
	public static void ShowDialog(final Context context,final int title,final int message,final int buttonText,final OnClickListener clickListener) {
		((Activity)context).runOnUiThread(new Runnable() {

			public void run() {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle(title);
				alertDialog.setMessage(context.getString(message));
				if (clickListener != null)
					alertDialog.setButton(context.getString(buttonText),clickListener);
				alertDialog.show();
			}
		
		});
	}
	
	public static void ShowDialog(final Context context,final int title,final String message,final int buttonText,final OnClickListener clickListener) {
		((Activity)context).runOnUiThread(new Runnable() {

			public void run() {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(context).create();
				alertDialog.setTitle(title);
				alertDialog.setMessage(message);
				if (clickListener != null)
					alertDialog.setButton(context.getString(buttonText),clickListener);
				alertDialog.show();
			}
		
		});
	}

}
