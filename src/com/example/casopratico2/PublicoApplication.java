package com.example.casopratico2;

import android.app.Application;

public class PublicoApplication extends Application {
	public String getRssUrl() {
		return "http://blog.masterd.pt/index.php/feed";
	}

}