package com.zhima.opendoor.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PstsAdapter extends FragmentPagerAdapter {
	private List<Fragment> data;
	private List<String> list;

	public PstsAdapter(FragmentManager fm, List<Fragment> data,
			List<String> list) {
		super(fm);
		this.data = data;
		this.list = list;
	}

	public PstsAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		
		return data.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
	public CharSequence getPageTitle(int position) {
		return this.list.get(position);
	}

}
