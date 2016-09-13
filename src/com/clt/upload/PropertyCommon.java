package com.clt.upload;

import java.io.Serializable;

/**
 * 共有属性
 *
 */
public class PropertyCommon implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5780210095310789539L;
	public String programName="";//节目名称
	/**
	 * 工程属性
	 */
	public String projectName="";
	
	public String projectBePacked="1";//节目打包状态：0：没有，1：打包状态
	
	public String projectFrameTime="50.000000";//帧时间
	
	
	/**
	 * 节目属性
	 */
	public String proId="";
	
	public String proName="LED";
	
	public String proVersion="7209065";//版本号
	
	//information节目详细信息
	public String infoWidth="128";// 屏宽（像素点数）

	public String infoHeight="128";// 屏高

	public String infoDuration="6000.000000";//播放持续时间(毫秒)
	
	public String infoDescription="";//描述
	
	public String infoCreator="";//作者
	
	public String infoCreateTime="";//创建时间
	
	public String infoLastModifyTime="";//最后一次修改时间
	
	/**
	 * 节目页属性
	 */

	public String pageId="";
	
	public String pageName="";
	
	public String pageVisibale="1";//节目页是否显示(0:隐藏1:显示) 
	
	public String pageGloble="0";
	
	public String pageAppointDuration="3600000";//节目页 播放播放时长(毫秒) 
	
	public String pagePlayOneTime="6000";//节目页 等待本节目页播完时的播放时长(毫秒) 
	
	public String pageLoopType="1";//节目页 循环类型(0:播放指定时长 1:等待本节目页播完) 
	
	public String pageBgColor="0xFF000000";//节目页背景颜色
	
	//节目页背景
	
	public String bgFileIsRelative="1";//图片文件路径是否是相对路径（0:绝对路径1：相对路径，该项用于以后节目文件打包。）
	
	public String bgFileFilePath=""; //图片文件路径
	
	/**
	 * 区域属性
	 */
	public String regionId="";
	
	public String regionName="";
	
	/*
	 * //区域类型详细描述如下：
		0,      //无效区域
		1,		//文件窗
		2,		//多行文本
		3,		//单行文本
		4,		//表格
		5,		//时钟
		6,		//计时
		7,		//外部视频
		8,		//数据库
		9,		//天气预报
		10,		//外部程序窗

	 */
	public String regionType="1";//区域区域类型
	
	public String regionShow="1";//区域是否显示(0:否1:是)
	
	public String regionLayer="1";//区域1显示层次（1为顶层，各区域依次向下罗列显示）
	
	public String regionlocked="0";//区域位置和大小是否锁定（0：没有锁定，1：锁定）
	
	public String regionIsSameAnim="1";
	
	
	//rect区域所在位置
	public String rectX="0";//起点坐标X
	
	public String rectY="0";//起点坐标Y
	
	public String rectWidth="128";//区域宽
	
	public String rectHeight="128";//区域高
	
	public String rectBorderWidth="0";//边框宽度
	
	public String rectBorderColor="0xFFFFFF00";//边框颜色
	
	 
}
