package com.clt.util;

import android.os.Environment;

/**
 * 常量类
 * 
 * @author caocong 2014.3.3
 * 
 */
public class Const {

	public static final String VERSION = "0.2.4";

	/** 英文. */
	public static final String EN = "en"; // en

	/** 中文. */
	public static final String CN = "cn"; // zh

	// 连接断开
	public static final String CONNECT_BEARK_ACTION = "com.clt.connectBreak";
	// 连接时长
	public static final String CONNECT_TIME_ACTION = "com.clt.connectTime";

	public static final String MODIF_TERM_NAME_ACTION = "com.clt.changeTermName";

	public static final int nmNetCommand = 1;

	public static final int findTerminateResult = 2;// 找到了终端

	public static final int endFindTerminateResult = 4;// 没有找到终端

	public static final int connectSuccess = 3;// 连接上了

	public static final int connectBreak = 5;// 断网

	public static final int connnectFail = 6;// 断网

	// public static final String SAVE_PATH =
	// "/mnt/sdcard/Android/data/com.color.home/files/Download/";
	public static final String SAVE_PATH = "/mnt/usb_storage/USB_DISK0/udisk0/";

	public static final String USB_PATH_0 = "/mnt/usb_storage/USB_DISK0/udisk0/";

	public static final String USB_PATH_1 = "/mnt/usb_storage/USB_DISK1/udisk1/";
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/";

	public static final String SDCARD_DOWNLOAD_PATH = "/mnt/sdcard/Android/data/com.color.home/files/Download/";
	public static final String SDCARD_SD_PATH = "/mnt/sdcard/Android/data/com.color.home/files/Usb/";
	public static final String SDCARD_SD_FTP = "/mnt/sdcard/Android/data/com.color.home/files/Ftp/program";

	// public static final String SAVE_PATH = Environment
	// .getExternalStorageDirectory().getAbsolutePath() + "/";

	public static final String PROGRAM_EXTENSION = ".vsn";
	
	//图片视频文字
	public static final int PICTURE=1;
	public static final int VIDEO=2;
	public static final int TXT=3;

}
