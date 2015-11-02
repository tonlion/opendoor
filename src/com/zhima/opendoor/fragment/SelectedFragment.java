package com.zhima.opendoor.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.MultiAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.db.DataBaseOpenHelper;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class SelectedFragment extends Fragment implements
		OnRefreshListener<ListView> {
	private ListView listView;
	private LinearLayout welcomeLogo;
	private List<Article> list = new ArrayList<Article>(); // �洢���µ�list
	private ViewPager myPager;
	private LinearLayout lineImg;
	private MultiAdapter adapter;
	private static final int MSG_CHANGE_PHOTO = 1;
	/**
	 * 
	 * װ����ImageView����
	 */

	// private ImageView[] tips;

	/**
	 * 
	 * װImageView����
	 */

	private ImageView[][] mImageViews;

	/**
	 * 
	 * ͼƬ��Դid
	 */

	private int[] imgIdArray;

	// ����ͼƬ��ԴID

	private Handler mHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {

			switch (msg.what) {

			case MSG_CHANGE_PHOTO:

				int index = myPager.getCurrentItem();

				myPager.setCurrentItem(index + 1);

				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,

				5 * 1000);

				break;

			}

			super.dispatchMessage(msg);

		}

	};

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.select_fragment_layout, null);
		listView = (ListView) view.findViewById(R.id.listview);
		// ���ؽ���viewpager�Ĳ���
		View pagerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.select_header, null);
		myPager = (ViewPager) pagerView.findViewById(R.id.mypager);
		lineImg = (LinearLayout) pagerView.findViewById(R.id.lineImg);

		// ����ͼƬ��ԴID

		imgIdArray = new int[] { R.drawable.firstpage,
				R.drawable.secondpage, R.drawable.thirdpage};
		mImageViews = new ImageView[2][];

		// ��ͼƬװ�ص�������,����һ�����ƻ��壬��ֹͼƬ��ʱ���ֺ�ɫͼƬ������ʾ������

		mImageViews[0] = new ImageView[imgIdArray.length];

		mImageViews[1] = new ImageView[imgIdArray.length];

		for (int i = 0; i < mImageViews.length; i++) {

			for (int j = 0; j < mImageViews[i].length; j++) {

				ImageView imageView = new ImageView(getActivity());

				imageView.setBackgroundResource(imgIdArray[j]);

				mImageViews[i][j] = imageView;

				Log.i("TwoActivity_WY", i + "," + j + "\t");

			}

		}

		// ����Adapter

		myPager.setAdapter(new MyAdapter());

		// ���ü�������Ҫ�����õ��ı���

		myPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {

				setImageBackground(arg0 % imgIdArray.length);

			}

		});

		myPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (imgIdArray.length == 0 || imgIdArray.length == 1)

					return true;

				else

					return false;

			}

		});

		// ����ViewPager��Ĭ����, ����Ϊ���ȵ�50���������ӿ�ʼ�������󻬶�

		myPager.setCurrentItem((imgIdArray.length) * 50);

		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, 5 * 1000);

		listView.addHeaderView(pagerView);
		View footer=LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, null);
		listView.addFooterView(footer);
		adapter = new MultiAdapter(list, getActivity());
		listView.setAdapter(adapter);
		initData();
		return view;
	}

	private void initData() {
		StringPostRequest rq = new StringPostRequest(urlUtils.articleServlet,
				new Listener<String>() {

					@Override
					public void onResponse(String result) {
						Gson gson = new Gson();
						List<Article> listm = gson.fromJson(result,
								new TypeToken<ArrayList<Article>>() {
								}.getType());
						list.addAll(listm);
						/**
						 * �����ֻ���д�뱾�����ݿ�
						 */
						try {
							DataBaseOpenHelper.getInstance(getActivity())
									.getArticleDao()
									.queryRaw("delete from article");
							DataBaseOpenHelper
									.getInstance(getActivity())
									.getArticleDao()
									.queryRaw(
											"update sqlite_sequence SET seq = 0 where name ='article'");
							DataBaseOpenHelper.getInstance(getActivity())
									.getArticleDao().create(listm);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						adapter.notifyDataSetChanged();

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError result) {
						Toast.makeText(getActivity(), "�������Ӵ���",
								Toast.LENGTH_SHORT).show();
						try {
							List<Article> aa = DataBaseOpenHelper
									.getInstance(getActivity()).getArticleDao()
									.queryForAll();
							list.addAll(aa);
							adapter.notifyDataSetChanged();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		rq.putParams("pageNum", "1");
		rq.putParams("type", "all");
		ZmApplication.getInstance().getRequestQueue().add(rq);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	/**
	 * �ڲ���
	 */

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return Integer.MAX_VALUE;

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;

		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			if (imgIdArray.length == 1)

				((ViewPager) container).removeView(mImageViews[position

				/ imgIdArray.length % 2][0]);

			else

				((ViewPager) container).removeView(mImageViews[position

				/ imgIdArray.length % 2][position % imgIdArray.length]);

		}

		/**
		 * 
		 * ����ͼƬ��ȥ���õ�ǰ��position ���� ͼƬ���鳤��ȡ�����ǹؼ�
		 */

		@Override
		public Object instantiateItem(View container, int position) {

			if (imgIdArray.length == 1)

				return mImageViews[position / imgIdArray.length % 2][0];

			else

				((ViewPager) container).addView(mImageViews[position

				/ imgIdArray.length % 2][position % imgIdArray.length],

				0);

			return mImageViews[position / imgIdArray.length % 2][position

			% imgIdArray.length];

		}

	}

	private void setImageBackground(int selectItemsIndex) {

		for (int i = 0; i < lineImg.getChildCount(); i++) {

			if (i == selectItemsIndex) {

				lineImg.getChildAt(i).setBackgroundResource(
						R.drawable.icon_dot_personal_s_1);

			} else {

				lineImg.getChildAt(i).setBackgroundResource(
						R.drawable.dot_live_n);

			}

		}

	}

}
