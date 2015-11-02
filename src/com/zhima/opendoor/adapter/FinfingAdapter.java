package com.zhima.opendoor.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.ModuleDetailsActivity;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.utils.ImageLoaderUtils;

public class FinfingAdapter extends BaseAdapter {
	private List<Module> modules;
	private Context context;

	public FinfingAdapter(List<Module> modules, Context context) {
		super();
		this.modules = modules;
		this.context = context;
	}

	@Override
	public int getCount() {
		
		return modules.size();
	}

	@Override
	public Object getItem(int arg0) {
		return modules.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		Holder mHolder;
		if(view==null){
			mHolder=new Holder();
			view=LayoutInflater.from(context).inflate(R.layout.finding_item, null);
			mHolder.title=(TextView) view.findViewById(R.id.title);
			mHolder.content=(TextView) view.findViewById(R.id.content);
			mHolder.moduleImg=(ImageView) view.findViewById(R.id.moduleImg);
			mHolder.enter=(Button) view.findViewById(R.id.enter);
			mHolder.divider=view.findViewById(R.id.view);
			view.setTag(mHolder);
			
		}else{
			mHolder=(Holder) view.getTag();
		}
		final Module m=modules.get(arg0);
		mHolder.title.setText(m.getModule_name());
		mHolder.content.setText(m.getModule_content());
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(context,ModuleDetailsActivity.class);
				intent.putExtra("module",m);	
				arg0.getContext().startActivity(intent);
				
			}
		});
		if(m.getImg()==null||m.getImg().length()==0){
			
			
		}else{
			ImageLoaderUtils.display(m.getImg(), mHolder.moduleImg);
		}
		
	
		return view;
	}
	public static class Holder{
		public TextView title;
		public ImageView moduleImg;
		public TextView content;
		public Button enter;
		public View divider;
	}

}
