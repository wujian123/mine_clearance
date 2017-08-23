package org.wj.util;

/**
 * �����ַ����Ĺ�����
 * 
 * @author ���
 *
 */
public class StringUtil {

	/**
	 * �ж��ַ����Ƿ�ΪNull���߿�
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isNull(String target) {
		return target == null || target.trim().length() == 0;
	}

	/**
	 * ����ģ���滻ֵ
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
	 * ����ģ�廹ԭ���������ʲô��Ҳ˵�������������������¡�û���ʵ���ԡ�
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
