package com.colibri.android;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class FacebookLoginActivity extends Activity {
	
	private class ToJava {
		@SuppressWarnings("unused")
		public void loggedSuccessfully() {
	    	FacebookLoginActivity.this.close();
		}
	}
	
	
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		
		WebView webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.addJavascriptInterface(new ToJava(), "ToJava");
		
		webView.loadUrl("http://nastop.com/colibri/fbLogin.php");
		this.setContentView(webView);
	}
	
	public void close() {
		this.finish();
	}

}
