package org.wj.thread;

import org.wj.common.Constant;

/**
 * Ϊ��ʵ�����˫��ͬ��
 * 
 * @author ���
 * 
 */
public class DoubleKeyClickThread extends Thread {

	// Ϊ���л�����ʱ���̵߳��ͷ�
	private boolean isClose;

	public void setClose(boolean isClose) {
		this.isClose = isClose;
	}

	/**
	 * ��ʵ���û�б�Ҫʹ��synchronized�� Ϊ���̸߳��ò��õġ�����ͬ�����wait��JVM�Ͳ�֪����ס����˭��
	 * ��֪����û�и��õ��̸߳��÷���
	 */
	@Override
	public synchronized void run() {
		while (!isClose) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// ��߹����������뱻����쳣�� Ŀ����Ҫ�̸߳��á��ȴ��е��̱߳���Ͼ��൱�ڱ����
				try {
					Thread.sleep(Constant.DOUBLE_KEY_CLICK_INTEVAL_TIME);
				} catch (InterruptedException e1) {
					// ��߲���Ҫ���κδ������㷢���쳣��Ҳֻ���ٴ�ѭ����wait״̬
				}
			}
		}
	}
}
