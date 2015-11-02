package com.zhima.opendoor.utils;



import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class StringPostRequest extends StringRequest {
	private Map<String,String> params;

	public StringPostRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		initMap();
		
	}
	public StringPostRequest( String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(Request.Method.POST, url, listener, errorListener);
		initMap();
		
	}
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}
	private void initMap(){
		params=new HashMap<String, String>();
	}
	public void putParams(String key,String value){
		this.params.put(key, value);
	}
	

}
