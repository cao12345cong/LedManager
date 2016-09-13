package com.clt.upload;

import java.io.File;
import java.io.Serializable;

public class UploadTask implements Serializable
{
	/**
	 * 上传任务
	 */
	private static final long serialVersionUID = 8992243524208197808L;

	private String remotePath; //远程路径
	
	private File localFile;//本地文件

	
	public UploadTask(File localFile,String remotePath)
	{
		super();
		this.remotePath = remotePath;
		this.localFile = localFile;
	}

	public String getRemotePath()
	{
		return remotePath;
	}

	public void setRemotePath(String remotePath)
	{
		this.remotePath = remotePath;
	}

	public File getLocalFile()
	{
		return localFile;
	}

	public void setLocalFile(File localFile)
	{
		this.localFile = localFile;
	}

		
	
}
