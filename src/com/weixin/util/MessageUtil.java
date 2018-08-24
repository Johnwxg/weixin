package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.weixin.bean.ClickButton;
import com.weixin.bean.Image;
import com.weixin.bean.ImageMessage;
import com.weixin.bean.Menu;
import com.weixin.bean.MenuButton;
import com.weixin.bean.Music;
import com.weixin.bean.MusicMessage;
import com.weixin.bean.Pic;
import com.weixin.bean.PicMessage;
import com.weixin.bean.TextMessage;
import com.weixin.bean.ViewButton;

import net.sf.json.JSONObject;
import sun.nio.cs.ext.MacArabic;

public class MessageUtil {

	/**
	 * xml转map
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String, String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		
		InputStream in = request.getInputStream();
		Document doc = reader.read(in);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		
		for(Element element : list){
			map.put(element.getName(), element.getText());
		}
		in.close();
		return map;
	}
	
	/**
	 * 消息文本对象转为xml
	 * @param message
	 * @return
	 */
	public static String textMessageToXml(TextMessage message){
		XStream xStream = new XStream();
		//将根节点转为<xml>
		xStream.alias("xml", message.getClass());
		return xStream.toXML(message);
	}
	
	/**
	 * 图文消息对象转为xml
	 * @param message
	 * @return
	 */
	public static String picMessageToXml(PicMessage message){
		XStream xStream = new XStream();
		//将根节点转为<xml>
		xStream.alias("xml", message.getClass());
		xStream.alias("item", new Pic().getClass());
		return xStream.toXML(message);
	}
	
	/**
	 * 初始化文本消息
	 * @param fromUserName
	 * @param ToUserName
	 * @param content
	 * @return
	 */
	public static String initText(String fromUserName, String ToUserName, String content){
		TextMessage text = new TextMessage();
		text.setToUserName(ToUserName);
		text.setFromUserName(fromUserName);
		text.setMsgType(Constants.MESSAGE_TYPE.TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return MessageUtil.textMessageToXml(text);
	}
	
	/**
	 * 初始化图文消息(组装)
	 * @param fromUserName
	 * @param ToUserName
	 * @param content
	 * @return
	 */
	public static String initPicText(String fromUserName, String ToUserName){
		String message = "";
		List<Pic> picList = new ArrayList<>();
		PicMessage picMessage = new PicMessage();
		
		Pic pic = new Pic();
		pic.setTitle("公司介绍");
		pic.setDescription("亚信是在美国纳斯达克成功上市的中国高科技企业（NASDAQ交易代码：ASIA）,"
				+ "致力于为中国电信运营商提供IT解决方案和服务，以使电信运营商迅速响应市场变化，降低运营成本，提升盈利能力。 "
				+ "目前，亚信提供的软件方案和服务涉及IP、VoIP、宽带、无线、3G等等技术领域，包括：业务支撑系统（计费、客户关系管理、商业智能分析、网管系统等）；电信增值应用系统以及电信级网络解决方案等。");
		pic.setPicUrl("http://wxgwechant.ngrok.xiaomiqiu.cn/weixin/image/c8177f3e6709c93d25e05c259d3df8dcd00054d5.jpg");
		pic.setUrl("http://www.asiainfo-sec.com");
		picList.add(pic);
		
		picMessage.setToUserName(ToUserName);
		picMessage.setFromUserName(fromUserName);
		picMessage.setMsgType(Constants.MESSAGE_TYPE.PIC);
		picMessage.setCreateTime(new Date().getTime());
		picMessage.setArticles(picList);
		picMessage.setArticleCount(picList.size());
		return MessageUtil.picMessageToXml(picMessage);
	}
	
	/**
	 * 主菜单
	 * @param str
	 * @return
	 */
	public static String menuText(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("欢迎关注，请按照以下提示进行操作：\n");
		sBuffer.append("回复'1',公司介绍\n");
		sBuffer.append("回复'2',公司详情\n");
		sBuffer.append("回复'?',展示帮助。");
		return sBuffer.toString();
	}
	
	/**
	 * 主菜单
	 * @param str
	 * @return
	 */
	public static String firstText(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("亚信是在美国纳斯达克成功上市的中国高科技企业（NASDAQ交易代码：ASIA）,"
				+ "致力于为中国电信运营商提供IT解决方案和服务，以使电信运营商迅速响应市场变化，降低运营成本，提升盈利能力。 "
				+ "目前，亚信提供的软件方案和服务涉及IP、VoIP、宽带、无线、3G等等技术领域，包括：业务支撑系统（计费、客户关系管理、商业智能分析、网管系统等）；电信增值应用系统以及电信级网络解决方案等。");
		return sBuffer.toString();
	}
	
	/**
	 * 主菜单
	 * @param str
	 * @return
	 */
	public static String secondText(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("亚信十多年的创业、发展历程，书写了中国高科技企业崛起的经典传奇。作为中国归国留学人员'科技报国'的重要成果，亚信曾率先将Internet引入中国，为中国信息化建设作出卓越贡献，享有'中国互联网建筑师'的美誉。亚信首开中国企业吸纳风险投资之先河，率先引入国际化、规范的公司治理和管理制度。亚信成功实现向软件与服务的转型，立足于自主开发软件产品，"
				+ "形成大型软件研发和质量控制体系以及电信级IT项目实施管理体系，成为中国软件行业的重点企业。亚信一贯注重人才培养，凝聚着一批具有国际化背景和本地化经验的优秀人才，被称为'中国IT业的黄埔军校'。");
		return sBuffer.toString();
	}
	
	/**
	 * 初始化图片消息回复
	 * @param fromUserName
	 * @param ToUserName
	 * @param mediaId
	 * @return
	 */
	public static String initImageMessage(String fromUserName, String ToUserName, String mediaId){
		String message = null;
		Image image = new Image();
		image.setMediaId(mediaId);
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setToUserName(ToUserName);
		imageMessage.setFromUserName(fromUserName);
		imageMessage.setMsgType(Constants.MESSAGE_TYPE.IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message = imageMessageToXml(imageMessage);
		return message;
	}
	
	/**
	 * 图片消息对象转为xml
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xStream = new XStream();
		//将根节点转为<xml>
		xStream.alias("xml", imageMessage.getClass());
		return xStream.toXML(imageMessage);
	}
	
	/**
	 * 初始化音乐消息回复
	 * @param fromUserName
	 * @param ToUserName
	 * @return
	 */
	public static String initMusicMessage(String fromUserName, String ToUserName){
		String message = null;
		Music music = new Music();
		music.setTitle("燃烧我的卡路里");
		music.setDescription("燃烧我的卡路里，拜拜 甜甜圈 珍珠奶茶方便面，火锅米饭大盘鸡，拿走拿走别客气。拜拜 咖啡因 戒掉可乐戒油腻，沙发外卖玩游戏，别再熬夜伤身体，来来 后转体 高温瑜伽仰卧起，动感单车普拉提，"
				+ "保温杯里泡枸杞，来来 深呼吸 晨跑夜跑游几米 平板哑铃划船机 不达目的不放弃");
		music.setHQMusicUrl("http://wxgwechant.ngrok.xiaomiqiu.cn/weixin/music/kaluli.mp3");
		music.setMusicUrl("http://wxgwechant.ngrok.xiaomiqiu.cn/weixin/music/kaluli.mp3");
		music.setThumbMediaId("g0zvHOG_fPLaZ5P4XeexIdveRhc8cridhDVaoacOs3POVgsYR1WJxwhYjlcQX0dm");
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setToUserName(ToUserName);
		musicMessage.setFromUserName(fromUserName);
		musicMessage.setMsgType(Constants.MESSAGE_TYPE.MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		message = musicMessageToXml(musicMessage);
		return message;
	}

	/**
	 * 音乐消息对象转为xml
	 * @param musicMessage
	 * @return
	 */
	public static String musicMessageToXml(MusicMessage musicMessage){
		XStream xStream = new XStream();
		xStream.alias("xml", musicMessage.getClass());
		return xStream.toXML(musicMessage);
	}
	
	/**
	 * 初始化菜单（组装）
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton click = new ClickButton();
		click.setName("click菜单");
		click.setType("click");
		click.setKey("menu_ckick01");
		
		ViewButton view = new ViewButton();
		view.setName("view菜单");
		view.setType("view");
		view.setUrl("http://wxgwechant.ngrok.xiaomiqiu.cn/weixin/html/weather.html");
		
		ClickButton subClick1 = new ClickButton();
		subClick1.setName("扫码");
		subClick1.setType("scancode_push");
		subClick1.setKey("menu_subClick1");
		
		ClickButton subClick2 = new ClickButton();
		subClick2.setName("地理位置");
		subClick2.setType("location_select");
		subClick2.setKey("menu_subClick2");
		
		MenuButton button = new MenuButton();
		button.setName("菜单");
		button.setSub_button(new MenuButton[]{subClick1,subClick2});
		
		menu.setButton(new MenuButton[]{click,view,button});
		return menu;
	} 
	
	/**
	 * 创建菜单
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token, String menu){
		System.out.println("创建菜单入参："+menu);
		int result = 0;
		String url = Constants.CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject json = HttpClientUtil.doPost(url, menu);
		System.out.println("创建菜单结果"+json.toString());
		if(json != null){
			result = json.getInt("errcode");
		}
		return result;
	}
	
	/**
	 * 自定义菜单查询
	 * @param token
	 * @return
	 */
	public static String getMenu(String token){
		System.out.println("自定义菜单查询入参："+token);
		int result = 0;
		String url = Constants.GET_MENU_INFO_URL.replace("ACCESS_TOKEN", token);
		JSONObject json = HttpClientUtil.doGet(url);
		System.out.println("自定义菜单查询结果"+json.toString());
		return json.toString();
	}
	
	/**
	 * 自定义菜单删除
	 * @param token
	 * @return
	 */
	public static int delMenu(String token){
		System.out.println("自定义菜单删除入参："+token);
		int result = 0;
		String url = Constants.DEL_MENU_INFO_URL.replace("ACCESS_TOKEN", token);
		JSONObject json = HttpClientUtil.doGet(url);
		System.out.println("自定义菜单删除结果"+json.toString());
		if(json != null){
			result = json.getInt("errcode");
		}
		return result;
	}
}
