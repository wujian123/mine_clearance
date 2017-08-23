package org.wj.manager;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.common.SHOW;
import org.wj.model.Grid;
import org.wj.model.LevelInfo;
import org.wj.thread.DoubleKeyClickThread;
import org.wj.thread.DoubleKeyClickThreadManagerThread;
import org.wj.util.XMLReader;
import org.wj.view.View;


/**
 * 为了减少Manager的工作量。和代码的清晰度，这边专门写了个初始化类。
 * 
 * 1.加载配置文件config.xml<br>
 * 2.初始化所有格子。（包括布雷，填写数字）。 这边用到这个项目中比较复杂的算法之一，单拿出来也理所应当<br>
 * 
 * @author 吴键
 * 
 */
public class Initialization {

	private static Initialization instance = new Initialization();

	private Initialization() {
	}

	public static Initialization getInstance() {
		return instance;
	}

	// ----------------------单例----------------------------------

	/**
	 * 所有初始化工作<br>
	 * 1.加载关于图片的properties文件<br>
	 * 2.加载config.xml文件<br>
	 * 3.加载页面，在切换页面的时候changeLevel()方法可以共用<br>
	 * 
	 * @return
	 */
	public void init() {
		this.loadProperties();
		this.loadConfig();
		this.changeLevel();
		// 全局都开着的一个线程。
		this.startDoubleKeyClickThreadManagerThread();

	}

	/**
	 * 切换界面时所做的工作，在初始化方法init中可以复用<br>
	 * 1.new 一个Font对象，大小是从配置文件中得到，供页面使用<br>
	 * 2.如果是切换，则需要清理上一页面留下来的，处于阻塞状态的线程，这个工作很重要，不做，则线程会越来越多，最终导致内存溢出。<br>
	 * 3.初始化所有格子，随机布雷等算法都在内<br>
	 * 4.初始化界面<br>
	 * 
	 */
	public void changeLevel() {
		this.initFont();
		// 清理所有遗留线程
		this.clearThread();
		this.initGridArray();
		this.initView();
	}

	/**
	 * 加载properties文件
	 */
	private void loadProperties() {
		Properties imgInfo = new Properties();
		File file = null;
		InputStream is = null;
		try {
			file = new File("src/image.properties");
			is = new FileInputStream(file);
			imgInfo.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO 异常处理
		} catch (IOException e) {
			e.printStackTrace();
			// TODO 异常处理
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 放入缓存
		Cache.imgInfo = imgInfo;
	}

	/**
	 * 加载配置文件。 并且放入缓存levelInfoMap中
	 */
	private void loadConfig() {
		XMLReader.load();
	}

	/**
	 * 界面显示的字体初始化
	 */
	private void initFont() {
		Constant.myFont = new Font("myFont", Font.BOLD, Cache.levelInfoMap.get(
				Constant.CURRENT_LEVEL).getFontSize());
	}

	/**
	 * 启动线程DoubleKeyClickThreadManagerThread
	 */
	private void startDoubleKeyClickThreadManagerThread() {
		new DoubleKeyClickThreadManagerThread().start();
	}

	/**
	 * 清理线程
	 */
	public void clearThread() {
		for (int i = 0; i < Cache.tempGridList.size(); i++) {
			DoubleKeyClickThread doubleKeyClickThread = Cache.tempGridList
					.get(i).getClearMineListener().getDoubleKeyClickThread();
			// 只要状态不是程序结束状态就手动结束
			if (!doubleKeyClickThread.getState()
					.equals(Thread.State.TERMINATED)) {
				doubleKeyClickThread.setClose(true);
				doubleKeyClickThread.interrupt();
			}
		}
	}

	/**
	 * 界面的显示
	 */
	private void initView() {
		if (Cache.view != null) {
			Cache.view.setVisible(false);
			Cache.view = null;
		}
		Cache.view = new View();
	}

	/**
	 * 创建所有的格子。布雷，填写数字
	 */
	public void initGridArray() {
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		int rows = info.getRows();
		int columns = info.getColumns();
		// 赋值给缓存
		Cache.gridArray = new Grid[rows][columns];
		// 布雷
		this.layMines(Cache.gridArray, info);
		// 填数字
		this.fillShow(Cache.gridArray);
		// isFirst置为true
		Cache.isFirst = true;
	}

	/**
	 * 布雷<br>
	 * 1.根据雷数产生相应个数的随机数<br>
	 * 2.遍历所有格子，序号和随机数相同的那个格子就设置为雷<br>
	 * 
	 * @param gridArray
	 */
	private void layMines(Grid[][] gridArray, LevelInfo info) {
		int mineCount = info.getMineCount();
		// 得到随机数的集合
		Set<Integer> randomSet = this.createRandom(
				info.getRows() * info.getColumns(), mineCount);
		int count = 0;
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[i].length; j++) {
				// 先创建格子
				gridArray[i][j] = new Grid(i, j);
				// 如果随机数中包含count，此格子就最为雷出现
				if (randomSet.contains(count++)) {
					// 标记为是雷
					gridArray[i][j].setMine(true);
				}
			}
		}
	}

	/**
	 * 产生mineCount数量的随机数，不重复的，所以使用Set，而且在hash查询的时候是最快的
	 * 
	 * @param gridSize
	 * @param mineCount
	 * @return
	 */
	private Set<Integer> createRandom(int gridSize, int mineCount) {
		Set<Integer> set = new HashSet<Integer>();
		Random random = new Random();
		while (true) {
			set.add(random.nextInt(gridSize));
			// set数量增加到mineCount的时候会return
			if (set.size() == mineCount) {
				return set;
			}
		}
	}

	/**
	 * 填写格子中的数据。这边叫做show，包括——数字，空，
	 * 
	 * 1.判断本身是否是雷<br>
	 * 2.判断它周围每一个格子是否是雷<br>
	 * 
	 * @param gridArray
	 */
	private void fillShow(Grid[][] gridArray) {
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[i].length; j++) {
				// 具体检查每一个数字
				this.checkGrid(gridArray[i][j]);
			}
		}
	}

	/**
	 * 1.判断本身是否是雷<br>
	 * 2.判断它周围每一个格子是否是雷<br>
	 * 
	 * @param grid
	 * 
	 * @return
	 */
	private void checkGrid(Grid grid) {
		// 判断是否是雷
		if (grid.isMine()) {
			return;
		}
		List<Grid> roundGridList = Manager.getInstance().addRound8GridToList(
				grid);
		int count = 0;
		for (Grid g : roundGridList) {
			count = this.isMineAndCount(g, count);
		}

		// 判断count
		SHOW show = SHOW.getShow(count);
		if (show == null) {
			// TODO show不存在，需要做异常处理
			// 正常程序执行一般不可能走到这一步
		}
		grid.setShow(show);
	}

	/**
	 * 判断这个格子是否是雷。
	 * 
	 * @param grid
	 * @param count
	 * @return
	 */
	private int isMineAndCount(Grid grid, int count) {
		if (grid.isMine()) {
			count++;
		}
		return count;
	}
}
