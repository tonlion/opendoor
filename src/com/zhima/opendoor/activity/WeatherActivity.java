package com.zhima.opendoor.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import json.entity.Result;
import json.entity.Weather;
import json.entity.Weather_data;

import org.xmlpull.v1.XmlPullParserException;
import com.zhima.opendoor.R;
import com.zhima.opendoor.entity.City;
import com.zhima.opendoor.entity.District;
import com.zhima.opendoor.entity.Province;
import com.zhima.opendoor.utils.HttpUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	private Spinner sp_province;
	private Spinner sp_city;
	private Spinner sp_district;
	private int currentPro;
	private ArrayAdapter<Province> provinceAdapter;
	private ArrayAdapter<City> cityAdapter;
	private ArrayAdapter<District> districtAdapter;
	private List<Province> provinces;

	private TextView tvCity;
	private TextView tvPM25;
	private TextView tvDate;
	private ImageView ivpic11;
	private ImageView ivpic12;
	private TextView tvweek1;
	private TextView tvwea1;
	private TextView tvwind1;
	private TextView tvtemper1;

	private ImageView ivpic21;
	private ImageView ivpic22;
	private TextView tvweek2;
	private TextView tvwea2;
	private TextView tvwind2;
	private TextView tvtemper2;

	private ImageView ivpic31;
	private ImageView ivpic32;
	private TextView tvweek3;
	private TextView tvwea3;
	private TextView tvwind3;
	private TextView tvtemper3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		sp_province = (Spinner) findViewById(R.id.spinner1);
		sp_city = (Spinner) findViewById(R.id.spinner2);
		sp_district = (Spinner) findViewById(R.id.spinner3);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvPM25 = (TextView) findViewById(R.id.tvPM25);
		tvDate = (TextView) findViewById(R.id.tvDate);

		// �ڶ�������
		ivpic21 = (ImageView) findViewById(R.id.ivpic21);
		ivpic22 = (ImageView) findViewById(R.id.ivpic22);

		tvweek2 = (TextView) findViewById(R.id.tvweek2);
		tvwea2 = (TextView) findViewById(R.id.tvwea2);
		tvwind2 = (TextView) findViewById(R.id.tvwind2);
		tvtemper2 = (TextView) findViewById(R.id.tvtemper2);

		// ����������
		ivpic31 = (ImageView) findViewById(R.id.ivpic31);
		ivpic32 = (ImageView) findViewById(R.id.ivpic32);

		tvweek3 = (TextView) findViewById(R.id.tvweek3);
		tvwea3 = (TextView) findViewById(R.id.tvwea3);
		tvwind3 = (TextView) findViewById(R.id.tvwind3);
		tvtemper3 = (TextView) findViewById(R.id.tvtemper3);

		// ��һ������
		ivpic11 = (ImageView) findViewById(R.id.ivpic11);
		ivpic12 = (ImageView) findViewById(R.id.ivpic12);

		tvweek1 = (TextView) findViewById(R.id.tvweek1);
		tvwea1 = (TextView) findViewById(R.id.tvwea1);
		tvwind1 = (TextView) findViewById(R.id.tvwind1);
		tvtemper1 = (TextView) findViewById(R.id.tvtemper1);

		// ��ȡ��������Ϣ
		try {
			provinces = HttpUtils.getProvinces(this);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		// adapter�������Ϣ
		provinceAdapter = new ArrayAdapter<Province>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				provinces);
		provinceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_province.setAdapter(provinceAdapter);

		cityAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				provinces.get(0).getCitys());
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_city.setAdapter(cityAdapter);

		districtAdapter = new ArrayAdapter<District>(this,
				android.R.layout.simple_spinner_item, android.R.id.text1,
				provinces.get(0).getCitys().get(0).getDisList());
		districtAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_district.setAdapter(districtAdapter);
		// ��ѡ��ʡ��ʱ�����к͵ط��б��仯
		sp_province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currentPro = position;
				cityAdapter = new ArrayAdapter<City>(WeatherActivity.this,
						android.R.layout.simple_spinner_item,
						android.R.id.text1, provinces.get(position).getCitys());
				cityAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_city.setAdapter(cityAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// ��ѡ�����ʱ���ط��б��仯
		sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				districtAdapter = new ArrayAdapter<District>(WeatherActivity.this,
						android.R.layout.simple_spinner_item,
						android.R.id.text1, provinces.get(currentPro)
								.getCitys().get(position).getDisList());
				districtAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_district.setAdapter(districtAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// ��ѡ��ط�ʱ����ʾ������������
		sp_district.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// ѡ��ĳ���
				District dis = districtAdapter.getItem(position);
				// Log.i("i", dis.getName());
				new WeatherAsyncTask().execute(dis.getName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	// �첽�࣬��ȡ��������
	class WeatherAsyncTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			String url = HttpUtils.getURl(params[0]);
			String jsonStr = HttpUtils.getJsonStr(url);
			Weather weather = HttpUtils.fromJson(jsonStr);
			Result r = weather.getResults().get(0);
			/*List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			list = HttpUtils.toListMap(r);*/
			for(int i = 0;i<3;i++){
				Weather_data w = r.getWeather_data().get(i);
				w.setDayPicture(HttpUtils.getImage(w.getDayPictureUrl()));
				w.setNightPicture(HttpUtils.getImage(w.getNightPictureUrl()));
			}
			return weather;
		}

		@Override
		protected void onPostExecute(Weather result ) {
			Result res = result.getResults().get(0);
			Weather_data wa = res.getWeather_data().get(0);
			// System.out.println(res.getWeather_data());
			tvCity.setText("����:" + res.getCurrentCity());
			String pm2_5 = "".equals(res.getPm25()) ? "75" : res.getPm25();
//			Log.i("i",res.getPm25()+"wwwww");
			tvPM25.setText("PM2.5:" + pm2_5);
			tvDate.setText("����:" + result.getDate());
			// Ӧ��Ϊ�������ϻ�ȡ����
//			ivpic11.setImageResource(R.drawable.d00);
//			ivpic12.setImageResource(R.drawable.d01);
			ivpic11.setImageBitmap(wa.getDayPicture());
			ivpic12.setImageBitmap(wa.getNightPicture());
			String str = wa.getDate();
			tvweek1.setText(str.substring(0, 2));
			tvwea1.setText(wa.getWeather());
			tvwind1.setText(wa.getWind());
			tvtemper1.setText(str.substring(14, str.length()-1));

			wa = res.getWeather_data().get(1);
			// System.out.println(wa2);

			tvtemper2.setText(wa.getTemperature());
			ivpic21.setImageBitmap(wa.getDayPicture());
			ivpic22.setImageBitmap(wa.getNightPicture());
			tvweek2.setText(wa.getDate());
			tvwea2.setText(wa.getWeather());
			tvwind2.setText(wa.getWind());
			tvtemper2.setText(wa.getTemperature());

			wa = res.getWeather_data().get(2);

			// System.out.println(wa4);
			ivpic31.setImageBitmap(wa.getDayPicture());
			ivpic32.setImageBitmap(wa.getNightPicture());
			tvweek3.setText(wa.getDate());
			tvwea3.setText(wa.getWeather());
			tvwind3.setText(wa.getWind());
			tvtemper3.setText(wa.getTemperature());
		}
	}
}
