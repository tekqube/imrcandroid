package com.tekqube.about;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.webkit.WebView;

import com.tekqube.imrc.R;

public class WebViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = this.getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a00000")));
		setContentView(R.layout.webview_activity_main);
	}

	@Override
	public void onBackPressed() {
		MyWebViewFragment fragment = 
				(MyWebViewFragment)getFragmentManager().findFragmentById(R.id.myweb_fragment);
		WebView webView = fragment.myWebView;
		
		if(webView.canGoBack()){
			webView.goBack();
		}else{
			super.onBackPressed();
		}
	}

}
