package com.clt.upload;

import com.clt.upload.PropertyItem.MaterialType;

/**
 * 单行文本节目属性
 * 
 */
public class PropertySingleLineText extends PropertyItem
{
	public String txtName="";
	
	public String text="单行文本";
	
	public String textColor = "0xFFFFFFFF";// 文本颜色，值为ARGB值，格式：0xFF000000
	
	// 字体
	public String logFontIfHeight = "21";// 逻辑单位的字符或者字符元高度
	
	public String logFontIfWidth = "0";// 逻辑单位的字符字体的平均宽度
	
	public String logFontlfEscapement = "0";// 每行文本输出时相对于页面底端的角度，单位为1/10度
	
	public String logFontlfOrientation = "0";// 以1/10度为单位指定字符基线相对于页面底端的角度
	
	public String logFontlfWeight = "400";// 字体重量，范围0-1000，若为0，使用默认的字体重量,正常情况下的字体重量为400，粗体为700
	
	public String logFontlfItalic="0";// 是否使用斜体（0：否；1：是）
	
	public String logFontlfUnderline="0";// 是否添加下划线（0：否；1：是）
	
	public String logFontlfStrikeOut="0";// 是否添加删除线（0：否；1：是）
	
	public String logFontlfCharSet="1";// 字符集
	
	public String logFontlfOutPrecision="0";// 输出精度
	
	public String logFontlfQuality="0";// 输出质量
	
	public String logFontlfPitchAndFamily="32";// 字体的字符间距和族
	
	public String logFontlfFaceName="宋体";// 字体名称
	
	// 文件描述 fileSource
	
	public String fsIsIsRelative="0";
	
	public String fsFilePath="";
	
	public String fsMD5="";
	
	//
	public String isFromFile="0";// 单行文本是否来自外部txt文件(0:否1:是)
	
	public String isScroll="1";// 是否连续左移（0：否；1：是）
	
	public String speed="25.000000";// 每秒滚动的像素数,范围：0-200.000000的浮点数
	
	public String isHeadConnectTail="0";// 是否首尾相接（0：否；1：是）
	
	public String wordSpacing="0";// 字间距，范围：0-1000的整数
	
	public String repeatCount="1";// 滚动次数
	
	public String isScrollByTime="1";// 控制模式 (0：按次数控制1: 按时间控制)
	
	public String playLenth="300000";// 播放时长（毫秒）
	
	public String moveDir="0";// 滚动方向，目前无效（0：左；1：上；2：右；3：下）
	
	public String ifSpeedByFrame="1";// 是否按照每帧滚动多少像素指定滚动速度（0：否；1：是）
	
	public String speedByFrame="1.000000";// 每帧滚动的像素数，范围：0-8.000000的浮点数
	
	public String wordType="0";
	
	public PropertySingleLineText()
	{
		materialDuration="300000";
		materialName="Single Line Text";
		materialType=String.valueOf(MaterialType.SingleText);
	}
	
}
