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
 * 管理类，各种业务上的方法，使用单例
 * 
 * @author 吴键
 * 
 */
public class Manager {

	private static Manager instance = new Manager();

	private Manager() {
	}

	public static Manager getInstance() {
		return instance;
	}

	// ----------------------单例----------------------------------

	/**
	 * 刷新操作<br>
	 * 1.释放所有线程<br>
	 * 2.删除掉panel上的所有格子<br>
	 * 3.初始化所有格子<br>
	 * 4.把格子加入到panel<br>
	 * 5.panel重新显示<br>
	 * 6.还原所有参数<br>
	 * 
	 * @param panel
	 */
	public void reflash(JPanel panel) {
		// 清理所有遗留线程
		Initialization.getInstance().clearThread();
		// 删除掉panel上的所有格子
		panel.removeAll();
		// 初始化所有格子
		Initialization.getInstance().initGridArray();
		// 把格子加入到panel
		this.loadGrid(panel);
		// panel重新显示
		panel.setVisible(false);
		panel.setVisible(true);
		// 还原所有参数
		this.restoreParam();
	}

	/**
	 * 各个参数还原
	 */
	private void restoreParam() {
		// 设置为笑脸
		Cache.faceButton.setIcon(Constant.SMILE_ICON);
		// 重置完成后，需要把游戏结束标志设为false
		Cache.isGameOver = true;
		// isFirst置为true
		Cache.isFirst = true;
		// 消灭格子的计数器
		Cache.count = 0;
		// 清空 tempGridList
		Cache.tempGridList.clear();
		// 雷数还原
		this.restoreMineCount(Cache.levelInfoMap.get(Constant.CURRENT_LEVEL));
		// 计时器还原
		this.restoreTimeCount();
	}

	/**
	 * 加载格子
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
	 * 调整雷的个数
	 * 
	 * @param isAdd
	 */
	public void showMineCount(boolean isAdd) {
		int mineCount = Integer.parseInt(Cache.mineNumLabel.getText());
		if (isAdd) {
			mineCount++;
		} else {
			// 假如当前已经为0，则不再减少
			if (mineCount == 0) {
				return;
			}
			mineCount--;
		}
		Cache.mineNumLabel.setText(this.strToShow(String.valueOf(mineCount)));
	}

	/**
	 * 雷数还原
	 * 
	 * @param info
	 */
	public void restoreMineCount(LevelInfo info) {
		Cache.mineNumLabel.setText(this.strToShow(String.valueOf(info
				.getMineCount())));
	}

	/**
	 * 还原时间Label
	 */
	public void restoreTimeCount() {
		Cache.timeLabel.setText(Constant.INIT_TIME_COUNT);
	}

	/**
	 * 雷的个数计数器，和时间计数器，都需要用到转换
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
	 * 计数器 + 1，并且检查是否已经成功
	 */
	public void countAndCheckIsSuccess(boolean isAdd) {
		if (!isAdd) {
			Cache.count--;
			return;
		}
		int rows = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL).getRows();
		int columns = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL)
				.getColumns();
		// 累加到相等，就表示成功了。应该在这边判断是否破纪录，是，则跳出页面填写大名
		if (++Cache.count == rows * columns) {
			Cache.isGameOver = !Cache.isGameOver;
			// 检测是否破记录
			this.checkIsBreakRecord();
		}
	}

	/**
	 * 检查是否破几率
	 */
	private void checkIsBreakRecord() {
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		// 老记录
		Hero oldHero = info.getHero();
		int oldTimeCount = oldHero.getTime();
		// 新记录
		int newTimeCount = Integer.valueOf(Cache.timeLabel.getText());
		if (newTimeCount > oldTimeCount) {
			// 没破记录，直接return;
			return;
		}
		// 说明破纪录了
		BreakRecord breakRecord = new BreakRecord(Cache.view, info.getLevel()
				.getDescription(), oldHero.getName());
		breakRecord.setVisible(true);
	}

	/**
	 * 关闭之前把当前级别写入配置文件
	 */
	public void writeCurrentLevelToConfig() {
		XMLWriter.writeCurrentLevelToXML(String.valueOf(Constant.CURRENT_LEVEL
				.getId()));
	}

	/**
	 * 切换级别
	 * 
	 * @param level
	 */
	public void changeLevel(LEVEL level) {
		Constant.CURRENT_LEVEL = level;
		Initialization.getInstance().changeLevel();
		// 各个参数的还原
		this.restoreParam();
	}

	/**
	 * 双键单击时，根据被点击的格子获取某些个信息
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
	 * 把标记信息，错误标记信息，未标记的格子信息，记录到gridMap
	 * 
	 * @param gridMap
	 * @param grid
	 */
	private void countBeOpen(Map<String, List<Grid>> gridMap, Grid grid) {
		if (grid.isOpen()) {
			return;
		}
		
		// 未标记情况
		if (!grid.isFlag()) {
			gridMap.get(Constant.BE_OPEN_GRID_LIST).add(grid);
			return;
		}
		
		// 标记了但不是雷,说明标记有错误
		if (!grid.isMine()) {
			gridMap.put(Constant.ERROR, null);
		}
		gridMap.get(Constant.FLAG_GRID_LIST).add(grid);
	}

	/**
	 * 打开所有空白格子
	 */
	public void openAllNullGrid(Grid grid) {
		// 先把自身打开
		this.openOrdinaryGrid(grid);
		this.openNullGrid(grid);
	}

	/**
	 * 打开应该被显示为空的格子，应为要打开一大片，使用递归操作完成
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
	 * 检查某个格子所持有的线程双键单击等待线程是否可以close掉。
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
	 * 分别判断目标grid周围的8个格子是否会有数组下标越界显现，如果没有，则加入到List中，供使用
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

		// 左上角
		if (iLowBound && jLowBound) {
			gridList.add(Cache.gridArray[iUp][jLeft]);
		}
		// 上
		if (iLowBound) {
			gridList.add(Cache.gridArray[iUp][j]);
		}
		// 右上角
		if (iLowBound && jUpBound) {
			gridList.add(Cache.gridArray[iUp][jRight]);
		}
		// 右
		if (jUpBound) {
			gridList.add(Cache.gridArray[i][jRight]);
		}
		// 右下
		if (iUpBound && jUpBound) {
			gridList.add(Cache.gridArray[iDown][jRight]);
		}
		// 下
		if (iUpBound) {
			gridList.add(Cache.gridArray[iDown][j]);
		}
		// 左下角
		if (iUpBound && jLowBound) {
			gridList.add(Cache.gridArray[iDown][jLeft]);
		}
		// 左
		if (jLowBound) {
			gridList.add(Cache.gridArray[i][jLeft]);
		}
		return gridList;
	}

	/**
	 * 检查格子是否打开或者是否被标记<br>
	 * 打开或者被标记则返回true，否则返回false<br>
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
	 * 打开指定的格子，如果还是空白，则递归调用openNullGrid方法
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
	 * 普通单个的打开某个按钮
	 * 
	 * @param grid
	 */
	public void openOrdinaryGrid(Grid grid) {
		grid.setForeground(Constant.FONTGROUND_COLOR_AFTER_OPEN);
		grid.setBackground(Constant.BACKGROUND_COLOR_AFTER_OPEN);
		grid.setText(grid.getShow().getShow());
		// 设置为打开状态
		grid.setOpen(true);
		// 计数并判断是否成功
		Manager.getInstance().countAndCheckIsSuccess(true);
		// 把个放入tempGirdList中
		this.addGridToTempGridList(grid);
	}

	/**
	 * 把个放入tempGirdList中<br>
	 * 必须是open状态的。不为空的
	 * 
	 * @param grid
	 */
	private void addGridToTempGridList(Grid grid) {
		if (!grid.isOpen()) {
			return;
		}
		// 打开的格子不可能是雷，所以这边OK的
		if (grid.getShow().equals(SHOW.NULL)) {
			return;
		}
		Cache.tempGridList.add(grid);
	}

	/**
	 * 打开所有雷
	 */
	public void showAllMine(Grid grid) {
		// 标志游戏已经结束
		Cache.isGameOver = true;
		for (int i = 0; i < Cache.gridArray.length; i++) {
			for (int j = 0; j < Cache.gridArray[i].length; j++) {
				if (Cache.gridArray[i][j].isMine()) {
					Cache.gridArray[i][j].setIcon(Constant.MINE_ICON);
				}
			}
		}
		// 把自身置为猪头
		grid.setIcon(Constant.PIG_ICON);
		Cache.faceButton.setIcon(Constant.CRY_ICON);
	}
}
