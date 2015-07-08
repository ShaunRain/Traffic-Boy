package com.rain.traffic_boy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.rain.traffic_boy.R;
import com.rain.traffic_boy.model.Incident;

public class NearActivity extends Activity {

	private MapView mapView;
	private BaiduMap baiduMap;

	LocationClient locationClient;
	private LocationMode locationMode;
	BitmapDescriptor bitmapDescriptor;

	boolean isFirstLoc = true;

	private List<Incident> allInc;

	private List<Marker> troubleMaker;
	private List<Marker> crowdMaker;
	private List<Marker> clearMaker;
	private List<Marker> controlMaker;

	private CheckBox cb_trouble;
	private CheckBox cb_crowd;
	private CheckBox cb_clear;
	private CheckBox cb_control;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near);

		allInc = (List<Incident>) getIntent().getBundleExtra("bundle")
				.getSerializable("allInc");

		troubleMaker = new ArrayList<Marker>();
		crowdMaker = new ArrayList<Marker>();
		clearMaker = new ArrayList<Marker>();
		controlMaker = new ArrayList<Marker>();

		initCheckBoxes();

		locationMode = LocationMode.FOLLOWING;
		bitmapDescriptor = null;

		mapView = (MapView) findViewById(R.id.map_near);
		baiduMap = mapView.getMap();

		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				locationMode, true, bitmapDescriptor));

		baiduMap.setMyLocationEnabled(true);
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {

				String type = arg0.getTitle();
				String addr = arg0.getExtraInfo().getString("addr");

				Button bubble = new Button(getApplicationContext());
				bubble.setBackgroundResource(R.drawable.popup);
				bubble.setText("类型:" + type + "\n" + "地点:" + addr);
				InfoWindow window = new InfoWindow(bubble, arg0.getPosition(),
						-70);
				baiduMap.showInfoWindow(window);
				return true;
			}
		});

		locationClient = new LocationClient(this);
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null || mapView == null)
					return;
				MyLocationData locationData = new MyLocationData.Builder()
						.accuracy(location.getRadius()).direction(100)
						.latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				baiduMap.setMyLocationData(locationData);

				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(), location
							.getLongitude());
					MapStatusUpdate update = MapStatusUpdateFactory
							.newLatLng(ll);
					baiduMap.animateMapStatus(update);
				}
			}

		});

		LocationClientOption clientOption = new LocationClientOption();
		clientOption.setOpenGps(true);
		clientOption.setCoorType("bd09ll");
		clientOption.setScanSpan(1000);
		locationClient.setLocOption(clientOption);
		locationClient.start();

		addSignals();
	}

	public void initCheckBoxes() {
		cb_trouble = (CheckBox) findViewById(R.id.cb_trouble);
		cb_crowd = (CheckBox) findViewById(R.id.cb_crowd);
		cb_clear = (CheckBox) findViewById(R.id.cb_clear);
		cb_control = (CheckBox) findViewById(R.id.cb_control);

		cb_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				baiduMap.hideInfoWindow();
				if (!isChecked) {
					removeSignals(clearMaker);
				} else {
					for (Marker m : clearMaker)
						m.setVisible(true);
				}
			}
		});

		cb_control.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				baiduMap.hideInfoWindow();
				if (!isChecked) {
					removeSignals(controlMaker);
				} else {
					for (Marker m : controlMaker)
						m.setVisible(true);
				}

			}
		});

		cb_crowd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				baiduMap.hideInfoWindow();
				if (!isChecked) {
					removeSignals(crowdMaker);
				} else {
					for (Marker m : crowdMaker)
						m.setVisible(true);
				}

			}
		});

		cb_trouble.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				baiduMap.hideInfoWindow();
				if (!isChecked) {
					removeSignals(troubleMaker);
				} else {
					for (Marker m : troubleMaker)
						m.setVisible(true);
				}

			}
		});
	}

	public void removeSignals(List<Marker> markers) {
		for (Marker m : markers)
			m.setVisible(false);

	}

	public void addSignals() {
		for (Incident inc : allInc) {
			if (inc == null)
				continue;
			double la = inc.getLaltitude();
			double lo = inc.getLongtitude();
			String type = inc.getType();
			String addr = inc.getAddr();
			Log.d("newSignal", la + "," + lo + "," + type);
			addSignal(la, lo, type, addr);
		}
	}

	public void addSignal(double la, double lo, String type, String addr) {
		LatLng point = new LatLng(la, lo);
		BitmapDescriptor bitmap = null;
		Bundle bd = new Bundle();
		switch (type) {
		case "clear":
			bitmap = BitmapDescriptorFactory.fromResource(R.drawable.clear);
			bd.putString("addr", addr);
			break;
		case "crowd":
			bitmap = BitmapDescriptorFactory.fromResource(R.drawable.crowd);
			bd.putString("addr", addr);
			break;
		case "trouble":
			bitmap = BitmapDescriptorFactory.fromResource(R.drawable.trouble);
			bd.putString("addr", addr);
			break;
		case "control":
			bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher);
			bd.putString("addr", addr);
			break;
		}

		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).title(type).extraInfo(bd);
		switch (type) {
		case "trouble":
			troubleMaker.add((Marker) baiduMap.addOverlay(option));
			break;
		case "crowd":
			crowdMaker.add((Marker) baiduMap.addOverlay(option));
			break;
		case "clear":
			clearMaker.add((Marker) baiduMap.addOverlay(option));
			break;
		case "control":
			controlMaker.add((Marker) baiduMap.addOverlay(option));
			break;
		}

	}

	@Override
	protected void onDestroy() {

		locationClient.stop();

		baiduMap.setMyLocationEnabled(false);

		mapView.onDestroy();
		mapView = null;
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();

	}

	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();

	}

}
