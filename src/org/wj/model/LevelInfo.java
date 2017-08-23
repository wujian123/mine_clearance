package org.wj.model;

import org.wj.common.LEVEL;

/**
 * 游戏级别信息
 * 
 * @author 吴键
 * 
 */
public class LevelInfo {

	// 级别
	private LEVEL level;

	// 行数
	private int rows;

	// 列数
	private int columns;

	// 雷数
	private int mineCount;

	// 高度
	private int height;

	// 宽度
	private int width;

	// 间隙
	private int hgap;

	// 字体大小
	private int fontSize;

	// 英雄
	private Hero hero;

	public LEVEL getLevel() {
		return level;
	}

	public void setLevel(LEVEL level) {
		this.level = level;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getMineCount() {
		return mineCount;
	}

	public void setMineCount(int mineCount) {
		this.mineCount = mineCount;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHgap() {
		return hgap;
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
