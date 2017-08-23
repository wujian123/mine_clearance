package org.wj.util;

/**
 * 处理字符串的工具类
 * 
 * @author 吴键
 *
 */
public class StringUtil {

	/**
	 * 判断字符串是否为Null或者空
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isNull(String target) {
		return target == null || target.trim().length() == 0;
	}

	/**
	 * 根据模板替换值
	 * 
	 * @param model
	 * @param args
	 * @return
	 */
	public static String replaceString(String model, String... args) {
		for (int i = 0; i < args.length; i++) {
			model = model.replace("{" + i + "}", args[i]);
		}
		return model;
	}

	/**
	 * 根据模板还原，具体操作什么我也说不清楚。具体情况下用下。没多大实用性。
	 * 
	 * @param model
	 * @param target
	 * @return
	 */
	public static String[] restoreString(String model, String target) {
		String regex = "wj";
		String[] strArray = new String[getCommonStrNum(model)];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = regex;
		}
		String[] tempArray = replaceString(model, strArray).split(regex);
		for (int i = 0; i < tempArray.length; i++) {
			if("".equals(tempArray[i])){
				continue;
			}
			target = target.replace(tempArray[i], regex);
		}
		return target.split(regex);
	}

	public static int getCommonStrNum(String target) {
		int count = 0;
		for (int i = 0; i < target.length(); i++) {
			if ('{' == target.charAt(i)) {
				count++;
			}
		}
		return count;
	}
}
