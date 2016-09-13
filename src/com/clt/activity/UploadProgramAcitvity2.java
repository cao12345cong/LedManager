//package com.clt.activity;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.MediaMetadataRetriever;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
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
//import com.clt.activity.UploadProgramAcitvity2.ProgramItem;
//import com.clt.entity.FileSortModel;
//import com.clt.ledmanager.R;
//import com.clt.ui.CustomerSpinner;
//import com.clt.ui.TitleBarView;
//import com.clt.ui.TitleBarView.TitleBarListener;
//import com.clt.upload.PropertyCommon;
//import com.clt.upload.PropertyItem;
//import com.clt.upload.PropertyMultiLineText;
//import com.clt.upload.PropertyPicture;
//import com.clt.upload.PropertySingleLineText;
//import com.clt.upload.PropertyVedio;
//import com.clt.upload.UploadProgram;
//import com.clt.upload.UploadTask;
//import com.clt.upload.VsnFileFactory;
//import com.clt.upload.VsnFileFactoryImpl;
//import com.clt.util.Const;
//import com.clt.util.Tools;
//
///**
// * 上传节目
// */
//public class UploadProgramAcitvity2 extends BaseActivity
//{
//	
//	// 字体
//	public static final String[] fontFamily = { "宋体", "黑体", "楷体", "隶书", "仿宋" };
//	
//	// 字体大小
//	public static final String[] fontSize = { "8", "9", "10", "11", "12", "14",
//			"16", "18", "20", "22", "24", "26", "28", "36", "48", "72", "128" };
//	
//	// 文字颜色
//	public static final int[] textColorArr = { Color.BLACK, Color.DKGRAY,
//			Color.GRAY, Color.LTGRAY, Color.WHITE, Color.RED, Color.GREEN,
//			Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
//	
//	};
//	
//	/**
//	 * UI控件
//	 */
//	
//	// 标题栏
//	private TitleBarView titleBarView;// 标题栏
//	
//	// 节目属性
//	private EditText etProgramName;// 节目名
//	
//	// 窗口属性
//	private EditText etStartX, etStartY, etWidth, etHeight;
//	
//	// 添加素材
//	private CustomerSpinner spinnerFileNameList;
//	
//	private int currentSelectedIndex;
//	
//	private String[] arrFileNames;
//	
//	private Button btnAddFile;// 添加按钮
//	
//	private LinearLayout llInclude;
//	
//	private IProgramType programType;
//	
//	private Button btnCreateAndUpload; // 上传按钮
//	
//	/**
//	 * 变量
//	 */
//	
//	private String programName;// 节目名
//	
//	private View cachePicView, cacheVedioView, cacheTxtVideo;
//	
//	// 节目素材
//	public class ProgramItem
//	{
//		public String fileName;
//		
//		public String filePath;
//		
//		public int fileType;
//		
//		public PropertyItem property;
//	}
//	
//	// 素材列表
//	private ArrayList<ProgramItem> mFileItemList;
//	
//	private AsyncGetFilePropertyTask asyncGetFilePropertyTask;
//	//全局属性
//	private PropertyCommon propertyCommon;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		try
//		{
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.upload_program_2);
//			init();
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
//	private void init()
//	{
//		mFileItemList = new ArrayList<ProgramItem>();
//		currentSelectedIndex = -1;
//		
//	}
//	
//	private void initView()
//	{
//		// 标题
//		titleBarView = (TitleBarView) findViewById(R.id.titlebar);
//		// 节目名
//		etProgramName = (EditText) findViewById(R.id.et_program_name);
//		// 窗口属性
//		etStartX = (EditText) findViewById(R.id.et_start_x);
//		etStartY = (EditText) findViewById(R.id.et_start_y);
//		etWidth = (EditText) findViewById(R.id.et_program_width);
//		etHeight = (EditText) findViewById(R.id.et_program_height);
//		// 上传按钮
//		btnCreateAndUpload = (Button) findViewById(R.id.btn_creat_upload);
//		llInclude = (LinearLayout) findViewById(R.id.ll_include);
//		// 添加文件
//		spinnerFileNameList = (CustomerSpinner) findViewById(R.id.spinner_file_name);
//		btnAddFile = (Button) findViewById(R.id.btn_select_file);
//	}
//	
//	private void initData()
//	{
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
//		// 标题
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
//		 * 素材列表
//		 */
//		spinnerFileNameList.setOnItemClickListener(new OnItemClickListener()
//		{
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id)
//			{
//				llInclude.removeAllViews();
//				currentSelectedIndex = position;
//				
//				switch (mFileItemList.get(position).fileType)
//				{
//					case Const.PICTURE:
//					{
//						programType = new ProgramPicture();
//						llInclude.addView(programType.getRootView());
//					}
//					break;
//					case Const.VIDEO:
//					{
//						programType = new ProgramVideo();
//						llInclude.addView(programType.getRootView());
//					}
//					break;
//					case Const.TXT:
//					{
//						programType = new ProgramMultiLineText();
//						llInclude.addView(programType.getRootView());
//					}
//					break;
//				}
//				programType.setSelectedFile(mFileItemList.get(position).filePath);
//			}
//			
//		});
//		/**
//		 * 添加文件
//		 */
//		btnAddFile.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				skipToFileSelectAcitivty(0);
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
////					if (programType == null || !programType.submitFilter())
////					{
////						return;
////					}
//					UploadProgram uploadProgram = createUploadProgram();
//					if (uploadProgram == null)
//					{
//						return;
//					}
//					Intent intent = new Intent(UploadProgramAcitvity2.this,
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
////			if(asyncGetFilePropertyTask==null||!asyncGetFilePropertyTask.isFinish)
////			{
////				return null;
////			}
//			int width = Integer.parseInt(etWidth.getText().toString().trim());
//			int height = Integer.parseInt(etHeight.getText().toString().trim());
//			int startX = Integer.parseInt(etStartX.getText().toString().trim());
//			int startY = Integer.parseInt(etStartY.getText().toString().trim());
//			this.programName = etProgramName.getText().toString().trim();
//			
//			//共有属性
//			propertyCommon.infoWidth = String.valueOf(width);
//			propertyCommon.infoHeight = String.valueOf(height);
//			propertyCommon.rectWidth = propertyCommon.infoWidth;
//			propertyCommon.rectHeight = propertyCommon.infoHeight;
//			propertyCommon.rectX = String.valueOf(startX);
//			propertyCommon.rectY = String.valueOf(startY);
//			propertyCommon.programName = programName;
//			
//			
//			ProgramItem programItem = null;
//			PropertyItem p=null;
//			//配置素材属性
//			for (int i = 0; i < mFileItemList.size(); i++)
//			{
//				programItem = mFileItemList.get(i);
//				p=programItem.property;
//				
//				switch (programItem.fileType)
//				{
//					case Const.PICTURE:
//					{
//						((PropertyPicture) p).pictureName = new File(
//								programItem.filePath).getName();
//						((PropertyPicture) p).fsFilePath = "./" +  this.programName
//								+ ".files/" + ((PropertyPicture) p).pictureName;
//						p.materialName=((PropertyPicture) p).pictureName;
//					}
//					break;
//					case Const.VIDEO:
//					{
//						((PropertyVedio) p).vedioName=new File(programItem.filePath).getName();
//						((PropertyVedio) p).fsFilePath="./" + this.programName + ".files/" + ((PropertyVedio) p).vedioName;
//						p.materialName=((PropertyVedio) p).vedioName;
//					}
//					break;
//					case Const.TXT:
//					{
//						((PropertyMultiLineText) p).fileName = new File(programItem.filePath).getName();
//						((PropertyMultiLineText) p).fsFilePath="./" +this.programName + ".files/" + ((PropertyMultiLineText) p).fileName;
//						p.materialName=((PropertyMultiLineText) p).fileName;
//					}
//					break;
//				}
//				
//			}
//			
//			UploadProgram uploadProgram = new UploadProgram();
//			// 创建vsn文件
//			VsnFileFactory factory = new VsnFileFactoryImpl();
//			String vsnPath = "/mnt/sdcard/" + this.programName + ".vsn";
//			
//			ArrayList<PropertyItem> ptlist=new ArrayList<PropertyItem>();
//			for (int i = 0; i < mFileItemList.size(); i++)
//			{
//				ptlist.add(mFileItemList.get(i).property);
//			}
//			
//			File vsnFile = factory.createVsnFile(propertyCommon,ptlist, vsnPath);
//			if (vsnFile == null)
//			{
//				return null;
//			}
//			uploadProgram.setVsnFileTask(new UploadTask(vsnFile, "/program/"));
//			// 构建上传节目
//			String remoteDir = "/program/" + this.programName + ".files/";
//			uploadProgram.setRemoteDirtory(remoteDir);
//			
//			String filePath=null;
//			String fileName=null;
//			for (int i = 0; i < mFileItemList.size(); i++)
//			{
//				filePath=mFileItemList.get(i).filePath;
//				fileName=mFileItemList.get(i).fileName;
//				uploadProgram.getFileTaskList().add(new UploadTask(new File(filePath), remoteDir+fileName));
//			}
//			return uploadProgram;
//		}
//		catch (Exception e)
//		{
//			return null;
//		}
//		
//		
//	}
//	
//	/**
//	 * 选择了节目素材之后回调
//	 * 
//	 * @param programItems
//	 */
//	public void onSelectProgramItems(ArrayList<ProgramItem> programItems)
//	{
//		if (programItems == null || programItems.size() == 0)
//		{
//			return;
//		}
//		
//		propertyCommon=new PropertyCommon();
//		
//		// mFileItemList=programItems;
//		mFileItemList.clear();
//		mFileItemList=new ArrayList<UploadProgramAcitvity2.ProgramItem>();
//		mFileItemList.addAll(programItems);
//		
//		int size = mFileItemList.size();
//		arrFileNames = new String[size];
//		for (int i = 0; i < size; i++)
//		{
//			arrFileNames[i] = mFileItemList.get(i).fileName;
//		}
//		spinnerFileNameList.initView(arrFileNames);
//		spinnerFileNameList.setSelection(0, true);
//		
//		// 起线程获取文件的参数
////		asyncGetFilePropertyTask = new AsyncGetFilePropertyTask(
////				mFileItemList);
////		asyncGetFilePropertyTask.execute();
//		getFilesProperty(mFileItemList);
//		
//		llInclude.removeAllViews();
//		switch (mFileItemList.get(0).fileType)
//		{
//			case Const.PICTURE:
//			{
//				programType = new ProgramPicture();
//				llInclude.addView(programType.getRootView());
//			}
//			break;
//			case Const.VIDEO:
//			{
//				programType = new ProgramVideo();
//				llInclude.addView(programType.getRootView());
//			}
//			break;
//			case Const.TXT:
//			{
//				programType = new ProgramMultiLineText();
//				llInclude.addView(programType.getRootView());
//			}
//			break;
//		}
//		programType.setSelectedFile(mFileItemList.get(0).filePath);
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
//				// 一个或多个
//				ArrayList<FileSortModel> list = (ArrayList<FileSortModel>) data.getSerializableExtra("selectedFiles");
////				String filePath = data.getStringExtra("FilePath");
////				int fileType = data.getIntExtra("FileType", Const.PICTURE);
////				// 属性
////				PropertyItem p = getFilePropertyByType(fileType);
////				
////				ProgramItem pt = new ProgramItem();
////				pt.fileName = fileName;
////				pt.filePath = filePath;
////				pt.fileType = fileType;
////				pt.property = p;
//				
//				ArrayList<ProgramItem> selectFiles = new ArrayList<ProgramItem>();
//				ProgramItem pt=null;
//				FileSortModel fsm=null;
//				for (int i = 0; i < list.size(); i++)
//				{
//					pt = new ProgramItem();
//					fsm=list.get(i);
//					pt.fileName=fsm.getFileName();
//					pt.filePath=fsm.getFilePath();
//					pt.fileType=fsm.getFileType();
//					switch (pt.fileType)
//					{
//						case Const.PICTURE:
//							pt.property=new PropertyPicture();
//							programType=new ProgramPicture();
//							llInclude.addView(programType.getRootView());
//						break;
//							
//						case Const.VIDEO:
//							pt.property=new PropertyVedio();
//							programType=new ProgramVideo();
//							llInclude.addView(programType.getRootView());
//						break;
//						case Const.TXT:
//							pt.property=new PropertyMultiLineText();
//							programType=new ProgramMultiLineText();
//							llInclude.addView(programType.getRootView());
//						break;
//					}
//					selectFiles.add(pt);
//				}
//				onSelectProgramItems(selectFiles);
//				
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
//	 * 异步获取文件的属性
//	 */
//	private class AsyncGetFilePropertyTask extends
//			AsyncTask<Object, Object, Object>
//	{
//		private boolean isFinish;
//		
//		private ArrayList<ProgramItem> selectFiles;
//		
//		public AsyncGetFilePropertyTask(ArrayList<ProgramItem> selectFiles)
//		{
//			this.selectFiles = selectFiles;
//			isFinish = false;
//		}
//		
//		@Override
//		protected void onPostExecute(Object result)
//		{
//			super.onPostExecute(result);
//			isFinish = true;
//		}
//		
//		public boolean isFinish()
//		{
//			return isFinish;
//		}
//
//		@Override
//		protected Object doInBackground(Object... params)
//		{
//			getFilesProperty(selectFiles);
//			return null;
//		}
//
//		
//		
//	}
//	/**
//	 * 获得文件的属性
//	 */
//	private void getFilesProperty(ArrayList<ProgramItem> selectFiles)
//	{
//		int size = selectFiles.size();
//		int type = -1;
//		ProgramItem pt = null;
//		for (int i = 0; i < size; i++)
//		{
//			pt = selectFiles.get(i);
//			switch (pt.fileType)
//			{
//				case Const.PICTURE:
//					getImageProperty((PropertyPicture) pt.property,
//							pt.filePath);
//				break;
//				case Const.VIDEO:
//					getVedioProperty((PropertyVedio) pt.property,
//							pt.filePath);
//				break;
//			}
//		}
//	}
//	private void getImageProperty(PropertyPicture p, String filePath)
//	{
//		try
//		{
//			Bitmap bf = BitmapFactory.decodeFile(filePath);
//			p.pictureWidth = String.valueOf(bf.getWidth());
//			p.pictureHeight = String.valueOf(bf.getHeight());
//		}
//		catch (Exception e)
//		{
//			return;
//		}
//	}
//	
//	private void getVedioProperty(PropertyVedio p, String filePath)
//	{
//		try
//		{
//			int vedioWidth = 0, vedioHeight = 0, vedioDuration = 0;
//			// 获得视频的元数据， 两种方式解析
//			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//			retriever.setDataSource(filePath);
//			String width = retriever.extractMetadata(18);
//			String height = retriever.extractMetadata(19);
//			String duration = retriever
//					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//			if (width != null)
//			{
//				vedioWidth = Integer.parseInt(retriever.extractMetadata(18));
//			}
//			if (height != null)
//			{
//				vedioHeight = Integer.parseInt(retriever.extractMetadata(19));
//			}
//			
//			if (duration != null)
//			{
//				vedioDuration = Integer
//						.parseInt(retriever
//								.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//			}
//			MediaPlayer mediaPlayer = MediaPlayer.create(this,
//					Uri.parse(filePath));
//			
//			if (vedioWidth <= 0)
//			{
//				vedioWidth = mediaPlayer.getVideoWidth();
//			}
//			if (vedioHeight <= 0)
//			{
//				vedioHeight = mediaPlayer.getVideoHeight();
//			}
//			if (vedioDuration <= 0)
//			{
//				vedioDuration = mediaPlayer.getDuration();
//			}
//			p.vedioWidth = String.valueOf(vedioWidth);
//			p.vedioHeight = String.valueOf(vedioHeight);
//			p.showWidth = p.vedioWidth;
//			p.showHeight = p.vedioHeight;
//			p.length = String.valueOf(vedioDuration);
//			p.playLength = ((PropertyVedio) p).length;
//			p.materialDuration = ((PropertyVedio) p).length;
//		}
//		catch (Exception e)
//		{
//		}
//	}
//	
//	/**
//	 * 根据文件类型获得不同的参数
//	 * 
//	 * @param fileType
//	 * @return
//	 */
//	public PropertyItem getFilePropertyByType(int fileType)
//	{
//		if (fileType == Const.PICTURE)
//		{
//			return new PropertyPicture();
//			
//		}
//		else if (fileType == Const.VIDEO)
//		{
//			return new PropertyVedio();
//		}
//		else if (fileType == Const.TXT)
//		{
//			return new PropertyMultiLineText();
//		}
//		return null;
//	}
//	
//	/**
//	 * 跳转到选择文件的页面
//	 */
//	public void skipToFileSelectAcitivty(int fileType)
//	{
//		try
//		{
//			Intent intent = new Intent(UploadProgramAcitvity2.this,
//					FilesViewActivity.class);
//			intent.putExtra("fileType", fileType);
//			UploadProgramAcitvity2.this.startActivityForResult(intent, 0);
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
//		private TextView tvPicWH;
//		
//		private SeekBar sbAlpha;
//		
//		private EditText etAlpha;
//		
//		private CheckBox cblimitScale;
//		
//		private EditText etDuring;
//		
//		
//		// params
//		private String filePath;
//		
//		
//		private int picWidth, picHeight;
//		
//		private PropertyPicture propertyPicture;
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
//			ProgramItem fileItem = mFileItemList.get(currentSelectedIndex);
//			propertyPicture=(PropertyPicture) fileItem.property;
//			initMView(getRootView());
//			initMListener();
//			initMData();
//		}
//		
//		private void initMView(View view)
//		{
//			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
//			tvPicWH = (TextView) view.findViewById(R.id.tv_pic_wh);
//			sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
//			etAlpha = (EditText) view.findViewById(R.id.et_file_alpha);
//			cblimitScale = (CheckBox) view.findViewById(R.id.cb_reserve_as);
//			etDuring = (EditText) view.findViewById(R.id.et_during);
//		}
//		
//		private void initMData()
//		{
//			etDuring.setText("5");
//		}
//		
//		private void initMListener()
//		{
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
//					//etAlpha.setText(progress + "%");
//					propertyPicture.materialAlhpa = String
//							.valueOf(sbAlpha.getProgress() * 1.00f / 100);
//				}
//			});
//			cblimitScale.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//				{
//					propertyPicture.reserveAs = String.valueOf(isChecked ? 1 : 0);
//				}
//			});
//			etDuring.addTextChangedListener(new TextWatcher()
//			{
//				
//				@Override
//				public void onTextChanged(CharSequence s, int start, int before, int count)
//				{
//					
//				}
//				
//				@Override
//				public void beforeTextChanged(CharSequence s, int start, int count,
//						int after)
//				{
//					propertyPicture.materialDuration=s.toString()+"000";
//				}
//				
//				@Override
//				public void afterTextChanged(Editable s)
//				{
//					
//				}
//			});
//			
//		}
//		
//		public PropertyPicture getConfigedProperty()
//		{
//			try
//			{
//				PropertyPicture p = new PropertyPicture();
////				Bitmap bf = BitmapFactory.decodeFile(filePath);
////				p.pictureWidth = String.valueOf(picWidth);
////				p.pictureHeight = String.valueOf(picHeight);
////				p.materialAlhpa = String
////						.valueOf(sbAlpha.getProgress() * 1.00f / 100);
////				p.reserveAs = String.valueOf(cblimitScale.isChecked() ? 1 : 0);
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
//						.from(UploadProgramAcitvity2.this);
//				rootView = inflater.inflate(R.layout.upload_program_picture,
//						null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
//			try
//			{
//				int i = Integer.parseInt(etDuring.getText().toString()
//						.trim());
//				if (i == 0)
//				{
//					throw new NumberFormatException();
//				}
//			}
//			catch (NumberFormatException e)
//			{
//				toast(getResString(R.string.please_input_int), 1000);
//				etDuring.requestFocus();
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
//		private TextView tvVedioWH, tvVedioDuring;
//		
//		private SeekBar sbVolume, sbAlpha;
//		
//		private EditText etVolume, etAlpha;
//		
//		private CheckBox cblimitScale;
//		
//		// params
//		private String filePath;
//		
//		private int vedioWidth, vedioHeight, vedioDuration;
//		
//		private PropertyVedio propertyVedio;
//		
//		private Handler vedioViewHandler = new Handler()
//		{
//			public void handleMessage(android.os.Message msg)
//			{
//				switch (msg.what)
//				{
//					case DECODE_SCUCESS:
//					{
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
//			ProgramItem fileItem = mFileItemList.get(currentSelectedIndex);
//			propertyVedio=(PropertyVedio) fileItem.property;
//			initMView(getRootView());
//			initMListener();
//			initMData();
//		}
//		
//		private void initMView(View view)
//		{
//			tlTable = (TableLayout) view.findViewById(R.id.tl_table);
//			tvVedioWH = (TextView) view.findViewById(R.id.tv_video_wh);
//			tvVedioDuring = (TextView) view.findViewById(R.id.tv_vedio_during);
//			sbVolume = (SeekBar) view.findViewById(R.id.sb_vedio_volume);
//			etVolume = (EditText) view.findViewById(R.id.et_vedio_volume);
//			sbAlpha = (SeekBar) view.findViewById(R.id.sb_file_alpha);
//			etAlpha = (EditText) view.findViewById(R.id.et_file_alpha);
//			cblimitScale = (CheckBox) view.findViewById(R.id.cb_reserve_as);
//		}
//		
//		private void initMListener()
//		{
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
//					//etAlpha.setText(progress + "%");
//					propertyVedio.materialAlhpa = String
//							.valueOf(progress * 1.00f / 100);
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
//					//etVolume.setText(progress + "%");
//					propertyVedio.volume = String.valueOf(progress * 1.00f / 100);
//				}
//			});
//			cblimitScale.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//				{
//					propertyVedio.reserveAs = String.valueOf(isChecked ? 1 : 0);	
//				}
//			});
//		}
//		
//		private void initMData()
//		{
//		}
//		
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
//				
//				p.materialAlhpa = String
//						.valueOf(sbAlpha.getProgress() * 1.00f / 100);
//				p.volume = String.valueOf(sbVolume.getProgress() * 1.00f / 100);
//				p.reserveAs = String.valueOf(cblimitScale.isChecked() ? 1 : 0);
////				p.materialPlayTimes = etPlayTimes.getText().toString().trim();
////				p.materialMirrorHandstand = String.valueOf(rotateIndex);
////				p.vedioName = new File(filePath).getName();
////				p.fsFilePath = "./" + programName + ".files/" + p.vedioName;
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
//						.from(UploadProgramAcitvity2.this);
//				rootView = inflater
//						.inflate(R.layout.upload_program_video, null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
////			try
////			{
////				int i = Integer.parseInt(etPlayTimes.getText().toString()
////						.trim());
////				if (i == 0)
////				{
////					throw new NumberFormatException();
////				}
////			}
////			catch (NumberFormatException e)
////			{
////				toast(getResString(R.string.please_input_int), 1000);
////				etPlayTimes.requestFocus();
////				return false;
////			}
//			return true;
//		}
//		
//		@Override
//		public void setSelectedFile(final String filePath)
//		{
//			this.filePath = filePath;
//			
//			new Thread()
//			{
//				public void run()
//				{
//					try
//					{
//						// 获得视频的元数据， 两种方式解析
//						MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//						retriever.setDataSource(filePath);
//						String width = retriever.extractMetadata(18);
//						String height = retriever.extractMetadata(19);
//						String duration = retriever
//								.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//						if (width != null)
//						{
//							vedioWidth = Integer.parseInt(retriever
//									.extractMetadata(18));
//						}
//						if (height != null)
//						{
//							vedioHeight = Integer.parseInt(retriever
//									.extractMetadata(19));
//						}
//						
//						if (duration != null)
//						{
//							vedioDuration = Integer
//									.parseInt(retriever
//											.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
//						}
//						MediaPlayer mediaPlayer = MediaPlayer.create(
//								UploadProgramAcitvity2.this,
//								Uri.parse(filePath));
//						if (vedioWidth <= 0)
//						{
//							vedioWidth = mediaPlayer.getVideoWidth();
//						}
//						if (vedioHeight <= 0)
//						{
//							vedioHeight = mediaPlayer.getVideoHeight();
//						}
//						if (vedioDuration <= 0)
//						{
//							vedioDuration = mediaPlayer.getDuration();
//						}
//						vedioViewHandler.obtainMessage(DECODE_SCUCESS).sendToTarget();
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
//					propertyCommon.pageBgColor = "0x"
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
//						.from(UploadProgramAcitvity2.this);
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
//			return true;
//		}
//		
//		@Override
//		public void setSelectedFile(String filePath)
//		{
//			this.filePath = filePath;
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
//		private EditText etDuring;
//		// params
//		private String filePath;
//		
//		private int textSourceIndex = 0;
//		
//		private int textColor = -1, bgColor = -1;
//		
//		private PropertyMultiLineText propertyMultiLineText;
//		
//		public ProgramMultiLineText()
//		{
//			try
//			{
//				ProgramItem fileItem = mFileItemList.get(currentSelectedIndex);
//				propertyMultiLineText=(PropertyMultiLineText) fileItem.property;
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
//			etDuring=(EditText) view.findViewById(R.id.et_during);
//		}
//		
//		private void initMListener()
//		{
//			
//			cbMoveUp.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView,
//						boolean isChecked)
//				{
//					propertyMultiLineText.isScroll = String.valueOf(isChecked? 1 : 0);
//					// 首尾相连
//					int centalAlign = 0;
//					
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
//			cbCentalAlign.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//				{
//					int centalAlign = 0;
//					if (cbMoveUp.isChecked() && isChecked)
//					{
//						centalAlign = 1;
//					}
//					propertyMultiLineText.centeralAlign = String.valueOf(centalAlign);
//					
//					
//				}
//			});
//			spinnerFont.setOnItemClickListener(new OnItemClickListener()
//			{
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					propertyMultiLineText.logFontlfFaceName = fontFamily[position].toString();
//				}
//				
//			});
//			spinnerFontSize.setOnItemClickListener(new OnItemClickListener()
//			{
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id)
//				{
//					int fs = Integer.parseInt(fontSize[position]);
//					propertyMultiLineText.logFontIfHeight = String.valueOf((int) (97 * fs / 72));
//				}
//			});
//			cbFontBold.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//				{
//					propertyMultiLineText.logFontlfWeight = String.valueOf(isChecked ? 700: 400);
//								
//				}
//			});
//			cbFontItalic.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//				{
//					propertyMultiLineText.logFontlfItalic = String.valueOf(isChecked ? 1: 0);
//											
//				}
//			});
//			cbFontUndeline.setOnCheckedChangeListener(new OnCheckedChangeListener()
//			{
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//				{
//					propertyMultiLineText.logFontlfUnderline = String.valueOf(isChecked? 1 : 0);	
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
//					propertyMultiLineText.textColor = "0x"
//							+ Integer.toHexString(textColor).toUpperCase();
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
//					propertyCommon.pageBgColor = "0x"
//							+ Integer.toHexString(bgColor).toUpperCase();
//				}
//			});
//			etDuring.addTextChangedListener(new TextWatcher()
//			{
//				
//				@Override
//				public void onTextChanged(CharSequence s, int start, int before, int count)
//				{
//					
//				}
//				
//				@Override
//				public void beforeTextChanged(CharSequence s, int start, int count,
//						int after)
//				{
//					
//				}
//				
//				@Override
//				public void afterTextChanged(Editable s)
//				{
//					propertyMultiLineText.materialDuration=s.toString();
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
//				
//				
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
//					propertyCommon.pageBgColor = "0x"
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
//						.from(UploadProgramAcitvity2.this);
//				rootView = inflater.inflate(
//						R.layout.upload_program_multi_line_text, null);
//			}
//			return rootView;
//		}
//		
//		@Override
//		public boolean submitFilter()
//		{
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
//		//PropertyItem getConfigedProperty();
//		
//		View getRootView();
//		
//		boolean submitFilter();
//		
//		void setSelectedFile(String filePath);
//		
//	}
//	
//}
