package com.colibri.android.data;

import java.net.URL;


public class ColibriEvent {
	public double Longitude;
	public double Latitude;
	public String Description;
	public ColibriEvent.Type type;
	public URL ImageUrl;
	public URL ThumbImageUrl;
	
	public enum Type {
		Drinking,
		Concert,
		Movie
	}
}
