package com.colibri.android;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ColibriActivity extends TabActivity {
   Facebook facebook = new Facebook("306736219374446");
   
   private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        tabHost.setCurrentTab(2);
	}

	private void FacebookLogin() {
		mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if (access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        if(facebook.isSessionValid()) 
        	return;

        facebook.authorize(this, new String[] { "email", "publish_checkins" },  new DialogListener() {

	        public void onComplete(Bundle values) {
	            SharedPreferences.Editor editor = mPrefs.edit();
	            editor.putString("access_token", facebook.getAccessToken());
	            editor.putLong("access_expires", facebook.getAccessExpires());
	            editor.commit();
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
}