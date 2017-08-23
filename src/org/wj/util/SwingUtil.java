package org.wj.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

/**
 * ��swing�����Ĺ�����
 * 
 * @author ���
 * 
 */
public class SwingUtil {

	/**
	 * ��ȡ��ǰ��Ļ�е�
	 * 
	 * @return
	 */
	public static Point getCurrentWindowCenterPoint() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point(dimension.width / 2, dimension.height / 2);
	}

	/**
	 * Ҫʹ�ô������м�򿪣���ȡlocation�ĵ�
	 * 
	 * @param dimension
	 * @return
	 */
	public static Point getCenterLocationPoint(Dimension dimension) {
		Point centerPoint = getCurrentWindowCenterPoint();
		return new Point(centerPoint.x - dimension.width / 2, centerPoint.y
				- dimension.height / 2);
	}

	/**
	 * ����ѡ��Ի���
	 * @param parent
	 * @param message
	 * @return
	 */
	public static int showConfirm(Component parent, String message) {
		return JOptionPane.showConfirmDialog(parent, message);
	}

	/**
	 * ������Ϣ
	 * @param parent
	 * @param message
	 */
	public static void showMessage(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message);
	}
}
