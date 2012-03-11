package com.colibri.android.data;


public class ColibriEvent {
	public double Longitude;
	public double Latitude;
	public String Description;
	public ColibriEvent.Type type;
	
	public enum Type {
		Drinking,
		Concert,
		Movie
	}
}
