package com.weixin.util;

public interface Constants {
	
	//微信公众号token
	public static final String token = "wxgWehantTest";
	
	//微信APPID
	public static final String appId = "wxbc931eb249f35568";
	
	//微信appsecret
	public static final String appSecret= "8baeefb2dee1f37e369c97532fc39413";
	
	//获取access_token
	public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	//素材上传url  类型：image、voice、video、thumb（缩略图）
	public static final String GET_Material_upload_url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	//菜单创建地址
	public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	//自定义菜单查询
	public static final String GET_MENU_INFO_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	//自定义菜单删除
	public static final String DEL_MENU_INFO_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	/**
	 * 消息类型
	 * @author Admin
	 *
	 */
	public interface MESSAGE_TYPE{
		//普通消息
		String TEXT = "text"; //文本消息
		String IMAGE = "image"; //图片消息
		String VOICE = "voice"; //语音消息
		String MUSIC = "music"; //音乐消息
		String VIDEO = "video"; //视频消息
		String LOCATION = "location"; //地理位置消息
		String LINK = "link"; //链接消息
		//事件推送
		String EVENT = "event"; //
		String SUBSCRIBE = "subscribe";  //订阅
		String UNSUBSCRIBE = "unsubscribe"; //取消订阅
		//自定义菜单事件
		String CLICK = "CLICK"; //点击菜单拉取消息时的事件推送
		String VIEW = "VIEW"; //点击菜单跳转链接时的事件推送
		String SCANCODE_PUSH = "scancode_push"; //扫码推事件的事件推送
		String LOCATION_SELECT = "location_select"; //弹出地理位置选择器的事件推送
		
		String PIC = "news"; //图文消息
	}
}
