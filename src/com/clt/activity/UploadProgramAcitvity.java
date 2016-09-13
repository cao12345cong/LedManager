//package com.clt.activity;
//
//import java.io.File;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.MediaMetadataRetriever;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TableLayout;
//import android.widget.TextView;
//
//import com.clt.ledmanager.R;
//import com.clt.ui.CustomerSpinner;
//import com.clt.ui.TitleBarView;
//import com.clt.ui.TitleBarView.TitleBarListener;
//import com.clt.upload.PropertyItem;
//import com.clt.upload.PropertyMultiLineText;
//import com.clt.upload.PropertyPicture;
//import com.clt.upload.PropertySingleLineText;
//import com.clt.upload.PropertyVedio;
//import com.clt.upload.UploadProgram;
//import com.clt.upload.UploadTask;
//import com.clt.upload.VsnFileFactory;
//import com.clt.upload.VsnFileFactoryImpl;
//import com.clt.upload.PropertyItem.MaterialType;
//import com.clt.util.Tools;
///**
// * 上传节目
// */
//public class UploadProgramAcitvity extends BaseActivity
//{
//	//文件类型
//	public class FileType
//	{
//		
//		public static final int PICTURE = 0;// 图片
//		
//		public static final int VIDEO = 1;// 视频
//		
//		public static final int TXT = 2;// txt
//		
//	}
//	
//	public static final String[] fontFamily = { "宋体", "黑体", "楷体", "隶书", "仿宋" };
//	
//	public static final String[] fontSize = { "8", "9", "10", "11", "12", "14",
//			"16", "18", "20", "22", "24", "26", "28", "36", "48", "72", "128" };
//	
//	public static final int[] textColorArr = { Color.BLACK, Color.DKGRAY,
//			Color.GRAY, Color.LTGRAY, Color.WHITE, Color.RED, Color.GREEN,
//			Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
//	
//	};
//	
//	private CustomerSpinner spinnerProgramType;//节目类型下拉菜单
//	
//	/**
//	 * 标题栏
//	 */
//	private TitleBarView titleBarView;//标题栏
//	
//	private EditText etProgramName;//节目名
//	
//	/**
//	 * 窗口属性
//	 */
//	private EditText etStartX, etStartY, etWidth, etHeight;
//	
//	private LinearLayout llInclude;// 视图容器
//	
//	private TextView tvPicPath;//图片路径
//	
//	private Button btnCreateAndUpload; // 上传按钮
//	
//	private String fileName, filePath;//文件名，文件路径
//	
//	/**
//	 * 节目类型
//	 */
//	private IProgramType programType;//节目类型
//	
//	private int programTypeIndex;//节目类型索引
//	
//	private PropertyItem programProperty;// 节目属性
//	
//	private String programName;//节目名
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		try
//		{
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.upload_program);
//			// init();
//			initView();
//			initListener();
//			initData();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
//	
//	// private void init()
//	// {
//	// Intent intent = getIntent();
//	// fileName = intent.getStringExtra("FileName");
//	// filePath = intent.getStringExtra("FilePath");
//	//
//	// }
//	
//	private void initView()
//	{
//		// 标题
//		titleBarView = (TitleBarView) findViewById(R.id.titlebar);
//		// 节目类型
//		spinnerProgramType = (CustomerSpinner) findViewById(R.id.spinner_program_type);
//		// 节目名
//		etProgramName = (EditText) findViewById(R.id.et_program_name);
//		// 窗口属性
//		etStartX = (EditText) findViewById(R.id.et_start_x);
//		etStartY = (EditText) findViewById(R.id.et_start_y);
//		etWidth = (EditText) findViewById(R.id.et_program_width);
//		etHeight = (EditText) findViewById(R.id.et_program_height);
//		//
//		llInclude = (LinearLayout) findViewById(R.id.ll_include);
//		programType = new ProgramPicture();
//		llInclude.addView(programType.getRootView());
//		// 上传按钮
//		btnCreateAndUpload = (Button) findViewById(R.id.btn_creat_upload);
//		
//	}
//	
//	private void initData()
//	{
//		
//		spinnerProgramType.initView(R.array.program_type);
//		spinnerProgramType.setSelection(0, true);
//		
//		etStartX.setText("0");
//		etStartY.setText("0");
//		etWidth.setText("128");
//		etHeight.setText("128");
//	}
//	
//	/**
//	 * 
//	 */
//	private void initListener()
//	{
//		titleBarView.setTitleBarListener(new TitleBarListener()
//		{
//			
//			@Override
//			public void onClickRightImg(View v)
//			{
//				
//			}
//			
//			@Override
//			public void onClickLeftImg(View v)
//			{
//				finish();
//			}
//		});
//		/**
//		 * 节目类型
//		 */
//		spinnerProgramType.setOnItemClickListener(new OnItemClickListener()
//		{
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View mView,
//					int position, long id)
//			{
//				llInclude.removeAllViews();
//				programTypeIndex = position;
//				switch (position)
//				{
//					case 0:
//					{
//						programType = new ProgramPicture();
//						llInclude.addView(programType.getRootView());
//						
//					}
//					break;
//					case 1:
//					{
//						programType = new ProgramVideo();
//						llInclude.addView(programType.getRootView());
//						
//					}
//					break;
//					case 2:
//					{
//						programType = new ProgramSingleLineText();
//						llInclude.addView(programType.getRootView());
//					}
//					break;
//					case 3:
//					{
//						programType = new ProgramMultiLineText();
//						llInclude.addView(programType.getRootView());
//					}
//					break;
//				}
//			}
//		});
//		/**
//		 * 创建并上传
//		 */
//		btnCreateAndUpload.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				try
//				{
//					if (!doFilter())
//					{
//						return;
//					}
//					if (programType == null || !programType.submitFilter())
//					{
//						return;
//					}
//					UploadProgram uploadProgram = createUploadProgram();
//					if (uploadProgram == null)
//					{
//						return;
//					}
//					Intent intent = new Intent(UploadProgramAcitvity.this,
//							MyTabActivity.class);
//					intent.putExtra("Type", "uploadProgram");
//					intent.putExtra("ProgramInfo", uploadProgram);
//					startActivity(intent);
//					finish();
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//				
//			}
//			
//		});
//	}
//	
//	/**
//	 * 构建上传节目
//	 * 
//	 * @return
//	 */
//	private UploadProgram createUploadProgram()
//	{
//		try
//		{
//			
//			int width = Integer.parseInt(etWidth.getText().toString().trim());
//			int height = Integer.parseInt(etHeight.getText().toString().trim());
//			int startX = Integer.parseInt(etStartX.getText().toString().trim());
//			int startY = Integer.parseInt(etStartY.getText().toString().trim());
//			this.programName = etProgramName.getText().toString().trim();
//			
//			// 配置私有参数
//			PropertyItem p = programType.getConfigedProperty();
//			if (p == null)
//			{
//				return null;
//			}
//			// 配置公共参数
//			p.infoWidth = String.valueOf(width);
//			p.infoHeight = String.valueOf(height);
//			p.rectWidth = p.infoWidth;
//			p.rectHeight = p.infoHeight;
//			p.rectX = String.valueOf(startX);
//			p.rectY = String.valueOf(startY);
//			
//			p.programName = programName;
//			
//			UploadProgram uploadProgram = new UploadProgram();
//			// 创建vsn文件
//			VsnFileFactory factory = new VsnFileFactoryImpl();
//			String vsnPath = "/mnt/sdcard/" + p.programName + ".vsn";
//			File vsnFile = factory.createVsnFile(p, vsnPath);
//			if (vsnFile == null)
//			{
//				return null;
//			}
//			uploadProgram.setVsnFileTask(new UploadTask(vsnFile, "/program/"));
//			// 构建上传节目
//			String remoteDir = "/program/" + p.programName + ".files/";
//			uploadProgram.setRemoteDirtory(remoteDir);
//			if (fileName != null)
//			{
//				uploadProgram.getFileTaskList()
//						.add(new UploadTask(new File(filePath), remoteDir
//								+ fileName));
//			}
//			
//			return uploadProgram;
//		}
//		catch (Exception e)
//		{
//			return null;
//		}
//		
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		try
//		{
//			if (requestCode == 0 && resultCode == Activity.RESULT_OK)
//			{
//				fileName = data.getStringExtra("FileName");
//				filePath = data.getStringExtra("FilePath");
//				if (programType != null)
//				{
//					programType.setSelectedFile(filePath);
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
//	
//	/**
//	 * 跳转到选择文件的页面
//	 */
//	public void skipToFileSelectAcitivty(int fileType)
//	{
//		try
//		{
//			Intent intent = new Intent(UploadProgramAcitvity.this,
//					FilesViewActivity.class);
//			intent.putExtra("fileType", fileType);
//			UploadProgramAcitvity.this.startActivityForResult(intent, 0);
//		}
//		catch (Exception e)
//		{
//		}
//		
//	}
//	
//	/**
//	 * 过滤条件
//	 */
//	protected boolean doFilter()
//	{
//		// 宽高检测
//		int width = 0, height = 0;
//		int startX = 0, startY = 0;
//		try
//		{
//			width = Integer.parseInt(etWidth.getText().toString().trim());
//		}
//		catch (NumberFormatException e)
//		{
//			toast(getResString(R.string.please_input_int), 1000);
//			etWidth.requestFocus();
//			return false;
//		}
//		
//		try
//		{
//			height = Integer.parseInt(etHeight.getText().toString().trim());
//		}
//		catch (NumberFormatException e)
//		{
//			toast(getResString(R.string.please_input_int), 1000);
//			etHeight.requestFocus();
//			return false;
//		}
//		try
//		{
//			startX = Integer.parseInt(etStartX.getText().toString().trim());
//		}
//		catch (NumberFormatException e)
//		{
//			toast(getResString(R.string.please_input_int), 1000);
//			etStartX.requestFocus();
//			return false;
//		}
//		
//		try
//		{
//			startY = Integer.parseInt(etStartY.getText().toString().trim());
//		}
//		catch (NumberFormatException e)
//		{
//			toast(getResString(R.string.please_input_int), 1000);
//			etStartY.requestFocus();
//			return false;
//		}
//		
//		// 节目名检测
//		if (TextUtils.isEmpty(etProgramName.getText().toString().trim()))
//		{
//			toast(getResString(R.string.please_input_program_name), 1000);
//			etProgramName.requestFocus();
//			return false;
//		}
//		
//		return true;
//		
//	}
//	
//	@Override
//	protected void onDestroy()
//	{
//		super.onDestroy();
//	}
//	
//	// /////////////////////////////////////////////////////////////////////////////
//	// //各类型节目和UI
//	// /////////////////////////////////////////////////////////////////////////////
//	
//	/**
//	 * 
//	 * 图片节目
//	 */
//	public class ProgramPicture implements IProgramType
//	{
//		// view
//		private View rootView;
//		
//		private TableLayout tlTable;
//		
//		private EditText etFilePath;
//		
//		private Button btnSelectFile;
//		
//		private TextView tvPicWH;
//		
//		private SeekBar sbAlpha;
//		
//		private EditText etAlpha;
//		
//		private CheckBox cblimitScale;
//		
//		private EditText etPlayTimes;
//		
//		private CustomerSpinner spinnerRotate;
//		
//		// params
//		private String filePath;
//		
//		private int rotateIndex = 0;
//		
//		private int picWidth, picHeight;
//		
//		private Handler picViewHandler = new Handler()
//		{
//			public void handleMessage(android.os.Message msg)
//			{
//				switch (msg.what)
//				{
//					case 1:
//					{
//						rootView.findViewById(R.id.tv_file_property)
//								.setVisibility(View.VISIBLE);
//						tlTable.setVisibility(View.VISIBLE);
//						tvPicWH.setText(picWidth + "*" + picHeight);
//					}
//					break;
//				
//				}
//			};
//		};
//		
//		public ProgramPicture()
//		{
//			initMView(getRootView());
//			initMListener();
//			initMData();
//		}
//		
//		private void initMView(View view)
//		{
//			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
//			etFilePath = (EditText) view.findViewById(R.id.et_file_path);
//			btnSelectFile = (Button) view.findViewById(R.id.btn_select_file);
//			tvPicWH = (TextView) view.findViewById(R.id.tv_pic_wh);
//			sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
//			etAlpha = (EditText) view.findViewById(R.id.et_file_alpha);
//			cblimitScale = (CheckBox) view.findViewById(R.id.cb_reserve_as);
//			etPlayTimes = (EditText) view.findViewById(R.id.et_play_times);
//			spinnerRotate = (CustomerSpinner) view
//					.findViewById(R.id.spinner_file_rotate);
//		}
//		
//		private void initMData()
//		{
//			etPlayTimes.setText("1");
//			spinnerRotate.initView(R.array.file_rotate);
//			spinnerRotate.setSelection(0, true);
//		}
//		
//		private void initMListener()
//		{
//			btnSelectFile.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					skipToFileSelectAcitivty(FileType.PICTURE);
//				}
//			});
//			sbAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
//			{
//				
//				@Override
//				public void onStopTrackingTouch(SeekBar seekBar)
//				{
//					
//				}
//				
//				@Override
//				public void onStartTrackingTouch(SeekBar seekBar)
//				{
//					
//				}
//				
//				@Override
//				public void onProgressChanged(SeekBar seekBar, int progress,
//						boolean fromUser)
//				{
//					etAlpha.setText(progress + "%");
//				}
//			});
//			spinnerRotate.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					rotateIndex = position;
//				}
//				
//			});
//			
//			
//		}
//		
//		@Override
//		public PropertyPicture getConfigedProperty()
//		{
//			try
//			{
//				PropertyPicture p = new PropertyPicture();
//				Bitmap bf = BitmapFactory.decodeFile(filePath);
//				p.pictureWidth = String.valueOf(picWidth);
//				p.pictureHeight = String.valueOf(picHeight);
//				p.materialAlhpa = String
//						.valueOf(sbAlpha.getProgress() * 1.00f / 100);
//				p.reserveAs = String.valueOf(cblimitScale.isChecked() ? 1 : 0);
//				p.materialPlayTimes = etPlayTimes.getText().toString().trim();
//				p.materialMirrorHandstand = String.valueOf(rotateIndex);
//				p.pictureName = new File(filePath).getName();
//				p.fsFilePath = "./" + programName + ".files/" + p.pictureName;
//				return p;
//				
//			}
//			catch (Exception e)
//			{
//				return null;
//			}
//			
//		}
//		
//		@Override
//		public View getRootView()
//		{
//			if (rootView == null)
//			{
//				LayoutInflater inflater = LayoutInflater
//						.from(UploadProgramAcitvity.this);
//				rootView = inflater.inflate(R.layout.upload_program_picture,
//						null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
//			if (TextUtils.isEmpty(etFilePath.getText().toString().trim()))
//			{
//				toast(getString(R.string.please_add_image), 1000);
//				return false;
//			}
//			try
//			{
//				int i = Integer.parseInt(etPlayTimes.getText().toString()
//						.trim());
//				if (i == 0)
//				{
//					throw new NumberFormatException();
//				}
//			}
//			catch (NumberFormatException e)
//			{
//				toast(getResString(R.string.please_input_int), 1000);
//				etPlayTimes.requestFocus();
//				return false;
//			}
//			
//			return true;
//		}
//		
//		@Override
//		public void setSelectedFile(final String filePath)
//		{
//			try
//			{
//				this.filePath = filePath;
//				etFilePath.setText(filePath);
//				new Thread()
//				{
//					public void run()
//					{
//						Bitmap bf = BitmapFactory.decodeFile(filePath);
//						picWidth = bf.getWidth();
//						picHeight = bf.getHeight();
//						picViewHandler.obtainMessage(1).sendToTarget();
//					}
//				}.start();
//				
//				tvPicWH.setText(picWidth + "*" + picHeight);
//				
//			}
//			catch (Exception e)
//			{
//			}
//			
//		}
//		
//	}
//	
//	// //////////////////////////////////////////////////////////////////////
//	/**
//	 * 
//	 * 视频节目
//	 */
//	public class ProgramVideo implements IProgramType
//	{
//		private final static int DECODE_SCUCESS = 0;
//		
//		private final static int DECODE_FAIL = 1;
//		
//		// view
//		private View rootView;
//		
//		private TableLayout tlTable;
//		
//		private EditText etFilePath;
//		
//		private Button btnSelectFile;
//		
//		private TextView tvVedioWH, tvVedioDuring;
//		
//		private SeekBar sbVolume, sbAlpha;
//		
//		private EditText etVolume, etAlpha;
//		
//		private CheckBox cblimitScale;
//		
//		private EditText etPlayTimes;
//		
//		private CustomerSpinner spinnerRotate;
//		
//		// params
//		private String filePath;
//		
//		private int rotateIndex = 0;
//		
//		private int vedioWidth, vedioHeight, vedioDuration;
//		
//		private Handler vedioViewHandler = new Handler()
//		{
//			public void handleMessage(android.os.Message msg)
//			{
//				switch (msg.what)
//				{
//					case DECODE_SCUCESS:
//					{
//						rootView.findViewById(R.id.tv_file_property)
//								.setVisibility(View.VISIBLE);
//						tlTable.setVisibility(View.VISIBLE);
//						tvVedioWH.setText(vedioWidth + "*" + vedioHeight);
//						tvVedioDuring
//								.setText(Tools.formatDuring(vedioDuration));
//					}
//					break;
//					
//					case DECODE_FAIL:
//					{
//						
//					}
//					break;
//				}
//				
//			};
//		};
//		
//		public ProgramVideo()
//		{
//			initMView(getRootView());
//			initMListener();
//			initMData();
//		}
//		
//		private void initMView(View view)
//		{
//			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
//			etFilePath = (EditText) view.findViewById(R.id.et_file_path);
//			btnSelectFile = (Button) view.findViewById(R.id.btn_select_file);
//			tvVedioWH = (TextView) view.findViewById(R.id.tv_video_wh);
//			tvVedioDuring = (TextView) view.findViewById(R.id.tv_vedio_during);
//			sbVolume = (SeekBar) view.findViewById(R.id.sb_vedio_volume);
//			etVolume = (EditText) view.findViewById(R.id.et_vedio_volume);
//			sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
//			etAlpha = (EditText) view.findViewById(R.id.et_file_alpha);
//			cblimitScale = (CheckBox) view.findViewById(R.id.cb_reserve_as);
//			etPlayTimes = (EditText) view.findViewById(R.id.et_play_times);
//			spinnerRotate = (CustomerSpinner) view
//					.findViewById(R.id.spinner_file_rotate);
//		}
//		
//		private void initMListener()
//		{
//			btnSelectFile.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					skipToFileSelectAcitivty(FileType.VIDEO);
//				}
//			});
//			sbAlpha.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
//			{
//				
//				@Override
//				public void onStopTrackingTouch(SeekBar seekBar)
//				{
//					
//				}
//				
//				@Override
//				public void onStartTrackingTouch(SeekBar seekBar)
//				{
//					
//				}
//				
//				@Override
//				public void onProgressChanged(SeekBar seekBar, int progress,
//						boolean fromUser)
//				{
//					etAlpha.setText(progress + "%");
//				}
//			});
//			sbVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
//			{
//				
//				@Override
//				public void onStopTrackingTouch(SeekBar seekBar)
//				{
//					
//				}
//				
//				@Override
//				public void onStartTrackingTouch(SeekBar seekBar)
//				{
//					
//				}
//				
//				@Override
//				public void onProgressChanged(SeekBar seekBar, int progress,
//						boolean fromUser)
//				{
//					etVolume.setText(progress + "%");
//				}
//			});
//			spinnerRotate.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					rotateIndex = position;
//				}
//				
//			});
//		}
//		
//		private void initMData()
//		{
//			etPlayTimes.setText("1");
//			spinnerRotate.initView(R.array.file_rotate);
//			spinnerRotate.setSelection(0, true);
//		}
//		
//		@Override
//		public PropertyVedio getConfigedProperty()
//		{
//			try
//			{
//				PropertyVedio p = new PropertyVedio();
//				p.vedioWidth = String.valueOf(vedioWidth);
//				p.vedioHeight = String.valueOf(vedioHeight);
//				p.showWidth = p.vedioWidth;
//				p.showHeight = p.vedioHeight;
//				p.length = String.valueOf(vedioDuration);
//				p.playLength = ((PropertyVedio) p).length;
//				p.materialDuration = ((PropertyVedio) p).length;
//				p.materialAlhpa = String
//						.valueOf(sbAlpha.getProgress() * 1.00f / 100);
//				p.volume = String.valueOf(sbVolume.getProgress() * 1.00f / 100);
//				p.reserveAs = String.valueOf(cblimitScale.isChecked() ? 1 : 0);
//				p.materialPlayTimes = etPlayTimes.getText().toString().trim();
//				p.materialMirrorHandstand = String.valueOf(rotateIndex);
//				p.vedioName = new File(filePath).getName();
//				p.fsFilePath = "./" + programName + ".files/" + p.vedioName;
//				return p;
//			}
//			catch (Exception e)
//			{
//				return null;
//			}
//			
//		}
//		
//		@Override
//		public View getRootView()
//		{
//			if (rootView == null)
//			{
//				LayoutInflater inflater = LayoutInflater
//						.from(UploadProgramAcitvity.this);
//				rootView = inflater
//						.inflate(R.layout.upload_program_video, null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
//			if (TextUtils.isEmpty(etFilePath.getText().toString().trim()))
//			{
//				toast(getString(R.string.please_add_vedio), 1000);
//				return false;
//			}
//			try
//			{
//				int i = Integer.parseInt(etPlayTimes.getText().toString()
//						.trim());
//				if (i == 0)
//				{
//					throw new NumberFormatException();
//				}
//			}
//			catch (NumberFormatException e)
//			{
//				toast(getResString(R.string.please_input_int), 1000);
//				etPlayTimes.requestFocus();
//				return false;
//			}
//			return true;
//		}
//		
//		@Override
//		public void setSelectedFile(final String filePath)
//		{
//			this.filePath = filePath;
//			
//			etFilePath.setText(filePath);
//			
//			new Thread()
//			{
//				public void run()
//				{
//					try
//					{
//						int vedioWidth=0,vedioHeight=0,vedioDuration=0;
//						//获得视频的元数据， 两种方式解析
//						MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//						retriever.setDataSource(filePath);
//						String width=retriever.extractMetadata(18);
//						String height=retriever.extractMetadata(19);
//						String duration=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//						if(width!=null){
//							vedioWidth = Integer.parseInt(retriever
//									.extractMetadata(18));
//						}
//						if(height!=null){
//							vedioHeight = Integer.parseInt(retriever
//									.extractMetadata(19));
//						}		
//						
//						if(duration!=null){
//							vedioDuration = Integer
//									.parseInt(retriever
//											.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//						}
//						MediaPlayer mediaPlayer = MediaPlayer
//								.create(UploadProgramAcitvity.this,
//										Uri.parse(filePath));
//						if(vedioWidth<=0){
//							vedioWidth = mediaPlayer.getVideoWidth();
//						}
//						if(vedioHeight<=0){
//							vedioHeight = mediaPlayer.getVideoHeight();
//						}
//						if(vedioDuration<=0){
//							vedioDuration = mediaPlayer.getDuration();
//						}
//						
//					}
//					catch (Exception e)
//					{
//						vedioViewHandler.obtainMessage(DECODE_FAIL)
//								.sendToTarget();
//					}
//				}
//			}.start();
//			
//			//
//		}
//		
//	}
//	
//	/**
//	 * 
//	 * 单行文本节目
//	 */
//	public class ProgramSingleLineText implements IProgramType
//	{
//		// view
//		private View rootView;
//		
//		private CustomerSpinner spinnerTextSource;
//		
//		private EditText etFilePath;
//		
//		private Button btnSelectFile;
//		
//		private EditText etInputText;
//		
//		private CheckBox cbMoveLeft, cbHeadTailConn;
//		
//		private CustomerSpinner spinnerFont, spinnerFontSize;
//		
//		private CheckBox cbFontBold, cbFontItalic, cbFontUndeline;
//		
//		private CustomerSpinner spinnerTextColor, spinnerBgColor;
//		
//		// params
//		private String filePath;
//		
//		private int textSourceIndex = 0;
//		
//		private int rotateIndex = 0;
//		
//		private int textColor = -1, bgColor = -1;
//		
//		public ProgramSingleLineText()
//		{
//			try
//			{
//				initMView(getRootView());
//				initMListener();
//				initMData();
//			}
//			catch (Exception e)
//			{
//			}
//			
//		}
//		
//		private void initMView(View view)
//		{
//			spinnerTextSource = (CustomerSpinner) view
//					.findViewById(R.id.spinner_text_source);
//			etFilePath = (EditText) view.findViewById(R.id.et_file_path);
//			btnSelectFile = (Button) view.findViewById(R.id.btn_select_file);
//			
//			etInputText = (EditText) view.findViewById(R.id.et_text_input);
//			
//			cbMoveLeft = (CheckBox) view.findViewById(R.id.cb_left_move);
//			cbHeadTailConn = (CheckBox) view
//					.findViewById(R.id.cb_head_tail_conn);
//			spinnerFont = (CustomerSpinner) view
//					.findViewById(R.id.spinner_text_font);
//			spinnerFontSize = (CustomerSpinner) view
//					.findViewById(R.id.spinner_font_size);
//			cbFontBold = (CheckBox) view.findViewById(R.id.cb_font_bold);
//			cbFontItalic = (CheckBox) view.findViewById(R.id.cb_font_italic);
//			cbFontUndeline = (CheckBox) view
//					.findViewById(R.id.cb_font_underline);
//			
//			spinnerTextColor = (CustomerSpinner) view
//					.findViewById(R.id.spinner_text_color);
//			spinnerBgColor = (CustomerSpinner) view
//					.findViewById(R.id.spinner_back_color);
//		}
//		
//		private void initMListener()
//		{
//			btnSelectFile.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					skipToFileSelectAcitivty(FileType.TXT);
//				}
//			});
//			cbMoveLeft.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView,
//						boolean isChecked)
//				{
//					if (isChecked)
//					{
//						rootView.findViewById(R.id.ll_head_tail_conn)
//								.setVisibility(View.VISIBLE);
//					}
//					else
//					{
//						rootView.findViewById(R.id.ll_head_tail_conn)
//								.setVisibility(View.GONE);
//						cbHeadTailConn.setChecked(false);
//					}
//				}
//			});
//			spinnerTextSource.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					textSourceIndex = position;
//					switch (position)
//					{
//						case 0:
//						{
//							rootView.findViewById(R.id.tr_from_input)
//									.setVisibility(View.VISIBLE);
//							rootView.findViewById(R.id.tr_from_text)
//									.setVisibility(View.GONE);
//						}
//						break;
//						
//						case 1:
//						{
//							rootView.findViewById(R.id.tr_from_input)
//									.setVisibility(View.GONE);
//							rootView.findViewById(R.id.tr_from_text)
//									.setVisibility(View.VISIBLE);
//						}
//						break;
//					}
//				}
//			});
//			spinnerTextColor.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					textColor = textColorArr[position];
//				}
//			});
//			spinnerBgColor.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					bgColor = textColorArr[position];
//				}
//			});
//		}
//		
//		private void initMData()
//		{
//			spinnerTextSource.initView(R.array.single_text_source);
//			spinnerTextSource.setSelection(0, true);
//			
//			spinnerFont.initView(fontFamily);
//			spinnerFont.setSelection(0, true);
//			
//			spinnerFontSize.initView(fontSize);
//			spinnerFontSize.setSelection(6, true);
//			
//			spinnerTextColor.initView(R.array.text_color);
//			spinnerTextColor.setSelection(4, true);
//			
//			spinnerBgColor.initView(R.array.text_color);
//			spinnerBgColor.setSelection(0, true);
//		}
//		
//		@Override
//		public PropertySingleLineText getConfigedProperty()
//		{
//			try
//			{
//				PropertySingleLineText p = new PropertySingleLineText();
//				
//				p.isScroll = String.valueOf(cbMoveLeft.isChecked() ? 1 : 0);
//				// 首尾相连
//				int headTailConn = 0;
//				if (cbMoveLeft.isChecked() && cbHeadTailConn.isChecked())
//				{
//					headTailConn = 1;
//				}
//				p.isHeadConnectTail = String.valueOf(headTailConn);
//				p.logFontlfFaceName = spinnerFont.getText().toString();
//				int fontSize = Integer.parseInt(spinnerFontSize.getText()
//						.toString());
//				p.logFontIfHeight = String.valueOf((int) (97 * fontSize / 72));
//				p.logFontlfWeight = String.valueOf(cbFontBold.isChecked() ? 700
//						: 400);
//				p.logFontlfItalic = String.valueOf(cbFontItalic.isChecked() ? 1
//						: 0);
//				p.logFontlfUnderline = String.valueOf(cbFontUndeline
//						.isChecked() ? 1 : 0);
//				if (textSourceIndex == 0)// 输入
//				{
//					p.isFromFile = "0";
//					p.text = etInputText.getText().toString().trim();
//					p.isFromFile = "0";
//					
//				}
//				else if (textSourceIndex == 1)// txt
//				{
//					p.fsIsIsRelative = "1";
//					p.txtName = new File(filePath).getName();
//					p.fsFilePath = "./" + programName + ".files/" + p.txtName;
//					p.isFromFile = "1";
//				}
//				
//				if (textColor != -1)
//				{
//					p.textColor = "0x"
//							+ Integer.toHexString(textColor).toUpperCase();
//				}
//				if (bgColor != -1)
//				{
//					// p.materialBackColor="0x"+Integer.toHexString(bgColor).toUpperCase();
//					p.pageBgColor = "0x"
//							+ Integer.toHexString(bgColor).toUpperCase();
//				}
//				return p;
//			}
//			catch (Exception e)
//			{
//				return null;
//			}
//			
//		}
//		
//		@Override
//		public View getRootView()
//		{
//			if (rootView == null)
//			{
//				LayoutInflater inflater = LayoutInflater
//						.from(UploadProgramAcitvity.this);
//				rootView = inflater.inflate(
//						R.layout.upload_program_single_line_text, null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
//			if (textSourceIndex == 0
//					&& TextUtils.isEmpty(etInputText.getText().toString()
//							.trim()))// 输入
//			{
//				return false;
//			}
//			if (textSourceIndex == 1
//					&& TextUtils
//							.isEmpty(etFilePath.getText().toString().trim()))
//			{
//				toast(getString(R.string.please_add_txt), 1000);
//				return false;
//			}
//			return true;
//		}
//		
//		@Override
//		public void setSelectedFile(String filePath)
//		{
//			this.filePath = filePath;
//			etFilePath.setText(filePath);
//		}
//		
//	}
//	
//	/**
//	 * 
//	 * 多行文本节目
//	 */
//	public class ProgramMultiLineText implements IProgramType
//	{
//		// view
//		private View rootView;
//		
//		private TableLayout tlTable;
//		
//		private EditText etFilePath;
//		
//		private Button btnSelectFile;
//		
//		private SeekBar sbAlpha;
//		
//		private EditText etAlpha;
//		
//		private CheckBox cbMoveUp, cbCentalAlign, cbAutoUpdate,
//				cbUpdateInterval;
//		
//		private CustomerSpinner spinnerFont, spinnerFontSize;
//		
//		private CheckBox cbFontBold, cbFontItalic, cbFontUndeline;
//		
//		private CustomerSpinner spinnerTextColor, spinnerBgColor;
//		
//		// params
//		private String filePath;
//		
//		private int textSourceIndex = 0;
//		
//		private int rotateIndex = 0;
//		
//		private int textColor = -1, bgColor = -1;
//		
//		public ProgramMultiLineText()
//		{
//			try
//			{
//				initMView(getRootView());
//				initMListener();
//				initMData();
//			}
//			catch (Exception e)
//			{
//				
//			}
//			
//		}
//		
//		private void initMView(View view)
//		{
//			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
//			etFilePath = (EditText) view.findViewById(R.id.et_file_path);
//			btnSelectFile = (Button) view.findViewById(R.id.btn_select_file);
//			
//			// sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
//			// etAlpha = (EditText) view.findViewById(R.id.et_file_alpha);
//			
//			cbAutoUpdate = (CheckBox) view.findViewById(R.id.cb_left_move);
//			cbUpdateInterval = (CheckBox) view
//					.findViewById(R.id.cb_head_tail_conn);
//			
//			cbMoveUp = (CheckBox) view.findViewById(R.id.cb_up_move);
//			cbCentalAlign = (CheckBox) view
//					.findViewById(R.id.cb_centeral_align);
//			
//			spinnerFont = (CustomerSpinner) view
//					.findViewById(R.id.spinner_text_font);
//			spinnerFontSize = (CustomerSpinner) view
//					.findViewById(R.id.spinner_font_size);
//			cbFontBold = (CheckBox) view.findViewById(R.id.cb_font_bold);
//			cbFontItalic = (CheckBox) view.findViewById(R.id.cb_font_italic);
//			cbFontUndeline = (CheckBox) view
//					.findViewById(R.id.cb_font_underline);
//			
//			spinnerTextColor = (CustomerSpinner) view
//					.findViewById(R.id.spinner_text_color);
//			spinnerBgColor = (CustomerSpinner) view
//					.findViewById(R.id.spinner_back_color);
//		}
//		
//		private void initMListener()
//		{
//			btnSelectFile.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v)
//				{
//					skipToFileSelectAcitivty(FileType.TXT);
//				}
//			});
//			
//			cbMoveUp.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView,
//						boolean isChecked)
//				{
//					if (isChecked)
//					{
//						
//						rootView.findViewById(R.id.ll_centeral_align)
//								.setVisibility(View.INVISIBLE);
//						
//					}
//					else
//					{
//						rootView.findViewById(R.id.ll_centeral_align)
//								.setVisibility(View.VISIBLE);
//					}
//				}
//			});
//			spinnerTextColor.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					textColor = textColorArr[position];
//				}
//			});
//			spinnerBgColor.setOnItemClickListener(new OnItemClickListener()
//			{
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					bgColor = textColorArr[position];
//				}
//			});
//		}
//		
//		private void initMData()
//		{
//			
//			spinnerFont.initView(fontFamily);
//			spinnerFont.setSelection(0, true);
//			
//			spinnerFontSize.initView(fontSize);
//			spinnerFontSize.setSelection(6, true);
//			
//			spinnerTextColor.initView(R.array.text_color);
//			spinnerTextColor.setSelection(4, true);
//			
//			spinnerBgColor.initView(R.array.text_color);
//			spinnerBgColor.setSelection(0, true);
//		}
//		
//		@Override
//		public PropertyMultiLineText getConfigedProperty()
//		{
//			try
//			{
//				PropertyMultiLineText p = new PropertyMultiLineText();
//				
//				p.isScroll = String.valueOf(cbMoveUp.isChecked() ? 1 : 0);
//				// 首尾相连
//				int centalAlign = 0;
//				if (cbMoveUp.isChecked() && cbCentalAlign.isChecked())
//				{
//					centalAlign = 1;
//				}
//				p.centeralAlign = String.valueOf(centalAlign);
//				p.logFontlfFaceName = spinnerFont.getText().toString();
//				int fontSize = Integer.parseInt(spinnerFontSize.getText()
//						.toString());
//				p.logFontIfHeight = String.valueOf((int) (97 * fontSize / 72));
//				p.logFontlfWeight = String.valueOf(cbFontBold.isChecked() ? 700
//						: 400);
//				p.logFontlfItalic = String.valueOf(cbFontItalic.isChecked() ? 1
//						: 0);
//				p.logFontlfUnderline = String.valueOf(cbFontUndeline
//						.isChecked() ? 1 : 0);
//				p.fsIsIsRelative = "1";
//				p.fileName = new File(filePath).getName();
//				p.fsFilePath = "./" + programName + ".files/" + p.fileName;
//				if (textColor != -1)
//				{
//					p.textColor = "0x"
//							+ Integer.toHexString(textColor).toUpperCase();
//				}
//				if (bgColor != -1)
//				{
//					// p.materialBackColor="0x"+Integer.toHexString(bgColor).toUpperCase();
//					p.pageBgColor = "0x"
//							+ Integer.toHexString(bgColor).toUpperCase();
//				}
//				return p;
//			}
//			catch (Exception e)
//			{
//				return null;
//			}
//			
//		}
//		
//		@Override
//		public View getRootView()
//		{
//			if (rootView == null)
//			{
//				LayoutInflater inflater = LayoutInflater
//						.from(UploadProgramAcitvity.this);
//				rootView = inflater.inflate(
//						R.layout.upload_program_multi_line_text, null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
//			if (TextUtils.isEmpty(etFilePath.getText().toString().trim()))// 输入
//			{
//				toast(getString(R.string.please_add_txt), 1000);
//				return false;
//			}
//			return true;
//		}
//		
//		@Override
//		public void setSelectedFile(String filePath)
//		{
//			this.filePath = filePath;
//			rootView.findViewById(R.id.tv_file_property).setVisibility(
//					View.VISIBLE);
//			tlTable.setVisibility(View.VISIBLE);
//			etFilePath.setText(filePath);
//		}
//		
//	}
//	
//	/**
//	 * 节目类型接口
//	 */
//	private interface IProgramType
//	{
//		
//		PropertyItem getConfigedProperty();
//		
//		View getRootView();
//		
//		boolean submitFilter();
//		
//		void setSelectedFile(String filePath);
//		
//	}
//}
