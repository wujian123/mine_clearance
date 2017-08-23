package org.wj.cache;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.wj.common.LEVEL;
import org.wj.model.Grid;
import org.wj.model.LevelInfo;


/**
 * ������
 * 
 * @author ���
 * 
 */
public class Cache {

	/**
	 * ����3�ּ����������Ϣ<br>
	 * ����ʹ��HashMap�����������ֳ���˳�����⡣HashMapΪ�˴ﵽ����ٶȣ�ʹ��hash�㷨��������ġ�<br>
	 * ���ڻ���TreeMap<br>
	 * 
	 */
	public static final Map<LEVEL, LevelInfo> levelInfoMap = new TreeMap<LEVEL, LevelInfo>(
			new Comparator<LEVEL>() {
				@Override
				public int compare(LEVEL o1, LEVEL o2) {
					return o1.getId() - o2.getId();
				}
			});
	
	/**
	 * Ϊ�Ż�Ч�ʣ�ʹ����һ����ʱ��list���grid���ж��Ƿ�Ӧ����������˫�������¼��ȴ��߳�<br>
	 * ��Ϊ�����Ҫ��������������������ʹ��linkedList��ArrayListҪ��<br>
	 * 
	 */
	public static final List<Grid> tempGridList = new LinkedList<Grid>();

	/**
	 * �������еĸ�����Ϣ,��ʼ����ʱ��ᱻ����ֵ
	 */
	public static Grid[][] gridArray = null;

	/**
	 * ����ͼƬ·��,ʹ��Properties�ļ�����
	 */
	public static Properties imgInfo = null;

	/**
	 * ��Ϸ�Ƿ��ڽ���״̬
	 */
	public static boolean isGameOver;

	/**
	 * �ָ���ť
	 */
	public static JButton faceButton = null;

	/**
	 * ��¼������label
	 */
	public static JLabel mineNumLabel = null;

	/**
	 * ��¼ʱ���label
	 */
	public static JLabel timeLabel = null;

	/**
	 * ��ǰ��ҳ��
	 */
	public static JFrame view = null;

	/**
	 * �Ƿ��ǵ�һ�ε��
	 */
	public static boolean isFirst;

	/**
	 * ��¼�û�����ĸ��Ӹ����������ж��������Ƿ�ɹ�ɨ��
	 */
	public static int count;
}
