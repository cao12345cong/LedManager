package com.clt.upload;

import java.io.Serializable;

/**
 * 素材属性
 *
 */
public class PropertyItem implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8428705223170682922L;

	/**
	 * 素材类型
	 *
	 */
	public class MaterialType{
		public static final int InVaild=0;
		public static final int Voice=1;
		public static final int Picture=2;
		public static final int Video=3;
		public static final int SingleText=4;
		public static final int MultiText=5;
		public static final int Gif=6;
		public static final int Swf=7;
		
	}
	
	
	 
	/**
	 * 素材共有属性
	 */
	public String materialId="";
	
	public String materialName="";
	/*
	 *  0,			//非法类型
		1,			//声音
		2,			//图片
		3,			//视频
		4,			//单行文本
		5,			//多行文本
		6,			//Gif
		7,			//swf
		8,			//TV
		9,			//时钟
		10,			//流媒体
		11,			//Word
		12,			//Excel
		13,			//PowerPoint
		14,			//天气预报
		15,			//倒计时
		16,			//表格
		17,			//数据库
		20,			//有格式文本
		21,			//湿度
		22,			//温度
		23,			//噪声
		24,			//空气污染指数
		25,			//天敏系列卡支持SAA7134 SAA7130 视音频主芯片
		26,			//桌面区域
		27,			//网页
		28,			//烟雾
		65,			//外部程序
		99,			//体育比分
		100,		//内存图像
		101,		//多页中的一页，内部使用
	 	2048,		//用户自定义类型起始编号

	 */
	public String materialType="";//素材类型，值为前面描述过的素材类型的值之一
	
	public String materialVersion="0x00000001";//素材版本号
	
	public String materialBackColor="0xFF000000";//素材背景颜色，值为ARGB值，格式：0xFF000000
	
	public String materialAlhpa="1.000000";
	
	public String materialDuration="6000";//素材播放时长（毫秒）
	
	public String materialBeGlaring="0";//素材是否炫彩（只适用于单行文本）（0：否；1：是）
	
	public String materialIsNeedUpdate="0";//是否需要更新（ 0：否；1：是）
	
	public String materialUpdateInterval="10000";//更新的时间间隔（毫秒） 
	
	public String materialMirrorHandstand="0";// 翻转效果，（0：无效果；1：镜像；2：倒立（旋转180°））
	
	public String materialPlayTimes="1";
	
	//进出场特效
	public String effectIsStatic="0";
	
	public String effectStayType="0";//停留类型 0：无效果 1:闪烁
	
	//inEffect进场特效
	public String inEffectType="0";//特效类型 （整数）
	
	public String inEffectTime="500";//进场时间(毫秒)
	
	public String inEffectRepeatX="1";//水平方向重复次数（整数）
	
	public String inEffectRepeatY="1";//垂直方向重复次数（整数）
	
	public String inEffectIsTran="1";//是否首尾衔接（0：否；1：是）
	
	//outEffect出场特效，同进场
	public String outEffectType="0";
	
	public String outEffectTime="500";
	
	public String outEffectRepeatX="1";
	
	public String outEffectRepeatY="1";
	
	public String outEffectIsTran="1";
	
	//multipicInfo多图信息，将其转换为临时多图片保存
	public String multiPicInfoSource="0";//来源， 0:播放时生成；1：从文件来；
	
	public String multiPicInfoPicCount="0";//多图时，图片数量（整数）
	
	public String multiPicInfoOnePicDuration="0";//一个图片的播放时间（毫秒）
	
	//filePath多图信息：转换为临时多图片时的图片路径
	
	public String filePathIsRelative="0";//是否为相对路径（0：否；1：是）
	
	public String filePathFilePath="";//节目打包后的MD5码，节目没有打包时没有此节点
	
	
	
}
