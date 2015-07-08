package com.rain.traffic_boy.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.rain.traffic_boy.R;
import com.rain.traffic_boy.model.Incident;
import com.rain.traffic_boy.util.MyAdapter;

public class RecyclerActivity extends Activity {

	private List<Incident> mDatas;
	private MyAdapter mAdapter;
	RecyclerView mRecyclerView;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycel);
		mRecyclerView = (RecyclerView) findViewById(R.id.rv_waterfall);

		mDatas = (List<Incident>) getIntent().getSerializableExtra("incidents");
		mAdapter = new MyAdapter(this, mDatas);
		mRecyclerView.setAdapter(mAdapter);
		StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(manager);

	}
}
