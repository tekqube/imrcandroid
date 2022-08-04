package com.tekqube.imrc;


	import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
	 
	public class SplashScreen extends Activity {
	 
	    // Splash screen timer
	    private static int SPLASH_TIME_OUT = 2000;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_splash);
	 
	        final ProgressDialog dialog = new ProgressDialog(SplashScreen.this);
	        
	        new Handler().postDelayed(new Runnable() {

	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashScreen.this, com.tekqube.imrc.NavDrawerMainActivity.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
	    }
	 
	}