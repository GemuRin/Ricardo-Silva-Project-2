package com.example.casopratico2;

import android.app.Application;

public class TSFApplication extends Application {
	public String getRssUrl() {
		return "http://feeds.tsf.pt/TSF-Ultimas";
	}

}
