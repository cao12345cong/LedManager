package com.clt.service;

import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.clt.IService;
import com.clt.Interceptor;
import com.clt.OnHandlerMessageListener;
import com.clt.TCPConnector;
import com.clt.UDPConnector;
import com.clt.activity.Application;
import com.clt.commondata.SenderParameters;
import com.clt.entity.ConnectionParam;
import com.clt.entity.Program;
import com.clt.entity.ReceiverSettingInfo;
import com.clt.netmessage.NMBase;
import com.clt.netmessage.NMChangeTermName;
import com.clt.netmessage.NMDeleteProgram;
import com.clt.netmessage.NMDetectSender;
import com.clt.netmessage.NMGetProgramsNames;
import com.clt.netmessage.NMGetReceiverCardInfo;
import com.clt.netmessage.NMGetSomeInfo;
import com.clt.netmessage.NMSaveBrightAndColorTemp;
import com.clt.netmessage.NMSaveSenderBright;
import com.clt.netmessage.NMSaveSenderColorTemp;
import com.clt.netmessage.NMSetConnectionToReceiverCard;
import com.clt.netmessage.NMSetConnectionToSenderCard;
import com.clt.netmessage.NMSetDayPeriodBright;
import com.clt.netmessage.NMSetEDID;
import com.clt.netmessage.NMSetPlayProgram;
import com.clt.netmessage.NMSetReceiverCardInfoSaveToReceiver;
import com.clt.netmessage.NMSetReceiverCardInfoSender;
import com.clt.netmessage.NMSetSenderBasicParameters;
import com.clt.netmessage.NMSetSenderBright;
import com.clt.netmessage.NMSetSenderColorTemp;
import com.clt.netmessage.NMSetSenderColorTempRGB;
import com.clt.netmessage.NMSetSenderShowOnOff;
import com.clt.netmessage.NMSetTestMode;
import com.clt.netmessage.NetMessageType;
import com.clt.service.HeartBreakHanlder.OnHeartBreakListener;
import com.clt.service.TCPFindTerminal.OnCallBack;
import com.clt.util.CommonUtil;
import com.clt.util.Const;
import com.clt.util.NetUtil;

/**
 * 作用：1.作为Connector的容器  2.对发送的消息打包，并交给Connector发送，3.处理Connector接收到的消息
 */
public class NetService extends BaseService implements
        OnHandlerMessageListener, OnHeartBreakListener
{
    private static String TAG = "ManagerNetService";

    private static final boolean DEBUG = true;

    private Application app;// app

    private Handler nmHandler;// mHandler引用

    private UDPConnector searchServer;// UDP查找服务端

    private TCPConnector connector;// tcp连接器
    
    private TCPFindTerminal tcpFindTerminal;//TCP查找服务器

    private Interceptor interceptor;// 拦截器

    private HeartBreakHanlder heartBreakHanlder;// 心跳处理器

    private Clock clock;// 计算连接时长

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = (Application) getApplication();
        
        // tcp连接器
        connector = new TCPConnectorImpl(this);
        connector.setOnHandlerMessageListener(this);

        // 心跳处理
        heartBreakHanlder = new HeartBreakHanlder(connector);
        heartBreakHanlder.setOnHeartBreakListener(this);
        // 计时
        clock = new Clock(this);

        // 消息拦截器
        interceptor = new InterceptorImpl();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (connector != null)
        {
            connector.stop();
        }
        if (searchServer != null)
        {
            searchServer.stop();
        }
        if(tcpFindTerminal!=null){
        	tcpFindTerminal.stop();
        }
    }

    /**
     * 服务器地址改变
     * 
     * @param newServerAddress
     */
    public void onSeverAddressChanged(String newServerAddress)
    {
        try
        {
            if (connector.isConnecting())
            {
                connector.stop();

            }
            connector.setIpAddress(newServerAddress);
            connector.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前的Socket对象是否处于连接状态
     * @return
     */
    public boolean isConnecting()
    {
        return connector.isConnecting();
    }
    
    /**
     * 设置handler
     * @param nmHandler
     */
    public void setNmHandler(Handler nmHandler)
    {
        this.nmHandler = nmHandler;
    }
    /**
     * 查找终端，开启线程
     */
    public void searchTerminate()
    {
        if (searchServer != null)
        {
            searchServer.stop();
            searchServer = null;
        }
        //UDP
        searchServer = new UDPConnectorImpl(this);
        searchServer.setHandler(this.nmHandler);
        searchServer.start();
    }
    
    @Override
	public void searchTerminateByTcpLoop(Object callback)
	{
    	if(callback instanceof TCPFindTerminal.OnCallBack){
    		if(tcpFindTerminal!=null&&tcpFindTerminal.isRunning()){
    			tcpFindTerminal.stop();
    			tcpFindTerminal=null;
    		}
    		tcpFindTerminal=new TCPFindTerminal(this);
    		tcpFindTerminal.setOnCallBack((OnCallBack)callback);
    		tcpFindTerminal.start();
    	}
	}
    
    /**
     * 请求命令，添加消息到消息队列
     * 
     * @param nmBase
     */

    public void addOutNetMessage(NMBase nmBase)
    {
        // 网络连接正常
        if (connector.isConnecting() && app.isOnline()
                && NetUtil.isWifiConnect(this))
        {
            //消息拦截
            if(interceptor!=null){
                interceptor.filterRequest(nmBase);
            }
            nmBase.setNetType(NMBase.WIFI);
            connector.addOneMessage(nmBase);
            return;
        }
        //网络连接失败
        if (nmHandler != null)
        {
            // 避免调节亮度时重复显示提示框
            int type = nmBase.getmType();
            if (type == NetMessageType.SetSenderBright
                    || type == NetMessageType.SetColorTemperture)
            {
                return;
            }
            nmHandler.obtainMessage(Const.connnectFail).sendToTarget();
        }
    }
  

    /**
     *  响应命令，处理
     */
    @Override
    public void handlerMessage(String readMsg)
    {
        try
        {
            //消息拦截
            if(interceptor!=null){
                interceptor.filterResponse(readMsg);
            }
            //
            JSONObject jsonObject = new JSONObject(readMsg);
            int nmType = jsonObject.getInt("mType");
            // dealRecevierMessage(readMsg, nmType);
            if (nmType == NetMessageType.ConnectSuccess)// 连接成功
            {

                heartBreakHanlder.start();
                clock.start();
            }
            else if (nmType == NetMessageType.KickOutOf)// 被踢
            {
                connector.stop();
                clock.stop();

            }else if(nmType==Const.connectBreak){
            	clock.stop();
            }
            heartBreakHanlder.recevierOneHeartBreak();
            nmHandler.obtainMessage(nmType, readMsg).sendToTarget();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 回调  心跳接收失败
     */
    @Override
    public void onReceiverHeartBreakFail()
    {
        connector.stop();
        heartBreakHanlder.stop();
        sendBroadcast(new Intent(Const.CONNECT_BEARK_ACTION));
        clock.stop();

    }
    // ///////////////////////////////////////////////////////////////////////////////////////
    ///////////              处理各种命令                                                                                                 /////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////
    /**
     * 设置亮度
     * @param bright
     */
    public void SetBrightness(int bright)
    {
        NMSetSenderBright nmSetSenderBright = new NMSetSenderBright();
        nmSetSenderBright.setBright(bright);
        addOutNetMessage(nmSetSenderBright);
    }

    /**
     * 设置色温
     * @param colorTemp
     */
    public void SetColorTemp(int colorTemp)
    {
        NMSetSenderColorTemp nmSetSenderColorTemp = new NMSetSenderColorTemp();
        nmSetSenderColorTemp.setColorTemp(colorTemp);
        addOutNetMessage(nmSetSenderColorTemp);
    }

    /**
     * 设置色温RGB
     * @param colorTemp
     */
    public void SetColorTempRGB(int colorTempR, int colorTempG, int colorTempB)
    {
        NMSetSenderColorTempRGB nmSetSenderColorTempRGB = new NMSetSenderColorTempRGB();
        nmSetSenderColorTempRGB.setColorTempR(colorTempR);
        nmSetSenderColorTempRGB.setColorTempG(colorTempG);
        nmSetSenderColorTempRGB.setColorTempB(colorTempB);
        addOutNetMessage(nmSetSenderColorTempRGB);
    }

    /**
     * 设置开关
     * @param bShowOn
     */
    public void SetShowOnOff(boolean bShowOn)
    {
        NMSetSenderShowOnOff nmSetSenderShowOnOff = new NMSetSenderShowOnOff();
        nmSetSenderShowOnOff.setShowOn(bShowOn);
        addOutNetMessage(nmSetSenderShowOnOff);
    }

    /**
     * 播放节目
     * @param index
     */
    public void setPlayProgram(Program program)
    {
        NMSetPlayProgram nmSetPlayProgram = new NMSetPlayProgram();
        nmSetPlayProgram.setProgram(program);
        addOutNetMessage(nmSetPlayProgram);
    }

    /**
     * 删除节目
     * @param program
     */
    public void deletePlayProgram(Program program)
    {
        NMDeleteProgram nmDeleteProgram = new NMDeleteProgram();
        nmDeleteProgram.setProgram(program);
        addOutNetMessage(nmDeleteProgram);

    }

    /**
     * 设置测试模式
     * @param index
     */
    public void setTestMode(int index)
    {
        NMSetTestMode nmSetTestMode = new NMSetTestMode();
        nmSetTestMode.setIndex(index);
        addOutNetMessage(nmSetTestMode);
    }

    /**
     * 探卡
     */
    public void DetectSender()
    {
        NMDetectSender nmDetectSender = new NMDetectSender();
        addOutNetMessage(nmDetectSender);
    }

    /**
     * 设置发送卡分辨率
     * @param width
     * @param height
     */
    public void setSenderResolution(int width, int height, int freq)
    {
        NMSetEDID nmSetEdid = new NMSetEDID();
        nmSetEdid.setWidth(width);
        nmSetEdid.setHeight(height);
        nmSetEdid.setFreq(freq);
        addOutNetMessage(nmSetEdid);
    }

    /**
     * 保存亮度
     */
    public void saveBright(int bright)
    {
        NMSaveSenderBright nmSaveSenderBright = new NMSaveSenderBright();
        nmSaveSenderBright.setBright(bright);
        addOutNetMessage(nmSaveSenderBright);
    }

    /**
     * 保存色温
     * @param colorTemp
     */
    public void saveColorTemp(int colorTemp)
    {
        NMSaveSenderColorTemp nmSaveSenderColorTemp = new NMSaveSenderColorTemp();
        nmSaveSenderColorTemp.setColorTemp(colorTemp);
        addOutNetMessage(nmSaveSenderColorTemp);
    }

    /**
     * 保存亮度和色温
     * @param bright
     * @param colorTemp
     */
    public void saveBrightAndColorTemp(int bright, int colorTemp)
    {
        NMSaveBrightAndColorTemp saveBrightAndColorTemp = new NMSaveBrightAndColorTemp();
        saveBrightAndColorTemp.setBright(bright);
        saveBrightAndColorTemp.setColorTemp(colorTemp);
        addOutNetMessage(saveBrightAndColorTemp);
    }

    public void saveBrightAndColorTemp(int bright, int r, int g, int b)
    {
        int colorTemp = RGB2ColorTemp(r, g, b);
        saveBrightAndColorTemp(bright, colorTemp);
    }

    /**
     * RGB转成色温
     */
    public int RGB2ColorTemp(int r, int g, int b)
    {
        double total = 0.66697 * r + 1.13240 * g + 1.20063 * b;
        double x = (0.341427 * r + 0.188273 * g + 0.390202 * g) / total;
        double y = (0.138972 * r + 0.837182 * g + 0.073588 * b) / total;
        double z = (0.0375154 * g + 2.038878 * b) / total;
        double n = (x - 0.3320) / (y - 0.1858);
        double T = -437 * Math.pow(n, 3) + 3601 * Math.pow(n, 2) - 6861 * n
                + 5514.31;

        if (r == g && g == b && r != 0)
            T = 6500;
        if (r == 0 && g == 0 && b == 0)
            T = 0;
        if (T < 0)
            T = 0;
        return CommonUtil.double2Round(T);
    }

    /**
     * 色温转成RGB
     * @param colorTemp
     */
    public void colorTemp2RGB(int colorTemp)
    {
        float r = 0.0f, g = 0.0f, b = 0.0f;
        int rr = CommonUtil.getRounding(r * 255);
        int gg = CommonUtil.getRounding(g * 255);
        int bb = CommonUtil.getRounding(b * 255);

    }

    /**
     * 分时亮度设置
     * @param maps
     */
    public void setDayPeriodBright(LinkedHashMap<String, Integer> maps)
    {
        NMSetDayPeriodBright nmSetDayPeriodBright = new NMSetDayPeriodBright();
        nmSetDayPeriodBright.setMaps(maps);
        addOutNetMessage(nmSetDayPeriodBright);

    }

    /**
     * 获取节目名称
     */
    public void getProgramsNames()
    {
        NMGetProgramsNames nmGetProgramsNames = new NMGetProgramsNames();
        addOutNetMessage(nmGetProgramsNames);
    }

    /**
     * 发送基本参数到发送卡
     * @param connectionParam
     */
    public void setConnectionToSenderCard(ConnectionParam connectionParam)
    {
        NMSetConnectionToSenderCard nmSetConnectionToSenderCard = new NMSetConnectionToSenderCard();
        nmSetConnectionToSenderCard.setConnectionParam(connectionParam);
        addOutNetMessage(nmSetConnectionToSenderCard);
    }

    /**
     * 固化连接关系到接收卡
     * @param connectionParam
     */
    public void setConnectionToReceiverCard(ConnectionParam connectionParam)
    {
        NMSetConnectionToReceiverCard nmSetConnectionToReceiverCard = new NMSetConnectionToReceiverCard();
        nmSetConnectionToReceiverCard.setConnectionParam(connectionParam);
        addOutNetMessage(nmSetConnectionToReceiverCard);
    }

    /**
     * 发送接收卡基本参数
     * @param receiverSettingInfo
     */
    public void setReceiverCardInfoSend(ReceiverSettingInfo receiverSettingInfo)
    {
        NMSetReceiverCardInfoSender nmSetReceiverCardInfo = new NMSetReceiverCardInfoSender();
        nmSetReceiverCardInfo.setFileName(receiverSettingInfo.getFileName());
        nmSetReceiverCardInfo.setBoxWidth(receiverSettingInfo.getWidth());
        nmSetReceiverCardInfo.setBoxHeight(receiverSettingInfo.getHeight());
        addOutNetMessage(nmSetReceiverCardInfo);
    }

    /**
     * 固化接收卡基本参数
     * @param receiverSettingInfo
     */
    public void setReceiverCardInfoSaveToReceiver(
            ReceiverSettingInfo receiverSettingInfo)
    {
        NMSetReceiverCardInfoSaveToReceiver nmSetReceiverCardInfo = new NMSetReceiverCardInfoSaveToReceiver();
        nmSetReceiverCardInfo.setFileName(receiverSettingInfo.getFileName());
        nmSetReceiverCardInfo.setBoxWidth(receiverSettingInfo.getWidth());
        nmSetReceiverCardInfo.setBoxHeight(receiverSettingInfo.getHeight());
        addOutNetMessage(nmSetReceiverCardInfo);
    }

    /**
     * 获得接收卡参数信息，从bin文件中获取
     */
    public void getReceiverCardInfo()
    {
        NMGetReceiverCardInfo nmGetReceiverCardInfo = new NMGetReceiverCardInfo();
        addOutNetMessage(nmGetReceiverCardInfo);
    }

    /**
     * 设置发送卡基本参数
     * @param params
     */
    public void setBasicParams(SenderParameters params)
    {
        NMSetSenderBasicParameters basicParams = new NMSetSenderBasicParameters();
        basicParams.setParams(params);
        addOutNetMessage(basicParams);
    }

    /**
     * 修改终端名
     * @param termName
     */
    public void modifyTermName(String termName)
    {
        NMChangeTermName nmChangeTermName = new NMChangeTermName();
        nmChangeTermName.setTermName(termName);
        addOutNetMessage(nmChangeTermName);

    }

    /**
     * 获取磁盘使用，版本等信息
     * @param someInfo
     */
    public void getSomeInfo()
    {
        NMGetSomeInfo nmGetSomeInfo = new NMGetSomeInfo();
        addOutNetMessage(nmGetSomeInfo);

    }

	

}