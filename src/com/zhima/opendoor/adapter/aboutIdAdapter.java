package com.zhima.opendoor.adapter;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.LoginActivity;
import com.zhima.opendoor.activity.QQSlidingMenuActivity;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.db.DataBaseOpenHelper;
import com.zhima.opendoor.entity.AboutIdEntity;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class aboutIdAdapter extends BaseAdapter {
	private List<AboutIdEntity> user;

	public aboutIdAdapter(List<AboutIdEntity> user, Context context) {
		super();
		this.user = user;
		this.context = context;
	}

	private Context context;
	private onContactSlectedListener listener;

	public onContactSlectedListener getListener() {
		return listener;
	}

	public void setListener(onContactSlectedListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return user.size();
	}

	@Override
	public Object getItem(int arg0) {

		return user.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		Holder mHolder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.about_id_item,
					null);
			mHolder = new Holder();
			mHolder.userName = (TextView) view
					.findViewById(R.id.huanlegemingzi);
			mHolder.checked = (ImageView) view.findViewById(R.id.checked);
			mHolder.delete = (ImageView) view.findViewById(R.id.delete);
			view.setTag(mHolder);
		} else {
			mHolder = (Holder) view.getTag();
		}
		final AboutIdEntity u = user.get(arg0);
		mHolder.userName.setText(u.getU().getUser_name());
		if (u.isSelect()) {
			mHolder.checked.setVisibility(View.VISIBLE);
			
		} else {
			mHolder.checked.setVisibility(View.INVISIBLE);
		}
		if (u.getType() == 0) {
			mHolder.delete.setVisibility(View.INVISIBLE);
		} else if(u.getType()==1&&!u.isSelect()){
			mHolder.delete.setVisibility(View.VISIBLE);
		}
		mHolder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					if (u.isSelect()) {
						AlertDialog.Builder builder;
						builder = new AlertDialog.Builder(arg0.getContext());
						builder.setTitle("提示").setMessage("确认退出当前账号？ ");
						builder.setPositiveButton(
								"是的",
								new android.content.DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										listener.onContactSelectedChanged(u);
										arg0.dismiss();

									}
								});
						builder.setNegativeButton(
								"呀，点错",
								new android.content.DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();

									}
								});
						AlertDialog dialog = builder.create();
						dialog.setCancelable(true);
						dialog.show();

					} else {
						listener.onContactSelectedChanged(u);
					}

				}

			}
		});
		
		return view;
	}

	public static class Holder {
		public TextView userName;
		public ImageView checked;
		public ImageView delete;

	}

	public interface onContactSlectedListener {
		public void onContactSelectedChanged(AboutIdEntity e);
	}

}
