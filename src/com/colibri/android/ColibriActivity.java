package com.colibri.android;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.colibri.android.Server.EventsReceiver;
import com.colibri.android.Server.Server;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ColibriActivity extends TabActivity {

	public static Activity currentActivity;
	
	public static ColibriActivity instance;
	
   Facebook facebook = new Facebook("306736219374446");
   public static String accessToken;
   
   private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	instance = this;
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.InitTabInterface();
        
        this.FacebookLogin();
    }

	private void InitTabInterface() {
		Resources res = this.getResources(); // Resource object to get Drawables
        TabHost tabHost = this.getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MapTab.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Stroll").setIndicator("Stroll",
                          res.getDrawable(R.drawable.map))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, WhatsCookingTab.class);
        spec = tabHost.newTabSpec("Whats' Cooking").setIndicator("Whats' Cooking",
                          res.getDrawable(R.drawable.cook))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MyAgendaTab.class);
        spec = tabHost.newTabSpec("My Agenda").setIndicator("My Agenda",
                          res.getDrawable(R.drawable.agenda))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
	}

	private void FacebookLogin() {
		mPrefs = getPreferences(MODE_PRIVATE);
        accessToken = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if (accessToken != null) {
            facebook.setAccessToken(accessToken);
        }
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        if(facebook.isSessionValid()) {
        	   
            Server.getEvents(accessToken, new EventsReceiver());
        	return;
        }

        facebook.authorize(this, new String[] { "email", "user_events","create_event","rsvp_event","publish_stream","read_friendlists" }, new DialogListener() {

	        public void onComplete(Bundle values) {
	            SharedPreferences.Editor editor = mPrefs.edit();

	            String accessToken = facebook.getAccessToken();
	            editor.putString("access_token", accessToken);
	            editor.putLong("access_expires", facebook.getAccessExpires());
	            editor.commit();
	            
	            Server.getEvents(accessToken, new EventsReceiver());
	        }
	
	
	        public void onFacebookError(FacebookError error) {
	        	Log.d("Colibri", error.getLocalizedMessage());
	        }
	
	
	        public void onError(DialogError e) {
	        	
	        }
	
	        public void onCancel() {
	        	
	        }
        });
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	
	    facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}