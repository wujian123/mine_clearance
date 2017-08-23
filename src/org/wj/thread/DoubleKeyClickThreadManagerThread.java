package org.wj.thread;

import java.util.concurrent.BrokenBarrierException;

import org.wj.action.ClearMineListener;
import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.manager.Manager;
import org.wj.model.Grid;


/**
 * 原先的设计：在扫雷游戏（高级）一启动就需要占据40M的内存空间。原因在于两个，16*30=480个格子所占内存和480个为了实现双键单击事件所起的线程<br>
 * 480个格子不知道能不能实用轻量模式来优化，这个暂时先放着，等我把轻量模式研究下再考虑优化<br>
 * 480个线程现在考虑没有必要在启动时就开启，而是在需要用到的时候才开启，在可以结束的时候就over掉<br>
 * 现在新建一个480个线程的管理线程<br>
 * 把打开的格子grid放入一个List中（空白格子除外），一个将要被此线程管理的list中。<br>
 * 每次程序进入到要打开格子（包括普通格子，空白格子，也包括双键单击）之前就把上面的List给锁上，防止此线程读取List的时候得到脏数据。<br>
 * 当一次操作的打开格子行为结束后，main线程释放锁，把锁的拥有权交换给此线程，此线程遍历List中的grid对象<br>
 * 1.此格子所持有的线程处于State.NEW的状态。就把此格子持有的线程启动<br>
 * 2.此格子的周围（除了插旗的）格子都已经被打开，把此格子所持有的线程销毁<br>
 * 此线程要与main主线程进行锁拥有权的切换交互工作<br>
 * 此线程是在整个生命周期都应该存在的，触发它去遍历是在每次格子打开之后<br>
 * 
 * 下面使用三种方法实现与main线程交互时的同步工作<br>
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
public class DoubleKeyClickThreadManagerThread extends Thread {

//	@Override
//	public void run() {
//		while (true) {
//			// 启动锁
//			synchronized (Cache.tempGridList) {
//				// 进来之后首先等待
//				try {
//					Cache.tempGridList.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				// 进入list中的grid肯定是open=true，且不为空的。 
//				for (int i = 0; i < Cache.tempGridList.size(); i++) {
//					Grid grid = Cache.tempGridList.get(i);
//					ClearMineListener listener = grid.getClearMineListener();
//					DoubleKeyClickThread doubleKeyClickThread = listener.getDoubleKeyClickThread();
//					if(doubleKeyClickThread == null){
//						listener.setDoubleKeyClickThread(new DoubleKeyClickThread());
//						listener.getDoubleKeyClickThread().start();
//						continue;
//					}
//					// 查询这个格子周围的格子，假如都已经打开（插旗的除外），则说明双键单击事件已经没有意义，可以释放线程
//					if (Manager.getInstance().checkGridThreadCanBeClose(grid)) {
//						// 先把线程设置为可以关闭的状态
//						doubleKeyClickThread.setClose(true);
//						// 打断线程等于是激活，然后线程判断无法进入while会自动结束释放掉
//						doubleKeyClickThread.interrupt();
//						// 完成之后把格子从list中删除掉。
//						Cache.tempGridList.remove(i);
//						//这边的i--非常重要，少了它，那程序就是个大BUG。花了4个小时的时间找错误。最后发现是个低级错误。就是少了这行代码惹的祸
//						i--;
//					}
//					// 这两项都不成立的情况说明，这个格子所持有的双键单击等待线程还有利用价值。不做任何事情。
//				}
//			}
//		}
//	}
	
	/**
	 * 使用同步所ReentrantLock和CyclicBarrier来实现
	 */
//	@Override
//	public void run() {
//		while(true){
//			try {
//				
//				 // 线程此时处于等待当中，只有等到ClearMineListener中的barrier有main线程进入await()方法的时候才唤醒这边
//				Constant.barrier.await();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (BrokenBarrierException e) {
//				e.printStackTrace();
//			}
//			
//			
//			try {
//				//锁上
//				Constant.lock.lock();
//				// 进入list中的grid肯定是open=true，且不为空的。 
//				for (int i = 0; i < Cache.tempGridList.size(); i++) {
//					Grid grid = Cache.tempGridList.get(i);
//					ClearMineListener listener = grid.getClearMineListener();
//					DoubleKeyClickThread doubleKeyClickThread = listener.getDoubleKeyClickThread();
//					if(doubleKeyClickThread == null){
//						listener.setDoubleKeyClickThread(new DoubleKeyClickThread());
//						listener.getDoubleKeyClickThread().start();
//						continue;
//					}
//					// 查询这个格子周围的格子，假如都已经打开（插旗的除外），则说明双键单击事件已经没有意义，可以释放线程
//					if (Manager.getInstance().checkGridThreadCanBeClose(grid)) {
//						// 先把线程设置为可以关闭的状态
//						doubleKeyClickThread.setClose(true);
//						// 打断线程等于是激活，然后线程判断无法进入while会自动结束释放掉
//						doubleKeyClickThread.interrupt();
//						// 完成之后把格子从list中删除掉。
//						Cache.tempGridList.remove(i);
//						//这边的i--非常重要，少了它，那程序就是个大BUG。花了4个小时的时间找错误。最后发现是个低级错误。就是少了这行代码惹的祸
//						i--;
//					}
//					// 这两项都不成立的情况说明，这个格子所持有的双键单击等待线程还有利用价值。不做任何事情。
//				}
//			} finally {
//				Constant.lock.unlock();
//			}
//		}
//	}
	
	/**
	 * 使用Semaphore和CyclicBarrier配合实现同步
	 */
	@Override
	public void run() {
		while(true){
			try {
				// 进入之后就处于等待中，等待另一个线程也走到barrier.await()
				Constant.barrier.await();
				// 请求信号量semaphore进入单线程的过滤通道。
				// 如果main线程持有semaphore，那semaphore被独占中，还会继续等待，直到main线程执行semaphore.release()
				Constant.semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			
			// 进入list中的grid肯定是open=true，且不为空的。 
			for (int i = 0; i < Cache.tempGridList.size(); i++) {
				Grid grid = Cache.tempGridList.get(i);
				ClearMineListener listener = grid.getClearMineListener();
				DoubleKeyClickThread doubleKeyClickThread = listener.getDoubleKeyClickThread();
				if(doubleKeyClickThread == null){
					listener.setDoubleKeyClickThread(new DoubleKeyClickThread());
					listener.getDoubleKeyClickThread().start();
					continue;
				}
				// 查询这个格子周围的格子，假如都已经打开（插旗的除外），则说明双键单击事件已经没有意义，可以释放线程
				if (Manager.getInstance().checkGridThreadCanBeClose(grid)) {
					// 先把线程设置为可以关闭的状态
					doubleKeyClickThread.setClose(true);
					// 打断线程等于是激活，然后线程判断无法进入while会自动结束释放掉
					doubleKeyClickThread.interrupt();
					// 完成之后把格子从list中删除掉。
					Cache.tempGridList.remove(i);
					//这边的i--非常重要，少了它，那程序就是个大BUG。花了4个小时的时间找错误。最后发现是个低级错误。就是少了这行代码惹的祸
					i--;
				}
				// 这两项都不成立的情况说明，这个格子所持有的双键单击等待线程还有利用价值。不做任何事情。
			}
			// 线程执行完成后，需要放开对信号量semaphore的控制权
			Constant.semaphore.release();
		}
	}
}
