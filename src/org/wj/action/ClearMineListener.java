package org.wj.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;

import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.common.SHOW;
import org.wj.listener.DoubleKeyClickListener;
import org.wj.manager.Manager;
import org.wj.model.Grid;
import org.wj.thread.CountTimeThread;
import org.wj.thread.DoubleKeyClickThread;


/**
 * 这边涉及到扫雷核心的算法。应该比较复杂，所以单拿出来，不作为匿名内部类嵌在在Grid中<br>
 * 关于扫雷主界面的所有事件都在这个类中实现。<br>
 * 这个类中，基本只有一些if，else的判定工作，至于具体实现都放到了Manager类中来实现<br>
 *
 * 线程交互那块使用三种方法实现与DoubleKeyClickThreadManagerThread线程交互时的同步工作<br>
 * 1. synchronized同步块加wait()和notify(),老式的交互。<br>
 * 2. 并发包中ReentrantLock和CyclicBarrier配合实现同步<br>
 * 3. 使用Semaphore和CyclicBarrier配合实现同步<br>
 * 
 * 最后将内存从原来的40M降到了26M。
 * 
 * 其实一般情况下，这边无需同步，现在CPU的速度够快，当按下按钮后DoubleKeyClickThreadManagerThread线程可能在0.001秒就能完成它的run方法<br>
 * 人在下一次点击的时候应该是不可能有那么快的速度与DoubleKeyClickThreadManagerThread线程有相互冲突的地方<br>
 * 但是为了多练习线程方面的东西，这边故意加上了同步工作。其实没有必要<br>
 * 
 * @author 吴键
 * 
 */
public class ClearMineListener extends MouseAdapter {

	/*
	 * 鼠标单击时得到的数字 这边是为了实现双键单击 相当于在对象级别的一个缓存，配合线程等待100毫秒后
	 * 如果下一次点击的数字的数字与上一次不同则说明是双键单击 如果相同，说明不是。还原modifier 100毫秒后同样还原modifier
	 */
	private int modifier;

	/*
	 * 配合实现双键单击的线程 每次等待所设置的间隔时间之后会处于线程阻塞状态，需要下一次被打断。然后线程就能被重新激活，可以实现线程复用。
	 */
	private DoubleKeyClickThread doubleKeyClickThread = null;

	private Grid grid;

	public ClearMineListener(Grid grid) {
		this.grid = grid;
	}

	public DoubleKeyClickThread getDoubleKeyClickThread() {
		return doubleKeyClickThread;
	}

	public void setDoubleKeyClickThread(
			DoubleKeyClickThread doubleKeyClickThread) {
		this.doubleKeyClickThread = doubleKeyClickThread;
	}

	/**
	 * 这边变相使用了观察者模式自制了一个双键同击的事件<br>
	 * 这个项目中挺有意思的地方
	 */
	public DoubleKeyClickListener doubleKeyClickListener = new DoubleKeyClickListener() {
		@Override
		public void doubleKeyClicked() {
			// 如果在空的位置双键同击，没有意义
			if (grid.getShow().equals(SHOW.NULL)) {
				return;
			}
			// 如果是数字，则要判断它周围未打开的格子的个数
			int selfShow = grid.getShow().getId();
			Map<String, List<Grid>> gridMap = Manager.getInstance()
					.getUnopenWithoutFlag(grid);
			// 是否有错误的标记
			boolean isFlagError = gridMap.containsKey(Constant.ERROR);
			if (gridMap.get(Constant.FLAG_GRID_LIST).size() != selfShow) {
				return;
			}
			// 有错误，且相等，失败，全部打开
			if (isFlagError) {
				Manager.getInstance().showAllMine(grid);
			}

			/*
			 * 相等的情况,循环打开每个按钮 这边需要锁住Cache.tempGridList,在对tempGridList进行操作的时候
			 * 等到一次操作的所有格子打开完毕之后才能释放锁
			 * ，notify等待中的DoubleKeyClickThreadManagerThread
			 */
			// synchronized (Cache.tempGridList) {
			// for (Grid grid : gridMap.get(Constant.BE_OPEN_GRID_LIST)) {
			// if (grid.isOpen()) {
			// continue;
			// }
			// ClearMineListener.this.openGrid(grid);
			// }
			// Cache.tempGridList.notify();
			// }

			/*
			 * 上面使用老的synchronized的方法去实现线程同步工作
			 * 下面使用ReentrantLock和CyclicBarrier配合实现同步工作
			 */
			// try {
			// Constant.lock.lock();
			// for (Grid grid : gridMap.get(Constant.BE_OPEN_GRID_LIST)) {
			// if (grid.isOpen()) {
			// continue;
			// }
			// ClearMineListener.this.openGrid(grid);
			// }
			// Constant.barrier.await();
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// } catch (BrokenBarrierException e) {
			// e.printStackTrace();
			// } finally {
			// Constant.lock.unlock();
			// }

			/*
			 * 下面使用信号量Semaphore和CyclicBarrier配合实现 对并发包的使用应该还会有很多其他的方法去控制并发
			 */
			try {
				// 请求进入Semaphore
				Constant.semaphore.acquire();
				for (Grid grid : gridMap.get(Constant.BE_OPEN_GRID_LIST)) {
					if (grid.isOpen()) {
						continue;
					}
					ClearMineListener.this.openGrid(grid);
				}
				// 释放对semaphore的控制权
				Constant.semaphore.release();
				// 唤醒DoubleKeyClickThreadManagerThread线程
				Constant.barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 处理扫雷按钮事件<br>
	 * 1.判断是否是首次点击,是：切换isFirst标识，并开始计时。<br>
	 * 2.游戏是否已经结束，结束则直接返回<br>
	 * 3.判断格子是否已经被打开过，是：需要判断启动双键单击线程的等待，然后程序直接返回<br>
	 * 4.判定左击或者右击，分别进入<br>
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// isFirst为True就说明是第一次进来
		if (Cache.isFirst) {
			Cache.isFirst = !Cache.isFirst;
			Cache.isGameOver = false;
			// 计时开始
			new Thread(new CountTimeThread()).start();
		}

		// 判断游戏是否已经结束
		if (Cache.isGameOver) {
			return;
		}

		// 格子是否已经被打开过
		if (grid.isOpen()) {
			if (!grid.getShow().equals(SHOW.NULL)) {
				this.checkIsDoubleKeyClick(e.getModifiers());
			}
			return;
		}

		int modifier = e.getModifiers();
		// 左击
		if (modifier == 16) {
			this.leftClick(e);
		} else if (modifier == 4) {
			// 右击
			this.rightClick(e);
		}
	}

	/**
	 * 鼠标按下时笑脸变化
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (Cache.isGameOver) {
			return;
		}
		Cache.faceButton.setIcon(Constant.MIDDLE_ICON);
	}

	/**
	 * 鼠标放开时笑脸变化
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (Cache.isGameOver) {
			return;
		}
		Cache.faceButton.setIcon(Constant.SMILE_ICON);
	}

	/**
	 * 处理鼠标左击事件<br>
	 * 1.是否被插上小红旗<br>
	 * 2.判断自身是否是雷，是：打开所有雷，游戏结束<br>
	 * 3.没被插旗，也没有被打开过，就打开此格子 <br>
	 * 
	 * @param e
	 */
	private void leftClick(MouseEvent e) {
		// 如果被插上玫瑰，不做任何事情，直接返回
		if (grid.isFlag()) {
			return;
		}

		// 如果是雷，就显示全部的雷
		if (grid.isMine()) {
			// 打开所有雷
			Manager.getInstance().showAllMine(grid);
			return;
		}

		/*
		 * 这边需要锁住Cache.tempGridList,在对tempGridList进行操作的时候
		 * 等到一次操作的所有格子打开完毕之后才能释放锁，notify等待中的DoubleKeyClickThreadManagerThread
		 */
		// synchronized (Cache.tempGridList) {
		// this.openGrid(grid);
		// Cache.tempGridList.notify();
		// }

		/*
		 * 上面使用老的synchronized的方法去实现线程同步工作
		 * 下面使用ReentrantLock和CyclicBarrier配合实现同步工作
		 */
		// try {
		// Constant.lock.lock();
		// this.openGrid(grid);
		// // 这边不是让main线程去等待，而是去唤醒DoubleKeyClickThreadManagerThread线程
		// Constant.barrier.await();
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// } catch (BrokenBarrierException e1) {
		// e1.printStackTrace();
		// } finally {
		// Constant.lock.unlock();
		// }

		/*
		 * 下面使用信号量Semaphore和CyclicBarrier配合实现 对并发包的使用应该还会有很多其他的方法去控制并发
		 */
		try {
			// 请求进入Semaphore
			Constant.semaphore.acquire();
			this.openGrid(grid);
			// 释放对semaphore的控制权
			Constant.semaphore.release();
			// 唤醒DoubleKeyClickThreadManagerThread线程
			Constant.barrier.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 封装了格子的打开，供复用
	 * 
	 * @param grid
	 */
	private void openGrid(Grid grid) {
		// 如果是空白，就把一片都显示出来，比较复杂
		if (grid.getShow().equals(SHOW.NULL)) {
			Manager.getInstance().openAllNullGrid(grid);
		} else {
			// 普通各自打开
			Manager.getInstance().openOrdinaryGrid(grid);
		}
	}

	/**
	 * 处理鼠标右击事件。<br>
	 * 插旗
	 * 
	 * @param e
	 */
	private void rightClick(MouseEvent e) {
		if (grid.isOpen()) {
			return;
		}
		if (grid.isFlag()) {
			grid.setIcon(null);
			// 计录雷的个数 ++
			Manager.getInstance().showMineCount(true);
			// 计数器count-1
			Manager.getInstance().countAndCheckIsSuccess(false);
		} else {
			grid.setIcon(Constant.FLAG_ICON);
			// 计录雷的个数 --
			Manager.getInstance().showMineCount(false);
			// 计数器count+1
			Manager.getInstance().countAndCheckIsSuccess(true);
		}
		// 做标记
		grid.setFlag(!grid.isFlag());
	}

	/**
	 * 检查是否双键单击
	 * 
	 * @param modifier
	 */
	private void checkIsDoubleKeyClick(int modifier) {
		// 起一个线程，100毫秒后如果modifier自动变为0
		if (doubleKeyClickThread.getState().equals(Thread.State.TIMED_WAITING)) {
			if (this.modifier != modifier) {
				this.doubleKeyClickListener.doubleKeyClicked();
				this.modifier = 0;
				return;
			}
		} else {
			this.modifier = modifier;
			// 打断线程，其实是激活它，让它从新等待
			doubleKeyClickThread.interrupt();
		}
	}
}
