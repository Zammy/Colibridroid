package com.colibri.android.Server;


public interface ISendReceiver {
	public void parse(String result);
	public void error(String error);
}
