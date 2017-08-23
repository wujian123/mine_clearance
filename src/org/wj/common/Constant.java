package org.wj.common;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.wj.cache.Cache;


/**
 * 常量类
 * 
 * @author 吴键
 * 
 */
public class Constant {

	/**
	 * 当前级别<br>
	 * 这个变量会在程序初始化的时候或者在切换级别的时候被附上值<br>
	 * 
	 */
	public static LEVEL CURRENT_LEVEL;
	
	/**
	 * 初始化时间,表示形式
	 */
	public static final String INIT_TIME_COUNT = "000";
	
	/**
	 * error，仅仅是为了在程序中公用，不用重复写字符串而给出
	 */
	public static final String ERROR = "error";
	
	/**
	 * flagGridList标示，仅仅是为了在程序中公用，不用重复写字符串而给出
	 */
	public static final String FLAG_GRID_LIST = "flagGridList";
	
	/**
	 * beOpenGridList标示，仅仅是为了在程序中公用，不用重复写字符串而给出
	 */
	public static final String BE_OPEN_GRID_LIST = "beOpenGridList";
	
	/**
	 * 扫雷英雄榜每一行字符串的一个模板<br>
	 * {0},{1},表示占位符号，在<code>StringUtil</code>中使用replaceString()方法解析。<br>
	 * 具体参考<code>StringUtil</code>中replaceString()方法<br>
	 */
	public static final String HERO_BAR_MODEL = "{0} :  {1} 秒   -----------   {2}";
	
	/**
	 * 双键单击事件中，两个按钮之间等待的事件差，100毫秒<br>
	 * 间隔事件可以在这边做适当的调节<br>
	 */
	public static final int DOUBLE_KEY_CLICK_INTEVAL_TIME = 100;
	
	/**
	 * 显示字体，按照不同级别动态调整<br>
	 * 这个对象在初始化时被加载<br>
	 */
	public static Font myFont;
	
	/**
	 * 两个线程汇集到某个点<code>barrier.await()<code>后再继续往下走<br>
	 * 功能相当于原来的await()和notify()相互结合的作用。
	 */
	public static final CyclicBarrier barrier = new CyclicBarrier(2);
	
	/**
	 * 非公平锁
	 */
	public static final Lock lock = new ReentrantLock();
	
	/**
	 * 信号量<br>
	 * 我把它理解为一个线程过滤的通道<br>
	 */
	public static final Semaphore semaphore = new Semaphore(1);
	
	/**
	 * 哭脸，供复用
	 */
	public static final Icon CRY_ICON = new ImageIcon(Cache.imgInfo.getProperty("cry"));
	
	/**
	 * 插旗标记，供复用
	 */
	public static final Icon FLAG_ICON = new ImageIcon(Cache.imgInfo.getProperty("flag"));
	
	/**
	 * 扫雷时，点击按钮的同时，笑脸会被切换到这个状态，然后放开后又会被切换回去，供复用
	 */
	public static final Icon MIDDLE_ICON = new ImageIcon(Cache.imgInfo.getProperty("middle"));
	
	/**
	 * 雷图标，供复用
	 */
	public static final Icon MINE_ICON = new ImageIcon(Cache.imgInfo.getProperty("mine"));
	
	/**
	 * 猪头，供复用
	 */
	public static final Icon PIG_ICON = new ImageIcon(Cache.imgInfo.getProperty("pig"));
	
	/**
	 * 笑脸，供复用
	 */
	public static final Icon SMILE_ICON = new ImageIcon(Cache.imgInfo.getProperty("smile"));
	
	/**
	 * 打开后状态的前景色
	 */
	public static final Color FONTGROUND_COLOR_AFTER_OPEN = Color.BLUE;
	
	/**
	 * 打开后状态的背景色
	 */
	public static final Color BACKGROUND_COLOR_AFTER_OPEN = Color.CYAN;
}
