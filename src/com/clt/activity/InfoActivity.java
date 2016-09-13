package com.clt.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clt.IService;
import com.clt.commondata.SomeInfo;
import com.clt.ledmanager.R;
import com.clt.netmessage.NMChangeTermNameAnswer;
import com.clt.netmessage.NetMessageType;
import com.clt.service.BaseService.LocalBinder;
import com.clt.service.BaseServiceFactory;
import com.clt.service.Clock;
import com.clt.ui.DialogFactory;
import com.clt.ui.TitleBarView;
import com.clt.ui.TitleBarView.TitleBarListener;
import com.clt.util.Const;
import com.clt.util.Tools;
import com.google.gson.Gson;

/**
 * 查看信息
 */
public class InfoActivity extends BaseActivity
{
	private TextView tvTermName, tvTermIp, tvTermState, tvTermDuring;
	
	private Button btnModifTermName;
	
	private ProgressBar pbSdcardRoom;
	
	private TextView tvSdcardRoom, tvLedManagerVersion, tvLedServerVersion,
			tvSenderVersion, tvColorLightVersion;
	
	private Dialog dialogTermName;
	
	private IService mangerNetService;// 通信服务
	
	private Application app;
	
	private BroadcastReceiver receiver;
	
	private String newTermName;
	
	// 标题栏视图
	private TitleBarView titleBarView;
	
	/**
	 * 绑定通信service
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
				if (mangerNetService != null)
				{
					mangerNetService.setNmHandler(nmHandler);
				}
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
		setContentView(R.layout.other_info);
		init();
		initView();
		initListener();
		initData();
		
	}
	
	private void init()
	{
		app = (Application) getApplication();
		// 绑定mangerNetService
		Intent _intent1 = new Intent(InfoActivity.this,
				BaseServiceFactory.getBaseService());
		this.getApplicationContext().bindService(_intent1,
				mangerNetServiceConnection, Context.BIND_AUTO_CREATE);
		
		// 连接时长
		receiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Const.CONNECT_TIME_ACTION);
		registerReceiver(receiver, filter);
	}
	
	private void initView()
	{
		titleBarView = (TitleBarView) findViewById(R.id.titlebar);
		
		tvTermName = (TextView) findViewById(R.id.tv_term_name);
		tvTermIp = (TextView) findViewById(R.id.tv_term_ip);
		tvTermState = (TextView) findViewById(R.id.tv_term_state);
		tvTermDuring = (TextView) findViewById(R.id.tv_term_during);
		btnModifTermName = (Button) findViewById(R.id.btn_modif_term_name);
		
		pbSdcardRoom = (ProgressBar) findViewById(R.id.pb_sdcard_room);
		tvSdcardRoom = (TextView) findViewById(R.id.tv_sdcard_room);
		
		tvLedManagerVersion = (TextView) findViewById(R.id.tv_version_ledmanager);
		tvLedServerVersion = (TextView) findViewById(R.id.tv_version_ledserver);
		tvSenderVersion = (TextView) findViewById(R.id.tv_version_sender);
		tvColorLightVersion = (TextView) findViewById(R.id.tv_version_img);
		
	}
	
	private void initListener()
	{
		// 标题
		titleBarView.setTitleBarListener(new TitleBarListener()
		{
			
			@Override
			public void onClickRightImg(View v)
			{
			}
			
			@Override
			public void onClickLeftImg(View v)
			{
				finish();
			}
		});
		// 修改终端名
		btnModifTermName.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				LayoutInflater inflater = LayoutInflater
						.from(InfoActivity.this);
				View view = inflater.inflate(R.layout.modify_term_name, null);
				final TextView tvOldName = (TextView) view
						.findViewById(R.id.tv_term_name);
				final EditText etNewName = (EditText) view
						.findViewById(R.id.et_entry_name);
				final Button btnSubmit = (Button) view
						.findViewById(R.id.btn_submit);
				final Button btnCancel = (Button) view
						.findViewById(R.id.btn_cancel);
				if (app.ledTerminateInfo != null)
				{
					tvOldName.setText(app.ledTerminateInfo.getStrName());
				}
				
				btnCancel.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						if (dialogTermName != null)
						{
							dialogTermName.dismiss();
						}
					}
				});
				btnSubmit.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						newTermName = etNewName.getText().toString();
						if (TextUtils.isEmpty(newTermName))
						{
							etNewName.requestFocus();
							return;
						}
						if (mangerNetService != null)
						{
							mangerNetService.modifyTermName(newTermName);
						}
						if (dialogTermName != null)
						{
							dialogTermName.dismiss();
						}
					}
				});
				dialogTermName = DialogFactory.createDialog(InfoActivity.this,
						view);
				dialogTermName.show();
				// builder.setView(view);
				// dialogTermName = builder.create();
				// dialogTermName.show();
			}
		});
	}
	
	/**
	 * 获得版本号
	 * 
	 * @param context
	 * @return
	 */
	public String getVerName(Context context)
	{
		String verName = null;
		try
		{
			verName = context.getPackageManager().getPackageInfo(
					"com.clt.ledmanager", 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			Log.e("版本号获取异常", e.getMessage());
			e.printStackTrace();
		}
		
		return verName;
	}
	
	private void initData()
	{
		if (!TextUtils.isEmpty(getVerName(this)))
		{
			tvLedManagerVersion.setText(getVerName(this));
		}
		if (app.someInfo != null)
		{
			SomeInfo someInfo = app.someInfo;
			int progress = (int) ((someInfo.getSdTotalSize() - someInfo
					.getSdAviableSize()) * 100 / someInfo.getSdTotalSize());
			pbSdcardRoom.setProgress(progress);
			String sdcardRoom = getString(R.string.sdcard_room);
			String availableSize = Tools.byte2KbOrMb(someInfo
					.getSdAviableSize());
			String totalSize = Tools.byte2KbOrMb(someInfo.getSdTotalSize());
			tvSdcardRoom.setText(String.format(sdcardRoom, availableSize,
					totalSize));
			
			tvLedServerVersion.setText(someInfo.getLedServerVersion());
			
			tvColorLightVersion.setText(someInfo.getColorlightVersion());
		}
		
		if (app.ledTerminateInfo != null)
		{
			tvTermName.setText(app.ledTerminateInfo.getStrName());
			tvTermIp.setText("(" + app.ledTerminateInfo.getIpAddress() + ")");
			
			String state = null;
			if (app.isOnline)
			{
				state = getString(R.string.online);
			}
			else
			{
				state = getString(R.string.offline);
				
			}
			tvTermState.setText("(" + state + ")");
			
			tvTermDuring.setText(Tools.formatDuring(Clock.pastTime));
			
		}
		if (app.senderInfo != null)
		{
			
			tvSenderVersion.setText(app.senderInfo.getMajorVersion() + "."
					+ app.senderInfo.getMinorVersion());
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		try
		{
			unbindService(mangerNetServiceConnection);
			unregisterReceiver(receiver);
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 广播接收器
	 * 
	 * @author Administrator
	 * 
	 */
	class MyBroadcastReceiver extends BroadcastReceiver
	{
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equalsIgnoreCase(Const.CONNECT_TIME_ACTION))
			{
				long time = intent.getLongExtra("connTime", 0);
				tvTermDuring.setText(Tools.formatDuring(time));
			}
			
		}
		
	}
	
	@Override
	protected void dealHandlerMessage(Message netMessage)
	{
		switch (netMessage.what)
		{
			case NetMessageType.ModifyTerminateNameAnswer:// 修改终端名结果
			{
				
				String reslut = (String) netMessage.obj;
				Gson mGson = new Gson();
				NMChangeTermNameAnswer answer = mGson.fromJson(reslut,
						NMChangeTermNameAnswer.class);
				if (answer.getErrorCode() == 1)
				{
					Toast.makeText(InfoActivity.this,
							getResources().getString(R.string.setting_success),
							1000).show();
					app.ledTerminateInfo.setStrName(newTermName);
					tvTermName.setText(newTermName);
					
					sendBroadcast(new Intent(Const.MODIF_TERM_NAME_ACTION));
					
				}
				else
				{
					Toast.makeText(InfoActivity.this,
							getResources().getString(R.string.setting_fail),
							1000).show();
					
				}
			}
			break;
		}
	}
}
