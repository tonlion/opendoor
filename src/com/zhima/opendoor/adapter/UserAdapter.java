package com.zhima.opendoor.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.OtherPersonInfoActivity;
import com.zhima.opendoor.entity.UserFollow;
import com.zhima.opendoor.utils.ImageLoaderUtils;

public class UserAdapter extends BaseAdapter {
	private List<UserFollow> list;
	private Context context;

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
	
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
	
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		Holder holder;
		if(view==null){
			holder=new Holder();
			view=LayoutInflater.from(context).inflate(R.layout.follower_layout, null);
			holder.name=(TextView) view.findViewById(R.id.titleFollower);
			holder.content=(TextView) view.findViewById(R.id.followerContent);
			holder.head=(ImageView) view.findViewById(R.id.follower_img);
			view.setTag(holder);
		}else{
			holder=(Holder) view.getTag();
		}
		final UserFollow u=list.get(arg0);
		holder.name.setText(u.getUser_name());
		holder.content.setText(u.getUser_desc());
		//ÓÃimage¼ÓÔØ
		if(u.getHead_img()!=null&&u.getHead_img().length()>0){
			ImageLoaderUtils.display(u.getHead_img(), holder.head);
		}
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(arg0.getContext(), OtherPersonInfoActivity.class);
				intent.putExtra("userId", u.getUser_id());
				arg0.getContext().startActivity(intent);
				
			}
		});
		
		
		return view;
	}
	public UserAdapter(List<UserFollow> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}
	public class Holder{
		public TextView name;
		public TextView content;
		public ImageView head;
	}

}
