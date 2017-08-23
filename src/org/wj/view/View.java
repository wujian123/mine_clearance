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
 * 界面
 * 
 * @author 吴键
 * 
 */
@SuppressWarnings("serial")
public class View extends JFrame {

	// 菜单bar
	private JMenuBar menuBar = null;

	// 游戏菜单
	private JMenu gameMenu = null;
	// 帮助菜单
	private JMenu helpMenu = null;

	// 开始菜单item
	private JMenuItem startMenuItem = null;
	// 初级菜单item
	private JMenuItem primaryMenuItem = null;
	// 中级菜单item
	private JMenuItem middleMenuItem = null;
	// 高级菜单item
	private JMenuItem advanceMenuItem = null;
	// 扫雷英雄榜
	private JMenuItem heroBarItem = null;
	// 关于item
	private JMenuItem aboutMenuItem = null;

	// 分割线
	private JSeparator separator = null;

	// 计时，记雷数，恢复
	private JPanel countPanel = null;
	// 雷区
	private JPanel minePanel = null;

	// 脸型按钮
	private JButton faceButton = null;
	// 雷数Label
	private JLabel mineNumLabel = null;
	// 读秒时间Label
	private JLabel timeLabel = null;

	public View() {
		// 初始化控件
		this.initComponent();
		// 添加事件
		this.addListener();
	}

	/**
	 * 初始化界面
	 */
	private void initComponent() {
		LevelInfo info = Cache.levelInfoMap.get(Constant.CURRENT_LEVEL);
		menuBar = new JMenuBar();

		gameMenu = new JMenu("游戏");
		helpMenu = new JMenu("帮助");

		startMenuItem = new JMenuItem("开局");
		primaryMenuItem = new JMenuItem("初级");
		middleMenuItem = new JMenuItem("中级");
		advanceMenuItem = new JMenuItem("高级");
		heroBarItem = new JMenuItem("扫雷英雄榜");
		aboutMenuItem = new JMenuItem("关于扫雷");

		separator = new JSeparator();

		countPanel = new JPanel();
		countPanel.setLayout(new GridLayout(1, 3, info.getHgap(), 0));

		faceButton = new JButton(Constant.SMILE_ICON);
		// 把这个按钮缓存起来供后面使用。
		// 因为在另外一个类中再去拿这个空间比较烦，所以做个中间交换，偷懒。
		Cache.faceButton = faceButton;

		mineNumLabel = new JLabel();
		// 放入缓存
		Cache.mineNumLabel = mineNumLabel;
		Manager.getInstance().restoreMineCount(info);
		mineNumLabel.setForeground(Color.RED);
		mineNumLabel.setFont(Constant.myFont);

		timeLabel = new JLabel(Constant.INIT_TIME_COUNT);
		// 放入缓存
		Cache.timeLabel = timeLabel;

		timeLabel.setForeground(Color.RED);
		timeLabel.setFont(Constant.myFont);

		minePanel = new JPanel();
		minePanel.setLayout(new GridLayout(info.getRows(), info.getColumns()));

		// 加载格子
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

		this.setTitle("扫雷");
		this.setResizable(false);
		Dimension dimension = new Dimension(info.getWidth(), info.getHeight());
		this.setSize(dimension);
		this.setLocation(SwingUtil.getCenterLocationPoint(dimension));
		this.setVisible(true);
		//放入缓存
		Cache.view = this;
	}

	/**
	 * 为控件添加监听
	 */
	private void addListener() {
		// 为当前窗口添加事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 关闭程序之前先要把当前级别写入配置文件
				Manager.getInstance().writeCurrentLevelToConfig();
				System.exit(0);
			}
		});

		// 重置按钮
		faceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().reflash(minePanel);
			}
		});

		// 同重置按钮
		startMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().reflash(minePanel);
			}
		});

		// 切换到初级
		primaryMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().changeLevel(LEVEL.LEVEL_1);
			}
		});

		// 切换到中级
		middleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().changeLevel(LEVEL.LEVEL_2);
			}
		});

		// 切换到高级
		advanceMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Manager.getInstance().changeLevel(LEVEL.LEVEL_3);
			}
		});

		// 显示扫雷英雄榜
		heroBarItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HeroBar heroBar = new HeroBar(View.this);
				heroBar.setVisible(true);
			}
		});
	}
}
