package com.colibri.android.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.colibri.android.ColibriActivity;
import com.colibri.android.data.ColibriEvent;

public class Server {
	public static ArrayList<ColibriEvent> getEvents() {
		ArrayList<ColibriEvent> events = new ArrayList<ColibriEvent>();
		ColibriEvent e = new ColibriEvent();
		
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
		events.add(e);
		
		e = new ColibriEvent();
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
		events.add(e);
		
		e = new ColibriEvent();
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
		events.add(e);
		
		return events;
	}
	
	private static final String serviceUrl = "http://nastop.com/colibri/fbAllFriends.php";
//	private static String android_id;
	
//	public static void getUDID(ContentResolver resolver) {
//		if (android_id == null)
//			android_id = Secure.getString(resolver,Secure.ANDROID_ID);
//	}
	
	public static void getFriends(SendReceiverBase receiver) {
		HashMap<String,String> params = new HashMap<String,String>();
//		params.put("registrationID", registrationID);
		String payload = jsonify(params);
		
		send(payload,receiver);
	}
	
	private static String jsonify(HashMap<String,String> params) {
		JSONObject o = new JSONObject(params);
		return o.toString();
	}

	private static void send(final String payload,final SendReceiverBase sendReceiver) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(serviceUrl);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
				nameValuePairs.add(new BasicNameValuePair("access_token",ColibriActivity.instance.accessToken));
				try {
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = client.execute(post);
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),Charset.forName("UTF-8")));
					String received = rd.readLine();
					//sendReceiver.parse(received);		
					int x = 1;
					x++;
					
				} catch (IOException e) {
					e.printStackTrace();
					sendReceiver.error(e.getMessage());
				}
			}
		});
		t.start();
	}
}