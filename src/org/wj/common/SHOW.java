package org.wj.common;

/**
 * �㿪֮����ʾ�������֣������ǿ�
 * 
 * @author ���
 * 
 */
public enum SHOW {

	NULL(0, ""), ONE(1, "1"), TWO(2, "2"), THREE(3, "3"), FOUR(4,
			"4"), FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8");

	private int id;

	private String show;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	private SHOW(int id, String show) {
		this.id = id;
		this.show = show;
	}

	/**
	 * ����ID��ȡ������ʾֵ
	 * @param id
	 * @return
	 */
	public static String getShowContent(int id) {
		for (SHOW s : SHOW.values()) {
			if (id == s.id) {
				return s.show;
			}
		}
		return null;
	}

	/**
	 * ����ID��ȡö������
	 * @param id
	 * @return
	 */
	public static SHOW getShow(int id) {
		for (SHOW s : SHOW.values()) {
			if (id == s.id) {
				return s;
			}
		}
		return null;
	}
}
