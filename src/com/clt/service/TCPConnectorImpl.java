package com.clt.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clt.Interceptor;
import com.clt.OnHandlerMessageListener;
import com.clt.TCPConnector;
import com.clt.netmessage.NMBase;
import com.clt.netmessage.NMConnectSuccess;
import com.clt.netmessage.NMKickOutOf;
import com.clt.netmessage.NetMessageType;
import com.clt.service.HeartBreakHanlder.OnHeartBreakListener;
import com.clt.util.Config;
import com.clt.util.Const;
import com.clt.util.FileLogger;
import com.google.gson.Gson;
/**
 * TCP连接，并处理读和写
 *
 */
public class TCPConnectorImpl implements TCPConnector
{

    private String ip;
    
    private int port;
    
    private String newIp;

    private Socket socket;

    private InputHandler inputHandler;

    private OutputHandler outputHandler;

    private Context context;

    private static int CONNECT_TIME_OUT = 10000;// 10秒

    private ArrayList<NMBase> list = new ArrayList<NMBase>();

    private OnHandlerMessageListener onHandlerMessageListener;
    


    public TCPConnectorImpl(Context context,String ip,int port)
	{
    	this.context = context;
    	this.ip = ip;
    	this.port=port;
	}
    public TCPConnectorImpl(Context context)
	{
    	this.context = context;
    	this.port=Config.TCP_PORT;
	}
    @Override
    public void setIpAddress(String ip)
    {
        this.ip = ip;
    }

    
   

    /**
     * 开始
     */
    public void start()
    {
        ConnectThread connectThread = new ConnectThread();
        connectThread.start();
    }

    /**
     * 结束
     */
    public void stop()
    {
        if(inputHandler!=null){
            inputHandler.cancel();
        }
        if(outputHandler!=null){
            outputHandler.cancel();
        }
       
    }
////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 连接线程
     *
     */
    class ConnectThread extends Thread
    {

        @Override
        public void run()
        {
            try
            {
                initSocket();
                long startConnect = System.currentTimeMillis();
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port),
                        CONNECT_TIME_OUT);
                long endConnect = System.currentTimeMillis();
                FileLogger.getInstance(context).writeMessageToFile(
                        "连接用时" + String.valueOf(endConnect - startConnect));
                /**
                 * 连接成功
                 */
                if (isConnecting())
                {
                    setSocketOptions();
                    inputHandler = new InputHandler(socket);
                    outputHandler = new OutputHandler(socket);
                    inputHandler.start();
                    outputHandler.start();
                    
                    //更新状态，切换到在线
                    NMConnectSuccess nMConnectSuccess = new NMConnectSuccess();
                    Gson gson = new Gson();
                    String nmString = gson.toJson(nMConnectSuccess);
                    if (onHandlerMessageListener != null)
                    {
                        onHandlerMessageListener.handlerMessage(nmString);
                    }
                    
                    FileLogger.getInstance(context).writeMessageToFile(
                            "连接成功" + String.valueOf(endConnect - startConnect));
                    //发送心跳
                    
                }else{
                    FileLogger.getInstance(context).writeMessageToFile(
                            "连接失败" + String.valueOf(endConnect - startConnect));
                }
                

            }
            catch (IOException e)
            {
                e.printStackTrace();
                FileLogger.getInstance(context).writeMessageToFile(
                        "连接失败" + e.getMessage());
            }finally{
                FileLogger.getInstance(context).writeMessageToFile("ConnectThread退出");
            }

        }

    }

    public void initSocket()
    {
        
    }

    /**
     * 设置socket选项
     */
    public void setSocketOptions()
    {
        try
        {
            //所有包一就绪就能发送
            if(!socket.getTcpNoDelay()){
                socket.setTcpNoDelay(true);
            }
            //
            socket.setSoLinger(true, 0);
            socket.setReceiveBufferSize(Config.RECEVICE_BUF_SIZE);
            socket.setSendBufferSize(Config.SEND_BUF_SIZE);
            socket.setKeepAlive(true);
            socket.setReuseAddress(true);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isConnecting()
    {
        if(socket==null){
            return false;
        }
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     *
     * 读线程
     */
    class InputHandler extends Thread
    {
        //DataInputStream input;
        BufferedReader reader;

        String message;

        boolean running = true;

        public InputHandler(Socket socket)
        {
            try
            {
                reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //input = new DataInputStream(socket.getInputStream());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            try
            {
                while (running)
                {
                    //message = input.readUTF();
                    message=reader.readLine();
                    if (message != null)
                    {
                        if (onHandlerMessageListener != null)
                        {
                            onHandlerMessageListener.handlerMessage(message);
                        }
                        //Log.i("Input Message", message);
                        // 获得消息后，处理消息
                        FileLogger.getInstance(context).writeMessageToFile(
                                "收到消息" + message);

                    }

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }finally{
                if(reader!=null){
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                FileLogger.getInstance(context).writeMessageToFile("InputHandler退出");
            }
        }

        /**
         * 退出线程
         */
        public void cancel()
        {
            try
            {
                socket.shutdownInput();
                running = false;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
    *
    * 写线程
    */
    class OutputHandler extends Thread
    {
        //DataOutputStream output;
        PrintWriter writer;

        boolean running = true;

        public OutputHandler(Socket socket)
        {

            try
            {
                writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                //output = new DataOutputStream(socket.getOutputStream());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void run()
        {
            try
            {
                while (running)
                {
                    if (list.isEmpty())
                    {
                        synchronized (list)
                        {
                            try
                            {
                                list.wait();
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }

                    String message = null;
                    synchronized (list)
                    {
                        NMBase nmMsg = list.remove(0);
                        //退出线程
                        if(nmMsg.getmType()==NetMessageType.KickOutOf){
                            break;
                        }
                        Gson gson = new Gson();
                        message = gson.toJson(nmMsg);
                    }
                    writer.println(message);
                    writer.flush();
                    FileLogger.getInstance(context).writeMessageToFile(
                            "发送消息" + message);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                FileLogger.getInstance(context).writeMessageToFile("OutputHandler退出");
                if (writer != null)
                {
                    try
                    {
                        writer.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                if(socket!=null){
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        }

        /**
         * 
         * @param msg
         */
        public void pushString(NMBase nmBase)
        {
            synchronized (list)
            {
                int size = list.size();
                boolean bFind = false;
                for (int i = 0; i < size; i++)
                {
                    NMBase nmTemp = list.get(i);
                    if (nmTemp.getmType() == nmBase.getmType())
                    {
                        list.set(i, nmBase);
                        bFind = true;
                    }
                }
                if (!bFind)
                {
                    list.add(nmBase);
                }
                list.notifyAll();
            }
        }

        /**
         * 退出线程
         */
        public void cancel()
        {
            list.clear();
            pushString(new NMKickOutOf());
        }
    }

    /**
     * 向消息队列中添加一条消息
     * @param nmBase
     */
    public void addOneMessage(NMBase nmBase)
    {
         outputHandler.pushString(nmBase);
        
    }

    /**
     * 设置监听器
     * @param onHandlerMessageListener
     */
    @Override
    public void setOnHandlerMessageListener(
            OnHandlerMessageListener listener)
    {
        this.onHandlerMessageListener = listener;
    }


    
    
}
