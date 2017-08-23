package org.wj.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.wj.common.LEVEL;
import org.wj.model.Hero;


/**
 * 对XML的写操作
 * 
 * @author 吴键
 * 
 */
public class XMLWriter {

	/**
	 * 
	 * @param levelNum
	 */
	public static void writeCurrentLevelToXML(String levelNum) {
		// 获取根节点
		Element currentLevel = XMLCache.document.getRootElement().element(
				"current-level");
		String oldLevelNum = currentLevel.getText();
		if (oldLevelNum.equals(levelNum)) {
			return;
		}
		currentLevel.setText(levelNum);
		writeXML(XMLCache.document);
	}

	/**
	 * 写入一个Hero
	 * 
	 * @param hero
	 * @param level
	 */
	public static void writeHeroToXML(Hero hero, LEVEL level) {
		Element levelEle = (Element) XMLCache.document.getRootElement()
				.elements().get(level.getId() - 1);
		Element heroEle = levelEle.element("hero");
		heroEle.element("name").setText(hero.getName());
		heroEle.element("time").setText(String.valueOf(hero.getTime()));
		writeXML(XMLCache.document);
	}

	/**
	 * 写入所有Hero
	 * 
	 * @param heroMap
	 */
	public static void writeHeroToXML(Map<String, Hero> heroMap) {
		Set<Entry<String, Hero>> heroSet = heroMap.entrySet();
		for (Entry<String, Hero> entry : heroSet) {
			int levelId = LEVEL.getIdByDesc(entry.getKey());
			if (0 == levelId) {
				// TODO 这边应该有异常处理
				return;
			}
			Element levelEle = (Element) XMLCache.document.getRootElement()
					.elements().get(levelId - 1);
			Element heroEle = levelEle.element("hero");
			heroEle.element("name").setText(entry.getValue().getName());
			heroEle.element("time").setText(
					String.valueOf(entry.getValue().getTime()));
		}
		writeXML(XMLCache.document);
	}

	/**
	 * 真正写XML
	 * 
	 * @param document
	 */
	private static void writeXML(Document document) {
		// 写操作
		org.dom4j.io.XMLWriter writer = null;
		try {
			writer = new org.dom4j.io.XMLWriter(new FileOutputStream(new File(
					"src/config.xml")));
			writer.write(XMLCache.document);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
