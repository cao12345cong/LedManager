package com.clt.upload;

import java.io.File;

import com.clt.entity.Program;
import com.clt.util.FileUtil;

/**
 * 上传节目代理
 *
 */
public class UploadManager
{
	private UploadProgram uploadProgram;
	
	private OnUploadListener onUploadListener;
	
	public UploadManager(UploadProgram uploadProgram)
	{
		this.uploadProgram=uploadProgram;
	}
	
	public UploadProgram getUploadProgram()
	{
		return uploadProgram;
	}

	public void setUploadProgram(UploadProgram uploadProgram)
	{
		this.uploadProgram = uploadProgram;
	}

	public OnUploadListener getOnUploadListener()
	{
		return onUploadListener;
	}

	public void setOnUploadListener(OnUploadListener onUploadListener)
	{
		this.onUploadListener = onUploadListener;
	}

	/**
	 * 上传节目
	 * @param ip
	 * @return
	 */
	public boolean executeUpload(String ip,OnUploadListener onUploadListener){
		try
		{
			Ftp ftp = new Ftp(ip);
			boolean isOk = ftp.login();
			if (!isOk)
			{
				return false;
			}
			UploadTask vsnFileTask=uploadProgram.getVsnFileTask();
			isOk = ftp.uploadFile(vsnFileTask.getLocalFile(), vsnFileTask.getRemotePath());
			if (!isOk)
			{
				return false;
			}
			ftp.makeDirectory(uploadProgram.getRemoteDirtory());
			for (UploadTask task : uploadProgram.getFileTaskList())
			{
				ftp.uploadFile(task.getLocalFile(),task.getRemotePath(), 0L,onUploadListener);
			}
			
			if (!isOk)
			{
				return false;
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
		
		
	}
}
