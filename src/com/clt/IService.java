package com.clt;

import java.util.LinkedHashMap;

import android.os.Handler;

import com.clt.commondata.SenderParameters;
import com.clt.entity.ConnectionParam;
import com.clt.entity.Program;
import com.clt.entity.ReceiverSettingInfo;

/**
 * 服务
 */
public interface IService 
{
    
    /**
     * 设置handler
     * @param nmHandler
     */
    void setNmHandler(Handler nmHandler);
    
    /**
     * 服务器地址改变
     * 
     * @param newServerAddress
     */
    void onSeverAddressChanged(String newServerAddress);
    
    /**
     * 判断当前的Socket对象是否处于连接状态
     * @return
     */
    public boolean isConnecting();
    
    
    ///////////////////////所有的功能///////////////////////////////////////////////
    
    /**
     * 查找终端，开启线程
     */
    void searchTerminate();
    
    /**
     * 
     */
    void searchTerminateByTcpLoop(Object callback);
    
    /**
     * 设置亮度
     * @param bright
     */
    void SetBrightness(int bright);

    /**
     * 设置色温
     * @param colorTemp
     */
    void SetColorTemp(int colorTemp);

    /**
     * 设置色温RGB
     * @param colorTemp
     */
    void SetColorTempRGB(int colorTempR, int colorTempG, int colorTempB);

    /**
     * 设置开关
     * @param bShowOn
     */
    void SetShowOnOff(boolean bShowOn);

    /**
     * 播放节目
     * @param index
     */
    void setPlayProgram(Program program);

    /**
     * 删除节目
     * @param program
     */
    void deletePlayProgram(Program program);

    /**
     * 设置测试模式
     * @param index
     */
    void setTestMode(int index);

    /**
     * 探卡
     */
    void DetectSender();

    /**
     * 设置发送卡分辨率
     * @param width
     * @param height
     */
    void setSenderResolution(int width, int height, int freq);

    /**
     * 保存亮度
     */
    void saveBright(int bright);

    /**
     * 保存色温
     * @param colorTemp
     */
    void saveColorTemp(int colorTemp);

    /**
     * 保存亮度和色温
     * @param bright
     * @param colorTemp
     */
    void saveBrightAndColorTemp(int bright, int colorTemp);

    /**
     * 保存亮度和色温 RGB
     * @param bright
     * @param r
     * @param g
     * @param b
     */
    void saveBrightAndColorTemp(int bright, int r, int g, int b);

    /**
     * RGB转成色温
     */
    int RGB2ColorTemp(int r, int g, int b);

    /**
     * 色温转成RGB
     * @param colorTemp
     */
    void colorTemp2RGB(int colorTemp);

    /**
     * 分时亮度设置
     * @param maps
     */
    void setDayPeriodBright(LinkedHashMap<String, Integer> maps);

    /**
     * 获取节目名称
     */
    void getProgramsNames();


    /**
     * 发送基本参数到发送卡
     * @param connectionParam
     */
    void setConnectionToSenderCard(ConnectionParam connectionParam);

    /**
     * 固化连接关系到接收卡
     * @param connectionParam
     */
    void setConnectionToReceiverCard(ConnectionParam connectionParam);

    /**
     * 发送接收卡基本参数
     * @param receiverSettingInfo
     */
    void setReceiverCardInfoSend(ReceiverSettingInfo receiverSettingInfo);

    /**
     * 固化接收卡基本参数
     * @param receiverSettingInfo
     */
    void setReceiverCardInfoSaveToReceiver(
            ReceiverSettingInfo receiverSettingInfo);

    /**
     * 获得接收卡参数信息，从bin文件中获取
     */
    void getReceiverCardInfo();

    /**
     * 设置发送卡基本参数
     * @param params
     */
    void setBasicParams(SenderParameters params);

    /**
     * 修改终端名
     * @param termName
     */
    void modifyTermName(String termName);

    /**
     * 获取磁盘使用，版本等信息
     * @param someInfo
     */
    void getSomeInfo();
}
