package org.wj.thread;

import org.wj.cache.Cache;
import org.wj.manager.Manager;


/**
 * 扫雷计时器
 * 
 * @author 吴键
 *
 */
public class CountTimeThread implements Runnable {

	@Override
	public void run() {
		// 假如game没有结束的话就一直计时
		while (!Cache.isGameOver) {
			String timeShow = String.valueOf(Integer.valueOf(Cache.timeLabel
					.getText()) + 1);
			timeShow = Manager.getInstance().strToShow(timeShow);
			Cache.timeLabel.setText(timeShow);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				// TODO 异常处理
				// 没有打断操作，应该不会抛出异常
			}
		}
	}
}
