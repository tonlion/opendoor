package com.zhima.opendoor.fragment;


import com.zhima.opendoor.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


public class InnerFragment extends Fragment implements OnClickListener{
	
	@Nullable
	private ImageView img;
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.tab_item_op, null);
		
		return v;
	}


	@Override
	public void onClick(View arg0) {
	}
}
