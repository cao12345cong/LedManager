package com.clt.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clt.IService;
import com.clt.commondata.PortArea;
import com.clt.commondata.SenderParameters;
import com.clt.ledmanager.R;
import com.clt.netmessage.NMDetectSenderAnswer;
import com.clt.netmessage.NMSetEDIDAnswer;
import com.clt.netmessage.NMSetSenderBasicParametersAnswer;
import com.clt.netmessage.NetMessageType;
import com.clt.service.BaseService.LocalBinder;
import com.clt.service.BaseServiceFactory;
import com.clt.ui.CustomerSpinner;
import com.clt.util.Const;
import com.clt.util.DialogUtil;
import com.clt.util.NetUtil;
import com.clt.util.SendingCardFunctionHelper;
import com.google.gson.Gson;

/**
 * 发送卡 修改时间：2014.6.7
 */
public class SenderCardActivity extends BaseActivity

{
	private static boolean DEBUG = true;
	
	private Application app;
	
	private IService mangerNetService;
	
	// private SenderInfo app.senderInfo;// 探测发送卡信息
	
	private Dialog detectWaittingDialog;// 探卡时的等待圆圈
	
	private LinearLayout llLayout;// 根容器
	
	/**
	 * 视图：自动亮度调节
	 */
	private CustomerSpinner spinnerAutoBright;
	
	private String[] arrAutoBright;
	
	/**
	 * 发送卡型号
	 */
	private TextView tvSenderCardType; // 信息
	
	private Button btnDetectSenderCard; // 探测按钮
	
	/**
	 * 分辨率
	 */
	private CustomerSpinner spinnerResolution, spinnerFrameRate;// 分辨率，帧率
			
	private EditText etResolutionCustomWidth, etResolutionCustomHeight;
	
	private String[] senderResolutions;
	
	private Button btnSetResolutions;// 设置
	
	private int freq;// 当前的帧率
	
	/**
	 * 发送卡输出
	 */
	private CustomerSpinner spinnerOutPut;
	
	/**
	 * 显示模式，视频来源
	 */
	private CustomerSpinner spinnerDisplayMode, spinnerVideoSource;
	
	private EditText etEdidWidth, etEdidHeight;
	
	/**
	 * DVI信息
	 */
	private EditText etDviFrameRate, etDviWidth, etDviHeight;
	
	/**
	 * 温度
	 */
	private EditText etTempeture;
	
	/**
	 * 网口控制面积
	 */
	
	private CustomerSpinner spinnerControlArea;
	
	private EditText etAStartX, etAStartY, etAWidth, etAHeight;// A口列起点，行起点，宽度，高度。。以下B,C,D口
			
	private EditText etBStartX, etBStartY, etBWidth, etBHeight;
	
	private EditText etCStartX, etCStartY, etCWidth, etCHeight;
	
	private EditText etDStartX, etDStartY, etDWidth, etDHeight;
	
	private Button btnSaveCtrArea;
	
	// // handler
	// private Handler mHandler = new Handler()
	// {
	// public void handleMessage(android.os.Message msg)
	// {
	// dealHandlerMessage(msg);
	// }
	//
	// };
	
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
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		try
		{
			Application.getInstance().setSystemLanguage();
			setContentView(R.layout.sender_card);
			init();
			initView();
			initListener();
			initData();
		}
		catch (Exception e)
		{
			
		}
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		try
		{
			if (app != null && app.senderInfo != null)
			{
				updateView();
			}
			if (mangerNetService != null)
			{
				mangerNetService.setNmHandler(nmHandler);
			}
		}
		catch (Exception e)
		{
		}
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	private void init()
	{
		try
		{
			app = (Application) getApplication();
			// 绑定service
			Intent _intent1 = new Intent(SenderCardActivity.this,
					BaseServiceFactory.getBaseService());
			this.getApplicationContext().bindService(_intent1,
					mangerNetServiceConnection, Context.BIND_AUTO_CREATE);
			
			// 等待圆圈
			detectWaittingDialog = DialogUtil.createDownloadDialog(this);
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
			// 根容器
			llLayout = (LinearLayout) findViewById(R.id.ll_layout);
			llLayout.requestFocus();
			// 发送卡型号
			tvSenderCardType = (TextView) findViewById(R.id.tv_sender_typeinfo);
			btnDetectSenderCard = (Button) findViewById(R.id.btn_detect);
			
			/** 分辨率 ***/
			spinnerResolution = (CustomerSpinner) findViewById(R.id.spinner_resolution);
			spinnerResolution.initView(R.array.resolution);
			senderResolutions = getResources().getStringArray(
					R.array.resolution);
			spinnerResolution.setSelection(1, true);
			btnSetResolutions = (Button) findViewById(R.id.btn_resolution_set);
			etResolutionCustomWidth = (EditText) findViewById(R.id.et_resolution_width);
			etResolutionCustomHeight = (EditText) findViewById(R.id.et_resolution_height);
			// 帧率
			spinnerFrameRate = (CustomerSpinner) findViewById(R.id.spinner_res_frame_rate);
			spinnerFrameRate.initView(R.array.frame_rate);
			spinnerFrameRate.setSelection(0, true);
			freq = 60;
			
			/****** 自动亮度调节（C1S） ***********/
			spinnerAutoBright = (CustomerSpinner) findViewById(R.id.spinner_auto_bright_adjust);
			spinnerAutoBright.initView(R.array.on_off);
			spinnerAutoBright.setSelection(0, true);
			// 发送卡输出
			spinnerOutPut = (CustomerSpinner) findViewById(R.id.spinner_out_put);
			spinnerOutPut.initView(R.array.sender_output);
			spinnerOutPut.setSelection(0, true);
			
			// 显卡模式
			spinnerDisplayMode = (CustomerSpinner) findViewById(R.id.spinner_display_mode);
			spinnerDisplayMode.initView(R.array.display_mode);
			spinnerDisplayMode.setSelection(0, true);
			// 视频来源
			spinnerVideoSource = (CustomerSpinner) findViewById(R.id.spinner_video_source);
			spinnerVideoSource.initView(R.array.video_source);
			spinnerVideoSource.setSelection(0, true);
			// dvi信息
			etDviFrameRate = (EditText) findViewById(R.id.et_dvi_frame_rate);
			etDviWidth = (EditText) findViewById(R.id.et_dvi_width);
			etDviHeight = (EditText) findViewById(R.id.et_dvi_height);
			// 温度
			etTempeture = (EditText) findViewById(R.id.et_sender_tempeture);
			// 控制面积
			// spinnerControlArea = (CustomerSpinner)
			// findViewById(R.id.spinner_control_area);
			// spinnerControlArea.initView(R.array.sender_resolution);
			// spinnerControlArea.setSelection(0, true);
			// A口
			etAStartX = (EditText) findViewById(R.id.et_a_1);
			etAStartY = (EditText) findViewById(R.id.et_a_2);
			etAWidth = (EditText) findViewById(R.id.et_a_3);
			etAHeight = (EditText) findViewById(R.id.et_a_4);
			
			// B口
			etBStartX = (EditText) findViewById(R.id.et_b_1);
			etBStartY = (EditText) findViewById(R.id.et_b_2);
			etBWidth = (EditText) findViewById(R.id.et_b_3);
			etBHeight = (EditText) findViewById(R.id.et_b_4);
			// C口
			etCStartX = (EditText) findViewById(R.id.et_c_1);
			etCStartY = (EditText) findViewById(R.id.et_c_2);
			etCWidth = (EditText) findViewById(R.id.et_c_3);
			etCHeight = (EditText) findViewById(R.id.et_c_4);
			// D口
			etDStartX = (EditText) findViewById(R.id.et_d_1);
			etDStartY = (EditText) findViewById(R.id.et_d_2);
			etDWidth = (EditText) findViewById(R.id.et_d_3);
			etDHeight = (EditText) findViewById(R.id.et_d_4);
			btnSaveCtrArea = (Button) findViewById(R.id.btn_save_area);
			
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
		// 让EditText失去焦点
		llLayout.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				llLayout.requestFocus();
				return false;
			}
		});
		// 探测按钮
		btnDetectSenderCard.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					if (mangerNetService != null)
					{
						mangerNetService.setNmHandler(nmHandler);
						mangerNetService.DetectSender();
						if (detectWaittingDialog != null)
						{
							detectWaittingDialog.show();
						}
					}
				}
				catch (Exception e)
				{
				}
				
			}
		});
		// 自动亮度调节
		spinnerAutoBright.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				try
				{
					if (mangerNetService != null)
					{
						setAutoBright(position);
					}
				}
				catch (Exception e)
				{
					
				}
				
			}
			
		});
		// 修改分辨率
		spinnerResolution.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				try
				{
					if (position == 0)
					{
						findViewById(R.id.tr_res_custom).setVisibility(
								View.VISIBLE);
						findViewById(R.id.ll_res_custom).setVisibility(
								View.VISIBLE);
						btnSetResolutions.setVisibility(View.VISIBLE);
					}
					else
					{
						findViewById(R.id.tr_res_custom).setVisibility(
								View.GONE);
						findViewById(R.id.ll_res_custom).setVisibility(
								View.GONE);
						btnSetResolutions.setVisibility(View.GONE);
						String s = spinnerResolution.getText().toString();
						if (TextUtils.isEmpty(s)
								|| spinnerResolution.getText().toString()
										.indexOf("*") == -1)
						{
							return;
						}
						String[] arrWH = s.split("\\*");
						int width = Integer.parseInt(arrWH[0]);
						int height = Integer.parseInt(arrWH[1]);
						mangerNetService.setSenderResolution(width, height,
								freq);
						if (detectWaittingDialog != null)
						{
							detectWaittingDialog.show();
						}
					}
					
				}
				catch (Exception e)
				{
					
				}
				
			}
			
		});
		/**
		 * 设置分辨率
		 */
		btnSetResolutions.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					String widthStr = etResolutionCustomWidth.getText()
							.toString();
					String heightStr = etResolutionCustomHeight.getText()
							.toString();
					if (TextUtils.isEmpty(widthStr)
							|| TextUtils.isEmpty(heightStr))
					{
						Toast.makeText(SenderCardActivity.this,
								getResString(R.string.please_input_wh), 1000)
								.show();
						return;
					}
					int width = Integer.parseInt(widthStr);
					int height = Integer.parseInt(heightStr);
					if (width <= 0 || height <= 0)
					{
						Toast.makeText(SenderCardActivity.this,
								getResString(R.string.please_input_wh), 1000)
								.show();
						return;
					}
					// 保存自定义分辨率
					freq = Integer.parseInt(spinnerFrameRate.getText()
							.toString());
					mangerNetService.setSenderResolution(width, height, freq);
					if (detectWaittingDialog != null)
					{
						detectWaittingDialog.show();
					}
				}
				catch (Exception e)
				{
				}
				
			}
		});
		
		/**
		 * 逐帧，隔帧
		 */
		spinnerOutPut.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				try
				{
					if (position == 0)
					{
						setOutPut(60);
					}
					else if (position == 1)
					{
						setOutPut(30);
					}
				}
				catch (Exception e)
				{
				}
				
			}
			
		});
		/**
		 * 显示模式，2D/3D
		 */
		spinnerDisplayMode.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				
			}
			
		});
		/**
		 * 视频来源 HDMI/DVI/C1S
		 */
		spinnerVideoSource.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				try
				{
					if (position == 0)
					{
						setVideoSource(0);
					}
					else if (position == 1)
					{
						setVideoSource(1);
					}
					else if (position == 2)
					{
						setVideoSource(3);
					}
				}
				catch (Exception e)
				{
					
				}
				
				// if (detectWaittingDialog != null)
				// {
				// detectWaittingDialog.show();
				// }
			}
			
		});
		
		/**
		 * 保存控制面积
		 */
		btnSaveCtrArea.setOnClickListener(new OnClickListener()
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
									if (app.senderInfo == null)
									{
										return;
									}
									SenderParameters params = new SenderParameters();
									params.setbBigPack(app.senderInfo
											.isBigPacket());
									params.setbAutoBright(app.senderInfo
											.isAutoBright());
									params.setM_frameRate(app.senderInfo
											.getFrameRate());
									params.setRealParamFlag(app.senderInfo
											.isRealParamFlags());
									params.setbZeroDelay(app.senderInfo
											.isBZeroDelay());
									params.setRgbBitsFlag(app.senderInfo
											.getTenBitFlag());
									params.setbHDCP(app.senderInfo.isBHDCP());
									params.setInputType(app.senderInfo
											.getInputType());
									// 设置网口面积
									params.setPorts(getPorts());
									mangerNetService.setBasicParams(params);
								}
								catch (Exception e)
								{
								}
							}
						});
				
			}
		});
	}
	/**
	 * 设置视频亮度调节
	 */
	protected void setAutoBright(int value)
	{
		
		if (app.senderInfo == null)
		{
			return;
		}
		// 构建一个SenderParameters数据封装类
		SenderParameters params = new SenderParameters();
		params.setbBigPack(app.senderInfo.isBigPacket());
		params.setbAutoBright(value == 0 ? true : false);
		params.setM_frameRate(app.senderInfo.getFrameRate());
		params.setRealParamFlag(app.senderInfo.isRealParamFlags());
		params.setbZeroDelay(app.senderInfo.isBZeroDelay());
		params.setRgbBitsFlag(app.senderInfo.getTenBitFlag());
		params.setbHDCP(app.senderInfo.isBHDCP());
		params.setPorts(app.senderInfo.getPorts());
		params.setInputType(app.senderInfo.getInputType());
		mangerNetService.setBasicParams(params);
	}
	/**
	 * 发送卡输出，逐帧，隔帧
	 * 
	 * @param value
	 */
	protected void setOutPut(int frameRate)
	{
		try
		{
			if (app.senderInfo == null)
			{
				return;
			}
			// 构建一个SenderParameters数据封装类
			SenderParameters params = new SenderParameters();
			params.setbBigPack(app.senderInfo.isBigPacket());
			params.setbAutoBright(app.senderInfo.isAutoBright());
			params.setM_frameRate(frameRate);
			params.setRealParamFlag(app.senderInfo.isRealParamFlags());
			params.setbZeroDelay(app.senderInfo.isBZeroDelay());
			params.setRgbBitsFlag(app.senderInfo.getTenBitFlag());
			params.setbHDCP(app.senderInfo.isBHDCP());
			params.setPorts(app.senderInfo.getPorts());
			params.setInputType(app.senderInfo.getInputType());
			mangerNetService.setBasicParams(params);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * 设置视频来源，HDMI/DVI/C1S
	 */
	protected void setVideoSource(int value)
	{
		try
		{
			if (app.senderInfo == null)
			{
				return;
			}
			// 构建一个SenderParameters数据封装类
			SenderParameters params = new SenderParameters();
			params.setbBigPack(app.senderInfo.isBigPacket());
			params.setbAutoBright(app.senderInfo.isAutoBright());
			params.setM_frameRate(app.senderInfo.getFrameRate());
			params.setRealParamFlag(app.senderInfo.isRealParamFlags());
			params.setbZeroDelay(app.senderInfo.isBZeroDelay());
			params.setRgbBitsFlag(app.senderInfo.getTenBitFlag());
			params.setbHDCP(app.senderInfo.isBHDCP());
			params.setPorts(app.senderInfo.getPorts());
			// 信号输入类型 0为hdmi 1为DVI 3C1S
			int inputType = app.senderInfo.getInputType();
			int myvalue = ((inputType & 0xf0) | (value & 0x0f));
			params.setInputType(myvalue);
			mangerNetService.setBasicParams(params);
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 初始化数据
	 */
	private void initData()
	{
		
	}
	
	/**
	 * 探卡后更新界面
	 */
	public void updateView()
	{
		try
		{
			if (app.senderInfo != null)
			{
				/**
				 * 发送卡型号
				 */
				if (app.senderInfo.getSenderType() == null)
				{
					tvSenderCardType.setText(getResources().getString(
							R.string.sender_type));
				}
				else
				{
					tvSenderCardType.setText(getResources().getString(
							R.string.sender_type)
							+ " "
							+ app.senderInfo.getSenderType()
							+ " "
							+ app.senderInfo.getMajorVersion()
							+ "."
							+ app.senderInfo.getMinorVersion());
				}
				/**
				 * 发送卡分辨率
				 */
				int width = app.senderInfo.getResolutionWidth();
				int height = app.senderInfo.getResolutionHeight();
				String sss = width + "*" + height;
				int indexs = getIndexFromArray(senderResolutions, sss);// 非自定义
				if (indexs != -1)
				{
					// spinnerResolution.setText(sss);
					spinnerResolution.setSelection(indexs, true);
				}
				else
				{
					spinnerResolution.setText(sss);
				}
				
				/**
				 * 温度和湿度
				 */
				if (app.senderInfo.getTemperature() == null)
				{
					etTempeture.setText("");
				}
				else
				{
					etTempeture.setText(app.senderInfo.getTemperature() + "℃");
				}
				/**
				 * dvi信息
				 */
				
				double dvi_freq = 0;
				if (app.senderInfo.getVsyncNum() != 0)
				{
					dvi_freq = Math.pow(10.0f, 9.0f)
							/ app.senderInfo.getVsyncNum() / 8.0f;
				}
				
				float dvi_line_span = (app.senderInfo.getHsyncNum() - app.senderInfo
						.getDeNum()) * 8.0f;
				
				
				
				
				// 帧率
				etDviFrameRate.setText(String.format("%.2f", dvi_freq) + "hz");
				etDviWidth.setText(String.valueOf(app.senderInfo
						.getResolutionWidth()));
				etDviHeight.setText(String.valueOf(app.senderInfo
						.getResolutionHeight()));
				
				// 发送卡输出
				int outPut = app.senderInfo.getFrameRate();
				if (outPut == 60)
				{
					spinnerOutPut.setSelection(0, true);
				}
				else
				{
					
					spinnerOutPut.setSelection(1, true);
				}
				/**
				 * 视频来源
				 */
				int features = SendingCardFunctionHelper
						.getSupportedFeatures(app.senderInfo);
				if (SendingCardFunctionHelper.haveFeature(features,
						SendingCardFunctionHelper.SCF_VIDEO_SOURCE))
				{
					findViewById(R.id.tr_video_source).setVisibility(
							View.VISIBLE);
					int inputType = app.senderInfo.getInputType();
					int myvalue = (inputType & 0x0f);
					String[] videos = getResources().getStringArray(
							R.array.video_source);
					if (myvalue == 0)
					{
						spinnerVideoSource.setSelection(0, true);
					}
					else if (myvalue == 1)
					{
						spinnerVideoSource.setSelection(1, true);
					}
					else if (myvalue == 3)
					{
						spinnerVideoSource.setSelection(2, true);
					}
					else
					{
						spinnerVideoSource.setSelection(0, true);
					}
				}
				else
				{
					findViewById(R.id.tr_video_source).setVisibility(View.GONE);
				}
				// 判断是否有自动亮度调节功能
				if (SendingCardFunctionHelper.haveFeature(features,
						SendingCardFunctionHelper.SCF_AUTO_BRIGHT))
				{
					findViewById(R.id.tr_auto_bright_adjust).setVisibility(
							View.VISIBLE);
					spinnerAutoBright.setSelection(
							app.senderInfo.isAutoBright() ? 0 : 1, true);
				}
				else
				{
					findViewById(R.id.tr_auto_bright_adjust).setVisibility(
							View.GONE);
				}
				/**
				 * 控制面积
				 */
				int portCount = app.senderInfo.getPortCount();
				PortArea[] ports = app.senderInfo.getPorts();
				if (portCount == 2)
				{
					findViewById(R.id.row_c).setVisibility(View.GONE);
					findViewById(R.id.row_d).setVisibility(View.GONE);
					LinearLayout rowB = (LinearLayout) findViewById(R.id.row_b);
					rowB.setBackgroundResource(R.drawable.page_list_below);
					rowB.setPadding(0, 10, 10, 10);
					etAStartX.setText(ports[0].getStartX() + "");
					etAStartY.setText(ports[0].getStarty() + "");
					etAWidth.setText(ports[0].getWidth() + "");
					etAHeight.setText(ports[0].getHeight() + "");
					
					etBStartX.setText(ports[1].getStartX() + "");
					etBStartY.setText(ports[1].getStarty() + "");
					etBWidth.setText(ports[1].getWidth() + "");
					etBHeight.setText(ports[1].getHeight() + "");
				}
				else if (portCount == 4)
				{
					etAStartX.setText(ports[0].getStartX() + "");
					etAStartY.setText(ports[0].getStarty() + "");
					etAWidth.setText(ports[0].getWidth() + "");
					etAHeight.setText(ports[0].getHeight() + "");
					
					etBStartX.setText(ports[1].getStartX() + "");
					etBStartY.setText(ports[1].getStarty() + "");
					etBWidth.setText(ports[1].getWidth() + "");
					etBHeight.setText(ports[1].getHeight() + "");
					
					etCStartX.setText(ports[2].getStartX() + "");
					etCStartY.setText(ports[2].getStarty() + "");
					etCWidth.setText(ports[2].getWidth() + "");
					etCHeight.setText(ports[2].getHeight() + "");
					
					etDStartX.setText(ports[3].getStartX() + "");
					etDStartY.setText(ports[3].getStarty() + "");
					etDWidth.setText(ports[3].getWidth() + "");
					etDHeight.setText(ports[3].getHeight() + "");
					
				}
				
			}
		}
		catch (Exception e)
		{
			
		}
		
	}
	
	/**
	 * 从数组中获取当前值的索引，没有则返回-1
	 */
	private int getIndexFromArray(String[] arr, String res)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].trim().equalsIgnoreCase(res))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 日期字符串格式化 1->01
	 * 
	 * @param str
	 * @return
	 */
	public String formatTimeString(int str)
	{
		return String.format("%02d", str);
	}
	
	/**
	 * 操作之前的过滤操作，如wifi是否连接，socket是否断开
	 * 
	 * @return
	 */
	public boolean handleFilter()
	{
		if (!NetUtil.isConnnected(this))
		{
			Toast.makeText(this,
					getResources().getString(R.string.connect_network), 1000)
					.show();
			return false;
		}
		
		if (mangerNetService == null || !mangerNetService.isConnecting())
		{
			Toast.makeText(this,
					getResources().getString(R.string.fail_connect_to_server),
					1000).show();
			return false;
		}
		return true;
	}
	
	public PortArea[] getPorts()
	{
		PortArea[] ports = new PortArea[4];
		
		PortArea p0 = new PortArea();
		p0.setStartX(Integer.parseInt(etAStartX.getText().toString()));
		p0.setStarty(Integer.parseInt(etAStartY.getText().toString()));
		p0.setWidth(Integer.parseInt(etAWidth.getText().toString()));
		p0.setHeight(Integer.parseInt(etAHeight.getText().toString()));
		
		PortArea p1 = new PortArea();
		p1.setStartX(Integer.parseInt(etBStartX.getText().toString()));
		p1.setStarty(Integer.parseInt(etBStartY.getText().toString()));
		p1.setWidth(Integer.parseInt(etBWidth.getText().toString()));
		p1.setHeight(Integer.parseInt(etBHeight.getText().toString()));
		
		ports[0] = p0;
		ports[1] = p1;
		ports[2] = new PortArea();
		ports[3] = new PortArea();
		return ports;
	}
	
	// protected void makeEditTextFouceable(boolean flag)
	// {
	// // A口
	// etAStartX.setFocusable(flag);
	// etAStartY.setFocusable(flag);
	// etAWidth.setFocusable(flag);
	// etAHeight.setFocusable(flag);
	// // B
	// etBStartX.setFocusable(flag);
	// etBStartY.setFocusable(flag);
	// etBWidth.setFocusable(flag);
	// etBHeight.setFocusable(flag);
	// // C
	// etCStartX.setFocusable(flag);
	// etCStartY.setFocusable(flag);
	// etCWidth.setFocusable(flag);
	// etCHeight.setFocusable(flag);
	// // D
	// etDStartX.setFocusable(flag);
	// etDStartY.setFocusable(flag);
	// etDWidth.setFocusable(flag);
	// etDHeight.setFocusable(flag);
	// }
	/**
	 * 处理message
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
				case NetMessageType.DetectSenderAnswer:// 探卡成功
					if (detectWaittingDialog.isShowing())
					{
						detectWaittingDialog.dismiss();
					}
					String strMessage = (String) msg.obj;
					Log.i("DetectSenderAnswer", strMessage);
					Gson gson = new Gson();
					NMDetectSenderAnswer nmDetectSenderAnswer = gson.fromJson(
							strMessage, NMDetectSenderAnswer.class);
					if (nmDetectSenderAnswer.getErrorCode() == 0)
					{
						Toast.makeText(
								SenderCardActivity.this,
								getResources().getString(
										R.string.detect_card_fail), 500).show();
					}
					else
					{
						Toast.makeText(
								SenderCardActivity.this,
								getResources().getString(
										R.string.detect_card_success), 500)
								.show();
						app.senderInfo = nmDetectSenderAnswer.getSenderInfo();
						updateView();
					}
				break;
				case NetMessageType.SetSenderBasicParametersAnswer:// 保存基本参数是否成功
					if (detectWaittingDialog.isShowing())
					{
						detectWaittingDialog.dismiss();
					}
					String strMessage2 = (String) msg.obj;
					Gson gson2 = new Gson();
					NMSetSenderBasicParametersAnswer answer = gson2
							.fromJson(strMessage2,
									NMSetSenderBasicParametersAnswer.class);
					if (answer.getErrorCode() == 0)
					{
						Toast.makeText(SenderCardActivity.this,
								getResources().getString(R.string.save_fail),
								500).show();
					}
					else
					{
						Toast.makeText(
								SenderCardActivity.this,
								getResources().getString(R.string.save_success),
								500).show();
					}
				break;
				case NetMessageType.SetEDIDAnswer:
					if (detectWaittingDialog.isShowing())
					{
						detectWaittingDialog.dismiss();
					}
					
					String testEDIDAnswer = (String) msg.obj;
					Gson testEDIDgson = new Gson();
					NMSetEDIDAnswer setEDID = testEDIDgson.fromJson(
							testEDIDAnswer, NMSetEDIDAnswer.class);
					if (setEDID.getErrorCode() == 0)
					{
						Toast.makeText(
								SenderCardActivity.this,
								getResources().getString(R.string.setting_fail),
								500).show();
					}
					else
					{
						Toast.makeText(
								SenderCardActivity.this,
								getResources().getString(
										R.string.setting_success), 500).show();
					}
				break;
				case Const.connnectFail:
					if (detectWaittingDialog.isShowing())
					{
						detectWaittingDialog.dismiss();
					}
				break;
			}
		}
		catch (Exception e)
		{
			
		}
		
	}
}
