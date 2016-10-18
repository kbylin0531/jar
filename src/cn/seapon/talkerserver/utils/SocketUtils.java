package cn.seapon.talkerserver.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import cn.seapon.talkerserver.ActionNerve;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2015年1月3日 下午2:24:01 
 * 类说明 
 */
public class SocketUtils {
	
	public static final String GUTAG = "__EOS__";

	/**
	 * 向指定的套接字中写入String
	 * @param socket
	 * @param string
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean writeStringToSocket(Socket socket,String string) {
		try {
			BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			//向服务器写入请求信息 这里使用一个对象进行消息的封装
			bfw.write(string);
			bfw.newLine();//换行
			bfw.write(GUTAG);
			bfw.newLine();
			bfw.flush();
			return true;
		}
		catch (UnsupportedEncodingException e) {
			string = "无法支持编码格式："+e.getMessage();
		} catch (IOException e) {
			string = "获取套接字的输出流出错："+e.getMessage();
		} catch (Exception e) {
			string = "套接字输出流写入器创建出错 ："+e.getMessage();
		}
		ActionNerve.logger.severe(string);
		return false;
	}
	
	/**
	 * 阻塞并从指定的套接字中读取字符串
	 * @param socket
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String readStringFromSocket(Socket socket) {
		BufferedReader bfr;
		try {
			bfr = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
		StringBuilder builder = new StringBuilder();
		//等待消息
		String line = null;
		while ((line = bfr.readLine()) != null) {
			//接收到EOF时表示发送结束
			if (GUTAG.equals(line)) {
				break;
			}
			builder.append(line);
		}
		return builder.toString();
	}
	
	
	
}
