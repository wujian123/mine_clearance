package org.wj.model;

/**
 * Ӣ�۰񡪡�Ӣ��
 * @author ���
 *
 */
public class Hero {

	//����
	private String name;
	
	//��ʱ
	private int time;
	
	public Hero(){
	}

	public Hero(String name, int time){
		this.time = time;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
