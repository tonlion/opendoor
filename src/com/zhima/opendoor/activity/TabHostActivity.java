package com.zhima.opendoor.activity;

import com.zhima.opendoor.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TabHostActivity extends FragmentActivity {
	private FragmentTabHost host;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_tab_host_layout);
		host = (FragmentTabHost) findViewById(android.R.id.tabhost);
		host.setup(this, getSupportFragmentManager(), R.id.useFrame);
		// addTabs("home", "首页", InnerFragment.class,null);
		// addTabs("msg", "消息", InnerFragment.class,null);
		// addTabs("setting", "设置", InnerFragment.class,null);
	}

	private void addTabs(String tag, String labelValue, int resourse,
			Class clzss, Bundle args) {
		FragmentTabHost.TabSpec tab = host.newTabSpec(tag);
		View v = LayoutInflater.from(this).inflate(R.layout.tab_item_op, null);
		TextView label = (TextView) v.findViewById(R.id.text);
		ImageView icon = (ImageView) v.findViewById(R.id.icon);
		label.setText(labelValue);
		icon.setImageResource(resourse);
		tab.setIndicator(v);
		host.addTab(tab, clzss, args);
	}
}
