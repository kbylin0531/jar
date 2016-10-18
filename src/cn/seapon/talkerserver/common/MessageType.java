package cn.seapon.talkerserver.common;

public class MessageType {

	/**
	 * 文本消息类型
	 * 默认消息类型
	 */
	public static final short MESSAGE_TYPE_TEXT = 0;

	//消息类型为用户登录 登录信息见MessageContent的loginEntity字段
	public final static short MESSAGE_TYPE_LOGIN_REQUEST = 10;
	//消息类型为登录返回消息，查看MessageContent的resultCode,和userInfo以及friendInfos,和groupinfos,和unread
	public final static short MESSAGE_TYPE_LOGIN_RESULT = 20;
	//聊天类型，聊天实体见MessageContent的chatMsgEntity字段
	public final static short MESSAGE_TYPE_CHAT_MESSAGE = 30;
	//聊天数据发送成功
	public final static short MESSAGE_TYPE_CHAT_SUCCESS = 60;
	//聊天数据发送失败
	public final static short MESSAGE_TYPE_CHAT_FAILED = 50;
	//添加好友,消息需要设置senderId字段
	public final static short MESSAGE_TYPE_ADD_FRIEND = 70;
	//好友添加消息发送结果
	public final static short MESSAGE_TYPE_ADD_FRIEND_RESULT = 90;
	//下线通知
	public final static short MESSAGE_TYPE_LOGOUT_REQUEST = 80;
	
	//获得和某人聊天的历史消息,服务端暂时不保存这些信息
	public final static short MESSAGE_TYPE_GET_HISTORY_MESSAGE = 40;
	
}
