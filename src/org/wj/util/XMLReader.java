package org.wj.util;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.wj.cache.Cache;
import org.wj.common.Constant;
import org.wj.common.LEVEL;
import org.wj.model.Hero;
import org.wj.model.LevelInfo;


/**
 * 对XML的读操作
 * 
 * @author 吴键
 * 
 */
public class XMLReader {

	/**
	 * 使用dom4j加载配置文件，得到所需要的初始化信息
	 */
	@SuppressWarnings("unchecked")
	public static void load() {
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(new File("src/config.xml"));
			// 写入缓存
			XMLCache.document = doc;
		} catch (DocumentException e) {
			e.printStackTrace();
			// TODO 异常处理
			return;
		}
		
		// 获取根节点
		Element root = doc.getRootElement();
		String currentLevel = root.element("current-level").getText();
		// 当前级别赋值给Constant.CURRENT_LEVE常量
		Constant.CURRENT_LEVEL = LEVEL.getLevel(Integer.valueOf(currentLevel));

		// 解析level
		List<Element> levels = root.elements("level");
		LevelInfo info = null;
		Hero hero = null;
		for (Element levelEle : levels) {
			info = new LevelInfo();
			info.setLevel(LEVEL.getLevel(Integer.parseInt(levelEle.attribute("id").getText())));
			info.setRows(Integer.parseInt(levelEle.element("rows").getText()));
			info.setColumns(Integer.parseInt(levelEle.element("columns").getText()));
			info.setMineCount(Integer.parseInt(levelEle.element("mine-count").getText()));
			info.setHeight(Integer.parseInt(levelEle.element("height").getText()));
			info.setWidth(Integer.parseInt(levelEle.element("width").getText()));
			info.setHgap(Integer.parseInt(levelEle.element("hgap").getText()));
			info.setFontSize(Integer.parseInt(levelEle.element("font-size").getText()));
			//英雄
			hero = new Hero();
			Element heroEle = levelEle.element("hero");
			hero.setName(heroEle.element("name").getText());
			hero.setTime(Integer.parseInt(heroEle.element("time").getText()));
			info.setHero(hero);
			//放入缓存
			Cache.levelInfoMap.put(info.getLevel(), info);
		}
	}
}
