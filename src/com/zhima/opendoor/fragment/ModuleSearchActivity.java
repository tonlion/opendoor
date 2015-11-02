package com.zhima.opendoor.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.FinfingAdapter;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.view.SwipeBackActivity;

public class ModuleSearchActivity extends SwipeBackActivity implements OnClickListener{
	private RelativeLayout r;
	private RelativeLayout r1;
	private PullToRefreshListView listView;
	private List<Module> myModule = new ArrayList<Module>();
	private Intent intent;
	private FinfingAdapter adapter;
	private TextView mokuai;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finding_layout);
		intent=getIntent();
		myModule=intent.getParcelableArrayListExtra("module");
		r=(RelativeLayout) findViewById(R.id.contentSearch);
		r.setVisibility(View.GONE);
		mokuai=(TextView) findViewById(R.id.mokuaisousuo);
		mokuai.setOnClickListener(this);
		r1=(RelativeLayout) findViewById(R.id.newSearch);
		r1.setVisibility(View.VISIBLE);
		listView=(PullToRefreshListView) findViewById(R.id.moduleContainer);
		adapter=new FinfingAdapter(myModule, this);
		listView.setAdapter(adapter);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.mokuaisousuo:
			finish();
			break;

		default:
			break;
		}
	}

}
