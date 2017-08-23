package org.wj.common;

/**
 * 级别枚举类<br>
 * <code>SELF_DEFINED</code>自定义级别暂时没有实现，先保留着<br>
 * 
 * @author 吴键
 * 
 */
public enum LEVEL {

	LEVEL_1(1, "初级"), LEVEL_2(2, "中级"), LEVEL_3(3, "高级"), SELF_DEFINED(4, "自定义");

	// 标识
	private int id;

	// 描述或者说是具体显示
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
	 * 根据数字获取具体显示的值
	 * 
	 * @param id
	 * @return 级别名称如："初级"
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
	 * 根据ID获取级别枚举
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
	 * 根据显示信息得到对应标识
	 * @param levelName 级别名称如："初级"
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
	 * 根据显示信息得到相应级别枚举
	 * @param levelName 级别名称如："初级"
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
