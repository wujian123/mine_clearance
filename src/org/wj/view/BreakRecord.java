package org.wj.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.model.Hero;
import org.wj.util.StringUtil;
import org.wj.util.SwingUtil;
import org.wj.util.XMLWriter;


/**
 * 破记录的页面<br>
 * 
 * @author 吴键
 * 
 */
@SuppressWarnings("serial")
public class BreakRecord extends JDialog {

	private JLabel label1 = null;
	private JLabel label2 = null;

	private JTextField textField = null;

	private JButton button = null;

	private String levelName;

	private String oldHeroName;

	public BreakRecord(Frame owner, String levelName, String oldHeroName) {
		super(owner, true);
		this.levelName = levelName;
		this.oldHeroName = oldHeroName;

		this.initComponent();
		this.addListener();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		Font font = new Font("1", Font.BOLD, 12);
		label1 = new JLabel(
				StringUtil.replaceString("已破{0}记录。", this.levelName));
		label1.setFont(font);
		label2 = new JLabel("请留尊姓大名。");
		label2.setFont(font);

		textField = new JTextField(oldHeroName, 8);
		textField.selectAll();
		textField.setSize(50, 10);
		textField.setFocusable(true);

		button = new JButton("确定");

		this.setLayout(new FlowLayout());
		this.add(label1);
		this.add(label2);
		this.add(textField);
		this.add(button);

		// 去除标题栏
		this.setUndecorated(true);
		Dimension dimension = new Dimension(100, 150);
		this.setSize(dimension);
		this.setLocation(SwingUtil.getCenterLocationPoint(dimension));
	}

	/**
	 * 添加监听
	 */
	private void addListener() {
		// 此模式对话框关闭时
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				BreakRecord.this.setVisible(false);
			}
		});

		// 点击提交后
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取所用时间
				int useTime = Integer.valueOf(Cache.timeLabel.getText());
				// 判断输入是否为空,简单判断，不做太多验证，实现功能即可
				if (StringUtil.isNull(textField.getText())) {
					SwingUtil.showMessage(BreakRecord.this, "名字不能为空");
					return;
				}
				String name = textField.getText().trim();
				Hero hero = new Hero(name, useTime);
				// 写入XML
				XMLWriter.writeHeroToXML(hero, Constant.CURRENT_LEVEL);
				// 更新缓存
				BreakRecord.this.updateCache(hero);
				BreakRecord.this.setVisible(false);
			}
		});
	}

	/**
	 * 更新缓存
	 * @param hero
	 */
	private void updateCache(Hero hero) {
		Cache.levelInfoMap.get(Constant.CURRENT_LEVEL).setHero(hero);
	}
}
