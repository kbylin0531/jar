package cn.seapon.talkerserver.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import cn.seapon.talkerserver.common.ChatMsgEntity;
import cn.seapon.talkerserver.common.FriendListInfomation;
import cn.seapon.talkerserver.common.FriendRequestEntity;
import cn.seapon.talkerserver.common.GroupInfo;
import cn.seapon.talkerserver.common.MessageContent;
import cn.seapon.talkerserver.common.MessageGenre;
import cn.seapon.talkerserver.common.TMessage;
import cn.seapon.talkerserver.common.UserInformation;
import cn.seapon.talkerserver.model.ManageLogin;
import cn.seapon.talkerserver.model.ThreadManager;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015年1月4日 下午10:55:07 类说明
 */
public class SocketThread extends Thread {
	private static final String unreadWriteUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=1006";
	private static final String saveFriendRequestUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=3002";
	// 与客户端连接的线程
	private Socket socket;

	public SocketThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String jsonString = SocketUtils.readStringFromSocket(socket);
				System.out.println("_读取到的是:" + jsonString);
				JSONObject json = new JSONObject(jsonString);
				TMessage tMessage = TMessage.getObjectFromJson(json);
				switch (tMessage.getGenre()) {
					// 处理用户登录
					case MessageGenre.LOGIN_REQUEST:
						if (!doWithLoginRequest(tMessage)) {
							//登录失败了，套接字已经关闭，线程退出
							return ;
						}
						
						break;
	
					case MessageGenre.CHAT_MESSAGE:
						doWithChatMessage(tMessage);
						break;
	
					case MessageGenre.LOGOUT_REQUEST:
						doWithLogoutRequest(tMessage);
						break;
	
					case MessageGenre.ADD_FRIEND:
						doWithAddFriend(tMessage);
						break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doWithLogoutRequest(TMessage tMessage) {
		String sender = tMessage.getSenderId();
		ThreadManager.delete(sender);
		System.out.println("亲爱的'" + sender + "'下线了");
	}

	private void doWithChatMessage(TMessage tMessage) {
		long startTime1 = System.currentTimeMillis();
		System.out.println("接收到数据：" + tMessage.getJSonString());
		boolean result = chatTMessageBuild(tMessage);
		if (result) {
			System.out.println("消息聊天数据成功处理");
			// 通知客户端处理成功
			reportOfChatSuccess(tMessage);
		} else {
			System.out.println("消息聊天数据处理失败");
			// 不通知，客户端4秒钟没收到表示失败
			reportOfChatFailed(tMessage);
		}
		long endTime1 = System.currentTimeMillis();
		System.out.println("执行时间为'" + (endTime1 - startTime1)
				+ "'微秒");
	}

	/**
	 * 处理用户登录请求
	 * 
	 * @param tMessage
	 * @return  boolean  处理结果，用于通知线程是否退出
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	 
	private boolean doWithLoginRequest(TMessage tMessage)
			throws UnsupportedEncodingException, IOException {
		long startTime = System.currentTimeMillis();
		// 返回给用户的信息
		TMessage tmlr = loginTMessageBuild(tMessage);
		SocketUtils.writeStringToSocket(socket,
				tmlr.getJSonString());
		System.out.println("返回给客户端的是：" + tmlr.getJSonString());
		// 根据是否成功登录 决定是否将该Socket加入HashMap中
		if (tmlr.getContent().getResultCode()) {
			// 加入socket管理中
			ThreadManager.add(tMessage.getSenderId(), socket);
			System.out.println(tMessage.getSenderId() + "成功加入线程池中");
			long endTime = System.currentTimeMillis();
			System.out
					.println("执行时间为'" + (endTime - startTime) + "'微秒");
			return true;
		} else {// 释放Socket连接j以及IO
			try {
				socket.close();// 参照源代码 同时会关闭它的IO流
			} catch (IOException e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			System.out
					.println("执行时间为'" + (endTime - startTime) + "'微秒");
			return false;
		}
	}

	private void doWithAddFriend(TMessage tMessage) throws JSONException,
			UnsupportedEncodingException, IOException {
		FriendRequestEntity entity = tMessage.getContent().getSingleFriendRequestEntity();
		String targetId = entity.getReceiver();
		boolean transtResult = false;
//		System.out.println("接收目标是："+targetId+"    "+ThreadManager.exist(targetId)+"   "+ThreadManager.get(targetId));
//		String[] arrayStrings = ThreadManager.getKeys();
//		for (int i = 0; i < arrayStrings.length; i++) {
//			System.out.println("||__"+arrayStrings[i]+"__||");
//		}
		if (ThreadManager.exist(targetId)) {
			// 在线，发消息
			Socket targetSocket = ThreadManager.get(targetId);
			if (!SocketUtils.writeStringToSocket(targetSocket,
					tMessage.getJSonString())) {
				System.out.println("转发加友请求成功！");
				transtResult = true;
			} else {
				System.out.println("转发加友请求失败！");
			}
		} else {// 不在线，写入数据库
			System.out.println("好友不在线，正将请求保存到数据库中！");
			transtResult = writeRequestToDatabase(entity);
		}
		//回复用户
		TMessage retMessage = new TMessage();
		retMessage.setGenre(MessageGenre.ADD_FRIEND_RESULT);
		MessageContent messageContent = new MessageContent();
		messageContent.setResultCode(transtResult);
		retMessage.setContent(messageContent);
		System.out.println("返回的是：" + retMessage.getJSonString());
		SocketUtils.writeStringToSocket(socket, retMessage.getJSonString());
	}

	/**
	 * 处理聊天消息
	 * 
	 * @param tMessage
	 * @return
	 */
	private boolean chatTMessageBuild(TMessage tMessage) {
		ChatMsgEntity entity = tMessage.getContent().getChatMsgEntity();
		String sender = entity.getSenderId();
		String content = entity.getContent();
		String receiver = entity.getReceivers().getTheReceiver();
		int type = entity.getType();
		long posttime = entity.getPosttime();

		Socket targetSocket = ThreadManager.get(receiver);

		if (targetSocket == null) {
			// 目标不在线 写入数据库中
			System.out.println("\n——————目标不在线 写入数据库中\n"
					+ entity.getJSONString());
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("sender", sender);
			hashMap.put("content", content);
			hashMap.put("receiver", receiver);
			hashMap.put("type", type + "");
			hashMap.put("posttime", posttime + "");
			try {
				String retString = HttpRequest.doPostRequest(unreadWriteUrl,
						hashMap);
				System.out.println("http返回的数据是：" + retString);
				if ("success".equals(retString)) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			// 目标在线 写入到输出流中
			System.out.println("\n——————目标在线 写入到输出流中");
			try {
				// 向指定的套接字中写入数据
				return SocketUtils.writeStringToSocket(targetSocket,
						tMessage.getJSonString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private TMessage loginTMessageBuild(TMessage tMessage) {
		ManageLogin manageLogin = new ManageLogin(tMessage);
		UserInformation userInformation = manageLogin.getLoginResult();
		TMessage tm = new TMessage();
		if (userInformation == null) {// 登录失败
			System.out.println("登录失败");
			MessageContent messageContent = new MessageContent();
			messageContent.setResultCode(false);

			tm.setContent(messageContent);

		} else {
			MessageContent messageContent = new MessageContent();
			// 设置响应吗
			messageContent.setResultCode(true);
			// 设置用户信息
			messageContent.setUserInfo(userInformation);
			// 好友列表
			FriendListInfomation friendInfos = manageLogin.getFriendInfos();
			// 检查好友在线情况
			messageContent.setFriendInfos(friendInfos);
			// 分组信息
			GroupInfo groupInfo = manageLogin.getGroupInfo();
			messageContent.setGroupInfo(groupInfo);
			// 未读取消息
			ChatMsgEntity[] unread = manageLogin.getUnread();
			//请求的消息-json字符串
			FriendRequestEntity[] entities = manageLogin.getRequestMessage(userInformation.getAccount());
			messageContent.setFriendRequest(entities);
			messageContent.setUnreads(unread);
			tm.setContent(messageContent);
		}
		long currentTime = new Date().getTime();// 服务器处理完毕的时间
		tm.setSendTime(currentTime);
		return tm;
	}

	private boolean writeRequestToDatabase(FriendRequestEntity entity)
			throws JSONException, IOException {
		String senderInfoString = entity.getInformation().getJSONString();
		System.out.println(senderInfoString);
		String receiver = entity.getReceiver();
		String text = entity.getText();
		HashMap<String, String> nameValuePair = new HashMap<String, String>();
		nameValuePair.put("senderInfo", senderInfoString);
		nameValuePair.put("receiver", receiver);
		nameValuePair.put("text", text);

		String retString = HttpRequest.doPostRequest(saveFriendRequestUrl,
				nameValuePair);
		if ("success".equals(retString)) {
			return true;
		}
		return false;
	}



	/**
	 * 聊天数据发送成功时返回给客户端的数据
	 * 
	 * @param tMessage
	 */
	private void reportOfChatSuccess(TMessage tMessage) {
		TMessage tm = new TMessage();
		tm.setGenre(MessageGenre.CHAT_SUCCESS);
		MessageContent content = new MessageContent();
		content.setChatMsgEntity(tMessage.getContent().getChatMsgEntity());
		tm.setContent(content);
		try {
			SocketUtils.writeStringToSocket(socket, tm.getJSonString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 聊天数据发送失败
	 */
	private void reportOfChatFailed(TMessage tMessage) {
		TMessage tm = new TMessage();
		tm.setGenre(MessageGenre.CHAT_FAILED);
		MessageContent content = new MessageContent();
		content.setChatMsgEntity(tMessage.getContent().getChatMsgEntity());
		tm.setContent(content);
		try {
			SocketUtils.writeStringToSocket(socket, tm.getJSonString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
