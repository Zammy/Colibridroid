package com.colibri.android.whatscooking;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.colibri.android.ColibriActivity;
import com.colibri.android.R;
import com.colibri.android.Server.DrawableManager;
import com.colibri.android.data.ColibriEvent;

public class EventAdapter extends BaseAdapter {
	
	private final ArrayList<ColibriEvent> events;
	private final DrawableManager drawableManager = new DrawableManager();

	public EventAdapter(ArrayList<ColibriEvent> events) {
		this.events = events;	
	}

	public int getCount() {
		return events.size();// / 2 + 1;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	private int lastIndex = -1;
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)ColibriActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.event_image_view, null);
		}
		
		if (position != this.lastIndex) {
			ColibriEvent e = this.events.get(position);
			this.hookEventInformation(convertView,e);
			this.lastIndex = position;
		}
		return convertView;
	}

	private void hookEventInformation(View eventImageView,ColibriEvent event) {
		ImageView thumbImage = (ImageView) eventImageView.findViewById(R.id.eventImageThumb);
		this.drawableManager.fetchDrawableOnThread(event.ThumbImageUrl.toString(), thumbImage);
		
		TextView description = (TextView)eventImageView.findViewById(R.id.eventDescription);
		description.setText(event.Description);
	}
}
