package com.clt.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
 * 查找服务端的线程，UDP组播
 * 
 */
public class UDPConnectorMulticastImpl implements UDPConnector
{
    private Handler mHandler;

    private static final int TIME_OUT = 6000;// 超时时间

    private ArrayList<LedTerminateInfo> terminateList;// 获取的终端信息

    private SendDataThread sendDataThread;

    private ReciverDataThread reciverDataThread;

    private Context context;

    private MulticastSocket searchsocket;

    final static int RECEIVE_LENGTH = 1024 * 3;// 3Kb

    static String multicastHost = "224.0.0.255";

    static int localPort = 9998;

    InetAddress group;

    public UDPConnectorMulticastImpl(Context context)
    {
        try
        {
            terminateList = new ArrayList<LedTerminateInfo>();
            group = InetAddress.getByName(multicastHost);
            if (!group.isMulticastAddress())
            {// 测试是否为多播地址
                throw new Exception("请使用多播地址");

            }
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    public void stop()
    {
        if (reciverDataThread != null)
        {
            reciverDataThread.cancel();
        }
        if (searchsocket != null)
        {
            searchsocket.close();
        }
    }

    class SendDataThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {

                searchsocket = new MulticastSocket();
                searchsocket.joinGroup(group);
                setSocketOptions();

                /**
                 *  UDP广播，发送消息 {"mType":71}
                 */
                NMFindTerminate findTerminate = new NMFindTerminate();
                Gson gson = new Gson();
                String strFindTerminateCmd = gson.toJson(findTerminate);
                byte [] data = strFindTerminateCmd.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(data,
                        data.length, group, localPort);

                reciverDataThread = new ReciverDataThread();
                reciverDataThread.start();
                // 发5次广播
                for (int i = 0; i < 10; i++)
                {
                    searchsocket.send(sendPacket);
                    Thread.sleep(600);
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
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ReciverDataThread extends Thread
    {
        private boolean running = true;

        public void run()
        {

            /**
             * 接受广播
             */
            byte [] getBuf = new byte [1024];
            DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);
            try
            {

                while (running)
                {
                    long start = System.currentTimeMillis();
                    searchsocket.receive(getPacket);
                    long end = System.currentTimeMillis();
                    Log.i("connect Time", end - start + "");
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

                        // 判断集合中是否包含
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
                    if (mHandler != null)
                    {
                        mHandler.sendMessage(newMsg);
                    }

                }// while结束
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
                FileLogger.getInstance(context).writeMessageToFile(
                        "查找服务器异常" + e.getMessage());
            }
            catch (IOException e)
            {
                if (e instanceof SocketTimeoutException)
                {
                    Message newMsg = new Message();
                    newMsg.what = Const.endFindTerminateResult;
                    mHandler.sendMessage(newMsg);
                    FileLogger.getInstance(context).writeMessageToFile(
                            "查找服务器超时结束" + e.getMessage());
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (searchsocket != null)
                {
                    try
                    {
                        searchsocket.leaveGroup(group);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    searchsocket.close();
                    searchsocket = null;
                }
            }

        }

        public void cancel()
        {
            this.running = false;
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

    @Override
    public void setHandler(Handler mHandler)
    {
        this.mHandler=mHandler;
    }

}
