package com.zhima.opendoor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Switch;

import com.zhima.opendoor.R;
import com.zhima.opendoor.view.SwipeBackActivity;

public class ToolActivity extends SwipeBackActivity {
	private WebView content;
	private Intent intent;
	private int type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent=getIntent();
		type=intent.getIntExtra("type", 0);
		setContentView(R.layout.tool_activity_layout);
		content=(WebView) findViewById(R.id.webView);
		 startWebview();
	}
	private void startWebview(){
		switch (type) {
		case 1:
			content.loadUrl("http://10years.me");
			content.setWebViewClient(new WebViewClient());
			break;
		case 2:
			content.loadUrl("http://animetaste.net/");
			content.setWebViewClient(new WebViewClient());
			break;
		case 3:
			content.loadUrl("http://www.shaoerduo.com/");
			content.setWebViewClient(new WebViewClient());
			break;
		case 4:
			content.loadUrl("http://www.rainymood.com/");
			content.setWebViewClient(new WebViewClient());
			break;

		default:
			break;
		}
	}

}
