package com.bricks.utils.test.file;

import java.io.File;
import java.util.Arrays;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class PomAnalysor implements LogAble {

	@Test
	public void test() throws Exception {
		String dir = "D:\\dev\\workspace\\sdp\\jichu\\PaymentEngine\\trunk";
		parse(dir);
	}

	@SuppressWarnings("unchecked")
	void parse(String path) throws Exception {
		File f = new File(path);
		if (f.isDirectory()) {
			Arrays.asList(f.listFiles()).forEach(fp -> {
				try {
					parse(fp.getAbsolutePath());
				} catch (Exception e) {
					err(e);
				}
			});
		}
		if (f.isFile() && "pom.xml".equals(f.getName())) {
			log().debug("Parsing pom[{}]", f.getAbsolutePath());
			SAXReader reader = new SAXReader();
			Document d = reader.read(f);
			// parse(d.getRootElement(), 0, false);

			d.selectNodes("/*[name()='project']/*[name()='dependencies']/*[name()='dependency']").forEach(n -> {
				try {
					Element node = (Element) n;
					if (node.element("groupId") != null && (node.elementText("groupId").contains("shengpay") || node.elementText("groupId").contains("sdo"))) {
						parse(node, 0, false);
					}
				} catch (Exception e) {
					err(e);
				}
				// log().info(((Node) n).getPath());
			});
		}
	}

	@SuppressWarnings("unchecked")
	void parse(Element node, final int level, final boolean output) throws Exception {
		// final boolean b = "dependencies".equals(node.getName()) && !node.isRootElement() && "dependencyManagement".equals(node.getParent().getName());
		final boolean b = true;
		if (output) {
			output(node, level);
		}
		if (node.hasContent()) {
			node.elements().forEach(e -> {
				try {
					parse((Element) e, level + 1, output || b);
				} catch (Exception e1) {
					err(e1);
				}
			});
		}
	}

	void output(Element node, final int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i++ < level;) {
			sb.append("\t");
		}
		sb.append(node.getName());
		sb.append(node.isTextOnly() ? "\t=\t" + node.getText() : "");
		log().info(sb.toString());
	}
}
