package com.clt.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.clt.entity.FileSortModel;
import com.clt.ledmanager.R;
import com.clt.util.Const;
import com.clt.util.Tools;

/**
 * 文件浏览
 * 
 */
public class FileListAdapter extends BaseAdapter
{
	protected List<FileSortModel> models;
	
	private boolean[] isCheckedList;
	
	private Context mContext;
	
	private String ipAddress;
	
	private OnSelectFilesListener mOnSelectFilesListener;
	
	private AsyncGetFileThumbTask asyncGetFileThumbTask;
	
	private HashMap<String, Bitmap> imageCache;
	
	public FileListAdapter(Context mContext, List<FileSortModel> models)
	{
		this.mContext = mContext;
		imageCache=new HashMap<String, Bitmap>();
		setData(models);
	}
	
	public void setOnSelectFilesListener(OnSelectFilesListener onSelectFilesListener)
	{
		mOnSelectFilesListener=onSelectFilesListener;
	}
	
	public void setshowCheckBoxs(boolean flag)
	{
		updateView(this.models);
	}
	
	public void setData(List<FileSortModel> models)
	{
		if (models != null&&models.size()>0)
		{
			this.models = models;
			this.isCheckedList=new boolean[models.size()];
		}
		else
		{
			this.models = new ArrayList<FileSortModel>();
		}
	}
	
	public void updateView(List<FileSortModel> models)
	{
		setData(models);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return models.size();
	}
	
	@Override
	public FileSortModel getItem(int position)
	{
		return models.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final int index=position;
		Holder holder = null;
		if(convertView==null)
		{	
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.file_view_item, null);
			holder = new Holder();
			holder.tvFileName = (TextView) convertView
					.findViewById(R.id.tv_file_name);
			holder.ivFileImage = (ImageView) convertView
					.findViewById(R.id.iv_file_image);
			holder.cbSelect = (CheckBox) convertView
					.findViewById(R.id.cb_chose_file);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		
		final FileSortModel model = getItem(position);
		
		holder.tvFileName.setText(model.getFileName());
		holder.ivFileImage.setImageResource(model.getImgResId());
		File file = new File(model.getFilePath());
		holder.cbSelect
				.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked)
					{
						if(mOnSelectFilesListener!=null)
						{
							if(isChecked){
								
								mOnSelectFilesListener.selectedFile(models.get(position));
							}else{
								mOnSelectFilesListener.disSelectedFile(models.get(position));
							}
							isCheckedList[index]=isChecked;
						}
						
					}
				});
		if (file.isFile())
		{
			
			final boolean isChecked=isCheckedList[index];
			if(isChecked){
				holder.cbSelect.setSelected(true);
			}else{
				holder.cbSelect.setSelected(false);
				
			}
			holder.cbSelect.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.cbSelect.setVisibility(View.GONE);
			
		}
		
		if(model.getFileType()==Const.PICTURE){
			if(imageCache.containsKey(model.getFilePath())){
				holder.ivFileImage.setImageBitmap(imageCache.get(model.getFilePath()));
			}else{
				asyncGetFileThumbTask=new AsyncGetFileThumbTask();
				asyncGetFileThumbTask.setParam(holder.ivFileImage, model.getFilePath());
				asyncGetFileThumbTask.execute();
			}
			
		}
		return convertView;
	}
	
	private class AsyncGetFileThumbTask extends AsyncTask<Object, Object, Bitmap>
	{

		private String filePath;
		private ImageView imageView;
		public AsyncGetFileThumbTask()
		{
			
		}
		public void setParam(ImageView iv,String filePath)
		{
			this.filePath=filePath;
			this.imageView=iv;
		}
		@Override
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			
			if(imageView!=null){
				imageView.setImageBitmap(result);
			}
		}
		@Override
		protected Bitmap doInBackground(Object... params)
		{
			
			Bitmap thumb = Tools.getimage(filePath, 36, 36);
			imageCache.put(filePath, thumb);
			return thumb;
		}
		
		
		
	}
//	/*
//	 * 获得选中的文件
//	 */
//	public List<FileSortModel> getSelectedList(){
//		ArrayList<FileSortModel> list=new ArrayList<FileSortModel>();
//		FileSortModel fsm=null;
//		for (int i = 0; i < models.size(); i++)
//		{
//			fsm=models.get(i);
//			if(fsm.isSelected()){
//				list.add(fsm);
//			}
//		}
//		return list;
//	}
	class Holder
	{
		private TextView tvFileName;
		
		private ImageView ivFileImage;
		
		private CheckBox cbSelect;
		
	}
	
	public interface OnSelectFilesListener{
		void selectedFile(FileSortModel fsm);
		void disSelectedFile(FileSortModel fsm);
	}
}
