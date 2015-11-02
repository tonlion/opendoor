package com.zhima.opendoor.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.PstsAdapter;
import com.zhima.opendoor.entity.Chanel;
import com.zhima.opendoor.fragment.SlideMenuFragment;

public class MyAttentionActivity extends FragmentActivity implements OnClickListener{
	private PagerSlidingTabStrip psts;
	private TextView attention;
	private PstsAdapter adapter;
	private Intent intent;
	private List<Chanel> list;
	private List<String> name;
	private List<Fragment> fragments;
	private ViewPager myPager;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_attention_layout);
		psts=(PagerSlidingTabStrip) findViewById(R.id.psts);
		attention=(TextView) findViewById(R.id.attention_title);
		myPager=(ViewPager) findViewById(R.id.mylistcontainer);
		fragments=new ArrayList<Fragment>();
		attention.setOnClickListener(this);
		intent=getIntent();
		list=intent.getParcelableArrayListExtra("data");
		num=intent.getIntExtra("num", 0);
		initFragment();
		initTitle();
		adapter=new PstsAdapter(getSupportFragmentManager(),fragments,name);
		myPager.setAdapter(adapter);
		psts.setViewPager(myPager);
		
		
	}
	/**
	 * 用来加载fragment，设置其中的数据
	 */
	private void initFragment(){
		name=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getParent_id()==num){
				Chanel c=list.get(i);
				name.add(list.get(i).getTitle());
				Bundle b=new Bundle();
				b.putParcelable("pageCount",c);
				b.putInt("type", 1);
				Fragment f=new SlideMenuFragment();
				f.setArguments(b);
				fragments.add(f);
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.attention_title:
			finish();
			break;

		default:
			break;
		}
		
	}
	private void initTitle(){
		switch (num) {
		case 0:
			attention.setText("我的关注");
			break;
		case 1:
			attention.setText("我的收藏");
			break;
		case 2:
			attention.setText("浏览历史");
			break;
		case 3:
			attention.setText("消息中心");

		default:
			break;
		}
	}

}
