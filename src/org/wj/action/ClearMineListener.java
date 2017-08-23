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
 * ����漰��ɨ�׺��ĵ��㷨��Ӧ�ñȽϸ��ӣ����Ե��ó���������Ϊ�����ڲ���Ƕ����Grid��<br>
 * ����ɨ��������������¼������������ʵ�֡�<br>
 * ������У�����ֻ��һЩif��else���ж����������ھ���ʵ�ֶ��ŵ���Manager������ʵ��<br>
 *
 * �߳̽����ǿ�ʹ�����ַ���ʵ����DoubleKeyClickThreadManagerThread�߳̽���ʱ��ͬ������<br>
 * 1. synchronizedͬ�����wait()��notify(),��ʽ�Ľ�����<br>
 * 2. ��������ReentrantLock��CyclicBarrier���ʵ��ͬ��<br>
 * 3. ʹ��Semaphore��CyclicBarrier���ʵ��ͬ��<br>
 * 
 * ����ڴ��ԭ����40M������26M��
 * 
 * ��ʵһ������£��������ͬ��������CPU���ٶȹ��죬�����°�ť��DoubleKeyClickThreadManagerThread�߳̿�����0.001������������run����<br>
 * ������һ�ε����ʱ��Ӧ���ǲ���������ô����ٶ���DoubleKeyClickThreadManagerThread�߳����໥��ͻ�ĵط�<br>
 * ����Ϊ�˶���ϰ�̷߳���Ķ�������߹��������ͬ����������ʵû�б�Ҫ<br>
 * 
 * @author ���
 * 
 */
public class ClearMineListener extends MouseAdapter {

	/*
	 * ��굥��ʱ�õ������� �����Ϊ��ʵ��˫������ �൱���ڶ��󼶱��һ�����棬����̵߳ȴ�100�����
	 * �����һ�ε�������ֵ���������һ�β�ͬ��˵����˫������ �����ͬ��˵�����ǡ���ԭmodifier 100�����ͬ����ԭmodifier
	 */
	private int modifier;

	/*
	 * ���ʵ��˫���������߳� ÿ�εȴ������õļ��ʱ��֮��ᴦ���߳�����״̬����Ҫ��һ�α���ϡ�Ȼ���߳̾��ܱ����¼������ʵ���̸߳��á�
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
	 * ��߱���ʹ���˹۲���ģʽ������һ��˫��ͬ�����¼�<br>
	 * �����Ŀ��ͦ����˼�ĵط�
	 */
	public DoubleKeyClickListener doubleKeyClickListener = new DoubleKeyClickListener() {
		@Override
		public void doubleKeyClicked() {
			// ����ڿյ�λ��˫��ͬ����û������
			if (grid.getShow().equals(SHOW.NULL)) {
				return;
			}
			// ��������֣���Ҫ�ж�����Χδ�򿪵ĸ��ӵĸ���
			int selfShow = grid.getShow().getId();
			Map<String, List<Grid>> gridMap = Manager.getInstance()
					.getUnopenWithoutFlag(grid);
			// �Ƿ��д���ı��
			boolean isFlagError = gridMap.containsKey(Constant.ERROR);
			if (gridMap.get(Constant.FLAG_GRID_LIST).size() != selfShow) {
				return;
			}
			// �д�������ȣ�ʧ�ܣ�ȫ����
			if (isFlagError) {
				Manager.getInstance().showAllMine(grid);
			}

			/*
			 * ��ȵ����,ѭ����ÿ����ť �����Ҫ��סCache.tempGridList,�ڶ�tempGridList���в�����ʱ��
			 * �ȵ�һ�β��������и��Ӵ����֮������ͷ���
			 * ��notify�ȴ��е�DoubleKeyClickThreadManagerThread
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
			 * ����ʹ���ϵ�synchronized�ķ���ȥʵ���߳�ͬ������
			 * ����ʹ��ReentrantLock��CyclicBarrier���ʵ��ͬ������
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
			 * ����ʹ���ź���Semaphore��CyclicBarrier���ʵ�� �Բ�������ʹ��Ӧ�û����кܶ������ķ���ȥ���Ʋ���
			 */
			try {
				// �������Semaphore
				Constant.semaphore.acquire();
				for (Grid grid : gridMap.get(Constant.BE_OPEN_GRID_LIST)) {
					if (grid.isOpen()) {
						continue;
					}
					ClearMineListener.this.openGrid(grid);
				}
				// �ͷŶ�semaphore�Ŀ���Ȩ
				Constant.semaphore.release();
				// ����DoubleKeyClickThreadManagerThread�߳�
				Constant.barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * ����ɨ�װ�ť�¼�<br>
	 * 1.�ж��Ƿ����״ε��,�ǣ��л�isFirst��ʶ������ʼ��ʱ��<br>
	 * 2.��Ϸ�Ƿ��Ѿ�������������ֱ�ӷ���<br>
	 * 3.�жϸ����Ƿ��Ѿ����򿪹����ǣ���Ҫ�ж�����˫�������̵߳ĵȴ���Ȼ�����ֱ�ӷ���<br>
	 * 4.�ж���������һ����ֱ����<br>
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// isFirstΪTrue��˵���ǵ�һ�ν���
		if (Cache.isFirst) {
			Cache.isFirst = !Cache.isFirst;
			Cache.isGameOver = false;
			// ��ʱ��ʼ
			new Thread(new CountTimeThread()).start();
		}

		// �ж���Ϸ�Ƿ��Ѿ�����
		if (Cache.isGameOver) {
			return;
		}

		// �����Ƿ��Ѿ����򿪹�
		if (grid.isOpen()) {
			if (!grid.getShow().equals(SHOW.NULL)) {
				this.checkIsDoubleKeyClick(e.getModifiers());
			}
			return;
		}

		int modifier = e.getModifiers();
		// ���
		if (modifier == 16) {
			this.leftClick(e);
		} else if (modifier == 4) {
			// �һ�
			this.rightClick(e);
		}
	}

	/**
	 * ��갴��ʱЦ���仯
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (Cache.isGameOver) {
			return;
		}
		Cache.faceButton.setIcon(Constant.MIDDLE_ICON);
	}

	/**
	 * ���ſ�ʱЦ���仯
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (Cache.isGameOver) {
			return;
		}
		Cache.faceButton.setIcon(Constant.SMILE_ICON);
	}

	/**
	 * �����������¼�<br>
	 * 1.�Ƿ񱻲���С����<br>
	 * 2.�ж������Ƿ����ף��ǣ��������ף���Ϸ����<br>
	 * 3.û�����죬Ҳû�б��򿪹����ʹ򿪴˸��� <br>
	 * 
	 * @param e
	 */
	private void leftClick(MouseEvent e) {
		// ���������õ�壬�����κ����飬ֱ�ӷ���
		if (grid.isFlag()) {
			return;
		}

		// ������ף�����ʾȫ������
		if (grid.isMine()) {
			// ��������
			Manager.getInstance().showAllMine(grid);
			return;
		}

		/*
		 * �����Ҫ��סCache.tempGridList,�ڶ�tempGridList���в�����ʱ��
		 * �ȵ�һ�β��������и��Ӵ����֮������ͷ�����notify�ȴ��е�DoubleKeyClickThreadManagerThread
		 */
		// synchronized (Cache.tempGridList) {
		// this.openGrid(grid);
		// Cache.tempGridList.notify();
		// }

		/*
		 * ����ʹ���ϵ�synchronized�ķ���ȥʵ���߳�ͬ������
		 * ����ʹ��ReentrantLock��CyclicBarrier���ʵ��ͬ������
		 */
		// try {
		// Constant.lock.lock();
		// this.openGrid(grid);
		// // ��߲�����main�߳�ȥ�ȴ�������ȥ����DoubleKeyClickThreadManagerThread�߳�
		// Constant.barrier.await();
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// } catch (BrokenBarrierException e1) {
		// e1.printStackTrace();
		// } finally {
		// Constant.lock.unlock();
		// }

		/*
		 * ����ʹ���ź���Semaphore��CyclicBarrier���ʵ�� �Բ�������ʹ��Ӧ�û����кܶ������ķ���ȥ���Ʋ���
		 */
		try {
			// �������Semaphore
			Constant.semaphore.acquire();
			this.openGrid(grid);
			// �ͷŶ�semaphore�Ŀ���Ȩ
			Constant.semaphore.release();
			// ����DoubleKeyClickThreadManagerThread�߳�
			Constant.barrier.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * ��װ�˸��ӵĴ򿪣�������
	 * 
	 * @param grid
	 */
	private void openGrid(Grid grid) {
		// ����ǿհף��Ͱ�һƬ����ʾ�������Ƚϸ���
		if (grid.getShow().equals(SHOW.NULL)) {
			Manager.getInstance().openAllNullGrid(grid);
		} else {
			// ��ͨ���Դ�
			Manager.getInstance().openOrdinaryGrid(grid);
		}
	}

	/**
	 * ��������һ��¼���<br>
	 * ����
	 * 
	 * @param e
	 */
	private void rightClick(MouseEvent e) {
		if (grid.isOpen()) {
			return;
		}
		if (grid.isFlag()) {
			grid.setIcon(null);
			// ��¼�׵ĸ��� ++
			Manager.getInstance().showMineCount(true);
			// ������count-1
			Manager.getInstance().countAndCheckIsSuccess(false);
		} else {
			grid.setIcon(Constant.FLAG_ICON);
			// ��¼�׵ĸ��� --
			Manager.getInstance().showMineCount(false);
			// ������count+1
			Manager.getInstance().countAndCheckIsSuccess(true);
		}
		// �����
		grid.setFlag(!grid.isFlag());
	}

	/**
	 * ����Ƿ�˫������
	 * 
	 * @param modifier
	 */
	private void checkIsDoubleKeyClick(int modifier) {
		// ��һ���̣߳�100��������modifier�Զ���Ϊ0
		if (doubleKeyClickThread.getState().equals(Thread.State.TIMED_WAITING)) {
			if (this.modifier != modifier) {
				this.doubleKeyClickListener.doubleKeyClicked();
				this.modifier = 0;
				return;
			}
		} else {
			this.modifier = modifier;
			// ����̣߳���ʵ�Ǽ��������������µȴ�
			doubleKeyClickThread.interrupt();
		}
	}
}
