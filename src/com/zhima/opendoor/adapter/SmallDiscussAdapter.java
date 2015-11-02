package com.zhima.opendoor.adapter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.OtherPersonInfoActivity;
import com.zhima.opendoor.entity.Discuss;

public class SmallDiscussAdapter extends BaseAdapter{
	private List<Discuss> send;
	private Context context;
	private smallListener listener;

	public smallListener getListener() {
		return listener;
	}

	public void setListener(smallListener listener) {
		this.listener = listener;
	}

	public SmallDiscussAdapter(List<Discuss> send,Context context) {
		super();
		this.send = send;
		this.context=context;
	}

	@Override
	public int getCount() {
		
		return send.size();
	}

	@Override
	public Object getItem(int arg0) {
		return send.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View smallView, ViewGroup arg2) {
		Holder holer;
		if(smallView==null){
			holer=new Holder();
			
			 smallView=LayoutInflater.from(context).inflate(R.layout.sencond_discuss_item, null);
			 holer.name=(TextView) smallView.findViewById(R.id.floorName);
			 holer.content=(TextView) smallView.findViewById(R.id.floorContent);
			smallView.setTag(holer);
		}else{
			holer=(Holder) smallView.getTag();
		}
		final Discuss d=send.get(arg0);
		holer.name.setText(d.getUser_name());
		Pattern p=Pattern.compile("smiley_[0-9]{1,2}");
		Matcher m=p.matcher(d.getContent());
		SpannableString sb=new SpannableString(d.getContent());
		while(m.find()){
			ImageSpan span=new ImageSpan(context,getResourceId(m.group()));
			sb.setSpan(span, m.start(), m.start()+m.group().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		holer.content.setText(sb);
		holer.content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(listener!=null){
					listener.getItemContent(d.getUser_name(),d.getUser_id());
				}
				
			}
		});
		holer.name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(context, OtherPersonInfoActivity.class);
			intent.putExtra("userId", d.getUser_id());
				arg0.getContext().startActivity(intent);		
			}
		});
		return smallView;
	}
	public class Holder{
		public TextView name;
		public TextView content;
	}
	public interface smallListener{
		public void getItemContent(String name,String id);
	}
	public int getResourceId(String name){
		Field filed;
		try {
			filed = R.drawable.class.getField(name);
			return Integer.parseInt(filed.get(null).toString());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
		
	}
	

}

