package cn.seapon.talkerserver.model;

import java.util.ArrayList;

import cn.seapon.talkerserver.common.MessageReceiver;
import cn.seapon.talkerserver.utils.HttpRequest;

public class ManageLogin {

	/**
	 * 登录验证和个人信息返回地址
	 */
	private String loginCheckUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=1002";
	/**
	 * 好友信息返回地址
	 */
	private String friendListUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=1003";
	/**
	 * 分组信息返回地址
	 */
	private String groupInfoUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=1004";
	/**
	 * 未读取得消息返回地址
	 */
	private String unreadMsgUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=1005";
	/**
	 * 上线是朋友请求消息返回地址
	 */
	private static final String readFriendRequestUrl = "http://1.lichtung.sinaapp.com/appserver/appservice.php?request_code=3003";

	private TMessage tMessage;
	private String userid = "";
	private LoginRequestEntity loginRequestEntity;

	// public static void main(String[] argv){
	// DoLogin dLogin = new DoLogin(new User("123456",
	// "e10adc3949ba59abbe56e057f20f883e"));
	// UserInfo userInfo = dLogin.getLoginResult();
	// }

	public ManageLogin(TMessage tMessage) {
		this.tMessage = tMessage;
		this.loginRequestEntity = tMessage.getContent().getLoginEntity();
		this.userid = loginRequestEntity.getAccount();
	}

	public UserInformation getLoginResult() {
		String passwd = loginRequestEntity.getPasswd();
		String urlString = loginCheckUrl + "&userid=" + userid + "&passwd="
				+ passwd;
		try {
			println("登录请求地址是:" + urlString);
			String retString = HttpRequest.doGetRequest(urlString);

			println("getLoginResult:" + retString);

			// 引入org.json包
			JSONObject jsonObject = new JSONObject(retString);
			// 判断用户是否存在
			if (jsonObject.has("userid")) {
				UserInformation userInformation = new UserInformation(
						jsonObject.getString("userid"),
						jsonObject.getString("tinyname"),
						jsonObject.getString("signature"),
						jsonObject.getString("phone"),
						jsonObject.getString("email"));
				userInformation.setAvatar(jsonObject.getString("logo"));
				return userInformation;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FriendListInfomation getFriendInfos() {
		FriendListInfomation infos = new FriendListInfomation();
		// -------- 获取好友列表 --------------//
		String urlString = friendListUrl + "&userid=" + userid;
		try {
			String retString = HttpRequest.doGetRequest(urlString);
			println("getFriendInfos:" + retString);

			JSONArray jsonArray = new JSONArray(retString);
			// array成员是该类型数据 {"account":"123456","friendid":"f1","groupid":"1"}
			for (int i = 0; i < jsonArray.length(); i++) {
				FriendInfomation info = new FriendInfomation();
				JSONObject object = jsonArray.getJSONObject(i);
				String id = object.getString("friendid");
				info.setId(id);
				info.setGroupid(object.getInt("groupid"));
				info.setOnline(ThreadManager.exist(id));
				info.setAvatar(object.getString("logo"));
				info.setSignature(object.getString("signature"));
				info.setNickname(object.getString("tinyname"));
				infos.add(info);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// -------------------------------------//
		return infos;
	}

	public GroupInfo getGroupInfo() {
		GroupInfo groupInfo = new GroupInfo();
		// -------- 获取分组信息 --------------//
		String urlString = groupInfoUrl + "&userid=" + userid;
		try {
			String retString = HttpRequest.doGetRequest(urlString);

			println("getGroupInfo:" + retString);

			JSONArray jsonArray = new JSONArray(retString);
			// {"account":"123456","groupid":"1","groupname":"\u597d\u53cb\u5217\u8868","groupindex":"1"}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				GroupInfoEntity entity = new GroupInfoEntity();
				entity.setGroupId(object.getInt("groupid"));
				entity.setGroupIndex(object.getInt("groupindex"));
				entity.setGroupNameString(object.getString("groupname"));
				entity.setOwnerId(object.getString("account"));
				groupInfo.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return groupInfo;
	}

	public ChatMsgEntity[] getUnread() {
		ArrayList<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
		String urlString = unreadMsgUrl + "&userid=" + userid;
		try {
			String retString = HttpRequest.doGetRequest(urlString);

			println("getUnread:" + retString);

			JSONArray jsonArray = new JSONArray(retString);
			// {"senderId":"f1","receiver":"123456","type":"1","content":"\u4f60\u597d\u554a\u554a ","posttime":"1111"}
			for (int i = 0; i < jsonArray.length(); i++) {
				ChatMsgEntity entity = new ChatMsgEntity();
				JSONObject object = jsonArray.getJSONObject(i);
				entity.setContent(object.getString("content"));
				entity.setPosttime(object.getLong("posttime"));
				MessageReceiver receivers = new MessageReceiver();
				receivers.addReceiver(object.getString("receiver"));
				entity.setReceivers(receivers);
				entity.setSenderId(object.getString("senderId"));
				entity.setType(object.getInt("type"));
				list.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return list.toArray(new ChatMsgEntity[list.size()]);
	}

	private static void println(String string) {
		System.out.println(string);
	}

	public FriendRequestEntity[] getRequestMessage(String account) {
		String url = readFriendRequestUrl + "&account_name=" + account;
		String retString = HttpRequest.doGetRequest(url);
		try {
			JSONArray array = new JSONArray(retString);
			FriendRequestEntity[] entities = new FriendRequestEntity[array.length()];
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				FriendRequestEntity entity = new FriendRequestEntity();
				entity.setText(object.getString("text"));
				entity.setReceiver(object.getString("receiver"));
				entity.setInformation(UserInformation
						.getObjectFromJson(new JSONObject(object
								.getString("senderInfo"))));
				entities[i] = entity;
			}
			return entities;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
