package cn.seapon.talkerserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import cn.seapon.talkerserver.utils.SocketThread;

public class ActionNerve {

	/**
	 * Socket服务端
	 */
	private static ServerSocket serverSocket = null;
	/**
	 * 日志记录器
	 */
	public static final Logger logger =Logger.getLogger("cn.seapon.talkerserver");
	
	private static ArrayList<Thread> threadList = new ArrayList<Thread>();
	
	/**
	 * 正在处理的连接个数
	 */
	private static short socketSum = 0;
	/**
	 * 服务线程
	 */
	private static Thread serviceThread = null;
	
	/**
	 * 获取链接到此的套接字的数量
	 * @return
	 */
	public static short getSocketSum() {
		return socketSum;
	}
	public static void increaseSocketSum() {
		++ socketSum;
	}
	public static void decreaseSocketSum() {
		-- socketSum;
	}
	
	/**
	 * 开始服务
	 * @return void
	 */
	public static void startService() {
		serviceThread = new Thread(){
			@Override
			public void run() {
				//创建服务端套接字
				try {
					serverSocket = new ServerSocket(Property.getPortNum());
				} catch (IOException e1) {
					logger.severe("Action服务端套接字创建失败:"+e1.getMessage());
				}

				//服务端套接字等待请求
				while (!Thread.currentThread().isInterrupted()) {
					//获取连接的套接字
					Socket clientSocket;
					try {
						clientSocket = serverSocket.accept();
						Thread thread = new SocketThread(clientSocket);
						thread.start();
						logger.fine("接受一个套接字请求！");
						increaseSocketSum();
						threadList.add(thread);
					} catch (IOException e) {
						logger.severe("服务端套接字接收客户端请求异常:"+e.getMessage());
					}finally{
						stopService();
						for(Thread thread : threadList){
							thread.interrupt();
						}
						logger.fine("ServerSocket线程退出！");
					}
				}
			}
		};
		serviceThread.start();
	}
	
	/**
	 * 强制关闭
	 * 会导致accept()产生java.net.SocketException异常（java.net.SocketException子类）
	 * @return void
	 */
	public static void stopService() {
		if (serverSocket instanceof ServerSocket) {
			try {
				serverSocket.close();	
				logger.info("Action套接字服务成功关闭！");
			} catch (IOException e) {
				logger.severe("Action关闭服务端套接字出错："+e.getMessage());
			}
		}else{
			logger.info("Action套接字服务未开启！");
		}
		serviceThread.interrupt();
	}
	

}
