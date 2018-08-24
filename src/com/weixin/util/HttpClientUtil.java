package com.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.weixin.bean.AccessToken;

import net.sf.json.JSONObject;

public class HttpClientUtil {
	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static JSONObject doGet(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * post请求
	 * @param url
	 * @return
	 */
	public static JSONObject doPost(String url, String out){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(out, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 上传临时素材
	 * 		类型：image、voice、video、thumb（缩略图）
	 * @param filePath
	 * @param token
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public static String upload(String filePath, String token, String type) throws Exception{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			throw new Exception("文件不存在");
		}
		//素材上传地址
		String url = Constants.GET_Material_upload_url.replace("ACCESS_TOKEN", token).replace("TYPE", type);
		//建立连接
		URL urlCon = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlCon.openConnection();
		
		con.setRequestMethod("POST"); // 设置关键值,以Post方式提交表单，默认get方式
		con.setDoInput(true); //输入输出
		con.setDoOutput(true);
		con.setUseCaches(false); //忽略缓存(post方式不能使用缓存)
		//设置请求头
		con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
		//设置边界 分界符
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        /*if(!fileName.endsWith("mp4")){
            sb.append("Content-Type:image/jpeg\r\n\r\n");
        }else if(){
            sb.append("Content-Type:video/mp4\r\n\r\n");
        }*/
		//未知文件类型，以流的方式上传
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件以流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();

        // 读取返回数据    
        StringBuffer strBuf = new StringBuffer();  
        BufferedReader reader = null;
        String line = null;  
        String result = null;
        try {
	    	reader = new BufferedReader(new InputStreamReader(con.getInputStream()));  
	        while ((line = reader.readLine()) != null) {  
	            strBuf.append(line);  
	        }
	        if(result == null){
	        	result = strBuf.toString();
	        }
	        System.out.println("文件上传返回信息{"+strBuf.toString()+"}");  
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader != null){
				reader.close();  
			}
			con.disconnect();  
	        con = null;
		}
        
        //输出转为json
        JSONObject json = JSONObject.fromObject(result);
        System.out.println("输出结果"+json.toString());
        String mediaType = "media_id";
        if(!"image".equals(type)){
        	mediaType = type + "_media_id";
        }
        String mediaId = json.getString(mediaType); //媒体文件上传后，获取标识
		return mediaId;
	}
	
	/**
	 * 获取AccessToken
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = Constants.GET_ACCESS_TOKEN_URL.replace("APPID", Constants.appId).replace("APPSECRET", Constants.appSecret);
		JSONObject json = doGet(url);
		if(json != null){
			token.setAccessToken(json.getString("access_token"));
			token.setExpiresIn(json.getInt("expires_in"));
		}
		System.out.println("获取AccessToken"+json.toString());
		return token;
	}
}
