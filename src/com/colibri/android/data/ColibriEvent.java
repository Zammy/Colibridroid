package com.colibri.android.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;


public class ColibriEvent {
	public double Longitude;
	public double Latitude;
	public String Name;
	public String Description;
	public ColibriEvent.Type type;
	public URL ImageUrl;
	public URL ThumbImageUrl;
	
	public enum Type {
		Drinking,
		Concert,
		Movie
	}
	
	
	public static ArrayList<ColibriEvent> events;
	
	static {
		
		Log.i("A", "STATIC");
		
		ColibriEvent.events = new ArrayList<ColibriEvent>();
		
		ColibriEvent e = new ColibriEvent();
		
		e.Name = "The bar kick";
		e.Description = "Going out to bar!";
		e.Latitude = 42.64645919;
		e.Longitude = 23.39435896;
		e.type = ColibriEvent.Type.Drinking;
		try {
			e.ThumbImageUrl = new URL("http://dl.dropbox.com/u/4673216/Images/binge-drinking.jpg");
			e.ImageUrl = new URL("http://1.bp.blogspot.com/-prXOLy31Hv4/TkuxKrAt5AI/AAAAAAAAFgc/6IEJGxKqkzM/s1600/binge-drinking.jpg");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ColibriEvent.events.add(e);
		
		e = new ColibriEvent();
		e.Name = "Horror night!";
		e.Description = "Going out to movie!";
		e.Latitude = 42.63445919;
		e.Longitude = 23.38235196;
		e.type = ColibriEvent.Type.Movie;
		try {
			e.ThumbImageUrl = new URL("http://dl.dropbox.com/u/4673216/Images/Fast-and-Furious-5-Movie-Poster.jpg");
			e.ImageUrl = new URL("http://www.trailershut.com/movie-posters/Fast-and-Furious-5-Movie-Poster.jpg");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ColibriEvent.events.add(e);
		
		e = new ColibriEvent();
		e.Name = "SOAD TIME";
		e.Description = "Going out to rock!";
		e.Latitude = 42.63446919;
		e.Longitude = 23.37245196;
		e.type = ColibriEvent.Type.Concert;
		try {
			e.ThumbImageUrl = new URL("http://dl.dropbox.com/u/4673216/Images/hard-rock-hotel-concerts.jpg");
			e.ImageUrl = new URL("http://www.destination360.com/north-america/us/nevada/las-vegas/images/s/hard-rock-hotel-concerts.jpg");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ColibriEvent.events.add(e);
	}
}
