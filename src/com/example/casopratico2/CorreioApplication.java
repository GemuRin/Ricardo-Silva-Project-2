package com.example.casopratico2;

import android.app.Application;

public class CorreioApplication extends Application {
	public String getRssUrl() {
		return "http://www.cmjornal.xl.pt/rss.aspx";
	}

}