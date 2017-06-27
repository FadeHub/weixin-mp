package com.cooperate.wxh5.wx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;

/**
 * 
* @ClassName: StringJsonToListUtil 
* @Description: TODO
* @author zhrt
* @date 2017年6月26日 下午3:55:31
* @Version 1.0
 */
public class StringJsonToListUtil {

	@SuppressWarnings("unchecked")
	public static <T> List<?> stringJsonToList(String string,Class<?> clazz) {
		if(StringUtils.isNotBlank(string)){
			JSONArray jsonArray = JSONArray.fromObject(string);
			Collection<?> collection = JSONArray.toCollection(jsonArray,clazz);
			if(collection!=null){
				List<T> list = new ArrayList<>();
				Iterator<?> it = collection.iterator();
				while(it.hasNext()){
					T t = (T) it.next();
					list.add(t);
				}
				return list;
			}
		}
		return null;
	}
}
