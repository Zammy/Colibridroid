package com.colibri.android;

import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.colibri.android.Server.DeleteEventReceiver;
import com.colibri.android.Server.DrawableManager;
import com.colibri.android.Server.ImageSendReceiver;
import com.colibri.android.Server.Server;
import com.colibri.android.data.ColibriEvent;
import com.colibri.android.maps.MapLocationDataHolder;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ViewEventActivity extends MapActivity {
	
	private static final int SELECT_PICTURE = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ColibriActivity.currentActivity = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vieweventactivity);
	}
	
	@Override
	protected void onResume() {
		super.onRestart();
		
		Intent i = this.getIntent();
		final int indexOfEvent = i.getIntExtra("event", -1);
		
		final ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
		
		ImageView imageView = (ImageView) this.findViewById(R.id.viewEventImage);
		DrawableManager.getInstance().fetchDrawableOnThread(event.thumbImageUrl.toString(),imageView);
		
		TextView textView = (TextView) this.findViewById(R.id.nameEvent);
		textView.setText(event.name);
		
		textView = (TextView) this.findViewById(R.id.eventDescription);
		textView.setText(event.description);
		
		MapView miniMap = (MapView) this.findViewById(R.id.eventLocationMap);
		MapController controller = miniMap.getController();

		controller.setCenter(new GeoPoint(
	            (int) (event.latitude * 1E6), 
	            (int) (event.longitude * 1E6)));
		controller.setZoom(17);
		
		
		textView = (TextView) this.findViewById(R.id.startTimeDate);
		textView.setText(event.startTimeAsString());
		textView = (TextView) this.findViewById(R.id.endTimeDate);
		textView.setText(event.endTimeAsString());
		
		Button editEventButton = (Button)this.findViewById(R.id.editEventButton);
		Button deleteEventButton = (Button)this.findViewById(R.id.deleteEventButton);
		if(event.isOwnedByMe) {
		
			editEventButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					 Intent intent = new Intent().setClass(ColibriActivity.currentActivity, NewEventActivity.class);
					 intent.putExtra("event", indexOfEvent);
					 ColibriActivity.currentActivity.startActivity(intent);
				}
				
			});
			
			deleteEventButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						
					    public void onClick(DialogInterface dialog, int which) {
					        switch (which){
					        case DialogInterface.BUTTON_POSITIVE:
					        	Server.deleteEvent(ColibriActivity.accessToken, event.fbPointer, new DeleteEventReceiver(indexOfEvent));
					            break;

					        case DialogInterface.BUTTON_NEGATIVE:
					            break;
					        }
					    }
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(ViewEventActivity.this);
					builder.setMessage("Are you sure?")
						.setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener)
					    .show();
				}
				
			});
		} else {
			deleteEventButton.setVisibility(View.GONE);
			editEventButton.setVisibility(View.GONE);
		}
	}
	
	public void imageClicked(final View view) {
		
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) { 

    		final ProgressDialog dialog = ProgressDialog.show(this, "", 
                    "Please wait while image is uploaded...", true);

            Uri selectedImageUri = imageReturnedIntent.getData();
            String filePath = this.getPath(selectedImageUri);

    		Intent i = this.getIntent();	
    		final int indexOfEvent = i.getIntExtra("event", -1);
    		final ColibriEvent event = ColibriEvent.events.get(indexOfEvent);
    		
    		Bitmap bitmapOrg = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            String filenameArray[] = filePath.split("\\.");
            String extension = filenameArray[filenameArray.length-1];
            if (extension.equalsIgnoreCase("png")) {
            	bitmapOrg.compress(Bitmap.CompressFormat.PNG, 90, bao);
            } else if (extension.equalsIgnoreCase("jpg")) {
            	bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            }
            byte [] ba = bao.toByteArray();
            String ba1 = com.colibri.util.Base64.encodeBytes(ba);

            Server.uploadImage(ColibriActivity.accessToken, event.fbPointer, ba1, extension , new ImageSendReceiver(this,event,dialog));

        }
	    
	}
	
	private String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor
	            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
