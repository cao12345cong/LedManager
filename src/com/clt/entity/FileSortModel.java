package com.clt.entity;

import java.io.Serializable;

/**
 * 
 * 文件比较
 */
public class FileSortModel implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8017463262318455356L;

	private int imgResId;
	
	private String fileName;
	
	private String filePath;
	
	private String sortLetters;
	
	private int fileType=-1;
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	public String getSortLetters()
	{
		return sortLetters;
	}
	
	public void setSortLetters(String sortLetters)
	{
		this.sortLetters = sortLetters;
	}
	
	public int getImgResId()
	{
		return imgResId;
	}
	
	public void setImgResId(int imgResId)
	{
		this.imgResId = imgResId;
	}

	public int getFileType()
	{
		return fileType;
	}

	public void setFileType(int fileType)
	{
		this.fileType = fileType;
	}

	
}
