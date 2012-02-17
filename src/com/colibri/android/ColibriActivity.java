package com.colibri.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ColibriActivity extends Activity {
   Facebook facebook = new Facebook("306736219374446");
   
//   final String FILENAME = "AndroidSSO_data";
   private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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