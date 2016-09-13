package com.clt.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.clt.LifeCycle;
import com.clt.TCPConnector;
import com.clt.commondata.LedTerminateInfo;
import com.clt.netmessage.NMFindTerminate;
import com.clt.netmessage.NMFindTerminateAnswer;
import com.google.gson.Gson;

/**
 * TCP轮询查找服务器ip
 * 
 */
public class TCPFindTerminal implements LifeCycle
{
	private static final int TCP_FIND_TERM_PORT=9043;
	
	private Object lock=new Object();
	
	private CyclicBarrier barrier;// 闭锁
	
	private static final int THREAD_NUM = 4;//使用的线程数
	
	private ExecutorService executorService;//线程池
	
	private int port;
	
	private Context context;
	
	private OnCallBack onCallBack;
	
	private boolean running;
	
	public TCPFindTerminal(Context context, int port)
	{
		this.context = context;
		this.port = port;
		executorService= Executors.newFixedThreadPool(THREAD_NUM+1);
	}
	
	public TCPFindTerminal(Context context)
	{
		this(context, TCP_FIND_TERM_PORT);
	}
	
	public void setOnCallBack(OnCallBack onCallBack)
	{
		this.onCallBack = onCallBack;
		
	}
	
	public boolean isRunning(){
		
		return running;
	}
	
	public void start()
	{
		try
		{
			running=true;
			barrier = new CyclicBarrier(THREAD_NUM, new Runnable()
			{
				@Override
				public void run()
				{
					if(onCallBack!=null){
						onCallBack.onFindDone();
					}
					
					running=false;
				}
			});
			int count = 254 / THREAD_NUM;
			int start = 1, end = count;
			for (int i = 0; i < THREAD_NUM; i++)
			{
				if (i == THREAD_NUM - 1)
				{
					executorService.submit(new ConnectTask(start, 254));
				}
				else
				{
					executorService.submit(new ConnectTask(start, end));
				}
				start = end + 1;
				end = start + count - 1;
			}
		}
		catch (Exception e)
		{
		}
	}
	
	public void stop()
	{
		try
		{
			running = false;
			executorService.shutdown();
		}
		catch (Exception e)
		{
		}
		
	}
	
	public void getTermInfo(String ip)
	{
		try
		{
			TCPConnector tcpConnector = new TCPConnectorImpl(context, ip, port);
			tcpConnector.start();
			NMFindTerminate nmFindTerminate = new NMFindTerminate();
			tcpConnector.addOneMessage(nmFindTerminate);
		}
		catch (Exception e)
		{
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
	 * 连接线程
	 * 
	 */
	class ConnectTask implements Runnable
	{
		private int startIp;
		
		private int endIp;
		
		public ConnectTask(int startIp, int endIp)
		{
			this.startIp = startIp;
			this.endIp = endIp;
		}
		
		@Override
		public void run()
		{
			String netNumIp = getNetNumIpAddress(context);
			String ip = null;
			Socket socket = null;
			int total = 254;
			for (int i = startIp; i <= endIp; i++)
			{
				if (!running)
				{
					break;
				}
				try
				{
					ip = netNumIp + "." + i;
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), 100);
					
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					Gson gson = new Gson();
					pw.println(gson.toJson(new NMFindTerminate()));
					pw.flush();
					
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					String message = reader.readLine();
					
					NMFindTerminateAnswer answer = gson.fromJson(message,
							NMFindTerminateAnswer.class);
					LedTerminateInfo ledTerminateInfo = new LedTerminateInfo();
					ledTerminateInfo.setIpAddress(ip);
					ledTerminateInfo.setPassword(answer.getPassword());
					ledTerminateInfo.setStrName(answer.getTerminateName());
					synchronized (lock)
					{
						if(onCallBack!=null){
							onCallBack.onFindSucess(ledTerminateInfo);
						}
						
					}
				}
				catch (Exception e)
				{
					continue;
				}
				
			}
			try
			{
				barrier.await();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public interface OnCallBack
	{
		void onFindSucess(LedTerminateInfo ledTerminateInfo);
		
		void onFindDone();
	}
}
