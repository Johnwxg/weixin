package com.weixin.util;

import java.util.UUID;

public class StingUtil {

	/**
	 * 判断非空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return str != null && !"".equals(str.trim());
	}
	
	/**
	 * 判空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return str == null || "".equals(str.trim());
	}
	
	/**
	 * 生成UUID
	 * @return
	 */
	public String getUuid(){
		return UUID.randomUUID().toString();
	}
}
