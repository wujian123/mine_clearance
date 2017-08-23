package org.wj.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.common.LEVEL;
import org.wj.model.Hero;
import org.wj.model.LevelInfo;
import org.wj.util.StringUtil;
import org.wj.util.SwingUtil;
import org.wj.util.XMLWriter;


/**
 * ɨ��Ӣ�۰�ҳ��
 * 
 * @author ���
 * 
 */
@SuppressWarnings("serial")
public class HeroBar extends JDialog {

	private List<JLabel> labelList = new ArrayList<JLabel>();

	private JButton restoreButton;

	private JButton submitButton;

	public HeroBar(Frame owner) {
		super(owner, true);
		this.initComponent();
		this.addListener();
	}

	/**
	 * ��ʼ�����
	 */
	private void initComponent() {
		this.loadLabel();

		restoreButton = new JButton("���¼Ʒ�");
		submitButton = new JButton("ȷ��");

		this.setLayout(new FlowLayout());
		for (JLabel label : labelList) {
			this.add(label);
		}
		this.add(restoreButton);
		this.add(submitButton);

		this.setTitle("ɨ��Ӣ�۰�");
		this.setResizable(false);
		Dimension dimension = new Dimension(200, 150);
		this.setSize(dimension);
		this.setLocation(SwingUtil.getCenterLocationPoint(dimension));
	}

	/**
	 * ��Ӽ���
	 */
	private void addListener() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				HeroBar.this.setVisible(false);
			}
		});

		// ���¼Ʒ�
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JLabel label : labelList) {
					label.setText(StringUtil.replaceString(Constant.HERO_BAR_MODEL, label
							.getText().substring(0, 2), "999", "����"));

				}
			}
		});

		// ȷ��
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, Hero> heroMap = new HashMap<String, Hero>();
				for (JLabel label : labelList) {
					String[] heroInfo = StringUtil.restoreString(Constant.HERO_BAR_MODEL,
							label.getText());
					Hero hero = new Hero(heroInfo[2], Integer
							.valueOf(heroInfo[1]));
					heroMap.put(heroInfo[0], hero);
				}
				// д��XML
				XMLWriter.writeHeroToXML(heroMap);
				// ���»���
				updateCache(heroMap);
				HeroBar.this.setVisible(false);
			}
		});
	}

	/**
	 * ���»���
	 * @param heroMap
	 */
	private void updateCache(Map<String, Hero> heroMap) {
		Set<Entry<String, Hero>> heroSet = heroMap.entrySet();
		for (Entry<String, Hero> entry : heroSet) {
			LEVEL level = LEVEL.getLevel(entry.getKey());
			if (level == null) {
				// TODO ����Ӧ�쳣����
				return;
			}
			Cache.levelInfoMap.get(level).setHero(entry.getValue());
		}
	}

	/**
	 * ��ʼ������JLabel
	 */
	private void loadLabel() {
		Set<Map.Entry<LEVEL, LevelInfo>> infos = Cache.levelInfoMap.entrySet();
		Font font = new Font("font", Font.BOLD, 12);
		for (Entry<LEVEL, LevelInfo> info : infos) {
			String levelName = info.getKey().getDescription();
			Hero hero = info.getValue().getHero();
			JLabel label = new JLabel(StringUtil.replaceString(Constant.HERO_BAR_MODEL,
					levelName, String.valueOf(hero.getTime()), hero.getName()));
			label.setFont(font);
			labelList.add(label);
		}
	}
}
