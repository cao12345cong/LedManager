package com.clt.activity;

import com.clt.ledmanager.R;
import com.clt.netmessage.NetMessageType;
import com.clt.service.BaseService;
import com.clt.service.NetService;
import com.clt.ui.DialogFactory;
import com.clt.util.Const;
import com.clt.util.DialogUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity基类
 * 
 */
public abstract class BaseActivity extends Activity
{
	private BroadcastReceiver receiver;
	
	Dialog enterPassDialog = null;
	
	/**
	 * 异步处理消息
	 */
	protected Handler nmHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
				case Const.connectBreak:// 连接断开
					// DialogUtil.createConnBreakDialog(BaseActivity.this).show();
				break;
				case NetMessageType.KickOutOf:// 被踢
					Application app = (Application) getApplication();
					app.setOnline(false);
					DialogUtil.createKickOfDialog(BaseActivity.this).show();
				break;
				case Const.connnectFail:// 连接失败
					toast(getResources().getString(
							R.string.fail_connect_to_server), 1000);
				break;
			
			}
			dealHandlerMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// 接收广播：心跳检测连接断开
		IntentFilter filter = new IntentFilter();
		filter.addAction(Const.CONNECT_BEARK_ACTION);
		
		receiver = new MyBrocastReceiver();
		registerReceiver(receiver, filter);
	}
	
	protected void dealHandlerMessage(Message msg)
	{
		
	}
	
	/**
	 * Toast显示信息
	 * 
	 * @param msg
	 *            消息
	 * @param time
	 *            时间
	 */
	public void toast(String msg, int time)
	{
		Toast.makeText(this, msg, time).show();
	}
	
	/**
	 * Activity之间的跳转
	 * 
	 * @param activityClass
	 */
	protected void skip(Class<?> activityClass)
	{
		try
		{
			Intent intent = new Intent();
			intent.setClass(this, activityClass);
			startActivity(intent);
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 获得字符串资源
	 * 
	 * @param resId
	 *            资源id
	 * @return
	 */
	public String getResString(int resId)
	{
		return getResources().getString(resId);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	/**
	 * 广播接收器
	 * 
	 */
	class MyBrocastReceiver extends BroadcastReceiver
	{
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equalsIgnoreCase(Const.CONNECT_BEARK_ACTION))
			{
				
				nmHandler.obtainMessage(Const.connectBreak).sendToTarget();
			}
		}
		
	}
	
	/**
	 * 显示密码输入框
	 */
	protected void showEnterPassDialog(final String rightPassword,
			final OnPassDialogSubmitCallback callBack)
	{
		try
		{
			LayoutInflater inflater = LayoutInflater.from(this);
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
					String entryPassword = etPass.getText().toString();
					if (entryPassword.equals(rightPassword))
					{
						if (callBack != null)
						{
							callBack.onSubmit();
							if (enterPassDialog != null)
							{
								enterPassDialog.dismiss();
							}
						}
						
					}
					
				}
			});
			btnCancel.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (enterPassDialog != null)
					{
						enterPassDialog.dismiss();
					}
					
				}
			});
			
			enterPassDialog = DialogFactory.createDialog(this, view);
			enterPassDialog.show();
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 
	 * enterPassDialog的sumbit点击后回调
	 */
	public interface OnPassDialogSubmitCallback
	{
		void onSubmit();
	}
	
}
