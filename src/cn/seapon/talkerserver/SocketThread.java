package cn.seapon.talkerserver;

import java.io.IOException;
import java.net.Socket;

import cn.seapon.talkerserver.controller.MessageReader;

/**
 * 套接字处理线程
 * @author linzh_000
 */
public class SocketThread extends Thread{
	/**
	 * 与客户端连接的线程
	 */
	private Socket socket = null;

	public SocketThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				//TODO:处理与客户端套接字的通信
				MessageReader.read(socket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(null != this.socket) {
				try {
					this.socket.close();
				} catch (IOException e) {
					ActionNerve.logger.severe("关闭请求套接字失败："+e.getMessage());
				}
			}
			
		}
	}
	
	
}
