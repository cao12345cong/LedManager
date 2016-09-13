package com.clt.activity;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clt.IService;
import com.clt.activity.BaseActivity.OnPassDialogSubmitCallback;
import com.clt.adapter.OnTextChangeListener;
import com.clt.commondata.SenderInfo;
import com.clt.entity.ConnectionParam;
import com.clt.ledmanager.R;
import com.clt.netmessage.NMSetConnectionToReceiverCardAnswer;
import com.clt.netmessage.NMSetConnectionToSenderCardAnswer;
import com.clt.netmessage.NetMessageType;
import com.clt.service.BaseService.LocalBinder;
import com.clt.service.BaseServiceFactory;
import com.clt.ui.CustomerSpinner;
import com.clt.ui.CustomerSpinner2;
import com.clt.ui.DialogProgressBar;
import com.clt.ui.ReceiverShowViewSimpleType;
import com.google.gson.Gson;

/**
 * 连接关系
 * 
 * @author caocong 2014.6.9
 * 
 */
public class ConnectRelationActivity extends BaseActivity
{
	private static final String TAG = "ReceiverSettingActivity";
	
	private GestureDetector gestureDetector;// 手势
	
	private Application app;
	
	private IService mangerNetService;
	
	private LinearLayout llLayout;// 父布局
	
	/***
	 * 行列数,宽高
	 */
	private EditText etRow, etColumn, etWidth, etHeight;
	
	/**
	 * 发送卡，网口序号，智能连接
	 */
	private CustomerSpinner spinnerSender, spinnerPort, spinnerSmartLink;
	
	/**
	 * 连线方向视图
	 */
	private CustomerSpinner2 spinnerLinkDirect;
	
	/**
	 * 按钮
	 */
	private Button btnAdanved, btnSend, btnSolid;// 高级，发送，固化 按钮
			
	private Button btnSelectAll, btnReset, btnEmptyCard;// 全选，重置，空卡 按钮
			
	/*** 连接视图 ******/
	private ReceiverShowViewSimpleType rsvShowView;// 连接视图
	
	/*** 等待对话框 *******/
	private DialogProgressBar waittingDialog;//
	
	private int row = 1, column = 1;// 行,列数
			
	private int width = 64, height = 64;// 宽度
			
	private int currentModeIndex = 1;// 连接方向
	
	private int portIndex = 0, senderIndex = 0;// 端口，发送卡索引
			
	/**
	 * 绑定service
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
		try
		{
			Application.getInstance().setSystemLanguage();
			setContentView(R.layout.connect_relation_simple);
			init();
			initView();
			initListener();
			initData();
		}
		catch (Exception e)
		{
		}
		
	}
	
	public void init()
	{
		try
		{
			app = (Application) getApplication();
			// 绑定service
			Intent _intent1 = new Intent(ConnectRelationActivity.this,
					BaseServiceFactory.getBaseService());
			this.getApplicationContext().bindService(_intent1,
					mangerNetServiceConnection, Context.BIND_AUTO_CREATE);
			
			waittingDialog = new DialogProgressBar(this);
			//
			// currentReceiver = 0;
			// currentPort = 0;
		}
		catch (Exception e)
		{
		}
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		try
		{
			this.getApplicationContext().unbindService(
					mangerNetServiceConnection);
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 初始化视图
	 */
	private void initView()
	{
		try
		{
			llLayout = (LinearLayout) findViewById(R.id.ll_layout);
			
			etRow = (EditText) findViewById(R.id.et_send_row);
			etRow.setText(String.valueOf(this.row));
			etColumn = (EditText) findViewById(R.id.et_send_column);
			etColumn.setText(String.valueOf(this.column));
			
			etWidth = (EditText) findViewById(R.id.et_width);
			etHeight = (EditText) findViewById(R.id.et_height);
			etWidth.setText(String.valueOf(width));
			etHeight.setText(String.valueOf(height));
			
			spinnerSender = (CustomerSpinner) findViewById(R.id.spinner_send_card);
			SenderInfo senderInfo = app.senderInfo;
			String[] senders = new String[] { "1", "2", "3", "4" };
			spinnerSender.initView(senders);
			spinnerSender.setSelection(0, true);
			
			spinnerPort = (CustomerSpinner) findViewById(R.id.spinner_port_number);
			spinnerPort.initView(R.array.ports);
			spinnerPort.setSelection(0, true);
			
			spinnerLinkDirect = (CustomerSpinner2) findViewById(R.id.spinner_link_path);
			ArrayList<Integer> linkTypes = new ArrayList<Integer>();
			linkTypes.add(R.drawable.bg_receiver_mode1);
			linkTypes.add(R.drawable.bg_receiver_mode2);
			linkTypes.add(R.drawable.bg_receiver_mode3);
			linkTypes.add(R.drawable.bg_receiver_mode4);
			linkTypes.add(R.drawable.bg_receiver_mode5);
			linkTypes.add(R.drawable.bg_receiver_mode6);
			linkTypes.add(R.drawable.bg_receiver_mode7);
			linkTypes.add(R.drawable.bg_receiver_mode8);
			
			spinnerLinkDirect.initView(linkTypes);
			spinnerLinkDirect.setTitle(getResString(R.string.please_select));
			// spinnerLinkDirect.setSelection(0, true);
			
			// spinnerSmartLink = (CustomerSpinner)
			// findViewById(R.id.spinner_smart_link);
			// spinnerSmartLink.initView(R.array.smart_link);
			// spinnerSmartLink.setSelection(0, true);// 是否
			
			btnAdanved = (Button) findViewById(R.id.btn_advanced);
			btnSend = (Button) findViewById(R.id.btn_send);
			btnSolid = (Button) findViewById(R.id.btn_save_to_rcv);
			btnSelectAll = (Button) findViewById(R.id.btn_select_all);
			btnReset = (Button) findViewById(R.id.btn_reset);
			btnEmptyCard = (Button) findViewById(R.id.btn_empty_card);
			
			rsvShowView = (ReceiverShowViewSimpleType) findViewById(R.id.rs_showview);
			
			// final LinearLayout llShowView=(LinearLayout)
			// findViewById(R.id.ll_showview);
			// Button btnGoDown=(Button) findViewById(R.id.btn_go_down);
			// btnGoDown.setOnClickListener(new OnClickListener()
			// {
			//
			// @Override
			// public void onClick(View v)
			// {
			// rsvShowView.goDown(llShowView.getHeight());
			//
			// }
			// });
			// Button btnGoRight=(Button) findViewById(R.id.btn_go_right);
			// spinner = (CustomerSpinner)
			// findViewById(R.id.spinner_receiver_port);
			// receivers = new ArrayList<String>();
			// receivers.add("1");
			// spinner.setList(receivers);
			// ArrayAdapter<String> resolutionAdapter = new
			// ArrayAdapter<String>(this,
			// android.R.layout.simple_spinner_item, receivers);
			// spinner.setAdapter(resolutionAdapter);
			// spinner.setFocusable(false);
			// spinner.setSelection(0, true);
			// //
			// // 初始化数据结构
			// receiver2Ports = new ArrayList<ArrayList<ReceiverPort>>();
			// receiver2Ports.add(receiverPorts);
		}
		catch (Exception e)
		{
			
		}
		
	}
	
	/**
	 * 初始化事件监听
	 */
	private void initListener()
	{
		// 让EditText失去焦点
		llLayout.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				llLayout.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etRow.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etColumn.getWindowToken(), 0);
				return false;
			}
		});
		// 行
		etRow.addTextChangedListener(new OnTextChangeListener()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				
				int mRow = 0;
				try
				{
					mRow = Integer.parseInt(s.toString().trim());
				}
				catch (NumberFormatException e)
				{
					toast(getResString(R.string.please_input_r_c), 1000);
					return;
				}
				if (mRow < 1 || mRow > 256)
				{
					toast(getResString(R.string.please_input_r_c), 1000);
					return;
				}
				
				if (mRow * column > 1024)
				{
					toast(getResString(R.string.please_input_r_c_mulity), 1000);
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etColumn.getWindowToken(), 0);
				rsvShowView.changeRow(mRow);
				spinnerLinkDirect
						.setTitle(getResString(R.string.please_select));
				row = mRow;
				
			}
		});
		// 列
		etColumn.addTextChangedListener(new OnTextChangeListener()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				// 行列判断
				int mColumn = 0;
				try
				{
					mColumn = Integer.parseInt(s.toString().trim());
				}
				catch (NumberFormatException e)
				{
					toast(getResString(R.string.please_input_r_c), 1000);
					return;
				}
				
				if (mColumn < 1 || mColumn > 256)
				{
					toast(getResString(R.string.please_input_r_c), 1000);
					return;
				}
				
				if (row * mColumn > 1024)
				{
					toast(getResString(R.string.please_input_r_c_mulity), 1000);
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etRow.getWindowToken(), 0);
				rsvShowView.changeColumn(mColumn);
				spinnerLinkDirect
						.setTitle(getResString(R.string.please_select));
				column = mColumn;
			}
		});
		// 宽
		etWidth.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				// int value = -1;
				// try
				// {
				// value = Integer.parseInt(s.toString());
				// }
				// catch (Exception e)
				// {
				// Toast.makeText(ConnectRelationActivity.this, "请输入数字", 1000)
				// .show();
				// }
				// if (value <= 0)
				// {
				// Toast.makeText(ConnectRelationActivity.this, "请输入正确的数字",
				// 1000).show();
				// return;
				// }
				// width = value;
				// rsvShowView.setWidth(value);
			}
		});
		// 高
		etHeight.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				// int value = -1;
				// try
				// {
				// value = Integer.parseInt(s.toString());
				// }
				// catch (Exception e)
				// {
				// Toast.makeText(ConnectRelationActivity.this, "请输入数字", 1000)
				// .show();
				// }
				// if (value <= 0)
				// {
				// Toast.makeText(ConnectRelationActivity.this, "请输入正确的数字",
				// 1000).show();
				// return;
				// }
				// height = value;
				// rsvShowView.setHeight(value);
			}
		});
		// 发送卡序号
		spinnerSender.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				senderIndex = position;
				// rsvShowView.setCurrentSenderCard(position);
			}
			
		});
		// 端口号
		spinnerPort.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				portIndex = position;
				// rsvShowView.setCurrentPort(position);
				
			}
			
		});
		// 走线方向
		spinnerLinkDirect
				.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id)
					{
						currentModeIndex = position + 1;
						rsvShowView.changeLinkMode(currentModeIndex);
						// rsvShowView.startDraw(currentModeIndex, column, row,
						// width, height);
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						
					}
				});
		// 重置
		btnReset.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// rsvShowView.resetView();
			}
		});
		// 发送
		btnSend.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				showEnterPassDialog(app.operationPassword,
						new OnPassDialogSubmitCallback()
						{
							
							@Override
							public void onSubmit()
							{
								try
								{
									boolean isOk = doConditional();
									if (!isOk)
									{
										return;
									}
									if (mangerNetService == null)
									{
										return;
									}
									mangerNetService
											.setConnectionToSenderCard(getConnectionParam());
									if (waittingDialog != null)
									{
										waittingDialog.show();
									}
								}
								catch (Exception e)
								{
									
								}
							}
							
						});
				
			}
		});
		// 固化
		btnSolid.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				showEnterPassDialog(app.operationPassword,
						new OnPassDialogSubmitCallback()
						{
							
							@Override
							public void onSubmit()
							{
								try
								{
									boolean isOk = doConditional();
									if (!isOk)
									{
										return;
									}
									if (mangerNetService == null)
									{
										return;
									}
									mangerNetService
											.setConnectionToReceiverCard(getConnectionParam());
									if (waittingDialog != null)
									{
										waittingDialog.show();
									}
								}
								catch (Exception e)
								{
								}
							}
							
						});
				
			}
		});
	}
	
	private void initData()
	{
		
	}
	
	/**
	 * 操作之前的条件判断
	 */
	public boolean doConditional()
	{
		filterRowColumn();
		filterWH();
		return true;
		
	}
	
	/**
	 * 行列判断
	 * 
	 * @return
	 */
	private boolean filterRowColumn()
	{
		
		// 行列判断
		int row = 0, column = 0;
		try
		{
			row = Integer.parseInt(etRow.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_r_c), 1000);
			etRow.requestFocus();
			return false;
		}
		try
		{
			column = Integer.parseInt(etColumn.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_r_c), 1000);
			etColumn.requestFocus();
			return false;
		}
		if (row < 1 || row > 256)
		{
			toast(getResString(R.string.please_input_r_c), 1000);
			etRow.requestFocus();
			return false;
		}
		
		if (column < 1 || column > 256)
		{
			toast(getResString(R.string.please_input_r_c), 1000);
			return false;
		}
		etColumn.requestFocus();
		
		if (row * column > 1024)
		{
			toast(getResString(R.string.please_input_r_c_mulity), 1000);
			etRow.requestFocus();
			return false;
		}
		return true;
		
	}
	
	/**
	 * 宽高判断
	 * 
	 * @return
	 */
	public boolean filterWH()
	{
		// 宽高判断
		int w = 0, h = 0;
		try
		{
			w = Integer.parseInt(etWidth.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_wh), 1000);
			etWidth.requestFocus();
			return false;
		}
		try
		{
			h = Integer.parseInt(etHeight.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_wh), 1000);
			etHeight.requestFocus();
			return false;
		}
		if (w < 1 || w > 1024)
		{
			toast(getResString(R.string.please_input_wh), 1000);
			etWidth.requestFocus();
			return false;
		}
		if (h < 1 || h > 1024)
		{
			toast(getResString(R.string.please_input_wh), 1000);
			etHeight.requestFocus();
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * 获得连接关系基本参数
	 * 
	 * @return
	 */
	private ConnectionParam getConnectionParam()
	{
		try
		{
			this.column = Integer.parseInt(etColumn.getText().toString());
			this.row = Integer.parseInt(etRow.getText().toString());
			this.width = Integer.parseInt(etWidth.getText().toString());
			this.height = Integer.parseInt(etHeight.getText().toString());
			
			ConnectionParam connectionParam = new ConnectionParam();
			connectionParam.setColumn(column);
			connectionParam.setRow(row);
			connectionParam.setWidth(width);
			connectionParam.setHeight(height);
			
			connectionParam.setMode(currentModeIndex);
			connectionParam.setSender(senderIndex);
			connectionParam.setPort(portIndex);
			return connectionParam;
		}
		catch (Exception e)
		{
			return null;
		}
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (mangerNetService != null)
		{
			mangerNetService.setNmHandler(nmHandler);
		}
	}
	
	/**
	 * 处理Handler消息
	 * 
	 * @param msg
	 */
	@Override
	public void dealHandlerMessage(android.os.Message msg)
	{
		try
		{
			switch (msg.what)
			{
				case NetMessageType.setConnectionToReceiverCardAnswer:
				{
					if (waittingDialog != null)
					{
						waittingDialog.dismiss();
					}
					String strMessage = (String) msg.obj;
					Gson gson = new Gson();
					NMSetConnectionToReceiverCardAnswer nm = gson.fromJson(
							strMessage,
							NMSetConnectionToReceiverCardAnswer.class);
					if (nm.getErrorCode() == 0)
					{
						Toast.makeText(ConnectRelationActivity.this,
								getResString(R.string.setting_fail), 500)
								.show();
					}
					else
					{
						Toast.makeText(ConnectRelationActivity.this,
								getResString(R.string.setting_success), 500)
								.show();
					}
				}
				break;
				case NetMessageType.setConnectionToSenderCardAnswer:
				{
					
					if (waittingDialog != null)
					{
						waittingDialog.dismiss();
					}
					String strMessage = (String) msg.obj;
					Gson gson = new Gson();
					NMSetConnectionToSenderCardAnswer nm = gson
							.fromJson(strMessage,
									NMSetConnectionToSenderCardAnswer.class);
					if (nm.getErrorCode() == 0)
					{
						Toast.makeText(ConnectRelationActivity.this,
								getResString(R.string.setting_fail), 500)
								.show();
					}
					else
					{
						Toast.makeText(ConnectRelationActivity.this,
								getResString(R.string.setting_success), 500)
								.show();
					}
				}
				break;
			
			}
		}
		catch (Exception e)
		{
		}
		
	};
	
}
