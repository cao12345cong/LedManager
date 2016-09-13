package com.clt.upload;

import com.clt.upload.PropertyItem.MaterialType;

/**
 * 视频节目属性
 * 
 */
public class PropertyVedio extends PropertyItem
{
	public String length="";//视频总长度(毫秒)
	
	public String vedioWidth="128";//视频实际宽
	
	public String vedioHeight="128";//视频实际高
	
	public String vedioName="vedio";
	
	public String vedioPath="";
	
	public String inOffset="0";//播放起点位置(毫秒)
	
	public String playLength="";//播放总时长(总长-播放起点) (毫秒)
	
	public String volume="1";
	
	public String loop="1";//是否循环播放（0：否；1：是）
	
	// fileSource
	public String fsIsRelation="1";//是否为相对路径（0：否；1：是）
	
	public String fsFilePath="";
	
	public String fsMd5="";
	
	public String reserveAs="0";
	
	//截取部分视频区域显示时的位置
	public String showX="0";//起点坐标X
	
	public String showY="0";//起点坐标Y
	
	public String showWidth="0";//宽度
	
	public String showHeight="0";//高度
	
	public String isSetShowRegion="0";//是否截取视频区域显示(0:否1:是)
	
	public String isSetPlayLen="0";
	
	public PropertyVedio()
	{
		materialName="vedio";
		materialType=String.valueOf(MaterialType.Video);
	}
	
}
