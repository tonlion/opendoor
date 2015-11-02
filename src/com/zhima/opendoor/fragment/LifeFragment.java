package com.zhima.opendoor.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.HowOldActivity;
import com.zhima.opendoor.activity.LuckyPanActivity;
import com.zhima.opendoor.activity.PinTuActivity;
import com.zhima.opendoor.activity.ToolActivity;
import com.zhima.opendoor.activity.WeatherActivity;
import com.zhima.opendoor.application.ZmApplication;
public class LifeFragment extends Fragment implements OnClickListener{
	private LinearLayout toolDetail;
	private LinearLayout gameDetail;
	private RelativeLayout report;
	private RelativeLayout game;
	private RelativeLayout tool;
	private FrameLayout xiaozi;
	private RelativeLayout fly;
	private boolean isGame=true;
	private boolean isTool=true;
	private TextView howOld;
	private TextView pintu;
	private TextView tenYear;
	private TextView catong;
	private TextView burning;
	private TextView rainy;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.life_fragment_layout, null);
		toolDetail=(LinearLayout) v.findViewById(R.id.toolDetail);
		gameDetail=(LinearLayout) v.findViewById(R.id.gameDetail);
		report=(RelativeLayout) v.findViewById(R.id.r1);
		report.setOnClickListener(this);
		game=(RelativeLayout) v.findViewById(R.id.r2);
		game.setOnClickListener(this);
		tool=(RelativeLayout) v.findViewById(R.id.r3);
		tool.setOnClickListener(this);
		xiaozi=(FrameLayout) v.findViewById(R.id.xiaozi);
		xiaozi.setOnClickListener(this);
		fly=(RelativeLayout) v.findViewById(R.id.r4);
		fly.setOnClickListener(this);
		howOld=(TextView) v.findViewById(R.id.howOld);
		howOld.setOnClickListener(this);
		pintu=(TextView) v.findViewById(R.id.pintu);
		pintu.setOnClickListener(this);
		tenYear=(TextView) v.findViewById(R.id.tenYeass);
		tenYear.setOnClickListener(this);
		burning=(TextView) v.findViewById(R.id.burningEar);
		burning.setOnClickListener(this);
		rainy=(TextView) v.findViewById(R.id.rainymood);
		rainy.setOnClickListener(this);
		catong=(TextView) v.findViewById(R.id.catong);
		catong.setOnClickListener(this);
		
		return v;
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.r2:
			if(isGame){
				gameDetail.setVisibility(View.GONE);
				isGame=false;
			}else{
				gameDetail.setVisibility(View.VISIBLE);
				isGame=true;
			}		
			break;
		case R.id.r3:
			if(isTool){
				toolDetail.setVisibility(View.GONE);
				isTool=false;
			}else{
				toolDetail.setVisibility(View.VISIBLE);
				isTool=true;
			}	
			break;
		case R.id.r1:
			startActivity(new Intent(arg0.getContext(),WeatherActivity.class));
			break;
		case R.id.xiaozi:
			if(ZmApplication.getInstance().getUser()!=null){
				startActivity(new Intent(arg0.getContext(),LuckyPanActivity.class));
			}else{
				Toast.makeText(getActivity(), "芝麻用户专享哦，快加入我们吧~", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.pintu:
			startActivity(new Intent(arg0.getContext(),PinTuActivity.class));
			break;
		case R.id.howOld:
			startActivity(new Intent(arg0.getContext(),HowOldActivity.class));
			break;
		case R.id.tenYeass:
			Intent intent=new Intent();
			intent.setClass(getActivity(), ToolActivity.class);
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		case R.id.catong:
			 intent=new Intent();
				intent.setClass(getActivity(), ToolActivity.class);
				intent.putExtra("type", 2);
				startActivity(intent);
			break;
		case R.id.burningEar:
			 intent=new Intent();
				intent.setClass(getActivity(), ToolActivity.class);
				intent.putExtra("type", 3);
				startActivity(intent);
			break;
		case R.id.rainymood:
			 intent=new Intent();
				intent.setClass(getActivity(), ToolActivity.class);
				intent.putExtra("type", 4);
				startActivity(intent);
			break;
			

		default:
			break;
		}
		
	}

}
