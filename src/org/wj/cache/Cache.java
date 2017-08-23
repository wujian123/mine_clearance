package org.wj.cache;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.wj.common.LEVEL;
import org.wj.model.Grid;
import org.wj.model.LevelInfo;


/**
 * 缓存类
 * 
 * @author 吴键
 * 
 */
public class Cache {

	/**
	 * 缓存3种级别的所有信息<br>
	 * 本来使用HashMap，但后来发现出现顺序问题。HashMap为了达到最快速度，使用hash算法，是无序的。<br>
	 * 现在换成TreeMap<br>
	 * 
	 */
	public static final Map<LEVEL, LevelInfo> levelInfoMap = new TreeMap<LEVEL, LevelInfo>(
			new Comparator<LEVEL>() {
				@Override
				public int compare(LEVEL o1, LEVEL o2) {
					return o1.getId() - o2.getId();
				}
			});
	
	/**
	 * 为优化效率，使用了一个临时的list存放grid，判断是否应该启动它的双键单击事件等待线程<br>
	 * 因为这边需要大量的增减操作。所以使用linkedList比ArrayList要好<br>
	 * 
	 */
	public static final List<Grid> tempGridList = new LinkedList<Grid>();

	/**
	 * 缓存所有的格子信息,初始化的时候会被附上值
	 */
	public static Grid[][] gridArray = null;

	/**
	 * 缓存图片路径,使用Properties文件类型
	 */
	public static Properties imgInfo = null;

	/**
	 * 游戏是否处于结束状态
	 */
	public static boolean isGameOver;

	/**
	 * 恢复按钮
	 */
	public static JButton faceButton = null;

	/**
	 * 记录雷数的label
	 */
	public static JLabel mineNumLabel = null;

	/**
	 * 记录时间的label
	 */
	public static JLabel timeLabel = null;

	/**
	 * 当前主页面
	 */
	public static JFrame view = null;

	/**
	 * 是否是第一次点击
	 */
	public static boolean isFirst;

	/**
	 * 记录用户解决的格子个数，用于判定所有雷是否成功扫完
	 */
	public static int count;
}
