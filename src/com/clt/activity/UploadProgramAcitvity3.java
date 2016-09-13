package com.clt.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;

import com.clt.activity.UploadProgramAcitvity3.ProgramItem;
import com.clt.adapter.ExpandableListAdapter;
import com.clt.entity.FileSortModel;
import com.clt.ledmanager.R;
import com.clt.ui.CustomerSpinner;
import com.clt.ui.TitleBarView;
import com.clt.ui.TitleBarView.TitleBarListener;
import com.clt.upload.PropertyCommon;
import com.clt.upload.PropertyItem;
import com.clt.upload.PropertyMultiLineText;
import com.clt.upload.PropertyPicture;
import com.clt.upload.PropertySingleLineText;
import com.clt.upload.PropertyVedio;
import com.clt.upload.UploadProgram;
import com.clt.upload.UploadTask;
import com.clt.upload.VsnFileFactory;
import com.clt.upload.VsnFileFactoryImpl;
import com.clt.util.Const;
import com.clt.util.SharedPreferenceUtil;
import com.clt.util.Tools;
import com.clt.util.SharedPreferenceUtil.ShareKey;

/**
 * 上传节目
 */
public class UploadProgramAcitvity3 extends BaseActivity
{
	
	// 字体
	public static final String[] fontFamily = { "宋体", "黑体", "楷体", "隶书", "仿宋" };
	
	// 字体大小
	public static final String[] fontSize = { "8", "9", "10", "11", "12", "14",
			"16", "18", "20", "22", "24", "26", "28", "36", "48", "72", "128" };
	
	// 文字颜色
	public static final int[] textColorArr = { Color.BLACK, Color.DKGRAY,
			Color.GRAY, Color.LTGRAY, Color.WHITE, Color.RED, Color.GREEN,
			Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
	
	};
	
	/**
	 * UI控件
	 */
	
	// 标题栏
	private TitleBarView titleBarView;// 标题栏
	
	// 节目属性
	private EditText etProgramName;// 节目名
	
	// 窗口属性
	private EditText etStartX, etStartY, etWidth, etHeight;
	
	// 添加素材
	private ExpandableListView elvItemList;
	
	private ExpandableListAdapter adapter;
	
	private int currentSelectedIndex;
	
	private String[] arrFileNames;
	
	private Button btnAddFile;// 添加按钮
	
	private LinearLayout llInclude;
	
	private Button btnCreateAndUpload; // 上传按钮
	
	/**
	 * 变量
	 */
	
	private String programName;// 节目名
	
	private View cachePicView, cacheVedioView, cacheTxtVideo;
	
	// 节目素材
	public class ProgramItem
	{
		public String fileName;
		
		public String filePath;
		
		public int fileType;
		
		public PropertyItem property;
	}
	
	// 素材列表
	private ArrayList<ProgramItem> mFileItemList;
	
	private AsyncGetFilePropertyTask asyncGetFilePropertyTask;
	//全局属性
	private PropertyCommon propertyCommon;
	
	private SharedPreferenceUtil sharedPreferenceUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.upload_expandable_list);
			init();
			initView();
			initListener();
			initData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void init()
	{
		mFileItemList = new ArrayList<ProgramItem>();
		currentSelectedIndex = -1;
		
	}
	
	private void initView()
	{
		LayoutInflater inflater=LayoutInflater.from(this);
		View headView=inflater.inflate(R.layout.upload_program_head,null);
		View footView=inflater.inflate(R.layout.upload_program_foot,null);
		// 标题
		titleBarView = (TitleBarView) findViewById(R.id.titlebar);
		// 节目名
		etProgramName = (EditText) headView.findViewById(R.id.et_program_name);
		// 窗口属性
		etStartX = (EditText) headView.findViewById(R.id.et_start_x);
		etStartY = (EditText) headView.findViewById(R.id.et_start_y);
		etWidth = (EditText) headView.findViewById(R.id.et_program_width);
		etHeight = (EditText) headView.findViewById(R.id.et_program_height);
		
		//llInclude = (LinearLayout) findViewById(R.id.ll_include);
		// 添加文件
		//spinnerFileNameList = (CustomerSpinner) findViewById(R.id.spinner_file_name);
		btnAddFile = (Button) headView.findViewById(R.id.btn_select_file);
		// 上传按钮
		btnCreateAndUpload = (Button) footView.findViewById(R.id.btn_creat_upload);
		
		elvItemList=(ExpandableListView) findViewById(R.id.elv_files);
		elvItemList.addHeaderView(headView);
		elvItemList.addFooterView(footView);
		elvItemList.setHeaderDividersEnabled(false);
		
		adapter=new ExpandableListAdapter(this,mFileItemList);
		
		elvItemList.setAdapter(adapter);
	}
	
	private void initData()
	{
		etStartX.setText("0");
		etStartY.setText("0");
		sharedPreferenceUtil=SharedPreferenceUtil.getInstance(this, null);
		etWidth.setText(sharedPreferenceUtil.getString(ShareKey.KEY_WIN_WIDTH, "128"));
		etHeight.setText(sharedPreferenceUtil.getString(ShareKey.KEY_WIN_HEIGHT, "128"));
	}
	
	/**
	 * 
	 */
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
		etWidth.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
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
				String w=s.toString().trim();
				if(!TextUtils.isEmpty(w))
				{
					try
					{
						int num=Integer.parseInt(w);
						sharedPreferenceUtil.putString(ShareKey.KEY_WIN_WIDTH, w);
					}
					catch (Exception e)
					{
					}
				}
			}
		});
		etHeight.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				String h=s.toString().trim();
				if(!TextUtils.isEmpty(h))
				{
					try
					{
						int num=Integer.parseInt(h);
						sharedPreferenceUtil.putString(ShareKey.KEY_WIN_HEIGHT, h);
					}
					catch (Exception e)
					{
					}
				}
			}
		});
		/**
		 * 可扩展列表
		 */
		elvItemList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				for (int i = 0; i < mFileItemList.size(); i++)
				{
					if(i==position){
						elvItemList.expandGroup(i);
					}else{
						elvItemList.collapseGroup(i);
					}
				}
				
			}
			
		});
		/**
		 * 添加文件
		 */
		btnAddFile.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				skipToFileSelectAcitivty(0);
			}
		});
		/**
		 * 创建并上传
		 */
		btnCreateAndUpload.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					if (!doFilter())
					{
						return;
					}
					if(mFileItemList==null||mFileItemList.size()==0){
						return;
					}
					UploadProgram uploadProgram = createUploadProgram();
					if (uploadProgram == null)
					{
						return;
					}
					Intent intent = new Intent(UploadProgramAcitvity3.this,
							MyTabActivity.class);
					intent.putExtra("Type", "uploadProgram");
					intent.putExtra("ProgramInfo", uploadProgram);
					startActivity(intent);
					finish();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
			
		});
	}
	
	/**
	 * 构建上传节目
	 * 
	 * @return
	 */
	private UploadProgram createUploadProgram()
	{
		try
		{
			int width = Integer.parseInt(etWidth.getText().toString().trim());
			int height = Integer.parseInt(etHeight.getText().toString().trim());
			int startX = Integer.parseInt(etStartX.getText().toString().trim());
			int startY = Integer.parseInt(etStartY.getText().toString().trim());
			this.programName = etProgramName.getText().toString().trim();
			
			//共有属性
			propertyCommon.infoWidth = String.valueOf(width);
			propertyCommon.infoHeight = String.valueOf(height);
			propertyCommon.rectWidth = propertyCommon.infoWidth;
			propertyCommon.rectHeight = propertyCommon.infoHeight;
			propertyCommon.rectX = String.valueOf(startX);
			propertyCommon.rectY = String.valueOf(startY);
			propertyCommon.programName = programName;
			
			
			ProgramItem programItem = null;
			PropertyItem p=null;
			//配置素材属性
			for (int i = 0; i < mFileItemList.size(); i++)
			{
				programItem = mFileItemList.get(i);
				p=programItem.property;
				
				switch (programItem.fileType)
				{
					case Const.PICTURE:
					{
						((PropertyPicture) p).pictureName = new File(
								programItem.filePath).getName();
						((PropertyPicture) p).fsFilePath = "./" +  this.programName
								+ ".files/" + ((PropertyPicture) p).pictureName;
						p.materialName=((PropertyPicture) p).pictureName;
					}
					break;
					case Const.VIDEO:
					{
						((PropertyVedio) p).vedioName=new File(programItem.filePath).getName();
						((PropertyVedio) p).fsFilePath="./" + this.programName + ".files/" + ((PropertyVedio) p).vedioName;
						p.materialName=((PropertyVedio) p).vedioName;
					}
					break;
					case Const.TXT:
					{
						((PropertyMultiLineText) p).fileName = new File(programItem.filePath).getName();
						((PropertyMultiLineText) p).fsFilePath="./" +this.programName + ".files/" + ((PropertyMultiLineText) p).fileName;
						p.materialName=((PropertyMultiLineText) p).fileName;
					}
					break;
				}
				
			}
			
			UploadProgram uploadProgram = new UploadProgram();
			// 创建vsn文件
			VsnFileFactory factory = new VsnFileFactoryImpl();
			String vsnPath = "/mnt/sdcard/" + this.programName + ".vsn";
			
			ArrayList<PropertyItem> ptlist=new ArrayList<PropertyItem>();
			for (int i = 0; i < mFileItemList.size(); i++)
			{
				ptlist.add(mFileItemList.get(i).property);
			}
			
			File vsnFile = factory.createVsnFile(propertyCommon,ptlist, vsnPath);
			if (vsnFile == null)
			{
				return null;
			}
			uploadProgram.setVsnFileTask(new UploadTask(vsnFile, "/program/"));
			// 构建上传节目
			String remoteDir = "/program/" + this.programName + ".files/";
			uploadProgram.setRemoteDirtory(remoteDir);
			
			String filePath=null;
			String fileName=null;
			for (int i = 0; i < mFileItemList.size(); i++)
			{
				filePath=mFileItemList.get(i).filePath;
				fileName=mFileItemList.get(i).fileName;
				uploadProgram.getFileTaskList().add(new UploadTask(new File(filePath), remoteDir+fileName));
			}
			return uploadProgram;
		}
		catch (Exception e)
		{
			return null;
		}
		
		
	}
	
	/**
	 * 选择了节目素材之后回调
	 * 
	 * @param programItems
	 */
	public void onSelectProgramItems(ArrayList<ProgramItem> programItems)
	{
		if (programItems == null || programItems.size() == 0)
		{
			return;
		}
		propertyCommon=new PropertyCommon();
		
		mFileItemList.clear();
		mFileItemList=new ArrayList<UploadProgramAcitvity3.ProgramItem>();
		mFileItemList.addAll(programItems);
		
		
		adapter.updateView(mFileItemList);
		elvItemList.setHeaderDividersEnabled(true);
		elvItemList.setAdapter(adapter);
		
//		int size = mFileItemList.size();
//		arrFileNames = new String[size];
//		for (int i = 0; i < size; i++)
//		{
//			arrFileNames[i] = mFileItemList.get(i).fileName;
//		}
////		spinnerFileNameList.initView(arrFileNames);
////		spinnerFileNameList.setSelection(0, true);
//		
//		// 起线程获取文件的参数
////		asyncGetFilePropertyTask = new AsyncGetFilePropertyTask(
////				mFileItemList);
////		asyncGetFilePropertyTask.execute();
		getFilesProperty(mFileItemList);
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
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		try
		{
			if (requestCode == 0 && resultCode == Activity.RESULT_OK)
			{
				// 一个或多个
				ArrayList<FileSortModel> list = (ArrayList<FileSortModel>) data.getSerializableExtra("selectedFiles");
				ArrayList<ProgramItem> selectFiles = new ArrayList<ProgramItem>();
				ProgramItem pt=null;
				FileSortModel fsm=null;
				for (int i = 0; i < list.size(); i++)
				{
					pt = new ProgramItem();
					fsm=list.get(i);
					pt.fileName=fsm.getFileName();
					pt.filePath=fsm.getFilePath();
					pt.fileType=fsm.getFileType();
					switch (pt.fileType)
					{
						case Const.PICTURE:
							pt.property=new PropertyPicture();
						break;
							
						case Const.VIDEO:
							pt.property=new PropertyVedio();
						break;
						case Const.TXT:
							pt.property=new PropertyMultiLineText();
						break;
					}
					selectFiles.add(pt);
				}
				onSelectProgramItems(selectFiles);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 异步获取文件的属性
	 */
	private class AsyncGetFilePropertyTask extends
			AsyncTask<Object, Object, Object>
	{
		private boolean isFinish;
		
		private ArrayList<ProgramItem> selectFiles;
		
		public AsyncGetFilePropertyTask(ArrayList<ProgramItem> selectFiles)
		{
			this.selectFiles = selectFiles;
			isFinish = false;
		}
		
		@Override
		protected void onPostExecute(Object result)
		{
			super.onPostExecute(result);
			isFinish = true;
		}
		
		public boolean isFinish()
		{
			return isFinish;
		}

		@Override
		protected Object doInBackground(Object... params)
		{
			getFilesProperty(selectFiles);
			return null;
		}

		
		
	}
	/**
	 * 获得文件的属性
	 */
	private void getFilesProperty(ArrayList<ProgramItem> selectFiles)
	{
		int size = selectFiles.size();
		int type = -1;
		ProgramItem pt = null;
		for (int i = 0; i < size; i++)
		{
			pt = selectFiles.get(i);
			switch (pt.fileType)
			{
				case Const.PICTURE:
					getImageProperty((PropertyPicture) pt.property,
							pt.filePath);
				break;
				case Const.VIDEO:
					getVedioProperty((PropertyVedio) pt.property,
							pt.filePath);
				break;
			}
		}
	}
	private void getImageProperty(PropertyPicture p, String filePath)
	{
		try
		{
			Bitmap bf = BitmapFactory.decodeFile(filePath);
			p.pictureWidth = String.valueOf(bf.getWidth());
			p.pictureHeight = String.valueOf(bf.getHeight());
		}
		catch (Exception e)
		{
			return;
		}
	}
	
	private void getVedioProperty(PropertyVedio p, String filePath)
	{
		try
		{
			String vedioWidth = null, vedioHeight =null, vedioDuration =null;
			// 获得视频的元数据， 两种方式解析
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			retriever.setDataSource(filePath);
			String width = retriever.extractMetadata(18);
			String height = retriever.extractMetadata(19);
			String duration = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			retriever.release();
			if (width != null)
			{
				vedioWidth = width;
			}
			if (height != null)
			{
				vedioHeight =height;
			}
			
			if (duration != null)
			{
				vedioDuration =duration;
			}
			MediaPlayer mediaPlayer = MediaPlayer.create(this,
					Uri.parse(filePath));
			if(mediaPlayer!=null){
				if (vedioWidth == null)
				{
					vedioWidth = String.valueOf(mediaPlayer.getVideoWidth());
				}
				if (vedioHeight == null)
				{
					vedioHeight = String.valueOf(mediaPlayer.getVideoHeight());
				}
				if (vedioDuration == null)
				{
					vedioDuration =String.valueOf( mediaPlayer.getDuration());
				}
			}
			
			if(vedioWidth==null)
			{
				vedioWidth=sharedPreferenceUtil.getString(ShareKey.KEY_WIN_WIDTH, "128");
			}
			if(vedioHeight==null)
			{
				vedioHeight=sharedPreferenceUtil.getString(ShareKey.KEY_WIN_HEIGHT, "128");
			}
			p.vedioWidth = String.valueOf(vedioWidth);
			p.vedioHeight = String.valueOf(vedioHeight);
			p.showWidth = p.vedioWidth;
			p.showHeight = p.vedioHeight;
			p.length = String.valueOf(vedioDuration);
			p.playLength = ((PropertyVedio) p).length;
			p.materialDuration = ((PropertyVedio) p).length;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文件类型获得不同的参数
	 * 
	 * @param fileType
	 * @return
	 */
	public PropertyItem getFilePropertyByType(int fileType)
	{
		if (fileType == Const.PICTURE)
		{
			return new PropertyPicture();
			
		}
		else if (fileType == Const.VIDEO)
		{
			return new PropertyVedio();
		}
		else if (fileType == Const.TXT)
		{
			return new PropertyMultiLineText();
		}
		return null;
	}
	
	/**
	 * 跳转到选择文件的页面
	 */
	public void skipToFileSelectAcitivty(int fileType)
	{
		try
		{
			Intent intent = new Intent(UploadProgramAcitvity3.this,
					FilesViewActivity.class);
			intent.putExtra("fileType", fileType);
			UploadProgramAcitvity3.this.startActivityForResult(intent, 0);
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 过滤条件
	 */
	protected boolean doFilter()
	{
		// 宽高检测
		int width = 0, height = 0;
		int startX = 0, startY = 0;
		try
		{
			width = Integer.parseInt(etWidth.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_int), 1000);
			etWidth.requestFocus();
			return false;
		}
		
		try
		{
			height = Integer.parseInt(etHeight.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_int), 1000);
			etHeight.requestFocus();
			return false;
		}
		try
		{
			startX = Integer.parseInt(etStartX.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_int), 1000);
			etStartX.requestFocus();
			return false;
		}
		
		try
		{
			startY = Integer.parseInt(etStartY.getText().toString().trim());
		}
		catch (NumberFormatException e)
		{
			toast(getResString(R.string.please_input_int), 1000);
			etStartY.requestFocus();
			return false;
		}
		
		// 节目名检测
		if (TextUtils.isEmpty(etProgramName.getText().toString().trim()))
		{
			toast(getResString(R.string.please_input_program_name), 1000);
			etProgramName.requestFocus();
			return false;
		}
		
		return true;
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	
}
