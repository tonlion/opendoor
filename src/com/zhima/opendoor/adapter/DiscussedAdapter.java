package com.zhima.opendoor.adapter;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.DetailDiscussActivity;
import com.zhima.opendoor.activity.OtherPersonInfoActivity;
import com.zhima.opendoor.entity.Discuss;
import com.zhima.opendoor.entity.DiscussList;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.view.MyListView;

public class DiscussedAdapter extends BaseAdapter {
	private List<DiscussList> main;
	//private List<Discuss> other;
	public DiscussedAdapter(List<DiscussList> main,
			Context context) {
		super();
		this.main = main;
		this.context = context;
	}

	private Context context;

	@Override
	public int getCount() {
		
		return main.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return main.get(arg0);
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
			view=LayoutInflater.from(context).inflate(R.layout.discuss_item_layout, null);
			holder.name=(TextView) view.findViewById(R.id.floorName);
			holder.time=(TextView) view.findViewById(R.id.floorTime);
			holder.floors=(TextView) view.findViewById(R.id.ownerFloor);
			holder.head=(ImageView) view.findViewById(R.id.floorImg);
			holder.more=(TextView) view.findViewById(R.id.forMore);
			holder.content=(TextView) view.findViewById(R.id.contentDis);
			holder.sendMessage=(ImageButton) view.findViewById(R.id.sendDis);
			holder.secondPinglun=(MyListView) view.findViewById(R.id.sencondPinglun);
			view.setTag(holder);
			
		}else{
			holder=(Holder) view.getTag();
		}
		DiscussList dis=main.get(arg0);
		//携带的其他评论
		final List<Discuss> other=dis.getOther();
		//主评论
		final Discuss disSim=dis.getMain();
		holder.name.setText(disSim.getUser_name());
		if(disSim.getHead_img()==null||disSim.getHead_img().length()==0){
			holder.head.setImageResource(R.drawable.zhima);
		}else{
			ImageLoaderUtils.display(disSim.getHead_img(), holder.head);
		}
		//给评论者的头像设置查看资料功能
		holder.head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.putExtra("userId", disSim.getUser_id());
				intent.setClass(context, OtherPersonInfoActivity.class);
				arg0.getContext().startActivity(intent);
				
			}
		});
		Pattern p=Pattern.compile("smiley_[0-9]{1,2}");
		Matcher m=p.matcher(disSim.getContent());
		SpannableString sb=new SpannableString(disSim.getContent());
		while(m.find()){
			ImageSpan span=new ImageSpan(context,getResourceId(m.group()));
			sb.setSpan(span, m.start(), m.start()+m.group().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		holder.content.setText(sb);
	//	holder.content.setText(disSim.getContent());
		final int floor=arg0+1;
		holder.floors.setText("第"+floor+""+"楼");
		//设置发送时间
		if(System.currentTimeMillis()-disSim.getTime()>1000*60*60){
			SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
			  String retStrFormatNowDate = sdFormatter.format(disSim.getTime());
			  holder.time.setText(retStrFormatNowDate);
		}else{
			long time=(System.currentTimeMillis()-disSim.getTime())/1000;
			if(time>60){
				long timee=time/60;
				holder.time.setText(timee+""+"分钟前");
			}else{
				 holder.time.setText(time+""+"秒前");
			}
			
		}	
		 
		  
		  
		  
		  if(other!=null){
			  List<Discuss> send=new ArrayList<Discuss>();
			  if(other.size()>3){
				  holder.more.setVisibility(View.VISIBLE);
				  holder.more.setText("共"+other.size()+""+"条评论");
				  for(int i=0;i<3;i++){
					  send.add(other.get(i));
				  }
				  holder.secondPinglun.setAdapter(new SmallDiscussAdapter(send, context));
			  }else{
				  holder.more.setVisibility(View.INVISIBLE);
				  holder.secondPinglun.setAdapter(new SmallDiscussAdapter(other, context));
			  }
		  }
		  holder.sendMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(context,DetailDiscussActivity.class);
				
				intent.putExtra("main", disSim);
				intent.putExtra("floor", floor);
				if(other!=null){
					intent.putParcelableArrayListExtra("other", (ArrayList<? extends Parcelable>) other);
				}else{
					List<Discuss> otherr=new ArrayList<Discuss>();
					intent.putParcelableArrayListExtra("other", (ArrayList<? extends Parcelable>) otherr);
				}
				
				arg0.getContext().startActivity(intent);
				
			}
		});
		return view;
	}
	public class Holder{
		public TextView name;
		public TextView time;
		public TextView floors;
		public TextView content;
		public TextView more;
		public ImageView head;
		public ImageButton sendMessage;
		public MyListView secondPinglun;
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
