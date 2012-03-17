package com.colibri.android.whatscooking;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.android.data.ColibriEvent;

public class EventAdapter extends BaseAdapter {
	
	private final ArrayList<ColibriEvent> events;

	public EventAdapter(ArrayList<ColibriEvent> events) {
		this.events = events;	
	}

	public int getCount() {
		return events.size();
	}

	public Object getItem(int position) {
		return this.events.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View eventImageView = convertView;
		
		if (eventImageView == null) {
            LayoutInflater vi = (LayoutInflater)ColibriActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            eventImageView = vi.inflate(R.layout.event_image_view, null);
		}
		
		ColibriEvent e = this.events.get(position);
		this.hookEventInformation(eventImageView,e);
		
		return eventImageView;
	}

	private void hookEventInformation(View eventImageView,ColibriEvent event) {
		ImageView thumbImage = (ImageView) eventImageView.findViewById(R.id.eventImageThumb);
		InputStream stream = null;
		try {
			stream = (InputStream) event.ThumbImageUrl.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(stream, "src");
		thumbImage.setImageDrawable(d);
		
		TextView description = (TextView)eventImageView.findViewById(R.id.eventDescription);
		description.setText(event.Description);

		//eventImageView.setLayoutParams(new GridView.LayoutParams(240, 150));
	}
}
