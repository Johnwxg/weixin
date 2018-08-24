package com.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.dom4j.DocumentException;

import com.weixin.bean.AccessToken;
import com.weixin.bean.TextMessage;
import com.weixin.util.CheckUtil;
import com.weixin.util.Constants;
import com.weixin.util.HttpClientUtil;
import com.weixin.util.MessageUtil;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class Test
 */
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		PrintWriter out = response.getWriter();
		String signature  = request.getParameter("signature");
		String timestamp  = request.getParameter("timestamp");
		String nonce  = request.getParameter("nonce");
		String echostr  = request.getParameter("echostr");
		
		if(CheckUtil.checkSign(signature, nonce, timestamp)){
			System.out.println(echostr);
			out.print(echostr);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			Map<String, String> map = MessageUtil.xmlToMap(request);
			System.out.println("入参"+map.toString());
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = "";
			if(Constants.MESSAGE_TYPE.TEXT.equals(msgType)){
				if("1".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstText());
				}else if("2".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondText());
				}else if("?".equals(content) || "？".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if("亚信".equals(content)){
					message = MessageUtil.initPicText(toUserName, fromUserName);
				}else if("图片".equals(content)){
					String mediaId = "iGjZbhCONT6LnQeVcTuvV0QGCywcMu7bstEIVbhnGqzOjYn2pMK1xMX8jQ8GUx2A";
					message = MessageUtil.initImageMessage(toUserName, fromUserName, mediaId);
				}else if("音乐".equals(content)){
					message = MessageUtil.initMusicMessage(toUserName, fromUserName);
				}else {
					TextMessage text = new TextMessage();
					text.setToUserName(fromUserName);
					text.setFromUserName(toUserName);
					text.setMsgType(Constants.MESSAGE_TYPE.TEXT);
					text.setCreateTime(new Date().getTime());
					text.setContent("您发送的消息是："+content);
					message = MessageUtil.textMessageToXml(text);
				}
			}else if(Constants.MESSAGE_TYPE.EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(Constants.MESSAGE_TYPE.SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(Constants.MESSAGE_TYPE.CLICK.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(Constants.MESSAGE_TYPE.VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(Constants.MESSAGE_TYPE.SCANCODE_PUSH.equals(eventType)){
					String scanCodeInfo = map.get("ScanCodeInfo");
					String scanResult = map.get("ScanResult");
					message = MessageUtil.initText(toUserName, fromUserName, "扫描信息:"+scanCodeInfo+
							"，扫描结果:"+scanResult);
				}
			}else if(Constants.MESSAGE_TYPE.LOCATION.equals(msgType)){
				String lable = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, lable);
			}
			System.out.println(message);
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		AccessToken token = HttpClientUtil.getAccessToken();
		System.out.println("票据"+token.getAccessToken());
		System.out.println("有效时间"+token.getExpiresIn());
		
//		String path = "F:/workspace-test/weixin/WebContent/image/03087bf40ad162d91aca65e61adfa9ec8a13cd20.jpg";
//		String mediaId = null;
//		try {
//			mediaId = HttpClientUtil.upload(path, token.getAccessToken(), "thumb");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(mediaId);
		
		String menu = JSONObject.fromObject(MessageUtil.initMenu()).toString();
		int resultCd = MessageUtil.createMenu(token.getAccessToken(), menu);
		if(resultCd == 0){
			System.out.println("创建菜单成功");
		}else{
			System.out.println(resultCd);
		}
		
//		String result = MessageUtil.getMenu(token.getAccessToken());
//		System.out.println(result);
		
		
	}

}
