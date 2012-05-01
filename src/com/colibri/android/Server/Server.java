package com.colibri.android.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.colibri.android.data.ColibriEvent;

public class Server {
	
	private static final String getEventsUrl = "http://nastop.com/colibri/getEvents.php";
	private static final String createEventUrl = "http://nastop.com/colibri/createEvent.php";
	
//	private static String android_id;
	
//	public static void getUDID(ContentResolver resolver) {
//		if (android_id == null)
//			android_id = Secure.getString(resolver,Secure.ANDROID_ID);
//	}
	
	public static void getEvents(String access_token, EventsReceiver receiver) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
	    send(getEventsUrl,params, receiver);
	}
	
	public static void newEvent(String access_token,ColibriEvent event, NewEventReceiver receiver) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("name", event.Name));
		params.add(new BasicNameValuePair("description", event.Description));
		params.add(new BasicNameValuePair("location","Not implemented."));
		params.add(new BasicNameValuePair("lon", Double.toString(event.Longitude)));
		params.add(new BasicNameValuePair("lat", Double.toString(event.Latitude)));
		params.add(new BasicNameValuePair("start_time", event.startTimeAsString()));
		params.add(new BasicNameValuePair("end_time", event.endTimeAsString()));
		if (event.isPrivate) 
			params.add(new BasicNameValuePair("privacy", "SECRET"));
		//String payload = jsonify(params);
		send(createEventUrl,params, receiver);
	}
	
//	private static String jsonify(HashMap<String,String> params) {
//		JSONObject o = new JSONObject(params);
//		return o.toString();
//	}

	private static void send(final String url, final ArrayList<NameValuePair> nameValuePairs,final ISendReceiver sendReceiver) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);
				try {
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = client.execute(post);
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),Charset.forName("UTF-8")));
					String received = rd.readLine();
					sendReceiver.parse(received);		
					
				} catch (IOException e) {
					e.printStackTrace();
					sendReceiver.error(e.getMessage());
				}
			}
		});
		t.start();
	}
}
