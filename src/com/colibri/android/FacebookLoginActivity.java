package com.colibri.android;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
//import android.webkit.WebViewClient;

public class FacebookLoginActivity extends Activity {
	
	private class ToJava {
		@SuppressWarnings("unused")
		public void loggedSuccessfully(String html)
		{
	    	FacebookLoginActivity.this.close();
		}
	}
	
//	private final WebViewClient client = new WebViewClient() {
//		public void onPageFinished(WebView view, String url) {
//			super.onPageFinished(view, url);
//		}
//	};
	
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		
		WebView webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);

//		webView.setWebViewClient(client);
		webView.addJavascriptInterface(new ToJava(), "toJava");
		
		webView.loadUrl("http://nastop.com/colibri/fbLogin.php");
		this.setContentView(webView);
	}
	
	public void close() {
		this.finish();
	}

}
