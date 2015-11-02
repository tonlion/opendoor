package com.zhima.opendoor.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.DetailsArticleActivity;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.utils.ImageLoaderUtils;

public class ArticleListAdapter extends BaseAdapter {
	private List<Article> list;
	private Context context;

	public ArticleListAdapter(List<Article> list, Context context) {
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
			view=LayoutInflater.from(context).inflate(R.layout.article_list_item_layout, null);
			
			holder.article_name=(TextView) view.findViewById(R.id.list_title);
			
			holder.time=(TextView) view.findViewById(R.id.list_time);
			holder.module=(TextView) view.findViewById(R.id.list_module);
			
			view.setTag(holder);
			
		}else{
			holder=(Holder) view.getTag();
		}
		final Article a=list.get(arg0);
		holder.article_name.setText(a.getArticle_title());
		//设置发送时间
		if(System.currentTimeMillis()-a.getCreate_time()>1000*60*60){
			SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
			  String retStrFormatNowDate = sdFormatter.format(a.getCreate_time());
			  holder.time.setText(retStrFormatNowDate);
		}else{
			long time=(System.currentTimeMillis()-a.getCreate_time())/1000;
			if(time>60){
				long timee=time/60;
				holder.time.setText(timee+""+"分钟前");
			}else{
				 holder.time.setText(time+""+"秒前");
			}
			
		}		
		holder.module.setText(a.getModule_name());
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(arg0.getContext(),DetailsArticleActivity.class);
				intent.putExtra("article", a);
				arg0.getContext().startActivity(intent);
			}
		});
	
		return view;
	}
	public static class Holder{
		public TextView time;
		public TextView article_name;
		public TextView module;
	}

}
