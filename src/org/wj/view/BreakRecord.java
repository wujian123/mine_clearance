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
 * �Ƽ�¼��ҳ��<br>
 * 
 * @author ���
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
	 * ��ʼ�����
	 */
	private void initComponent() {
		Font font = new Font("1", Font.BOLD, 12);
		label1 = new JLabel(
				StringUtil.replaceString("����{0}��¼��", this.levelName));
		label1.setFont(font);
		label2 = new JLabel("�������մ�����");
		label2.setFont(font);

		textField = new JTextField(oldHeroName, 8);
		textField.selectAll();
		textField.setSize(50, 10);
		textField.setFocusable(true);

		button = new JButton("ȷ��");

		this.setLayout(new FlowLayout());
		this.add(label1);
		this.add(label2);
		this.add(textField);
		this.add(button);

		// ȥ��������
		this.setUndecorated(true);
		Dimension dimension = new Dimension(100, 150);
		this.setSize(dimension);
		this.setLocation(SwingUtil.getCenterLocationPoint(dimension));
	}

	/**
	 * ��Ӽ���
	 */
	private void addListener() {
		// ��ģʽ�Ի���ر�ʱ
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				BreakRecord.this.setVisible(false);
			}
		});

		// ����ύ��
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ��ȡ����ʱ��
				int useTime = Integer.valueOf(Cache.timeLabel.getText());
				// �ж������Ƿ�Ϊ��,���жϣ�����̫����֤��ʵ�ֹ��ܼ���
				if (StringUtil.isNull(textField.getText())) {
					SwingUtil.showMessage(BreakRecord.this, "���ֲ���Ϊ��");
					return;
				}
				String name = textField.getText().trim();
				Hero hero = new Hero(name, useTime);
				// д��XML
				XMLWriter.writeHeroToXML(hero, Constant.CURRENT_LEVEL);
				// ���»���
				BreakRecord.this.updateCache(hero);
				BreakRecord.this.setVisible(false);
			}
		});
	}

	/**
	 * ���»���
	 * @param hero
	 */
	private void updateCache(Hero hero) {
		Cache.levelInfoMap.get(Constant.CURRENT_LEVEL).setHero(hero);
	}
}
