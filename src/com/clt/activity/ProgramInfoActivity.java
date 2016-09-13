package com.clt.activity;

import java.util.ArrayList;

import com.clt.adapter.ProgramListAdapter;
import com.clt.entity.Program;
import com.clt.ledmanager.R;
import com.clt.util.Tools;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
/**
 *  节目信息
 */
public class ProgramInfoActivity extends BaseActivity
{
	private ListView lvPrograms;
	
	private TextView tvCount;
	
	private ProgramListAdapter adapter;
	
	private Application app;
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			ArrayList<Program> programs = app.programs;
			if (programs == null || programs.size() == 0)
			{
				lvPrograms.setVisibility(View.GONE);
				tvCount.setText(getString(R.string.no_program));
			}
			else
			{
				adapter = new ProgramListAdapter(ProgramInfoActivity.this, programs);
				lvPrograms.setAdapter(adapter);
				//String str=getString(R.string.total)+programs.size()+","+Tools.byte2KbOrMb(countSize(programs));
				String str=String.format(getString(R.string.program_total), String.valueOf(programs.size()))+","+Tools.byte2KbOrMb(countSize(programs));
				tvCount.setText(str);
				
				
			}
		};
	};
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_info_list);
		init();
		initView();
		initListener();
		initData();
	}
	
	private void init()
	{
		app = (Application) getApplication();
	}
	
	private void initView()
	{
		lvPrograms = (ListView) findViewById(R.id.lv_programs_info);
		tvCount=(TextView) findViewById(R.id.tv_program_count);
	}
	
	/**
	 * 初始化监听器
	 */
	private void initListener()
	{
		
	}
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		
		new Thread(){
			@Override
			public void run()
			{
				
				mHandler.obtainMessage().sendToTarget();
			}
		}.start();
		
		
		
	}
	
	/**
	 * 计数
	 * @param programs
	 * @return
	 */
	private long countSize(ArrayList<Program> programs){
		long value=0L;
		for (Program program : programs)
		{
			value+=program.getSize();
		}
		return value;
	}
	
}
