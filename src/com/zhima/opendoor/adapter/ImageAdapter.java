package com.zhima.opendoor.adapter;
import com.zhima.opendoor.R;
import com.zhima.opendoor.utils.ImageLoaderUtils;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class ImageAdapter extends BaseAdapter {
	private Context context;
	private String[] data;
	private int size;

	public ImageAdapter(Context context,String[]data,int size) {
		this.context = context;
		this.data=data;
		this.size=size;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if(size>3){	
				if(position==2){
					View v=LayoutInflater.from(context).inflate(R.layout.multi_pic, null);
					ImageView view=(ImageView) v.findViewById(R.id.mul);
				//	view.setImageResource(data[position]);
					ImageLoaderUtils.display(data[position], view);
					TextView text=(TextView) v.findViewById(R.id.text);
					text.setGravity(Gravity.BOTTOM);
					text.setText("π≤"+size+""+"’≈");
					convertView=v;
				}else{
					View v=LayoutInflater.from(context).inflate(R.layout.image_layout, null);
					ImageView view=(ImageView) v.findViewById(R.id.image_layout);
					//view.setImageResource(data[position]);
					ImageLoaderUtils.display(data[position], view);
					convertView = v;
				}
				
			}else{
				View v=LayoutInflater.from(context).inflate(R.layout.image_layout, null);
				ImageView view=(ImageView) v.findViewById(R.id.image_layout);
				//view.setImageResource(data[position]);
				ImageLoaderUtils.display(data[position], view);
				convertView = v;
			}
			
		}

		return convertView;
	}

}
