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
 * ������
 * 
 * @author ���
 * 
 */
public class Constant {

	/**
	 * ��ǰ����<br>
	 * ����������ڳ����ʼ����ʱ��������л������ʱ�򱻸���ֵ<br>
	 * 
	 */
	public static LEVEL CURRENT_LEVEL;
	
	/**
	 * ��ʼ��ʱ��,��ʾ��ʽ
	 */
	public static final String INIT_TIME_COUNT = "000";
	
	/**
	 * error��������Ϊ���ڳ����й��ã������ظ�д�ַ���������
	 */
	public static final String ERROR = "error";
	
	/**
	 * flagGridList��ʾ��������Ϊ���ڳ����й��ã������ظ�д�ַ���������
	 */
	public static final String FLAG_GRID_LIST = "flagGridList";
	
	/**
	 * beOpenGridList��ʾ��������Ϊ���ڳ����й��ã������ظ�д�ַ���������
	 */
	public static final String BE_OPEN_GRID_LIST = "beOpenGridList";
	
	/**
	 * ɨ��Ӣ�۰�ÿһ���ַ�����һ��ģ��<br>
	 * {0},{1},��ʾռλ���ţ���<code>StringUtil</code>��ʹ��replaceString()����������<br>
	 * ����ο�<code>StringUtil</code>��replaceString()����<br>
	 */
	public static final String HERO_BAR_MODEL = "{0} :  {1} ��   -----------   {2}";
	
	/**
	 * ˫�������¼��У�������ť֮��ȴ����¼��100����<br>
	 * ����¼�������������ʵ��ĵ���<br>
	 */
	public static final int DOUBLE_KEY_CLICK_INTEVAL_TIME = 100;
	
	/**
	 * ��ʾ���壬���ղ�ͬ����̬����<br>
	 * ��������ڳ�ʼ��ʱ������<br>
	 */
	public static Font myFont;
	
	/**
	 * �����̻߳㼯��ĳ����<code>barrier.await()<code>���ټ���������<br>
	 * �����൱��ԭ����await()��notify()�໥��ϵ����á�
	 */
	public static final CyclicBarrier barrier = new CyclicBarrier(2);
	
	/**
	 * �ǹ�ƽ��
	 */
	public static final Lock lock = new ReentrantLock();
	
	/**
	 * �ź���<br>
	 * �Ұ������Ϊһ���̹߳��˵�ͨ��<br>
	 */
	public static final Semaphore semaphore = new Semaphore(1);
	
	/**
	 * ������������
	 */
	public static final Icon CRY_ICON = new ImageIcon(Cache.imgInfo.getProperty("cry"));
	
	/**
	 * �����ǣ�������
	 */
	public static final Icon FLAG_ICON = new ImageIcon(Cache.imgInfo.getProperty("flag"));
	
	/**
	 * ɨ��ʱ�������ť��ͬʱ��Ц���ᱻ�л������״̬��Ȼ��ſ����ֻᱻ�л���ȥ��������
	 */
	public static final Icon MIDDLE_ICON = new ImageIcon(Cache.imgInfo.getProperty("middle"));
	
	/**
	 * ��ͼ�꣬������
	 */
	public static final Icon MINE_ICON = new ImageIcon(Cache.imgInfo.getProperty("mine"));
	
	/**
	 * ��ͷ��������
	 */
	public static final Icon PIG_ICON = new ImageIcon(Cache.imgInfo.getProperty("pig"));
	
	/**
	 * Ц����������
	 */
	public static final Icon SMILE_ICON = new ImageIcon(Cache.imgInfo.getProperty("smile"));
	
	/**
	 * �򿪺�״̬��ǰ��ɫ
	 */
	public static final Color FONTGROUND_COLOR_AFTER_OPEN = Color.BLUE;
	
	/**
	 * �򿪺�״̬�ı���ɫ
	 */
	public static final Color BACKGROUND_COLOR_AFTER_OPEN = Color.CYAN;
}
