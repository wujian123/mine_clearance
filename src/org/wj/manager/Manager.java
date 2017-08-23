package org.wj.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.common.LEVEL;
import org.wj.common.SHOW;
import org.wj.model.Grid;
import org.wj.model.Hero;
import org.wj.model.LevelInfo;
import org.wj.util.XMLWriter;
import org.wj.view.BreakRecord;


/**
 * �����࣬����ҵ���ϵķ�����ʹ�õ���
 * 
 * @author ���
 * 
 */
public class Manager {

	private static Manager instance = new Manager();

	private Manager() {
	}

	public static Manager getInstance() {
		return instance;
	}

	// ----------------------����----------------------------------

	/**
	 * ˢ�²���<br>
	 * 1.�ͷ������߳�<br>
	 * 2.ɾ����panel�ϵ����и���<br>
	 * 3.��ʼ�����и���<br>
	 * 4.�Ѹ��Ӽ��뵽panel<br>
	 * 5.panel������ʾ<br>
	 * 6.��ԭ���в���<br>
	 * 
	 * @param panel
	 */
	public void reflash(JPanel panel) {
		// �������������߳�
		Initialization.getInstance().clearThread();
		// ɾ����panel�ϵ����и���
		panel.removeAll();
		// ��ʼ�����и���
		Initialization.getInstance().initGridArray();
		// �Ѹ��Ӽ��뵽panel
		this.loadGrid(panel);
		// panel������ʾ
		panel.setVisible(false);
		panel.setVisible(true);
		// ��ԭ���в���
		this.restoreParam();
	}

	/**
	 * ����������ԭ
	 */
	private void restoreParam() {
		// ����ΪЦ��
		Cache.faceButton.setIcon(Constant.SMILE_ICON);
		// ������ɺ���Ҫ����Ϸ������־��Ϊfalse
		Cache.isGameOver = true;
		// isFirst��Ϊtrue
		Cache.isFirst = true;
		// ������ӵļ�����
		Cache.count = 0;
		// ��� tempGridList
		Cache.tempGridList.clear();
		// ������ԭ
		this.restoreMineCount(Cache.levelInfoMap.get(Constant.CURRENT_LEVEL));
		// ��ʱ����ԭ
		this.restoreTimeCount();
	}

	/**
	 * ���ظ���
	 * 
	 * @param panel
	 */
	public void loadGrid(JPanel panel) {
		for (int i = 0; i < Cache.gridArray.length; i++) {
			for (int j = 0; j < Cache.gridArray[i].length; j++) {
				panel.add(Cache.gridArray[i][j]);
			}
		}
	}

	/**
	 * �����׵ĸ���
	 * 
	 * @param isAdd
	 */
	public void showMineCount(boolean isAdd) {
		int mineCount = Integer.parseInt(Cache.mineNumLabel.getText());
		if (isAdd) {
			mineCount++;
		} else {
			// ���統ǰ�Ѿ�Ϊ0�����ټ���
			if (mineCount == 0) {
				return;
			}
			mineCount--;
		}
		Cache.mineNumLabel.setText(this.strToShow(String.valueOf(mineCount)));
	}

	/**
	 * ������ԭ
	 * 
	 * @param info
	 */
	public void restoreMineCount(LevelInfo info) {
		Cache.mineNumLabel.setText(this.strToShow(String.valueOf(info
				.getMineCount())));
	}

	/**
	 * ��ԭʱ��Label
	 */
	public void restoreTimeCount() {
		Cache.timeLabel.setText(Constant.INIT_TIME_COUNT);
	}

	/**
	 * �׵ĸ�������������ʱ�������������Ҫ�õ�ת��
	 * 
	 * @param target
	 * @return
	 */
	public String strToShow(String target) {
		switch (target.length()) {
		case 1:
			target = "00" + target;
			break;
		case 2:
			target = "0" + target;
			break;
		}
		return target;
	}

	/**
	 * ������ + 1�����Ҽ���Ƿ��Ѿ��ɹ�
	 */
	public void countAndCheckIsSuccess(boolean isAdd) {
		if (!isAdd) {
			Cache.count--;
			return;
		}
		int rows = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL).getRows();
		int columns = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL)
				.getColumns();
		// �ۼӵ���ȣ��ͱ�ʾ�ɹ��ˡ�Ӧ��������ж��Ƿ��Ƽ�¼���ǣ�������ҳ����д����
		if (++Cache.count == rows * columns) {
			Cache.isGameOver = !Cache.isGameOver;
			// ����Ƿ��Ƽ�¼
			this.checkIsBreakRecord();
		}
	}

	/**
	 * ����Ƿ��Ƽ���
	 */
	private void checkIsBreakRecord() {
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		// �ϼ�¼
		Hero oldHero = info.getHero();
		int oldTimeCount = oldHero.getTime();
		// �¼�¼
		int newTimeCount = Integer.valueOf(Cache.timeLabel.getText());
		if (newTimeCount > oldTimeCount) {
			// û�Ƽ�¼��ֱ��return;
			return;
		}
		// ˵���Ƽ�¼��
		BreakRecord breakRecord = new BreakRecord(Cache.view, info.getLevel()
				.getDescription(), oldHero.getName());
		breakRecord.setVisible(true);
	}

	/**
	 * �ر�֮ǰ�ѵ�ǰ����д�������ļ�
	 */
	public void writeCurrentLevelToConfig() {
		XMLWriter.writeCurrentLevelToXML(String.valueOf(Constant.CURRENT_LEVEL
				.getId()));
	}

	/**
	 * �л�����
	 * 
	 * @param level
	 */
	public void changeLevel(LEVEL level) {
		Constant.CURRENT_LEVEL = level;
		Initialization.getInstance().changeLevel();
		// ���������Ļ�ԭ
		this.restoreParam();
	}

	/**
	 * ˫������ʱ�����ݱ�����ĸ��ӻ�ȡĳЩ����Ϣ
	 * 
	 * @param grid
	 * @return
	 */
	public Map<String, List<Grid>> getUnopenWithoutFlag(Grid grid) {
		Map<String, List<Grid>> gridMap = new HashMap<String, List<Grid>>();

		List<Grid> flagGridList = new ArrayList<Grid>();
		List<Grid> beOpenGridList = new ArrayList<Grid>();

		gridMap.put(Constant.FLAG_GRID_LIST, flagGridList);
		gridMap.put(Constant.BE_OPEN_GRID_LIST, beOpenGridList);

		List<Grid> roundGridList = this.addRound8GridToList(grid);
		for (Grid g : roundGridList) {
			this.countBeOpen(gridMap, g);
		}
		return gridMap;
	}

	/**
	 * �ѱ����Ϣ����������Ϣ��δ��ǵĸ�����Ϣ����¼��gridMap
	 * 
	 * @param gridMap
	 * @param grid
	 */
	private void countBeOpen(Map<String, List<Grid>> gridMap, Grid grid) {
		if (grid.isOpen()) {
			return;
		}
		
		// δ������
		if (!grid.isFlag()) {
			gridMap.get(Constant.BE_OPEN_GRID_LIST).add(grid);
			return;
		}
		
		// ����˵�������,˵������д���
		if (!grid.isMine()) {
			gridMap.put(Constant.ERROR, null);
		}
		gridMap.get(Constant.FLAG_GRID_LIST).add(grid);
	}

	/**
	 * �����пհ׸���
	 */
	public void openAllNullGrid(Grid grid) {
		// �Ȱ������
		this.openOrdinaryGrid(grid);
		this.openNullGrid(grid);
	}

	/**
	 * ��Ӧ�ñ���ʾΪ�յĸ��ӣ�ӦΪҪ��һ��Ƭ��ʹ�õݹ�������
	 * 
	 * @param grid
	 * @param rows
	 * @param columns
	 */
	private void openNullGrid(Grid grid) {
		List<Grid> roundGridList = this.addRound8GridToList(grid);
		for (Grid g : roundGridList) {
			this.open(g);
		}
	}

	/**
	 * ���ĳ�����������е��߳�˫�������ȴ��߳��Ƿ����close����
	 * 
	 * @param grid
	 * @return
	 */
	public boolean checkGridThreadCanBeClose(Grid grid) {
		List<Grid> roundGridList = this.addRound8GridToList(grid);
		for (Grid g : roundGridList) {
			if (!this.isOpenOrFlag(g)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �ֱ��ж�Ŀ��grid��Χ��8�������Ƿ���������±�Խ�����֣����û�У�����뵽List�У���ʹ��
	 * 
	 * @param grid
	 * @return
	 */
	public List<Grid> addRound8GridToList(Grid grid) {
		List<Grid> gridList = new ArrayList<Grid>();
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		int rows = info.getRows();
		int columns = info.getColumns();

		int i = grid.getRowNum();
		int j = grid.getColumnNum();

		int iUp = i - 1;
		int iDown = i + 1;
		int jLeft = j - 1;
		int jRight = j + 1;

		boolean iLowBound = iUp >= 0;
		boolean iUpBound = iDown < rows;
		boolean jLowBound = jLeft >= 0;
		boolean jUpBound = jRight < columns;

		// ���Ͻ�
		if (iLowBound && jLowBound) {
			gridList.add(Cache.gridArray[iUp][jLeft]);
		}
		// ��
		if (iLowBound) {
			gridList.add(Cache.gridArray[iUp][j]);
		}
		// ���Ͻ�
		if (iLowBound && jUpBound) {
			gridList.add(Cache.gridArray[iUp][jRight]);
		}
		// ��
		if (jUpBound) {
			gridList.add(Cache.gridArray[i][jRight]);
		}
		// ����
		if (iUpBound && jUpBound) {
			gridList.add(Cache.gridArray[iDown][jRight]);
		}
		// ��
		if (iUpBound) {
			gridList.add(Cache.gridArray[iDown][j]);
		}
		// ���½�
		if (iUpBound && jLowBound) {
			gridList.add(Cache.gridArray[iDown][jLeft]);
		}
		// ��
		if (jLowBound) {
			gridList.add(Cache.gridArray[i][jLeft]);
		}
		return gridList;
	}

	/**
	 * �������Ƿ�򿪻����Ƿ񱻱��<br>
	 * �򿪻��߱�����򷵻�true�����򷵻�false<br>
	 * 
	 * @param grid
	 * @return
	 */
	private boolean isOpenOrFlag(Grid grid) {
		if (grid.isOpen()) {
			return true;
		}

		if (grid.isFlag()) {
			return true;
		}
		return false;
	}

	/**
	 * ��ָ���ĸ��ӣ�������ǿհף���ݹ����openNullGrid����
	 * 
	 * @param rows
	 * @param columns
	 * @param grid
	 */
	private void open(Grid grid) {
		if (grid.isOpen()) {
			return;
		}
		if (grid.isFlag()) {
			return;
		}
		if (grid.isMine()) {
			return;
		}
		this.openOrdinaryGrid(grid);
		if (grid.getShow().equals(SHOW.NULL)) {
			this.openNullGrid(grid);
		}
	}

	/**
	 * ��ͨ�����Ĵ�ĳ����ť
	 * 
	 * @param grid
	 */
	public void openOrdinaryGrid(Grid grid) {
		grid.setForeground(Constant.FONTGROUND_COLOR_AFTER_OPEN);
		grid.setBackground(Constant.BACKGROUND_COLOR_AFTER_OPEN);
		grid.setText(grid.getShow().getShow());
		// ����Ϊ��״̬
		grid.setOpen(true);
		// �������ж��Ƿ�ɹ�
		Manager.getInstance().countAndCheckIsSuccess(true);
		// �Ѹ�����tempGirdList��
		this.addGridToTempGridList(grid);
	}

	/**
	 * �Ѹ�����tempGirdList��<br>
	 * ������open״̬�ġ���Ϊ�յ�
	 * 
	 * @param grid
	 */
	private void addGridToTempGridList(Grid grid) {
		if (!grid.isOpen()) {
			return;
		}
		// �򿪵ĸ��Ӳ��������ף��������OK��
		if (grid.getShow().equals(SHOW.NULL)) {
			return;
		}
		Cache.tempGridList.add(grid);
	}

	/**
	 * ��������
	 */
	public void showAllMine(Grid grid) {
		// ��־��Ϸ�Ѿ�����
		Cache.isGameOver = true;
		for (int i = 0; i < Cache.gridArray.length; i++) {
			for (int j = 0; j < Cache.gridArray[i].length; j++) {
				if (Cache.gridArray[i][j].isMine()) {
					Cache.gridArray[i][j].setIcon(Constant.MINE_ICON);
				}
			}
		}
		// ��������Ϊ��ͷ
		grid.setIcon(Constant.PIG_ICON);
		Cache.faceButton.setIcon(Constant.CRY_ICON);
	}
}
