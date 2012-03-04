package com.colibri.android.Server;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class SendReceiverBase {
	
	public void parse(String result) {
		String newResult = result.substring(3);
		try {
			JSONObject o = new JSONObject(newResult);
			this.onReceive(o);
		} catch (JSONException e) {
			e.printStackTrace();
			this.error("Error parsing json.");
		}
		
	}
	public abstract void error(String error);
	
	public abstract void onReceive(JSONObject o);
}
