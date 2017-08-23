package org.wj.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

/**
 * 对swing操作的工具类
 * 
 * @author 吴键
 * 
 */
public class SwingUtil {

	/**
	 * 获取当前屏幕中点
	 * 
	 * @return
	 */
	public static Point getCurrentWindowCenterPoint() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point(dimension.width / 2, dimension.height / 2);
	}

	/**
	 * 要使得窗口在中间打开，获取location的点
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
	 * 弹出选择对话框
	 * @param parent
	 * @param message
	 * @return
	 */
	public static int showConfirm(Component parent, String message) {
		return JOptionPane.showConfirmDialog(parent, message);
	}

	/**
	 * 弹出信息
	 * @param parent
	 * @param message
	 */
	public static void showMessage(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message);
	}
}
