package com.clt.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.clt.IService;
import com.clt.adapter.LedSelectAdapter;
import com.clt.commondata.LedTerminateInfo;
import com.clt.commondata.LedTerminateInfoList;
import com.clt.ledmanager.R;
import com.clt.service.BaseService.LocalBinder;
import com.clt.service.BaseServiceFactory;
import com.clt.service.TCPFindTerminal;
import com.clt.service.TCPFindTerminal.OnCallBack;
import com.clt.ui.CustomerSpinner;
import com.clt.ui.DialogFactory;
import com.clt.ui.DialogProgressBar;
import com.clt.ui.TitleBarView;
import com.clt.ui.TitleBarView.TitleBarListener;
import com.clt.util.Const;
import com.clt.util.NetUtil;
import com.google.gson.Gson;

/**
 * 选择服务端 列表
 */
public class LedSelectActivity extends BaseActivity
{
	/**
	 * 查找方式
	 */
	private static final class Type
	{
		
		private static final int UDP_Broadcast = 0;
		
		private static final int TCP_Poll = 1;
		
	}
	//网络连接服务
	protected IService mangerNetService;
	//标题栏视图
	private TitleBarView titleBarView;
	//列表
	private ListView listView;
	//适配器
	private LedSelectAdapter adapter;
	
	private TextView tvSearchNoResult;// 查找没有结果
	//led列表
	private ArrayList<LedTerminateInfo> ledList;
	//进度条
	private DialogProgressBar progressBar;
	
	private CustomerSpinner spinnerFindType;// 查找方式
	
	// private String terminateAddress;// 已经选中的终端ip
	//对话框
	private Dialog dialog = null;
	
	private Application app;
	//查找终端按钮
	private Button btnFindTerminal;
	
	private int findType = Type.UDP_Broadcast;
	
	private AsyncFindByTCP asyncFindByTCP;
	
	private Handler nmHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			try
			{
				switch (msg.what)
				{
					case Const.findTerminateResult:// 找到了
						String strTerminateList = (String) msg.obj;
						Gson gson = new Gson();
						LedTerminateInfoList terminateInfoList = gson.fromJson(
								strTerminateList, LedTerminateInfoList.class);
						ledList = terminateInfoList.getTerminateList();
						adapter.updateView(ledList);
						app.setIp2TerminateMap(ledList);
						// adapter.setChecked(terminateAddress, listView);
						// if (ledList != null && !ledList.isEmpty())
						// {
						// progressBar.setVisibility(View.GONE);
						// }
						if (progressBar != null)
						{
							progressBar.dismiss();
						}
					
					break;
					case Const.endFindTerminateResult:// 结束查找
						if (ledList.isEmpty())
						{
							listView.setVisibility(View.GONE);
							tvSearchNoResult.setVisibility(View.VISIBLE);
							if (progressBar != null)
							{
								progressBar.dismiss();
							}
						}
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	};
	
	/**
	 * 网络服务连接
	 */
	private ServiceConnection mangerNetServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mangerNetService = null;
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			try
			{
				mangerNetService = ((LocalBinder) service).getService();
				if (mangerNetService == null)
				{
					return;
				}
				mangerNetService.setNmHandler(nmHandler);
				findTerminal();
				
			}
			catch (Exception e)
			{
			}
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			Application.getInstance().setSystemLanguage();
			setContentView(R.layout.led_select);
			init();
			initView();
			initListener();
		}
		catch (Exception e)
		{
		}
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(mangerNetServiceConnection);
	}
	
	/**
	 * 初始化参数
	 */
	private void init()
	{
		try
		{
			app = (Application) getApplication();
			// 绑定service
			Intent _intent1 = new Intent(LedSelectActivity.this,
					BaseServiceFactory.getBaseService());
			bindService(_intent1, mangerNetServiceConnection,
					Context.BIND_AUTO_CREATE);
			Intent intent = getIntent();
			ledList = new ArrayList<LedTerminateInfo>();
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 初始化控件
	 */
	private void initView()
	{
		try
		{
			titleBarView = (TitleBarView) findViewById(R.id.titlebar);
			
			tvSearchNoResult = (TextView) findViewById(R.id.tv_search_no_result);
			// progressBar = (ProgressBar) findViewById(R.id.progressbar);
			progressBar = new DialogProgressBar(this,
					getString(R.string.search_server));
			progressBar.show();
			listView = (ListView) findViewById(R.id.lv_ledinfos);
			adapter = new LedSelectAdapter(this, ledList);
			String terminateAddress = null;
			if (app.ledTerminateInfo != null)
			{
				terminateAddress = app.ledTerminateInfo.getIpAddress();
			}
			adapter.setIpAddress(terminateAddress);
			listView.setAdapter(adapter);
			
			spinnerFindType = (CustomerSpinner) findViewById(R.id.spinner_search_type);
			spinnerFindType.initView(R.array.find_term_type);
			spinnerFindType.setSelection(0, true);
			
			btnFindTerminal = (Button) findViewById(R.id.btn_search_terminal);
		}
		catch (Exception e)
		{
			
		}
		
	}
	
	/**
	 * 初始化监听器
	 */
	private void initListener()
	{
		//标题
		titleBarView.setTitleBarListener(new TitleBarListener()
		{
			
			@Override
			public void onClickRightImg(View v)
			{
				progressBar.show();
				findTerminal();
			}
			
			@Override
			public void onClickLeftImg(View v)
			{
				finish();
			}
		});
		//查找方式
		spinnerFindType.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				if (position == 0)
				{
					findType = Type.UDP_Broadcast;
				}
				else
				{
					findType = Type.TCP_Poll;
				}
			}
		});
		//查找客户端
		btnFindTerminal.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				findTerminal();
			}
		});
		//
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3)
			{
				// 查看密码是否已经输入过
				final String ip = adapter.getItem(position).getIpAddress();
				if (app.getIp2TerminateMap().get(ip).isHasEnteredPass())
				{
					Intent intet = new Intent();
					intet.putExtra("type", "selectServer");
					intet.putExtra("terminateInfo", adapter.getItem(position));
					setResult(Activity.RESULT_OK, intet);
					finish();
					return;
				}
				LayoutInflater inflater = LayoutInflater
						.from(LedSelectActivity.this);
				View view = inflater.inflate(R.layout.entry_password, null);
				final EditText etPass = (EditText) view
						.findViewById(R.id.et_entry_password);
				Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
				Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
				btnSubmit.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						String currentPassword = adapter.getItem(position)
								.getPassword();
						String entryPassword = etPass.getText().toString();
						if (entryPassword.equals(currentPassword))
						{
							Intent intet = new Intent();
							intet.putExtra("type", "selectServer");
							intet.putExtra("terminateInfo",
									adapter.getItem(position));
							setResult(Activity.RESULT_OK, intet);
							finish();
							
							if (dialog != null)
							{
								dialog.dismiss();
							}
							app.getIp2TerminateMap().get(ip)
									.setHasEnteredPass(true);
						}
						
					}
				});
				btnCancel.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						if (dialog != null)
						{
							dialog.dismiss();
						}
						
					}
				});
				
				dialog = DialogFactory.createDialog(LedSelectActivity.this,
						view);
				dialog.show();
			}
		});
	}
	
	/**
	 * 查找服务端
	 */
	private void findTerminal()
	{
		if (mangerNetService == null)
		{
			return;
		}
		
		// 网络没连接
		if (!NetUtil.isConnnected(LedSelectActivity.this))
		{
			toast(getResString(R.string.network_not_connected), 1000);
			if (progressBar != null)
			{
				progressBar.dismiss();
			}
			return;
		}
		adapter.clearData();
		progressBar.show();
		switch (findType)
		{
			case Type.UDP_Broadcast:
			{
				
				findTerinalsByUdp();
			}
				
			break;
			
			case Type.TCP_Poll:
			{
				findTerminalsByTcp();
			}
				
			break;
		}
	}
	/**
	 * TCP方式查找服务端
	 */
	private void findTerminalsByTcp()
	{
		asyncFindByTCP=new AsyncFindByTCP();
		asyncFindByTCP.execute();
	}
	/**
	 * UDP方式查找服务端
	 */
	private void findTerinalsByUdp()
	{
		mangerNetService.searchTerminate();
	}
	
	
	/**
	 * TCP轮询查找
	 *
	 */
	class AsyncFindByTCP extends AsyncTask<Object, Object, Object>
	{
		
		private static final int Update_ProgressBar=1;
		
		private static final int Update_FindTerm=2;
		
		private static final int Update_Done=3;
		
		private TCPFindTerminal tcpFindTerminal;
		
		private ArrayList<LedTerminateInfo> mLedlists;
		
		public AsyncFindByTCP()
		{
			tcpFindTerminal = new TCPFindTerminal(LedSelectActivity.this, 9043);
			mLedlists = new ArrayList<LedTerminateInfo>();
			
		}
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Object... values)
		{
			super.onProgressUpdate(values);
			int type = (Integer) (values[0]);
			switch (type)
			{
				case Update_ProgressBar:
				{
					
				}
				break;
				
				case Update_FindTerm:
				{
					adapter.updateView(mLedlists);
					if (progressBar != null)
					{
						progressBar.dismiss();
					}
				}
				break;
				case Update_Done:
				{
					if (mLedlists.isEmpty())
					{
						listView.setVisibility(View.GONE);
						tvSearchNoResult.setVisibility(View.VISIBLE);
						if (progressBar != null)
						{
							progressBar.dismiss();
						}
					}
				}
				break;
			}
		}
		
		@Override
		protected void onPostExecute(Object result)
		{
			super.onPostExecute(result);
		}
		
		@Override
		protected Object doInBackground(Object... params)
		{
			if(mangerNetService==null){
				return null;
			}
			mangerNetService.searchTerminateByTcpLoop(new OnCallBack()
			{
				
				@Override
				public void onFindSucess(LedTerminateInfo ledTerminateInfo)
				{
					try
					{
						LedTerminateInfo ledTerInfo = new LedTerminateInfo();
						ledTerInfo.setIpAddress(ledTerminateInfo.getIpAddress());
						ledTerInfo.setPassword(ledTerminateInfo.getPassword());
						ledTerInfo.setStrName(ledTerminateInfo.getStrName());
						mLedlists.add(ledTerInfo);
						app.setIp2TerminateMap(mLedlists);
						publishProgress(Update_FindTerm);
					}
					catch (Exception e)
					{
					}
					
				}

				@Override
				public void onFindDone()
				{
					publishProgress(Update_Done);
				}
				
			});
			return null;
		}
		
	}
	
}
