package com.clt.upload;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 要上传的节目
 *
 */
public class UploadProgram implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3125834970441465012L;

	private UploadTask vsnFileTask;//vsn文件
	
	private String remoteDirtory;//远程文件夹
	
	private List<UploadTask> fileTaskList;//素材文件
	
	public UploadProgram()
	{
		fileTaskList=new ArrayList<UploadTask>();
	}

	public UploadTask getVsnFileTask()
	{
		return vsnFileTask;
	}

	public void setVsnFileTask(UploadTask vsnFileTask)
	{
		this.vsnFileTask = vsnFileTask;
	}

	public List<UploadTask> getFileTaskList()
	{
		return fileTaskList;
	}

	public void setFileTaskList(List<UploadTask> fileTaskList)
	{
		this.fileTaskList = fileTaskList;
	}
	
	

	public String getRemoteDirtory()
	{
		return remoteDirtory;
	}

	public void setRemoteDirtory(String remoteDirtory)
	{
		this.remoteDirtory = remoteDirtory;
	}

	public long getTotalSize(){
		long size=0;
		if(vsnFileTask!=null){
			size+=vsnFileTask.getLocalFile().length();
		}
		for (UploadTask task : fileTaskList)
		{
			size+=task.getLocalFile().length();
		}
		return size;
	}

	/**
	 * 删除vsn文件
	 * @return
	 */
	public boolean deleteVsnFile(){
		File file=null;
		if(vsnFileTask!=null){
			file=vsnFileTask.getLocalFile();
			if(file.exists()){
				return file.delete();
			}
		}
		return false;
	}
}
