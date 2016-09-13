package com.clt.util;

import com.clt.commondata.SenderInfo;

/**
 * 不同发送卡权限判断
 * @author Administrator
 *
 */
public class SendingCardFunctionHelper
{
    public static final int SCF_BASIC_PARAM = 0x00000001;// 基本参数

    public static final int SCF_CASCADE = 0x00000002;// 级联功能

    public static final int SCF_RCV_PARAM_FROM_SC = 0x00000004;// 接收卡参数来自发送卡

    public static final int SCF_SET_TEST_MODE = 0x00000008;// 设置测试模式

    public static final int SCF_ENCRYPTION = 0x00000010;// 加密

    public static final int SCF_TAKE_OUT_PIXEL = 0x00000020;// 抽点

    public static final int SCF_UGRADE_FIRMWARE = 0x00000040;// firmware升级

    public static final int SCF_VOICE_TRAN = 0x00000080;// 声音传输

    public static final int SCF_SHOW_LOGO = 0x00000100;// LOGO图标

    public static final int SCF_SET_EDID = 0x00000200;// 设置EDID

    public static final int SCF_SET_BRIGHTNESS = 0x00000400;// 设置亮度、色温

    public static final int SCF_RTC = 0x00000800;// 获取和设置发送卡的时间

    public static final int SCF_GET_TEMPERTURE = 0x00001000;// 获取温度

    public static final int SCF_GET_HUMIDITY = 0x00002000;// 获取湿度

    public static final int SCF_UGRADE_ARM = 0x00004000;// ARM升级

    public static final int SCF_LCD_LOGO = 0x00008000;// ARM升级

    public static final int SCF_AUTO_BRIGHT = 0x00010000;// 自动亮度调节
    
    public static final int SCF_VIDEO_SOURCE = 0x00020000;// 视频来源

    enum SC_PARAM_POSITION
    {
        scpp_basic_param, // 基本参数
        scpp_firmware, // FPGA程序
        scpp_rcv_param_port1, // 口 1 的实时参数
        scpp_rcv_param_port2, // 口 2 的实时参数
        scpp_rcv_param_port3, // 口 3 的实时参数
        scpp_rcv_param_port4, // 口 4 的实时参数
        scpp_encryption, // 加密
        scpp_edid, // EDID
        scpp_brightness, // 亮度
        scpp_logo, // LOGO
        scpp_arm, // ARM
        scpp_lcd_logo, // lcd logo
        scpp_rcv_param, // 保存接收卡参数
        scpp_adaptive_brightness, // 亮度自适应调节数据存储
        scpp_3d_function// 3D功能
    };

    /**
     * 根据发送卡型号获取改卡支持的功能
     * @param scType
     * @param majorVersion
     * @param minorVersion
     * @return
     */
    public static int getSupportedFeatures(String senderCardType,
            int majorVersion, int minorVersion)
    {
        if (isT7(senderCardType))
        {
            if (majorVersion < 10 || (majorVersion == 10 && minorVersion < 60))
            {
                return (SCF_BASIC_PARAM | SCF_SET_EDID | SCF_SET_BRIGHTNESS);
            }
            else
            {
                return (SCF_BASIC_PARAM | SCF_UGRADE_FIRMWARE | SCF_SET_EDID
                        | SCF_SET_BRIGHTNESS | SCF_VOICE_TRAN | SCF_AUTO_BRIGHT);
            }
        }
        else if (isIQ7E(senderCardType))
        {
            return (SCF_BASIC_PARAM | SCF_CASCADE | SCF_RCV_PARAM_FROM_SC
                    | SCF_SET_TEST_MODE | SCF_ENCRYPTION | SCF_UGRADE_FIRMWARE
                    | SCF_UGRADE_ARM | SCF_VOICE_TRAN | SCF_SET_EDID | SCF_RTC
                    | SCF_GET_TEMPERTURE | SCF_GET_HUMIDITY
                    | SCF_SET_BRIGHTNESS | SCF_SHOW_LOGO|SCF_VIDEO_SOURCE);
        }
        return 0;

    }

    public static int getSupportedFeatures(SenderInfo senderInfo)
    {
        if (senderInfo == null)
        {
            return -1;
        }
        return getSupportedFeatures(senderInfo.getSenderType(),
                senderInfo.getMajorVersion(), senderInfo.getMinorVersion());

    }

    public static boolean haveFeature(int allfeature, int feature)
    {
        return (allfeature & feature) == feature;
    }

    /**
     * 是T7卡
     * @param senderCardType
     * @return
     */
    public static boolean isT7(String senderCardType)
    {
        return senderCardType.equalsIgnoreCase(SenderInfo.T7);
    }

    /**
     * 是IT7卡
     * @param senderCardType
     * @return
     */
    public static boolean isIT7(String senderCardType)
    {
        return senderCardType.equalsIgnoreCase(SenderInfo.IT7);
    }

    /**
     * 是iQ7卡
     * @param senderCardType
     * @return
     */
    public static boolean isIQ7(String senderCardType)
    {
        return senderCardType.equalsIgnoreCase(SenderInfo.IQ7);
    }

    /**
     * 是iQ7E卡
     * @param senderCardType
     * @return
     */
    public static boolean isIQ7E(String senderCardType)
    {
        return senderCardType.equalsIgnoreCase(SenderInfo.IQ7E);
    }

    public static void getFlashAddressAndOpType(SC_PARAM_POSITION paramType,
            String senderCardType, int majorVersion, int minorVersion,
            FlashAddressAndOpType flashAddressAndOpType)
    {

        switch (paramType)
        {
            case scpp_basic_param:
                flashAddressAndOpType.startPos = 5 * 64 * 1024;
                flashAddressAndOpType.length = 64 * 1024;
                flashAddressAndOpType.b4kb = false;
                flashAddressAndOpType.bInSecondFlash = false;
                break;
            case scpp_edid:
                flashAddressAndOpType.startPos = 7 * 64 * 1024;
                flashAddressAndOpType.length = 256;
                flashAddressAndOpType.bInSecondFlash = false;
                if (isT7(senderCardType) || isIQ7E(senderCardType))
                {
                    flashAddressAndOpType.b4kb = false;
                }
                break;
            case scpp_brightness:
                flashAddressAndOpType.startPos = 3 * 64 * 1024;
                flashAddressAndOpType.length = 7;
                flashAddressAndOpType.bInSecondFlash = false;
                if (isT7(senderCardType) || isIQ7E(senderCardType))
                {
                    flashAddressAndOpType.b4kb = false;
                }

                break;
        }

    }

    class FlashAddressAndOpType
    {
        int startPos;

        int length;

        boolean b4kb;

        boolean bInSecondFlash;
    }
}
