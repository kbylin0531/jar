package cn.seapon.talkerserver.model;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2015年1月4日 下午10:56:35 
 * 类说明 
 */
public class ThreadManager {

	private static HashMap<String, Socket> socketPool = new HashMap<String, Socket>();

	/**
	 * 将连接的账户加入套接字池中
	 * @param account
	 * @param socket
	 */
	public static void add(String account,Socket socket){
		socketPool.put(account, socket);
	}
	
	public static String[] getKeys(){
		return socketPool.keySet().toArray(new String[socketPool.size()]);
	}
	
	/**
	 * 光比套接字并从池子中删除
	 * @param account
	 */
	public static boolean delete(String account) {
		Socket socket = null;
		if ((socket = socketPool.remove(account)) != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
		}else {//套接字不存在
		}
		return true;
	}
	
	public static boolean exist(String key){
		return socketPool.containsKey(key);
	}
	
	public static Socket get(String account){
		return socketPool.get(account);
	}

}
