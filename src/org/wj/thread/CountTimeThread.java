package org.wj.thread;

import org.wj.cache.Cache;
import org.wj.manager.Manager;


/**
 * ɨ�׼�ʱ��
 * 
 * @author ���
 *
 */
public class CountTimeThread implements Runnable {

	@Override
	public void run() {
		// ����gameû�н����Ļ���һֱ��ʱ
		while (!Cache.isGameOver) {
			String timeShow = String.valueOf(Integer.valueOf(Cache.timeLabel
					.getText()) + 1);
			timeShow = Manager.getInstance().strToShow(timeShow);
			Cache.timeLabel.setText(timeShow);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				// TODO �쳣����
				// û�д�ϲ�����Ӧ�ò����׳��쳣
			}
		}
	}
}
