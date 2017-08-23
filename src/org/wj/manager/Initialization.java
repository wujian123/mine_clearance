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
 * Ϊ�˼���Manager�Ĺ��������ʹ���������ȣ����ר��д�˸���ʼ���ࡣ
 * 
 * 1.���������ļ�config.xml<br>
 * 2.��ʼ�����и��ӡ����������ף���д���֣��� ����õ������Ŀ�бȽϸ��ӵ��㷨֮һ�����ó���Ҳ����Ӧ��<br>
 * 
 * @author ���
 * 
 */
public class Initialization {

	private static Initialization instance = new Initialization();

	private Initialization() {
	}

	public static Initialization getInstance() {
		return instance;
	}

	// ----------------------����----------------------------------

	/**
	 * ���г�ʼ������<br>
	 * 1.���ع���ͼƬ��properties�ļ�<br>
	 * 2.����config.xml�ļ�<br>
	 * 3.����ҳ�棬���л�ҳ���ʱ��changeLevel()�������Թ���<br>
	 * 
	 * @return
	 */
	public void init() {
		this.loadProperties();
		this.loadConfig();
		this.changeLevel();
		// ȫ�ֶ����ŵ�һ���̡߳�
		this.startDoubleKeyClickThreadManagerThread();

	}

	/**
	 * �л�����ʱ�����Ĺ������ڳ�ʼ������init�п��Ը���<br>
	 * 1.new һ��Font���󣬴�С�Ǵ������ļ��еõ�����ҳ��ʹ��<br>
	 * 2.������л�������Ҫ������һҳ���������ģ���������״̬���̣߳������������Ҫ�����������̻߳�Խ��Խ�࣬���յ����ڴ������<br>
	 * 3.��ʼ�����и��ӣ�������׵��㷨������<br>
	 * 4.��ʼ������<br>
	 * 
	 */
	public void changeLevel() {
		this.initFont();
		// �������������߳�
		this.clearThread();
		this.initGridArray();
		this.initView();
	}

	/**
	 * ����properties�ļ�
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
			// TODO �쳣����
		} catch (IOException e) {
			e.printStackTrace();
			// TODO �쳣����
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// ���뻺��
		Cache.imgInfo = imgInfo;
	}

	/**
	 * ���������ļ��� ���ҷ��뻺��levelInfoMap��
	 */
	private void loadConfig() {
		XMLReader.load();
	}

	/**
	 * ������ʾ�������ʼ��
	 */
	private void initFont() {
		Constant.myFont = new Font("myFont", Font.BOLD, Cache.levelInfoMap.get(
				Constant.CURRENT_LEVEL).getFontSize());
	}

	/**
	 * �����߳�DoubleKeyClickThreadManagerThread
	 */
	private void startDoubleKeyClickThreadManagerThread() {
		new DoubleKeyClickThreadManagerThread().start();
	}

	/**
	 * �����߳�
	 */
	public void clearThread() {
		for (int i = 0; i < Cache.tempGridList.size(); i++) {
			DoubleKeyClickThread doubleKeyClickThread = Cache.tempGridList
					.get(i).getClearMineListener().getDoubleKeyClickThread();
			// ֻҪ״̬���ǳ������״̬���ֶ�����
			if (!doubleKeyClickThread.getState()
					.equals(Thread.State.TERMINATED)) {
				doubleKeyClickThread.setClose(true);
				doubleKeyClickThread.interrupt();
			}
		}
	}

	/**
	 * �������ʾ
	 */
	private void initView() {
		if (Cache.view != null) {
			Cache.view.setVisible(false);
			Cache.view = null;
		}
		Cache.view = new View();
	}

	/**
	 * �������еĸ��ӡ����ף���д����
	 */
	public void initGridArray() {
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		int rows = info.getRows();
		int columns = info.getColumns();
		// ��ֵ������
		Cache.gridArray = new Grid[rows][columns];
		// ����
		this.layMines(Cache.gridArray, info);
		// ������
		this.fillShow(Cache.gridArray);
		// isFirst��Ϊtrue
		Cache.isFirst = true;
	}

	/**
	 * ����<br>
	 * 1.��������������Ӧ�����������<br>
	 * 2.�������и��ӣ���ź��������ͬ���Ǹ����Ӿ�����Ϊ��<br>
	 * 
	 * @param gridArray
	 */
	private void layMines(Grid[][] gridArray, LevelInfo info) {
		int mineCount = info.getMineCount();
		// �õ�������ļ���
		Set<Integer> randomSet = this.createRandom(
				info.getRows() * info.getColumns(), mineCount);
		int count = 0;
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[i].length; j++) {
				// �ȴ�������
				gridArray[i][j] = new Grid(i, j);
				// ���������а���count���˸��Ӿ���Ϊ�׳���
				if (randomSet.contains(count++)) {
					// ���Ϊ����
					gridArray[i][j].setMine(true);
				}
			}
		}
	}

	/**
	 * ����mineCount����������������ظ��ģ�����ʹ��Set��������hash��ѯ��ʱ��������
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
			// set�������ӵ�mineCount��ʱ���return
			if (set.size() == mineCount) {
				return set;
			}
		}
	}

	/**
	 * ��д�����е����ݡ���߽���show�������������֣��գ�
	 * 
	 * 1.�жϱ����Ƿ�����<br>
	 * 2.�ж�����Χÿһ�������Ƿ�����<br>
	 * 
	 * @param gridArray
	 */
	private void fillShow(Grid[][] gridArray) {
		for (int i = 0; i < gridArray.length; i++) {
			for (int j = 0; j < gridArray[i].length; j++) {
				// ������ÿһ������
				this.checkGrid(gridArray[i][j]);
			}
		}
	}

	/**
	 * 1.�жϱ����Ƿ�����<br>
	 * 2.�ж�����Χÿһ�������Ƿ�����<br>
	 * 
	 * @param grid
	 * 
	 * @return
	 */
	private void checkGrid(Grid grid) {
		// �ж��Ƿ�����
		if (grid.isMine()) {
			return;
		}
		List<Grid> roundGridList = Manager.getInstance().addRound8GridToList(
				grid);
		int count = 0;
		for (Grid g : roundGridList) {
			count = this.isMineAndCount(g, count);
		}

		// �ж�count
		SHOW show = SHOW.getShow(count);
		if (show == null) {
			// TODO show�����ڣ���Ҫ���쳣����
			// ��������ִ��һ�㲻�����ߵ���һ��
		}
		grid.setShow(show);
	}

	/**
	 * �ж���������Ƿ����ס�
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
