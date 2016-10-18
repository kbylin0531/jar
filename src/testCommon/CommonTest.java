package testCommon;

import cn.seapon.talkerserver.common.ChatMsgEntity;
import cn.seapon.talkerserver.common.FriendListInfomation;
import cn.seapon.talkerserver.common.LoginRequestEntity;
import cn.seapon.talkerserver.common.MessageContent;
import cn.seapon.talkerserver.common.MessageGenre;
import cn.seapon.talkerserver.common.MessageReceiver;
import cn.seapon.talkerserver.common.TMessage;
import cn.seapon.talkerserver.common.UserInformation;
import cn.seapon.talkerserver.common.FriendListInfomation.FriendInfomation;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2015年1月4日 上午9:33:03 
 * 类说明 
 */
public class CommonTest {
	
	private static String string = "{'senderId':'我是sender ID','genre':30,'content':{'userInfo':{'signature':'你的个性签名是GGGGGGGG','phone':'15658070289','nickname':'你的昵称是YYYY','account':'你的账号信息是XXX','email':'784855684@qq.com'},'friendInfos':[{'nickname':'高晓松','online':true,'lastIOnline':123456789,'id':'好友1的ID','remarkname':'小高'},{'nickname':'高晓波','online':false,'lastIOnline':987654321,'id':'好友2的ID','remarkname':'大高'}],'resultCode':true,'chatMsgEntity':{'posttime':1420336367628,'senderId':'这是ChatMsgEntity的发送者ID','receivers':['ChatMsgEntity接受者1','ChatMsgEntity接受者1','ChatMsgEntity接受者1','ChatMsgEntity接受者1','ChatMsgEntity接受者1'],'type':4,'content':'这是ChatMsgEntity消息的内容'},'loginEntity':{'passwd':'sds5415fd1fdf1d2z5sf1d5s1fecz','captcha':'XDEF','type':1,'account':'登录账号'}},'sendTime':1420336367625}";
//	
//	public static void main(String[] args) {
//		TMessage tMessage = getObj();
//		String jsonString = tMessage.getJsonObject().toString();
//		System.out.println(jsonString);
//		try {
//			JSONObject jsonObject = new JSONObject(string);
//			TMessage tMessage2 = TMessage.getObjectFromJson(jsonObject);
//			
//			String jsonString2 = tMessage2.getJsonObject().toString();
//			System.out.println(jsonString2);
//			
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static TMessage getObj() {
		TMessage tMessage = new TMessage();
		tMessage.setGenre(MessageGenre.CHAT_MESSAGE);
		tMessage.setSenderId("我是sender ID");
		tMessage.setSendTime(System.currentTimeMillis());
			MessageContent messageContent = new MessageContent();
							ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
								chatMsgEntity.setContent("这是ChatMsgEntity消息的内容");
								chatMsgEntity.setPosttime(System.currentTimeMillis());
								chatMsgEntity.setSenderId("这是ChatMsgEntity的发送者ID");
								chatMsgEntity.setType(ChatMsgEntity.ChatMsgType.CHAT_END);
								messageContent.setChatMsgEntity(chatMsgEntity);
							MessageReceiver receivers = new MessageReceiver();
									receivers.addReceiver("ChatMsgEntity接受者1");
									receivers.addReceiver("ChatMsgEntity接受者1");
									receivers.addReceiver("ChatMsgEntity接受者1");
									receivers.addReceiver("ChatMsgEntity接受者1");
									receivers.addReceiver("ChatMsgEntity接受者1");
							chatMsgEntity.setReceivers(receivers);
							FriendListInfomation infos = new FriendListInfomation();
									FriendInfomation info = new FriendInfomation();
										info.setId("好友1的ID");
										info.setLastIOnline(123456789);
										info.setNickname("高晓松");
										info.setOnline(true);
										info.setRemarkname("小高");
									infos.add(info);
									FriendInfomation info2 = new FriendInfomation();
										info2.setId("好友2的ID");
										info2.setLastIOnline(987654321);
										info2.setNickname("高晓波");
										info2.setOnline(false);
										info2.setRemarkname("大高");
									infos.add(info2);
						messageContent.setFriendInfos(infos);
						LoginRequestEntity loginEntity = new LoginRequestEntity();
							loginEntity.setAccount("登录账号");
							loginEntity.setCaptcha("XDEF");
							loginEntity.setPasswd("sds5415fd1fdf1d2z5sf1d5s1fecz");
							loginEntity.setType(LoginRequestEntity.AccountType.EMAIL_TYPE);
						messageContent.setLoginEntity(loginEntity);
						messageContent.setResultCode(true);
						UserInformation userinfo = new UserInformation();
							userinfo.setAccount("你的账号信息是XXX");
							userinfo.setEmail("784855684@qq.com");
							userinfo.setNickname("你的昵称是YYYY");
							userinfo.setPhone("15658070289");
							userinfo.setSignature("你的个性签名是GGGGGGGG");
						messageContent.setUserInfo(userinfo);
		tMessage.setContent(messageContent);
		return tMessage;
	}

}
