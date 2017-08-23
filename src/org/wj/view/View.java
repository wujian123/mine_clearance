package org.wj.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.common.LEVEL;
import org.wj.manager.Manager;
import org.wj.model.LevelInfo;
import org.wj.util.SwingUtil;


/**
 * ����
 * 
 * @author ���
 * 
 */
@SuppressWarnings("serial")
public class View extends JFrame {

	// �˵�bar
	private JMenuBar menuBar = null;

	// ��Ϸ�˵�
	private JMenu gameMenu = null;
	// �����˵�
	private JMenu helpMenu = null;

	// ��ʼ�˵�item
	private JMenuItem startMenuItem = null;
	// �����˵�item
	private JMenuItem primaryMenuItem = null;
	// �м��˵�item
	private JMenuItem middleMenuItem = null;
	// �߼��˵�item
	private JMenuItem advanceMenuItem = null;
	// ɨ��Ӣ�۰�
	private JMenuItem heroBarItem = null;
	// ����item
	private JMenuItem aboutMenuItem = null;

	// �ָ���
	private JSeparator separator = null;

	// ��ʱ�����������ָ�
	private JPanel countPanel = null;
	// ����
	private JPanel minePanel = null;

	// ���Ͱ�ť
	private JButton faceButton = null;
	// ����Label
	private JLabel mineNumLabel = null;
	// ����ʱ��Label
	private JLabel timeLabel = null;

	public View() {
		// ��ʼ���ؼ�
		this.initComponent();
		// ����¼�
		this.addListener();
	}

	/**
	 * ��ʼ������
	 */
	private void initComponent() {
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		menuBar = new JMenuBar();

		gameMenu = new JMenu("��Ϸ");
		helpMenu = new JMenu("����");

		startMenuItem = new JMenuItem("����");
		primaryMenuItem = new JMenuItem("����");
		middleMenuItem = new JMenuItem("�м�");
		advanceMenuItem = new JMenuItem("�߼�");
		heroBarItem = new JMenuItem("ɨ��Ӣ�۰�");
		aboutMenuItem = new JMenuItem("����ɨ��");

		separator = new JSeparator();

		countPanel = new JPanel();
		countPanel.setLayout(new GridLayout(1, 3, info.getHgap(), 0));

		faceButton = new JButton(Constant.SMILE_ICON);
		// �������ť��������������ʹ�á�
		// ��Ϊ������һ��������ȥ������ռ�ȽϷ������������м佻����͵����
		Cache.faceButton = faceButton;

		mineNumLabel = new JLabel();
		// ���뻺��
		Cache.mineNumLabel = mineNumLabel;
		Manager.getInstance().restoreMineCount(info);
		mineNumLabel.setForeground(Color.RED);
		mineNumLabel.setFont(Constant.myFont);

		timeLabel = new JLabel(Constant.INIT_TIME_COUNT);
		// ���뻺��
		Cache.timeLabel = timeLabel;

		timeLabel.setForeground(Color.RED);
		timeLabel.setFont(Constant.myFont);

		minePanel = new JPanel();
		minePanel.setLayout(new GridLayout(info.getRows(), info.getColumns()));

		// ���ظ���
		Manager.getInstance().loadGrid(minePanel);

		gameMenu.add(startMenuItem);
		gameMenu.add(separator);
		gameMenu.add(primaryMenuItem);
		gameMenu.add(middleMenuItem);
		gameMenu.add(advanceMenuItem);
		gameMenu.add(heroBarItem);

		helpMenu.add(aboutMenuItem);

		menuBar.add(gameMenu);
		menuBar.add(helpMenu);

		countPanel.add(mineNumLabel);
		countPanel.add(faceButton);
		countPanel.add(timeLabel);

		this.add(menuBar, BorderLayout.NORTH);
		this.add(minePanel, BorderLayout.CENTER);
		this.add(countPanel, BorderLayout.SOUTH);

		this.setTitle("ɨ��");
		this.setResizable(false);
		Dimension dimension = new Dimension(info.getWidth(), info.getHeight());
		this.setSize(dimension);
		this.setLocation(SwingUtil.getCenterLocationPoint(dimension));
		this.setVisible(true);
		//���뻺��
		Cache.view = this;
	}

	/**
	 * Ϊ�ؼ���Ӽ���
	 */
	private void addListener() {
		// Ϊ��ǰ��������¼�
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// �رճ���֮ǰ��Ҫ�ѵ�ǰ����д�������ļ�
				Manager.getInstance().writeCurrentLevelToConfig();
				System.exit(0);
			}
		});

		// ���ð�ť
		faceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().reflash(minePanel);
			}
		});

		// ͬ���ð�ť
		startMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().reflash(minePanel);
			}
		});

		// �л�������
		primaryMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().changeLevel(LEVEL.LEVEL_1);
			}
		});

		// �л����м�
		middleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().changeLevel(LEVEL.LEVEL_2);
			}
		});

		// �л����߼�
		advanceMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().changeLevel(LEVEL.LEVEL_3);
			}
		});

		// ��ʾɨ��Ӣ�۰�
		heroBarItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HeroBar heroBar = new HeroBar(View.this);
				heroBar.setVisible(true);
			}
		});
	}
}
