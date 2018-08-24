package com.weixin.util;

import java.util.Arrays;

public class CheckUtil {
	/**
	 * 微信服务器接入验证
	 * @param signature
	 * @param nonce
	 * @param timestamp
	 * @return
	 */
	public static boolean checkSign(String signature,String nonce,String timestamp){
		System.out.println("微信服务器接入验证"+signature+","+nonce+","+timestamp);
		if(signature != null && nonce != null && timestamp != null){
			String[] strArrs = new String[]{Constants.token,timestamp,nonce};
			//按字典排序
			Arrays.sort(strArrs);
			//生成字符串
			StringBuffer content = new StringBuffer();
			for(String str : strArrs){
				content.append(str);
			}
			//加密
			String sign = EncryptionUtil.getSha1(content.toString());
			System.out.println(sign);
			if(signature.equals(sign)){
				return true;
			}
		}
		
		return false;
	} 
}
