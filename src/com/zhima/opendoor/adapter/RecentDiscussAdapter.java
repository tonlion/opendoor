package com.zhima.opendoor.adapter;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
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
import com.zhima.opendoor.activity.DetailsArticleActivity;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.entity.Discuss;

public class RecentDiscussAdapter extends BaseAdapter {
	private List<Discuss> list;
	private Context context;

	public RecentDiscussAdapter(List<Discuss> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.list.get(arg0);
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
			view=LayoutInflater.from(context).inflate(R.layout.article_list_item_layout2, null);
			
			holder.article_name=(TextView) view.findViewById(R.id.list_title2);
			
			holder.time=(TextView) view.findViewById(R.id.list_time2);
			holder.content=(TextView) view.findViewById(R.id.Isay);
			view.setTag(holder);
			
		}else{
			holder=(Holder) view.getTag();
		}
		final Discuss a=list.get(arg0);
		holder.article_name.setText(a.getArticle_title());
		Pattern pp=Pattern.compile("smiley_[0-9]{1,2}");
		Matcher mm=pp.matcher(a.getContent());
		SpannableString sb=new SpannableString(a.getContent());
		while(mm.find()){
			ImageSpan span=new ImageSpan(context,getResourceId(mm.group()));
			sb.setSpan(span, mm.start(), mm.start()+mm.group().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		holder.content.setText(sb);
		//设置发送时间
		if(System.currentTimeMillis()-a.getTime()>1000*60*60){
			SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
			  String retStrFormatNowDate = sdFormatter.format(a.getTime());
			  holder.time.setText(retStrFormatNowDate);
		}else{
			long time=(System.currentTimeMillis()-a.getTime())/1000;
			if(time>60){
				long timee=time/60;
				holder.time.setText(timee+""+"分钟前");
			}else{
				 holder.time.setText((time+90)+""+"秒前");
			}
			
		}			
		return view;
	}
	public static class Holder{
		public TextView time;
		public TextView article_name;
		public TextView module;
		public TextView content;
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
