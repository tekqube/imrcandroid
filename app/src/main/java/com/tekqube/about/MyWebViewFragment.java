package com.tekqube.about;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tekqube.imrc.R;

//import com.example.androidwebviewfragment.MainActivity.MyWebViewFragment.MyWebViewClient;

public class MyWebViewFragment extends Fragment {
	
	WebView myWebView;
	final static String myBlogAddr = "https://www.facebook.com/imrcdetroit2014";
	String myUrl;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.webview_layout_webfragment, container, false);
		myWebView = (WebView)view.findViewById(R.id.mywebview);
		
		myWebView.getSettings().setJavaScriptEnabled(true);                
		myWebView.setWebViewClient(new MyWebViewClient());
		
		if(myUrl == null){
			myUrl = myBlogAddr;
		}
		myWebView.loadUrl(myUrl);
	     
        return view;

	}
	
	private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	myUrl = url;
            view.loadUrl(url);
            return true;
        }
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

}