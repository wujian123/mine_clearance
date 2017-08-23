package org.wj.model;

/**
 * Ó¢ÐÛ°ñ¡ª¡ªÓ¢ÐÛ
 * @author Îâ¼ü
 *
 */
public class Hero {

	//Ãû×Ö
	private String name;
	
	//ºÄÊ±
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
