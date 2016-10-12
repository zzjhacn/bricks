package com.bricks.utils.test.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class MvnChainGenerator implements LogAble {

	ConcurrentMap<String, P> projects = new ConcurrentHashMap<>();

	ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<>();

	static Map<String, String> svnMap = new HashMap<>();

	boolean log = false;

	@Test
	public void test() throws Exception {
		scan("D:/dev/workspace/sdp/jichu");
		report();
		q.forEach(p -> {
			if (log)
				log().error(p);
		});
	}

	void scan(String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			if (f.getName().startsWith(".")) {
				return;
			}
			List<String> files = Arrays.asList(f.list());
			if (files.contains("pom.xml")) {
				if (log)
					log().debug("Root Project Found at [{}]", path);
				new P(f.getAbsolutePath()).parse();
				return;
			}
			Arrays.asList(f.listFiles()).forEach(fp -> {
				try {
					if (fp.isDirectory()) {
						scan(fp.getAbsolutePath());
					}
				} catch (Exception e) {
					err(e);
				}
			});
		}
	}

	Document readPom(File f) {
		if (log)
			log().debug("Reading pom[{}]", f.getAbsolutePath());
		SAXReader reader = new SAXReader();
		Document d;
		try {
			d = reader.read(f);
		} catch (Exception e) {
			try {
				reader.setEncoding("UTF-8");
				d = reader.read(f);
			} catch (Throwable e1) {
				try {
					reader.setEncoding("gbk");
					d = reader.read(f);
				} catch (Throwable e2) {
					q.add(f.getAbsolutePath());
					return null;
				}
			}
		}
		return d;
	}

	void report() {
		List<String> ps = new ArrayList<>();
		ps.addAll(projects.keySet());
		Collections.sort(ps);
		ps.forEach(s -> {
			P p = projects.get(s);
			if (p.parent == null) {
				// print(p, 0);
				System.out.println("\t<ol  class='dd-list'>");
				printhtml(p, false, 0, "P.");
				System.out.println("\t</ol>");
			}
		});
	}

	String svn(String path) {
		File f = new File(path);
		if (svnMap.containsKey(path)) {
			return "[ " + svnMap.get(path).trim() + " ]";
		}
		return svn(f.getParent());
	}

	AtomicInteger c = new AtomicInteger();

	void printhtml(P p, boolean link, int l, String t) {
		String pre = "\t";
		for (int i = 0; i++ < l;) {
			pre += "\t";
		}
		final String preStr = pre;
		System.out
				.println(pre + "\t<li class='dd-item'" + (link ? "" : " id='" + p.toString() + "'") + "><div class='dd-handle'>" + (link ? "<a href='#" + p.toString() + "'>" : "")
						+ (l == 0 ? c.incrementAndGet() + "&gt;" : "") + l + "." + t + p.toString() + ".D" + p.deps.size() + ".M" + p.modules.size() + (link ? "</a>" : "")
						+ (l == 0 ? svn(p.path) : "") + p.desc
						+ "</div>");
		if (!p.deps.isEmpty()) {
			System.out.println(pre + "\t<ol  class='dd-list'>");
			for (String s : p.deps) {
				if (projects.containsKey(s)) {
					printhtml(projects.get(s), true, l + 1, "D.");
				} else {
					System.out.println(preStr + "\t<li class='dd-item'><div class='dd-handle'>" + (l + 1) + ".D.[" + s + "]</div></li>");
				}
			}
			System.out.println(pre + "\t</ol>");
		}

		if (!p.modules.isEmpty()) {
			System.out.println(pre + "\t<ol  class='dd-list'>");
			for (P m : p.modules) {
				printhtml(m, false, l + 1, "M.");
			}
			System.out.println(pre + "\t</ol>");
		}
		System.out.println(pre + "\t</li>");
	}

	void print(P p, final int l) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i++ < l;) {
			sb.append("\t");
		}
		log().info(sb.toString() + p.toString() + (l == 0 ? "\t" + p.path : ""));
		p.deps.forEach(d -> {
			if (projects.containsKey(d.toString())) {
				print(projects.get(d.toString()), l + 1);
			} else {
				log().info(sb.toString() + "[" + d + "]");
			}
		});
		p.modules.forEach(m -> {
			print(m, l + 1);
		});
	}

	void regist(P p) {
		projects.put(p.toString(), p);
	}

	class P {
		String groupId;
		String artifactId;
		String path;
		String desc;
		P parent;

		P(String p) {
			this.path = p;
		}

		@SuppressWarnings("unchecked")
		void parse() {
			Document d = readPom(new File(path + "/pom.xml"));
			if (d == null) {
				return;
			}
			groupId = d.selectSingleNode("/*[name()='project']/*[name()='groupId']") == null
					? d.selectSingleNode("/*[name()='project']/*[name()='parent']/*[name()='groupId']").getText()
					: d.selectSingleNode("/*[name()='project']/*[name()='groupId']").getText();
			artifactId = d.selectSingleNode("/*[name()='project']/*[name()='artifactId']").getText();
			desc = d.selectSingleNode("/*[name()='project']/*[name()='description']") == null ? "" : d.selectSingleNode("/*[name()='project']/*[name()='description']").getText();
			desc = desc == null || desc.trim().length() == 0 ? "" : " -- " + desc;

			d.selectNodes("/*[name()='project']/*[name()='dependencies']/*[name()='dependency']").forEach(n -> {
				try {
					Element node = (Element) n;
					if (node.element("groupId") != null
							&& (node.elementText("groupId").contains("shengpay") || node.elementText("groupId").contains("sdo") || node.elementText("groupId").contains("snda"))) {
						deps.add(node.elementText("groupId") + "." + node.elementText("artifactId"));
					}
				} catch (Exception e) {
					err(e);
				}
			});

			Element ms = (Element) d.selectSingleNode("/*[name()='project']/*[name()='modules']");
			final P parent = this;
			if (ms != null) {
				ms.elements().forEach(c -> {
					Element module = (Element) c;
					if ("".equals(module.getTextTrim())) {
						return;
					}
					P p = new P(path + "/" + module.getTextTrim());
					p.parent = parent;
					p.parse();
					modules.add(p);
				});
			}
			regist(this);
		}

		List<String> deps = new ArrayList<>();
		List<P> modules = new ArrayList<>();

		public String toString() {
			return groupId + "." + artifactId;
		}
	}

	static {
		svnMap.put("D:\\dev\\workspace\\sdp\\commons", "http://svn.shengpaydev.com/svn/commons/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\config.prod", "http://svn.shengpaydev.com/svn/env_conf/java/prod ");
		svnMap.put("D:\\dev\\workspace\\sdp\\db-dev", "http://svn.shengpaydev.com/svn/product/db_script/dev ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jianquan\\agw\\doc", "http://svn.shengpaydev.com/svn/jianquan/agw/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jianquan\\agw\\trunk", "http://svn.shengpaydev.com/svn/jianquan/agw/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\account-query-java\\doc", "http://svn.shengpaydev.com/svn/jichu/account-query-java/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\account-query-java\\trunk", "http://svn.shengpaydev.com/svn/jichu/account-query-java/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\auc\\doc", "http://svn.shengpaydev.com/svn/jichu/auc/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\auc\\trunk", "http://svn.shengpaydev.com/svn/jichu/auc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\authchannelservice\\doc", "http://svn.shengpaydev.com/svn/jichu/authchannelservice/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\authchannelservice\\trunk", "http://svn.shengpaydev.com/svn/jichu/authchannelservice/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\authenticationcenter\\doc", "http://svn.shengpaydev.com/svn/jichu/authenticationcenter/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\authenticationcenter\\trunk", "http://svn.shengpaydev.com/svn/jichu/authenticationcenter/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bc\\docs", "http://svn.shengpaydev.com/svn/BankChannel/bc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/bc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bcs\\docs", "http://svn.shengpaydev.com/svn/BankChannel/bcs/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bcs\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/bcs/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bcss\\docs", "http://svn.shengpaydev.com/svn/BankChannel/bcss/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bcss\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/bcss/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bda\\docs", "http://svn.shengpaydev.com/svn/BankChannel/bda/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bda\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/bda/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bdc\\docs", "http://svn.shengpaydev.com/svn/BankChannel/bdc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bdc\\proxy", "http://svn.shengpaydev.com/svn/BankChannel/bdc/proxy ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bdc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/bdc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\BindBank\\docs", "http://svn.shengpaydev.com/svn/BankChannel/BindBank/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bwhc\\docs", "http://svn.shengpaydev.com/svn/BankChannel/bwhc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\bwhc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/bwhc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\channel-root-pom\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/channel-root-pom/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\ctc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/ctc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\epc\\docs", "http://svn.shengpaydev.com/svn/BankChannel/epc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\epc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/epc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\fund-channel-framework\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/fund-channel-framework/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\mobile\\doc", "http://svn.shengpaydev.com/svn/BankChannel/mobile/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\mobile\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/mobile/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\mock\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/mock/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\posc\\docs", "http://svn.shengpaydev.com/svn/BankChannel/posc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\posc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/posc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\pospc\\docs", "http://svn.shengpaydev.com/svn/BankChannel/pospc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\pospc\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/pospc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\SPCard\\docs", "http://svn.shengpaydev.com/svn/BankChannel/SPCard/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\SPCard\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/SPCard/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\trunk", "http://svn.shengpaydev.com/svn/BankChannel/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\渠道接口文档汇总", "http://svn.shengpaydev.com/svn/BankChannel/渠道接口文档汇总 ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BankChannel\\渠道项目管理", "http://svn.shengpaydev.com/svn/BankChannel/渠道项目管理 ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\bankpay\\doc", "http://svn.shengpaydev.com/svn/jichu/bankpay/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\bankpay\\trunk", "http://svn.shengpaydev.com/svn/jichu/bankpay/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BASIS\\docs", "http://svn.shengpaydev.com/svn/BASIS/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\BASIS\\trunk", "http://svn.shengpaydev.com/svn/BASIS/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\basis2\\doc", "http://svn.shengpaydev.com/svn/jichu/basis2/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\basis2\\trunk", "http://svn.shengpaydev.com/svn/jichu/basis2/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ChannelDetector\\doc", "http://svn.shengpaydev.com/svn/jichu/ChannelDetector/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ChannelDetector\\trunk", "http://svn.shengpaydev.com/svn/jichu/ChannelDetector/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\cmf\\CmfSchedule\\doc", "http://svn.shengpaydev.com/svn/cmf/CmfSchedule/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\cmf\\CmfSchedule\\trunk", "http://svn.shengpaydev.com/svn/cmf/CmfSchedule/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\cmf\\doc", "http://svn.shengpaydev.com/svn/cmf/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\cmf\\trunk", "http://svn.shengpaydev.com/svn/cmf/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\component\\errorreporter\\doc", "http://svn.shengpaydev.com/svn/jichu/component/errorreporter/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\component\\errorreporter\\trunk", "http://svn.shengpaydev.com/svn/jichu/component/errorreporter/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\counter\\trunk", "http://svn.shengpaydev.com/svn/jichu/counter/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\cs\\doc", "http://svn.shengpaydev.com/svn/jichu/cs/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\cs\\trunk", "http://svn.shengpaydev.com/svn/jichu/cs/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\deposit\\doc", "http://svn.shengpaydev.com/svn/jichu/deposit/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\deposit\\trunk", "http://svn.shengpaydev.com/svn/jichu/deposit/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\dubbo-admin\\trunk", "http://svn.shengpaydev.com/svn/jichu/dubbo-admin/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\dubbo-extension\\trunk", "http://svn.shengpaydev.com/svn/jichu/dubbo-extension/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\FundChannelService\\doc", "http://svn.shengpaydev.com/svn/jichu/FundChannelService/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\FundChannelService\\trunk", "http://svn.shengpaydev.com/svn/jichu/FundChannelService/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\hps\\doc", "http://svn.shengpaydev.com/svn/hps/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\hps\\trunk", "http://svn.shengpaydev.com/svn/hps/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\authorize\\trunk", "http://svn.shengpaydev.com/svn/ma/authorize/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\authservice\\doc", "http://svn.shengpaydev.com/svn/ma/authservice/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\authservice\\trunk", "http://svn.shengpaydev.com/svn/ma/authservice/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\client", "http://svn.shengpaydev.com/svn/ma/client ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\common\\authfilter\\trunk", "http://svn.shengpaydev.com/svn/ma/common/authfilter/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\common\\localcache\\trunk", "http://svn.shengpaydev.com/svn/ma/common/localcache/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\common\\logger\\trunk", "http://svn.shengpaydev.com/svn/ma/common/logger/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\common\\spring-memcached\\trunk", "http://svn.shengpaydev.com/svn/ma/common/spring-memcached/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\common\\uaminterface\\trunk", "http://svn.shengpaydev.com/svn/ma/common/uaminterface/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\doc", "http://svn.shengpaydev.com/svn/ma/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\ma-client\\trunk", "http://svn.shengpaydev.com/svn/ma/ma-client/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\matool\\trunk", "http://svn.shengpaydev.com/svn/ma/matool/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\member\\trunk", "http://svn.shengpaydev.com/svn/ma/member/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\member_level\\doc", "http://svn.shengpaydev.com/svn/ma/member_level/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\member_level\\trunk", "http://svn.shengpaydev.com/svn/ma/member_level/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\ma\\member_new\\trunk", "http://svn.shengpaydev.com/svn/ma/member_new/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\mgateway\\doc", "http://svn.shengpaydev.com/svn/jichu/mgateway/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\mgateway\\trunk", "http://svn.shengpaydev.com/svn/jichu/mgateway/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\op", "http://svn.shengpaydev.com/svn/doc/op ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\p2p-mobile\\demo", "http://svn.shengpaydev.com/svn/jichu/p2p-mobile/demo ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\p2p-mobile\\doc", "http://svn.shengpaydev.com/svn/jichu/p2p-mobile/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\p2p-mobile\\nest", "http://svn.shengpaydev.com/svn/jichu/p2p-mobile/nest ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\p2p-mobile\\plugins", "http://svn.shengpaydev.com/svn/jichu/p2p-mobile/plugins ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\p2p-mobile\\trunk", "http://svn.shengpaydev.com/svn/jichu/p2p-mobile/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\PaymentEngine\\doc", "http://svn.shengpaydev.com/svn/jichu/PaymentEngine/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\PaymentEngine\\trunk", "http://svn.shengpaydev.com/svn/jichu/PaymentEngine/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\REG\\doc", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\REG\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\accore\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\common", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\counter\\doc", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\counter\\groovy-test", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\counter\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\fhfundin\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\monitor\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\pbs\\doc", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\pbs\\pbs\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\pbs\\pbs-admin\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\RMBAccouontBoss\\pbs\\pbs-bos\\trunk", "http://svn.shengpaydev.com/svn/ ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdo-common-lang\\doc", "http://svn.shengpaydev.com/svn/jichu/sdo-common-lang/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdo-common-lang\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdo-common-lang/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cmis\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-cmis/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cmis\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cmis/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-merchant-crm\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-merchant-crm/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-merchant-crm\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-merchant-crm/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-poss-poster\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-poss-poster/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-poss-poster\\sdp-commons\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-poss-poster/sdp-commons/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-poss-poster\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-poss-poster/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-service-account\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-service-account/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-service-account\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-service-account/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-service-contract\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-service-contract/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-service-contract\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-service-contract/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-service-realname\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-service-realname/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-cp-service-realname\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-cp-service-realname/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-mcs-pay\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-mcs-pay/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-mcs-pay\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-mcs-pay/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-bizlog\\doc", "http://svn.shengpaydev.com/svn/sdp-poss-bizlog/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-bizlog\\trunk", "http://svn.shengpaydev.com/svn/sdp-poss-bizlog/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-merchant-crm\\doc", "http://svn.shengpaydev.com/svn/sdp-poss-merchant-crm/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-merchant-crm\\trunk", "http://svn.shengpaydev.com/svn/sdp-poss-merchant-crm/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-service-contract\\trunk", "http://svn.shengpaydev.com/svn/sdp-poss-service-contract/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-service-realNameAuth\\trunk", "http://svn.shengpaydev.com/svn/sdp-poss-service-realNameAuth/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-poss-serviece-account\\trunk", "http://svn.shengpaydev.com/svn/sdp-poss-serviece-account/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-service-crm-query\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-service-crm-query/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-service-crm-query\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-service-crm-query/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-service-crm-sp\\doc", "http://svn.shengpaydev.com/svn/jichu/sdp-service-crm-sp/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\sdp-service-crm-sp\\trunk", "http://svn.shengpaydev.com/svn/jichu/sdp-service-crm-sp/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\shengpay-open\\doc", "http://svn.shengpaydev.com/svn/jichu/shengpay-open/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\shengpay-open\\trunk", "http://svn.shengpaydev.com/svn/jichu/shengpay-open/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\at\\trunk", "http://svn.shengpaydev.com/svn/src/at/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\etlTool\\doc", "http://svn.shengpaydev.com/svn/src/etlTool/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\etlTool\\trunk", "http://svn.shengpaydev.com/svn/src/etlTool/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\fos\\doc", "http://svn.shengpaydev.com/svn/src/fos/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\fos\\trunk", "http://svn.shengpaydev.com/svn/src/fos/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\jiaofei-card\\doc", "http://svn.shengpaydev.com/svn/src/jiaofei-card/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\jiaofei-card\\trunk", "http://svn.shengpaydev.com/svn/src/jiaofei-card/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\merchant-notify\\docs", "http://svn.shengpaydev.com/svn/src/merchant-notify/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\merchant-notify\\trunk", "http://svn.shengpaydev.com/svn/src/merchant-notify/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\op\\businesslog\\doc", "http://svn.shengpaydev.com/svn/src/op/businesslog/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\op\\businesslog\\trunk", "http://svn.shengpaydev.com/svn/src/op/businesslog/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\op\\pagination\\doc", "http://svn.shengpaydev.com/svn/src/op/pagination/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\op\\pagination\\trunk", "http://svn.shengpaydev.com/svn/src/op/pagination/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\receipt\\doc", "http://svn.shengpaydev.com/svn/src/receipt/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\receipt\\trunk", "http://svn.shengpaydev.com/svn/src/receipt/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\rnas\\trunk", "http://svn.shengpaydev.com/svn/src/rnas/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-ext\\doc", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-ext/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-ext\\jboss-ds-ucs\\trunk", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-ext/jboss-ds-ucs/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-ext\\tomcat-dbcp-ex\\trunk",
				"http://svn.shengpaydev.com/svn/src/sdp-boss-config-ext/tomcat-dbcp-ex/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-ext\\trunk", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-ext/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-web\\doc", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-web/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-web\\trunk", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-web/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-ws\\doc", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-ws/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-boss-config-ws\\trunk", "http://svn.shengpaydev.com/svn/src/sdp-boss-config-ws/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-poss-customerService\\sql", "http://svn.shengpaydev.com/svn/src/sdp-poss-customerService/sql ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-poss-customerService\\trunk", "http://svn.shengpaydev.com/svn/src/sdp-poss-customerService/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-poss-poster\\docs", "http://svn.shengpaydev.com/svn/src/sdp-poss-poster/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\sdp-poss-poster\\trunk", "http://svn.shengpaydev.com/svn/src/sdp-poss-poster/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\shengpay-commons\\trunk", "http://svn.shengpaydev.com/svn/src/shengpay-commons/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\tqc\\trunk", "http://svn.shengpaydev.com/svn/src/tqc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\Wallet_Deposit\\trunk", "http://svn.shengpaydev.com/svn/src/Wallet_Deposit/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\withholding-sign\\docs", "http://svn.shengpaydev.com/svn/src/withholding-sign/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\withholding-sign\\trunk", "http://svn.shengpaydev.com/svn/src/withholding-sign/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\youhui\\docs", "http://svn.shengpaydev.com/svn/src/youhui/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\src\\youhui\\trunk", "http://svn.shengpaydev.com/svn/src/youhui/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\workflow\\doc", "http://svn.shengpaydev.com/svn/jichu/workflow/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\jichu\\workflow\\trunk", "http://svn.shengpaydev.com/svn/jichu/workflow/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\masopen\\doc", "http://svn.shengpaydev.com/svn/mas/dota/masopen/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\masopen\\trunk", "http://svn.shengpaydev.com/svn/mas/dota/masopen/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\pos\\doc", "http://svn.shengpaydev.com/svn/src/pos/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\pos\\trunk", "http://svn.shengpaydev.com/svn/src/pos/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\api-auth\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/api-auth/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\api-auth\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/api-auth/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\credit\\doc", "http://svn.shengpaydev.com/svn/sdp_rms/credit/doc ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\credit\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/credit/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\credit-resource\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/credit-resource/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\credit-service\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/credit-service/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\dc\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/dc/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\dc\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/dc/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\dc-report\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/dc-report/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\pbd-data\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/pbd-data/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\police-data-api\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/police-data-api/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\policeDatas", "http://svn.shengpaydev.com/svn/sdp_rms/policeDatas ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\resource-pool\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/resource-pool/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\resource-pool\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/resource-pool/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-entrance\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rms-entrance/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-intra\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rms-intra/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-ng-console\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rms-ng-console/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-ng-datacenter\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rms-ng-datacenter/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-ng-scheduler\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rms-ng-scheduler/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-rules\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/rms-rules/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-rules\\rules", "http://svn.shengpaydev.com/svn/sdp_rms/rms-rules/rules ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rms-rules\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rms-rules/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rnas\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/rnas/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\rnas\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/rnas/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\sars-monitor\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/sars-monitor/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\sars-monitor\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/sars-monitor/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\sars-utils\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/sars-utils/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\sars-utils\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/sars-utils/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\SFS\\docs", "http://svn.shengpaydev.com/svn/sdp_rms/SFS/docs ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\SFS\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/SFS/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\venus-snda\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/venus-snda/trunk ");
		svnMap.put("D:\\dev\\workspace\\sdp\\sdp_rms\\venus_plus\\trunk", "http://svn.shengpaydev.com/svn/sdp_rms/venus_plus/trunk ");
	}
}
