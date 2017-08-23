package org.wj.model;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.wj.action.ClearMineListener;
import org.wj.common.SHOW;


/**
 * 普通格子
 * 
 * @author 吴键
 * 
 */
@SuppressWarnings("serial")
public class Grid extends JButton {
	
	// 是否是雷
	private boolean mine;
	
	// 是否被插旗,默认false
	private boolean flag;
	
	// 是否被点击过，或者说是否被打开
	private boolean open;
	
	// 这个格子在整个游戏panel中所处的行位置
	private int rowNum;
	
	// 这个格子在整个游戏panel中所处的列位置
	private int columnNum;

	// 打开之后出现的数字或者空白，或者地雷
	// new的时候默认是空白
	private SHOW show = SHOW.NULL;
	
	private ClearMineListener clearMineListener = new ClearMineListener(this);

	public Grid(int rowNum, int columnNum) {
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		this.setHorizontalAlignment(CENTER);
		this.setVerticalAlignment(CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		//添加监听
		this.addMouseListener(clearMineListener);
	}
	
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public SHOW getShow() {
		return show;
	}

	public void setShow(SHOW show) {
		this.show = show;
	}

	public int getRowNum() {
		return rowNum;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public ClearMineListener getClearMineListener() {
		return clearMineListener;
	}
}
