package org.wj.common;

/**
 * ����ö����<br>
 * <code>SELF_DEFINED</code>�Զ��弶����ʱû��ʵ�֣��ȱ�����<br>
 * 
 * @author ���
 * 
 */
public enum LEVEL {

	LEVEL_1(1, "����"), LEVEL_2(2, "�м�"), LEVEL_3(3, "�߼�"), SELF_DEFINED(4, "�Զ���");

	// ��ʶ
	private int id;

	// ��������˵�Ǿ�����ʾ
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private LEVEL(int id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * �������ֻ�ȡ������ʾ��ֵ
	 * 
	 * @param id
	 * @return ���������磺"����"
	 */
	public static String getDesc(int id) {
		for (LEVEL level : LEVEL.values()) {
			if (id == level.id) {
				return level.description;
			}
		}
		return "";
	}

	/**
	 * ����ID��ȡ����ö��
	 * @param id
	 * @return
	 */
	public static LEVEL getLevel(int id) {
		for (LEVEL level : LEVEL.values()) {
			if (id == level.id) {
				return level;
			}
		}
		return null;
	}
	
	/**
	 * ������ʾ��Ϣ�õ���Ӧ��ʶ
	 * @param levelName ���������磺"����"
	 * @return
	 */
	public static int getIdByDesc(String levelName){
		for(LEVEL level : LEVEL.values()){
			if(level.description.equals(levelName)){
				return level.id;
			}
		}
		return 0;
	}
	
	/**
	 * ������ʾ��Ϣ�õ���Ӧ����ö��
	 * @param levelName ���������磺"����"
	 * @return
	 */
	public static LEVEL getLevel(String levelName){
		for(LEVEL level : LEVEL.values()){
			if(level.description.equals(levelName)){
				return level;
			}
		}
		return null;
	}
}
