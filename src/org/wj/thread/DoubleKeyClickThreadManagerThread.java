package org.wj.thread;

import java.util.concurrent.BrokenBarrierException;

import org.wj.action.ClearMineListener;
import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.manager.Manager;
import org.wj.model.Grid;


/**
 * ԭ�ȵ���ƣ���ɨ����Ϸ���߼���һ��������Ҫռ��40M���ڴ�ռ䡣ԭ������������16*30=480��������ռ�ڴ��480��Ϊ��ʵ��˫�������¼�������߳�<br>
 * 480�����Ӳ�֪���ܲ���ʵ������ģʽ���Ż��������ʱ�ȷ��ţ����Ұ�����ģʽ�о����ٿ����Ż�<br>
 * 480���߳����ڿ���û�б�Ҫ������ʱ�Ϳ�������������Ҫ�õ���ʱ��ſ������ڿ��Խ�����ʱ���over��<br>
 * �����½�һ��480���̵߳Ĺ����߳�<br>
 * �Ѵ򿪵ĸ���grid����һ��List�У��հ׸��ӳ��⣩��һ����Ҫ�����̹߳����list�С�<br>
 * ÿ�γ�����뵽Ҫ�򿪸��ӣ�������ͨ���ӣ��հ׸��ӣ�Ҳ����˫��������֮ǰ�Ͱ������List�����ϣ���ֹ���̶߳�ȡList��ʱ��õ������ݡ�<br>
 * ��һ�β����Ĵ򿪸�����Ϊ������main�߳��ͷ�����������ӵ��Ȩ���������̣߳����̱߳���List�е�grid����<br>
 * 1.�˸��������е��̴߳���State.NEW��״̬���ͰѴ˸��ӳ��е��߳�����<br>
 * 2.�˸��ӵ���Χ�����˲���ģ����Ӷ��Ѿ����򿪣��Ѵ˸��������е��߳�����<br>
 * ���߳�Ҫ��main���߳̽�����ӵ��Ȩ���л���������<br>
 * ���߳����������������ڶ�Ӧ�ô��ڵģ�������ȥ��������ÿ�θ��Ӵ�֮��<br>
 * 
 * ����ʹ�����ַ���ʵ����main�߳̽���ʱ��ͬ������<br>
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
public class DoubleKeyClickThreadManagerThread extends Thread {

//	@Override
//	public void run() {
//		while (true) {
//			// ������
//			synchronized (Cache.tempGridList) {
//				// ����֮�����ȵȴ�
//				try {
//					Cache.tempGridList.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				// ����list�е�grid�϶���open=true���Ҳ�Ϊ�յġ� 
//				for (int i = 0; i < Cache.tempGridList.size(); i++) {
//					Grid grid = Cache.tempGridList.get(i);
//					ClearMineListener listener = grid.getClearMineListener();
//					DoubleKeyClickThread doubleKeyClickThread = listener.getDoubleKeyClickThread();
//					if(doubleKeyClickThread == null){
//						listener.setDoubleKeyClickThread(new DoubleKeyClickThread());
//						listener.getDoubleKeyClickThread().start();
//						continue;
//					}
//					// ��ѯ���������Χ�ĸ��ӣ����綼�Ѿ��򿪣�����ĳ��⣩����˵��˫�������¼��Ѿ�û�����壬�����ͷ��߳�
//					if (Manager.getInstance().checkGridThreadCanBeClose(grid)) {
//						// �Ȱ��߳�����Ϊ���Թرյ�״̬
//						doubleKeyClickThread.setClose(true);
//						// ����̵߳����Ǽ��Ȼ���߳��ж��޷�����while���Զ������ͷŵ�
//						doubleKeyClickThread.interrupt();
//						// ���֮��Ѹ��Ӵ�list��ɾ������
//						Cache.tempGridList.remove(i);
//						//��ߵ�i--�ǳ���Ҫ�����������ǳ�����Ǹ���BUG������4��Сʱ��ʱ���Ҵ���������Ǹ��ͼ����󡣾����������д����ǵĻ�
//						i--;
//					}
//					// ����������������˵����������������е�˫�������ȴ��̻߳������ü�ֵ�������κ����顣
//				}
//			}
//		}
//	}
	
	/**
	 * ʹ��ͬ����ReentrantLock��CyclicBarrier��ʵ��
	 */
//	@Override
//	public void run() {
//		while(true){
//			try {
//				
//				 // �̴߳�ʱ���ڵȴ����У�ֻ�еȵ�ClearMineListener�е�barrier��main�߳̽���await()������ʱ��Ż������
//				Constant.barrier.await();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (BrokenBarrierException e) {
//				e.printStackTrace();
//			}
//			
//			
//			try {
//				//����
//				Constant.lock.lock();
//				// ����list�е�grid�϶���open=true���Ҳ�Ϊ�յġ� 
//				for (int i = 0; i < Cache.tempGridList.size(); i++) {
//					Grid grid = Cache.tempGridList.get(i);
//					ClearMineListener listener = grid.getClearMineListener();
//					DoubleKeyClickThread doubleKeyClickThread = listener.getDoubleKeyClickThread();
//					if(doubleKeyClickThread == null){
//						listener.setDoubleKeyClickThread(new DoubleKeyClickThread());
//						listener.getDoubleKeyClickThread().start();
//						continue;
//					}
//					// ��ѯ���������Χ�ĸ��ӣ����綼�Ѿ��򿪣�����ĳ��⣩����˵��˫�������¼��Ѿ�û�����壬�����ͷ��߳�
//					if (Manager.getInstance().checkGridThreadCanBeClose(grid)) {
//						// �Ȱ��߳�����Ϊ���Թرյ�״̬
//						doubleKeyClickThread.setClose(true);
//						// ����̵߳����Ǽ��Ȼ���߳��ж��޷�����while���Զ������ͷŵ�
//						doubleKeyClickThread.interrupt();
//						// ���֮��Ѹ��Ӵ�list��ɾ������
//						Cache.tempGridList.remove(i);
//						//��ߵ�i--�ǳ���Ҫ�����������ǳ�����Ǹ���BUG������4��Сʱ��ʱ���Ҵ���������Ǹ��ͼ����󡣾����������д����ǵĻ�
//						i--;
//					}
//					// ����������������˵����������������е�˫�������ȴ��̻߳������ü�ֵ�������κ����顣
//				}
//			} finally {
//				Constant.lock.unlock();
//			}
//		}
//	}
	
	/**
	 * ʹ��Semaphore��CyclicBarrier���ʵ��ͬ��
	 */
	@Override
	public void run() {
		while(true){
			try {
				// ����֮��ʹ��ڵȴ��У��ȴ���һ���߳�Ҳ�ߵ�barrier.await()
				Constant.barrier.await();
				// �����ź���semaphore���뵥�̵߳Ĺ���ͨ����
				// ���main�̳߳���semaphore����semaphore����ռ�У���������ȴ���ֱ��main�߳�ִ��semaphore.release()
				Constant.semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			
			// ����list�е�grid�϶���open=true���Ҳ�Ϊ�յġ� 
			for (int i = 0; i < Cache.tempGridList.size(); i++) {
				Grid grid = Cache.tempGridList.get(i);
				ClearMineListener listener = grid.getClearMineListener();
				DoubleKeyClickThread doubleKeyClickThread = listener.getDoubleKeyClickThread();
				if(doubleKeyClickThread == null){
					listener.setDoubleKeyClickThread(new DoubleKeyClickThread());
					listener.getDoubleKeyClickThread().start();
					continue;
				}
				// ��ѯ���������Χ�ĸ��ӣ����綼�Ѿ��򿪣�����ĳ��⣩����˵��˫�������¼��Ѿ�û�����壬�����ͷ��߳�
				if (Manager.getInstance().checkGridThreadCanBeClose(grid)) {
					// �Ȱ��߳�����Ϊ���Թرյ�״̬
					doubleKeyClickThread.setClose(true);
					// ����̵߳����Ǽ��Ȼ���߳��ж��޷�����while���Զ������ͷŵ�
					doubleKeyClickThread.interrupt();
					// ���֮��Ѹ��Ӵ�list��ɾ������
					Cache.tempGridList.remove(i);
					//��ߵ�i--�ǳ���Ҫ�����������ǳ�����Ǹ���BUG������4��Сʱ��ʱ���Ҵ���������Ǹ��ͼ����󡣾����������д����ǵĻ�
					i--;
				}
				// ����������������˵����������������е�˫�������ȴ��̻߳������ü�ֵ�������κ����顣
			}
			// �߳�ִ����ɺ���Ҫ�ſ����ź���semaphore�Ŀ���Ȩ
			Constant.semaphore.release();
		}
	}
}
