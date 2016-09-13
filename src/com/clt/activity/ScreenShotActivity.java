package com.clt.activity;

import com.clt.ledmanager.R;
import com.clt.ui.DialogFactory;
import com.clt.ui.DialogProgressBar;
import com.clt.ui.TitleBarView;
import com.clt.ui.TitleBarView.TitleBarListener;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
/**
 *	视频截图
 */
public class ScreenShotActivity extends BaseActivity
{
	private ImageView ivImage;
	
	private TitleBarView titleBarView;
	
	private GetImageTask getImageTask;
	
	private Application app;
	
	private DialogProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			setContentView(R.layout.screen_shot);
			init();
			initView();
			initListener();
			initData();
		}
		catch (Exception e)
		{
			
		}
		
	}
	/**
	 * 
	 */
	private void init()
	{
		progressBar = new DialogProgressBar(this,null);
		app = (Application) getApplication();
	}
	
	private void initView()
	{
		titleBarView = (TitleBarView) findViewById(R.id.titlebar);
		ivImage = (ImageView) findViewById(R.id.iv_screen_shot);
	}
	/**
	 * 
	 */
	private void initListener()
	{
		titleBarView.setTitleBarListener(new TitleBarListener()
		{
			
			@Override
			public void onClickRightImg(View v)
			{
				String ipAddress = app.ledTerminateInfo.getIpAddress();
				String imageUrl = "http://" + ipAddress
						+ "/transmission/ftp/config/screenshot";
				getImageTask = new GetImageTask(imageUrl);
				getImageTask.execute();
			}
			
			@Override
			public void onClickLeftImg(View v)
			{
				finish();
			}
		});
	}
	/**
	 * 
	 */
	private void initData()
	{
		String ipAddress = app.ledTerminateInfo.getIpAddress();
		String imageUrl = "http://" + ipAddress
				+ "/transmission/ftp/config/screenshot";
		getImageTask = new GetImageTask(imageUrl);
		getImageTask.execute();
	}
	
	/**
	 * 获得图片
	 */
	class GetImageTask extends AsyncTask<Object, Object, byte[]>
	{
		
		private String imageUrl;
		
		public GetImageTask(String imageUrl)
		{
			this.imageUrl = imageUrl;
		}
		
		protected void onPreExecute()
		{
			progressBar.show();
		}
		
		@Override
		protected void onPostExecute(byte[] result)
		{
			try
			{
				if (progressBar != null)
				{
					progressBar.dismiss();
				}
				if (result != null)
				{
					Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
							result.length);
					ivImage.setImageBitmap(bitmap);
				}
			}
			catch (Exception e)
			{
				
			}
			
		}
		
		@Override
		protected byte[] doInBackground(Object... params)
		{
			if (TextUtils.isEmpty(imageUrl))
			{
				return null;
			}
			return HttpUtil.httpGetImage(imageUrl);
		}
		
	}
}
