package org.wj.model;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import org.wj.action.ClearMineListener;
import org.wj.common.SHOW;


/**
 * ��ͨ����
 * 
 * @author ���
 * 
 */
@SuppressWarnings("serial")
public class Grid extends JButton {
	
	// �Ƿ�����
	private boolean mine;
	
	// �Ƿ񱻲���,Ĭ��false
	private boolean flag;
	
	// �Ƿ񱻵����������˵�Ƿ񱻴�
	private boolean open;
	
	// ���������������Ϸpanel����������λ��
	private int rowNum;
	
	// ���������������Ϸpanel����������λ��
	private int columnNum;

	// ��֮����ֵ����ֻ��߿հף����ߵ���
	// new��ʱ��Ĭ���ǿհ�
	private SHOW show = SHOW.NULL;
	
	private ClearMineListener clearMineListener = new ClearMineListener(this);

	public Grid(int rowNum, int columnNum) {
		this.rowNum = rowNum;
		this.columnNum = columnNum;
		this.setHorizontalAlignment(CENTER);
		this.setVerticalAlignment(CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		//��Ӽ���
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
