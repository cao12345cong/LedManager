package com.clt.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.clt.LifeCycle;
import com.clt.UDPConnector;
import com.clt.commondata.LedTerminateInfo;
import com.clt.commondata.LedTerminateInfoList;
import com.clt.netmessage.NMFindTerminate;
import com.clt.netmessage.NMFindTerminateAnswer;
import com.clt.util.Config;
import com.clt.util.Const;
import com.clt.util.FileLogger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 查找服务端的线程，UDP广播
 * 
 */
public class UDPConnectorImpl implements UDPConnector
{
    private Handler mHandler;

    private static final int TIME_OUT = 10000;// 超时时间

    private ArrayList<LedTerminateInfo> terminateList;// 获取的终端信息

    private SendDataThread sendDataThread;

    private ReciverDataThread reciverDataThread;
    
    private Context context;
    
    public UDPConnectorImpl(Context context)
    {
        this.context=context;
        terminateList = new ArrayList<LedTerminateInfo>();
    }

    public void setHandler(Handler mHandler){
        this.mHandler=mHandler;
    }
    /**
     * 开始查找
     */
    public void start()
    {
        sendDataThread = new SendDataThread();
        sendDataThread.start();
        FileLogger.getInstance(context).writeMessageToFile("开始查找服务端");
    }
    /**
     * 结束
     */
    public void stop(){
        if(reciverDataThread!=null){
           reciverDataThread.cancel();
        }
        terminateList = new ArrayList<LedTerminateInfo>();
    }

    /**
     * 发送线程
     */
    class SendDataThread extends Thread
    {
        private DatagramSocket searchsocket;
        @Override
        public void run()
        {
            try
            {
                
                searchsocket = new DatagramSocket(Config.UDP_RESOURCE_PORT);
                setSocketOptions();
                
                /**
                 *  UDP广播，发送消息 {"mType":71}
                 */
                NMFindTerminate findTerminate = new NMFindTerminate();
                Gson gson = new Gson();
                String strFindTerminateCmd = gson.toJson(findTerminate);
                byte [] data = strFindTerminateCmd.getBytes();

                String address=getNetNumIpAddress(context)+".255";
                DatagramPacket sendPacket = new DatagramPacket(data,
                        data.length, InetAddress.getByName(address),
                        Config.UDP_TARGET_PORT);

                reciverDataThread = new ReciverDataThread(searchsocket);
                reciverDataThread.start();
                // 发5次广播
                for (int i = 0; i < 10; i++)
                {
                    searchsocket.send(sendPacket);
                    Thread.sleep(500);
                }

            }
            catch (SocketException e)
            {
                e.printStackTrace();
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        /**
         * 设置socket选项
         */
        public void setSocketOptions()
        {
            try
            {
                searchsocket.setBroadcast(true);
                searchsocket.setSoTimeout(TIME_OUT);
                searchsocket.setReceiveBufferSize(Config.RECEVICE_BUF_SIZE);
                searchsocket.setSendBufferSize(Config.SEND_BUF_SIZE);
            }
            catch (SocketException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 获得本机Ip地址的前三位
	 * 
	 * @return
	 */
	public String getNetNumIpAddress(Context context)
	{
		try
		{
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (!wifiManager.isWifiEnabled())
			{
				wifiManager.setWifiEnabled(true);
			}
			
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			
			// 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109
			String ip = String.format("%d.%d.%d", (ipAddress & 0xff),
					(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff));
			return ip;
		}
		catch (Exception e)
		{
			return null;
		}
		
		
	}
    
    
    /**
     * 接收线程
     *
     */
    class ReciverDataThread extends Thread
    {
        private DatagramSocket searchsocket;
        
        private boolean running=true;

        public ReciverDataThread(DatagramSocket searchsocket)
        {
            this.searchsocket = searchsocket;
            
        }

        public void run()
        {

            /**
             * 接受广播
             */
            byte [] getBuf = new byte [512];
            DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);
            try
            {
                
                while(running)
                {
                    long start=System.currentTimeMillis();
                    searchsocket.receive(getPacket);
                    long end=System.currentTimeMillis();
                    Log.i("connect Time", end-start+"");
                    if (getPacket.getLength() <= 0)
                    {
                        continue;
                    }

                    String rcvMsg = new String(getBuf, 0, getPacket.getLength());
                    JSONObject jsonObject = new JSONObject(rcvMsg);
                    if (jsonObject.has("terminateName"))
                    {
                        Gson gson = new Gson();
                        NMFindTerminateAnswer findTerminateAnwer = gson
                                .fromJson(rcvMsg, NMFindTerminateAnswer.class);

                        // 192.168.11.100
                        String strIpAddressString = getPacket.getAddress()
                                .toString();
                        if (strIpAddressString != null
                                && strIpAddressString.length() > 1)
                        {
                            strIpAddressString = strIpAddressString
                                    .substring(1);
                        }
                        
                        //判断集合中是否包含
                        int count = terminateList.size();
                        int i = 0;
                        for (i = 0; i < count; i++)
                        {
                            LedTerminateInfo serverInfoTemp = terminateList
                                    .get(i);
                            if (serverInfoTemp.getIpAddress()
                                    .compareToIgnoreCase(strIpAddressString) == 0)
                            {
                                break;
                            }
                        }

                        if (i >= count)
                        {
                            LedTerminateInfo serverInfo = new LedTerminateInfo();
                            serverInfo.setIpAddress(strIpAddressString);
                            serverInfo.setPassword(findTerminateAnwer
                                    .getPassword());
                            serverInfo.setStrName(findTerminateAnwer
                                    .getTerminateName());

                            terminateList.add(serverInfo);
                        }
                    }

                    LedTerminateInfoList ledTerminateInfoList = new LedTerminateInfoList();
                    ledTerminateInfoList.setTerminateList(terminateList);

                    Gson gsons = new Gson();
                    String strTerminateList = gsons
                            .toJson(ledTerminateInfoList);

                    Message newMsg = new Message();
                    newMsg.what = Const.findTerminateResult;
                    newMsg.obj = strTerminateList;
                    if(mHandler!=null){
                        mHandler.sendMessage(newMsg);
                    }
                    

                }//while结束
            }
            catch (JsonSyntaxException e)
            {
                e.printStackTrace();
            }
            catch (SocketException e)
            {
                Message newMsg = new Message();
                newMsg.what = Const.endFindTerminateResult;
                mHandler.sendMessage(newMsg);
                FileLogger.getInstance(context).writeMessageToFile("查找服务器异常"+e.getMessage());
            }
            catch (IOException e)
            {
                if(e instanceof SocketTimeoutException){
                    Message newMsg = new Message();
                    newMsg.what = Const.endFindTerminateResult;
                    mHandler.sendMessage(newMsg);
                    FileLogger.getInstance(context).writeMessageToFile("查找服务器超时结束"+e.getMessage());
                }
                
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }finally{
                if (searchsocket != null)
                {
                    searchsocket.close();
                    searchsocket = null;
                }
            }

        }
        public void cancel(){
            this.running=false;
            if(searchsocket!=null){
                searchsocket.close();
            }

        }
    }
    

}
