package com.zhima.opendoor.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.zhima.opendoor.activity.DetailsArticleActivity;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.utils.ImageLoaderUtils;

public class MultiAdapter extends BaseAdapter {

	private List<Article> data;
	private Context context;

	public MultiAdapter(List<Article> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getItemViewType(int position) {
		if(data.get(position).getImageList()!=null&&data.get(position).getImageList().length>=3){
			return 2;
		}else if(position%3==0){
			return 1;
		}
			return 0;
			//小单图布局
		
	}
	

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public Object getItem(int arg0) {

		return this.data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int arg0, View convertview, ViewGroup arg2) {
		OneHolder oneHolder = null;//大单图
		TwoHolder twoHolder = null; //小单图
		ThreeHolder threeHolder=null;
		int type = getItemViewType(arg0);
		if (convertview == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			switch (type) {
			case 0:
				//小单图
				convertview = inflater.inflate(R.layout.select_item_2, null);
				twoHolder = new TwoHolder();
				
				twoHolder.contentSmall=(TextView) convertview.findViewById(R.id.smallContent);
				twoHolder.moduleNameSmall=(TextView) convertview.findViewById(R.id.modulenameSmall);
				twoHolder.pinglunshuSmall=(TextView) convertview.findViewById(R.id.pinglunSmall);
				twoHolder.zanshuSmall=(TextView) convertview.findViewById(R.id.zanshuSmall);
				twoHolder.titleSmall=(TextView) convertview.findViewById(R.id.titleSmall);
				twoHolder.singlePpic=(ImageView) convertview.findViewById(R.id.singlePic);
				convertview.setTag(twoHolder);
				break;
			case 1:
				//大单图布局
				convertview = inflater.inflate(R.layout.select_itemt_1, null);
				oneHolder = new OneHolder();
				oneHolder.moduleName=(TextView) convertview.findViewById(R.id.modulename);
				oneHolder.pinglunshu=(TextView) convertview.findViewById(R.id.pinglunshu);
				oneHolder.zanshu=(TextView) convertview.findViewById(R.id.zanshu);
				oneHolder.title=(TextView) convertview.findViewById(R.id.title);
				oneHolder.singleBig=(ImageView) convertview.findViewById(R.id.singleBig);
			
				convertview.setTag(oneHolder);
				break;
			case 2:
				convertview=inflater.inflate(R.layout.select_item_3, null);
				threeHolder=new ThreeHolder();
				threeHolder.pic1=(ImageView) convertview.findViewById(R.id.pic1);
				threeHolder.pic2=(ImageView) convertview.findViewById(R.id.pic2);
				threeHolder.pic3=(ImageView) convertview.findViewById(R.id.pic3);
				threeHolder.pinglunshuThree=(TextView) convertview.findViewById(R.id.pinglunThree);
				threeHolder.zanshuThree=(TextView) convertview.findViewById(R.id.zanshuThree);
				threeHolder.moduleNameThree=(TextView) convertview.findViewById(R.id.modulenameThree);
				threeHolder.titleThree=(TextView) convertview.findViewById(R.id.titleThree);
				convertview.setTag(threeHolder);
				break;
			}
		} else {
			switch (type) {
			case 0:
				twoHolder = (TwoHolder)convertview.getTag();
				break;
			case 1:
				oneHolder = (OneHolder) convertview.getTag();
				break;
			case 2:
				threeHolder=(ThreeHolder) convertview.getTag();
			}

		}
		// 绑定数据
		final Article a = data.get(arg0);
		String html=a.getArticle_content();
		switch (type) {
		case 0:
			html=html.replaceAll("<br/>", "");
			Pattern p=Pattern.compile("<[^>]*>");
			Matcher m=p.matcher(html);
			while(m.find()){
				html=html.replaceAll(m.group(), "");
			}
			html=html.replaceAll("<zhima></zhima>", "");
			twoHolder.contentSmall.setText(html);
			//以后要改
			twoHolder.moduleNameSmall.setText(a.getModule_name());
			twoHolder.pinglunshuSmall.setText(a.getDiscuss()+"");
			twoHolder.zanshuSmall.setText(a.getZan()+"");
			twoHolder.titleSmall.setText(a.getArticle_title());
			if(a.getImageList()!=null&&a.getImageList().length>0){
				ImageLoaderUtils.display(a.getImageList()[0], twoHolder.singlePpic);
			}
		//	ImageLoaderUtils.display(a.getImageList()[0], twoHolder.singlePpic);
			
			//加载图片
			break;
		case 1:
			oneHolder.moduleName.setText(a.getModule_name());
			oneHolder.pinglunshu.setText(a.getDiscuss()+"");
			oneHolder.zanshu.setText(a.getZan()+"");
			oneHolder.title.setText(a.getArticle_title());
			if(a.getImageList()!=null&&a.getImageList().length>0){
				ImageLoaderUtils.display(a.getImageList()[0], oneHolder.singleBig);
			}
			
			break;
		case 2:
			threeHolder.moduleNameThree.setText(a.getModule_name());
			if(a.getImageList()!=null&&a.getImageList().length>0){
				ImageLoaderUtils.display(a.getImageList()[0], threeHolder.pic1);
				ImageLoaderUtils.display(a.getImageList()[1], threeHolder.pic2);
				ImageLoaderUtils.display(a.getImageList()[2], threeHolder.pic3);
			}
			threeHolder.titleThree.setText(a.getArticle_title());
			threeHolder.pinglunshuThree.setText(a.getDiscuss()+"");
			threeHolder.zanshuThree.setText(a.getZan()+"");
			
			
			break;
		}
		convertview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(arg0.getContext(),DetailsArticleActivity.class);
				intent.putExtra("article", a);
				arg0.getContext().startActivity(intent);
				
			}
		});

		return convertview;
	}

	public static class OneHolder {
		//大单图
		public TextView title;
		public ImageView singleBig;
		public TextView moduleName;
		public TextView zanshu;
		public TextView pinglunshu;
		
	}

	public static class TwoHolder {
		//小单图
		public TextView titleSmall;
		public ImageView singlePpic;
		public TextView moduleNameSmall;
		public TextView zanshuSmall;
		public TextView pinglunshuSmall;
		public TextView contentSmall;
	}
	public static class ThreeHolder{
		//三图
		public TextView titleThree;
		public ImageView pic1;
		public ImageView pic2;
		public ImageView pic3;
		public TextView moduleNameThree;
		public TextView zanshuThree;
		public TextView pinglunshuThree;
		
	}

}

