package cn.seapon.talkerserver.common;


/**
 * 通信的消息实体
 * @author linzh
 */
public class MessageEntity {

	/**
	 * 消息发送者ID
	 */
    private String senderId = "";
    /**
     * 消息接收者ID
     * ID可以是群，也可以个人（多个人使用逗号分隔）
     * 接收者的账号ID的解析使用 MessageReceiverParser 可以解析
     */
    private String receivers = "";
    /**
     * 消息类型
     */
    private short messageType = MessageType.MESSAGE_TYPE_TEXT;
    /**
     * 消息内容
     * 内容使用MessageContentParser进行解析
     */
    private String messageContent = "";
    /**
     * 消息发送时间（客户端）
     */
    private long sendTime = 0;
    
    
    
    
    
    
    
}
