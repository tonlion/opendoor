package com.zhima.opendoor.adapter;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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

public class ArticleAdapter extends BaseAdapter {
	private List<Article> list;
	private Context context;

	public ArticleAdapter(List<Article> list, Context context) {
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
			view=LayoutInflater.from(context).inflate(R.layout.article_item_layout, null);
			holder.userImg=(ImageView) view.findViewById(R.id.userImg);
			holder.article_name=(TextView) view.findViewById(R.id.article_name);
			holder.content=(TextView) view.findViewById(R.id.artile_content);
			holder.gridview=(GridView) view.findViewById(R.id.gridView);
			holder.pinglunshu=(TextView) view.findViewById(R.id.pinglunshu);
			holder.zanshu=(TextView) view.findViewById(R.id.zanshu1);
			holder.time=(TextView) view.findViewById(R.id.time);
			holder.user_name=(TextView) view.findViewById(R.id.user_name);
			view.setTag(holder);
			
		}else{
			holder=(Holder) view.getTag();
		}
		final Article a=list.get(arg0);
		holder.article_name.setText(a.getArticle_title());
		String html=a.getArticle_content();
		//设置发送时间
	/*	if(System.currentTimeMillis()-a.getUpdate_time()>1000*60*60){
			SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
			  String retStrFormatNowDate = sdFormatter.format(a.getCreate_time());
			  holder.time.setText(retStrFormatNowDate);
		}else{
			long time=(System.currentTimeMillis()-a.getCreate_time())/1000;
			if(time>60){
				long timee=time/60;
				holder.time.setText(timee+""+"分钟前");
			}else{
				 holder.time.setText(time+90+""+"秒前");
			}
			
		}*/		
		//holder.time.setText(a.getCreate_time()+"");
		SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
		  String retStrFormatNowDate = sdFormatter.format(a.getCreate_time());
		  holder.time.setText(retStrFormatNowDate);
		holder.zanshu.setText(a.getZan()+"");
		holder.pinglunshu.setText(a.getDiscuss()+"");
		String[] image=a.getImageList();
		if(a.getImageList()==null||a.getImageList().length==0){
			image=new String[0];
		}
		if(image.length>3){
			String images[]=new String[3];
			for(int i=0;i<3;i++){
				images[i]=image[i];
			}
			holder.gridview.setAdapter(new ImageAdapter(context, images,image.length));
		}else{
			holder.gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
			
			holder.gridview.setAdapter(new ImageAdapter(context, image,image.length));
		}
		if(image.length>0){
			holder.content.setVisibility(View.GONE);
		}else{
			holder.content.setVisibility(View.VISIBLE);
			html=html.replaceAll("<br/>", "");
			Pattern p=Pattern.compile("<[^>]*>");
			Matcher m=p.matcher(html);
			while(m.find()){
				html=html.replaceAll(m.group(), "");
			}
			html=html.replaceAll("<zhima></zhima>", "");
			
			Pattern pp=Pattern.compile("smiley_[0-9]{1,2}");
			Matcher mm=pp.matcher(html);
			SpannableString sb=new SpannableString(html);
			while(mm.find()){
				ImageSpan span=new ImageSpan(context,getResourceId(mm.group()));
				sb.setSpan(span, mm.start(), mm.start()+mm.group().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			holder.content.setText(sb);
		}
		if(a.getHead_img()!=null){
			ImageLoaderUtils.display(a.getHead_img(), holder.userImg);
		}else{
			holder.userImg.setImageResource(R.drawable.zhima);
		}
		
		holder.user_name.setText(a.getUser_name());
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
		public ImageView userImg;
		public TextView user_name;
		public TextView time;
		public TextView zanshu;
		public TextView pinglunshu;
		public TextView article_name;
		public TextView content;
		public GridView gridview;
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
