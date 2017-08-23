package org.wj.thread;

import org.wj.common.Constant;

/**
 * 为了实现鼠标双键同击
 * 
 * @author 吴键
 * 
 */
public class DoubleKeyClickThread extends Thread {

	// 为了切换界面时，线程的释放
	private boolean isClose;

	public void setClose(boolean isClose) {
		this.isClose = isClose;
	}

	/**
	 * 其实这边没有必要使用synchronized。 为了线程复用才用的。不用同步快就wait。JVM就不知道锁住的是谁。
	 * 不知道有没有更好的线程复用方法
	 */
	@Override
	public synchronized void run() {
		while (!isClose) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// 这边故意让它进入被打断异常区 目的是要线程复用。等待中的线程被打断就相当于被激活。
				try {
					Thread.sleep(Constant.DOUBLE_KEY_CLICK_INTEVAL_TIME);
				} catch (InterruptedException e1) {
					// 这边不需要做任何处理，就算发生异常，也只是再次循环到wait状态
				}
			}
		}
	}
}
