package com.clt.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clt.adapter.FileListAdapter;
import com.clt.adapter.FileListAdapter.OnSelectFilesListener;
import com.clt.entity.FileSortModel;
import com.clt.ledmanager.R;
import com.clt.ui.TitleBarView;
import com.clt.ui.TitleBarView.TitleBarListener;
import com.clt.upload.CharacterParser;
import com.clt.upload.PinyinComparator;
import com.clt.util.Const;

/**
 * 文件浏览器视图
 * 
 * @author Administrator
 * 
 */
public class FilesViewActivity extends BaseActivity implements OnSelectFilesListener
{
	//支持的图片格式
	public static final String[] picFormat = { ".jpg",".jpeg",".png",".bmp" };
	
	//支持的视频格式
	public static final String[] videoFormat = { ".avi", ".flv", ".mkv",
			".mp4", ".3gp", ".mov", ".mpg", ".mpeg", ".ts", ".tp", ".rm",
			 ".wmv", };
	//支持的文本格式
	public static final String[] txtFormat = { ".txt" };
	
	private int fileImgResId;
	
	private FileListAdapter adapter;
	
	private List<FileSortModel> dataList;
	
	private LinearLayout llGoBackOneStep;// 返回上一步
	
	private TextView tvGoBackOneStep;
	
	private ListView fileView;
	
	private TitleBarView titleBarView;
	
	private String currentDir;// 当前所在的文件夹
	
	private CharacterParser characterParser;
	
	private TextView tvSelectedFiles;
	
	private Button btnSubmit;//提交
	
	private LinkedHashMap<String, FileSortModel> mSelectedFilesMaps;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			Application.getInstance().setSystemLanguage();
			setContentView(R.layout.file_view);
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
		Intent intent = getIntent();
		int fileType = intent.getIntExtra("fileType", 0);
		mSelectedFilesMaps=new LinkedHashMap<String, FileSortModel>();
//		switch (fileType)
//		{
//			case UploadProgramAcitvity.FileType.PICTURE:
//				fileFormat = picFormat;
//				fileImgResId = R.drawable.ic_picture;
//			break;
//			
//			case UploadProgramAcitvity.FileType.VIDEO:
//				fileFormat = videoFormat;
//				fileImgResId = R.drawable.ic_vedio;
//			break;
//			case UploadProgramAcitvity.FileType.TXT:
//				fileFormat = txtFormat;
//				fileImgResId = R.drawable.ic_txt;
//			break;
//			default:
//				fileFormat = picFormat;
//				fileImgResId = R.drawable.ic_file;
//			break;
//		}
		characterParser = CharacterParser.getInstance();
		currentDir =  Environment.getExternalStorageDirectory().getAbsolutePath();
		dataList = new ArrayList<FileSortModel>();
	}
	
	private void initView()
	{
		titleBarView = (TitleBarView) findViewById(R.id.titlebar);
		llGoBackOneStep = (LinearLayout) findViewById(R.id.ll_go_back_step);
		tvGoBackOneStep = (TextView) findViewById(R.id.tv_go_back_step);
		fileView = (ListView) findViewById(R.id.fileView);
		tvSelectedFiles=(TextView) findViewById(R.id.tv_has_selected);
		btnSubmit=(Button) findViewById(R.id.btn_submit);
	}
	
	private void initData()
	{
		tvGoBackOneStep.setText(currentDir);
		// listview
		adapter = new FileListAdapter(FilesViewActivity.this, dataList);
		adapter.setOnSelectFilesListener(this);
		fileView.setAdapter(adapter);
		
		String s = getString(R.string.has_selected_file_num);
		tvSelectedFiles.setText(String.format(s,0));
		
		GetFileAsyncTask task = new GetFileAsyncTask(currentDir);
		task.execute();
		
	}
	
	private void initListener()
	{
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
		/**
		 * 返回上一级目录
		 */
		llGoBackOneStep.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				goBackToParentDir();
				
			}
		});
		/**
		 * 提交
		 */
		btnSubmit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(mSelectedFilesMaps.isEmpty()){
					return;
				}
				ArrayList<FileSortModel> list=new ArrayList<FileSortModel>();
				Set<Entry<String, FileSortModel>> set = mSelectedFilesMaps.entrySet();
				for (Entry<String, FileSortModel> entry : set)
				{
					list.add(entry.getValue());
				}
				
				Intent intent = new Intent();
				intent.putExtra("type", "selectProgram");
				intent.putExtra("selectedFiles", list);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		/**
		 * listview点击
		 */
		fileView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				String filePath = dataList.get(position).getFilePath();
				String fileName = dataList.get(position).getFileName();
				File file = new File(filePath);
//				int fileType=-1;
//				if(isFileImage(file)){
//					fileType=Const.PICTURE;
//				}else if(isFileVedio(file))
//				{
//					fileType=Const.VIDEO;
//				}else if(isFileTxt(file))
//				{
//					fileType=Const.TXT;
//				}
				
				if (file.isDirectory())
				{
					GetFileAsyncTask task = new GetFileAsyncTask(filePath);
					task.execute();
				}
//				else
//				{
//					
//					Intent intent = new Intent();
//					intent.putExtra("type", "selectProgram");
//					intent.putExtra("FileName", fileName);
//					intent.putExtra("FilePath", filePath);
//					intent.putExtra("FileType", fileType);
//					// startActivity(intent);
//					setResult(RESULT_OK, intent);
//					finish();
//					
//				}
			}
		});
	}
	
	/**
	 * 返回上一级目录
	 */
	public void goBackToParentDir()
	{
		try
		{
			File dir = new File(currentDir);
			File parent = dir.getParentFile();
			if (parent != null && parent.isDirectory())
			{
				GetFileAsyncTask task = new GetFileAsyncTask(dir.getParent());
				task.execute();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 文件是否支持的图片
	 * @param file
	 * @return
	 */
	public boolean isFileImage(File file)
	{
		String fileName=file.getName();
		int len=picFormat.length;
		for (int i = 0; i < len; i++)
		{
			if(fileName.toLowerCase().endsWith(picFormat[i]))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 文件是否支持的视频
	 * @param file
	 * @return
	 */
	public boolean isFileVedio(File file)
	{
		String fileName=file.getName();
		int len=videoFormat.length;
		for (int i = 0; i < len; i++)
		{
			if(fileName.toLowerCase().endsWith(videoFormat[i]))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 文件是否支持的文本
	 * @param file
	 * @return
	 */
	public boolean isFileTxt(File file)
	{
		String fileName=file.getName();
		int len=txtFormat.length;
		for (int i = 0; i < len; i++)
		{
			if(fileName.toLowerCase().endsWith(txtFormat[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 按钮事件
	 */
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			goBackToParentDir();
			return true;
		}
		return false;
	}
	
	/**
	 * 获得文件列表
	 * 
	 */
	public class GetFileAsyncTask extends AsyncTask<Object, Object, Object>
	{
		private String selectedPath;
		
		List<FileSortModel> filesLists;// 文件集合
		
		List<FileSortModel> dirLists;// 文件夹集合
		
		public GetFileAsyncTask(String selectedPath)
		{
			try
			{
				this.selectedPath = selectedPath;
				filesLists = new ArrayList<FileSortModel>();
				dirLists = new ArrayList<FileSortModel>();
			}
			catch (Exception e)
			{
			}
			
		}
		
		@Override
		protected void onPreExecute()
		{
			try
			{
				super.onPreExecute();
				dataList.clear();
			}
			catch (Exception e)
			{
				
			}
			
		}
		
		@Override
		protected void onPostExecute(Object result)
		{
			try
			{
				adapter.updateView(dataList);
				currentDir = this.selectedPath;
				tvGoBackOneStep.setText(currentDir);
			}
			catch (Exception e)
			{
			}
			
		}
		
		@Override
		protected Object doInBackground(Object... params)
		{
			try
			{
				getFilesList(this.selectedPath);
				// 排序
				PinyinComparator pinyinComparator = new PinyinComparator();
				Collections.sort(dirLists, pinyinComparator);
				Collections.sort(filesLists, pinyinComparator);
				dataList.addAll(dirLists);
				dataList.addAll(filesLists);
			}
			catch (Exception e)
			{
				
			}
			return null;
		}
		
		/**
		 * 获得文件集合
		 * 
		 * @param selectedPath
		 * @param format
		 *            过滤条件
		 */
		private void getFilesList(String selectedPath)
		{
			try
			{
				List<FileSortModel> mLists = null;
				File selectedFile = new File(selectedPath);
				if (selectedFile.canRead() && !selectedFile.isHidden())
				{
					mLists = new ArrayList<FileSortModel>();
					FileSortModel fsm = null;
					File[] files = selectedFile.listFiles();
					for (File file : files)
					{
						fsm = new FileSortModel();
						if (file.isHidden())
						{
							continue;
						}
						if (file.isFile())
						{
							if(isFileImage(file)){
								fileImgResId=R.drawable.ic_picture;
								fsm.setImgResId(fileImgResId);
								fsm.setFileType(Const.PICTURE);
							}else if(isFileTxt(file))
							{
								fileImgResId=R.drawable.ic_txt;
								fsm.setImgResId(fileImgResId);
								fsm.setFileType(Const.TXT);
							}else if(isFileVedio(file))
							{
								fileImgResId=R.drawable.ic_vedio;
								fsm.setImgResId(fileImgResId);
								fsm.setFileType(Const.VIDEO);
							}else{
								continue;
							}
							
							
						}
						else
						{
							fsm.setImgResId(R.drawable.ic_folder);
						}
						
						fsm.setFileName(file.getName());
						fsm.setFilePath(file.getPath());
						
						String firstSpell = file.getName().substring(0, 1);
						String pinyin = characterParser.getSelling(firstSpell);
						String sortString = firstSpell.toUpperCase();
						if (sortString.matches("[A-Z]"))
						{
							fsm.setSortLetters(sortString.toUpperCase());
						}
						else
						{
							fsm.setSortLetters("#");
						}
						if (file.isDirectory())
						{
							dirLists.add(fsm);
						}
						else
						{
							filesLists.add(fsm);
						}
					}
				}
			}
			catch (Exception e)
			{
			}
			
		}
	}

	/**
	 * 选中了某个文件
	 */
	@Override
	public void selectedFile(FileSortModel fsm)
	{
		if(!mSelectedFilesMaps.containsKey(fsm.getFilePath()))
		{
			mSelectedFilesMaps.put(fsm.getFilePath(), fsm);
			String s = getString(R.string.has_selected_file_num);
			tvSelectedFiles.setText(String.format(s,mSelectedFilesMaps.size()));
		}
	}

	/**
	 * 撤销选中某个文件
	 */
	@Override
	public void disSelectedFile(FileSortModel fsm)
	{
		if(mSelectedFilesMaps.containsKey(fsm.getFilePath()))
		{
			mSelectedFilesMaps.remove(fsm.getFilePath());
			String s = getString(R.string.has_selected_file_num);
			tvSelectedFiles.setText(String.format(s,mSelectedFilesMaps.size()));
		}
	}
	
	
	
}
