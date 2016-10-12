package com.bricks.helper.test.code;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.bricks.lang.log.LogAble;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author bricks <long1795@gmail.com>
 */
public class LianjiaCrawlerTest implements LogAble {
	Map<String, Boolean> history = Maps.newConcurrentMap();
	Map<String, List<String>> result = Maps.newConcurrentMap();

	String host = "http://sh.lianjia.com";

	List<String> ingores = Arrays.asList(new String[] { "浦东唐镇", "浦东书院镇", "浦东惠南", "浦东曹路", "浦东高东", "浦东泥城镇", "浦东新场", "浦东航头", "浦东祝桥", "浦东宣桥", "浦东高行", "浦东临港新城", "浦东合庆" });

	Pattern p = Pattern.compile("([0-9]+)\\.([0-9]+)");

	@Test
	public void test() throws Exception {
		crawler("/ershoufang/pudongxinqu/d1b200to450m60to400o1y3");
		reportHtml();
	}

	void crawler(String url) throws IOException {
		Document d = Jsoup.connect(host + url).post();

		history.put(url, true);
		collectPages(d);

		filter(d);

		history.forEach((h, v) -> {
			if (!v) {
				try {
					crawler(h);
				} catch (Exception e) {
					err(e);
				}
			}
		});
	}

	void collectPages(Document d) {
		Elements pages = d.getElementsByClass("page-box");
		if (!pages.isEmpty()) {
			pages.first().getElementsByTag("a").forEach(p -> {
				String href = p.attr("href");
				if (!history.containsKey(href)) {
					history.put(href, false);
				}
			});
		}
	}

	void filter(Document d) {
		Element lst = d.getElementById("house-lst");
		lst.getElementsByTag("li").forEach(n -> {
			Element info = n.getElementsByClass("info-panel").first();
			String detailUrl = info.getElementsByTag("a").first().attr("href");
			String where = info.getElementsByClass("where").first().text();
			String detail = info.getElementsByClass("con").first().text().replaceAll(" ", "");
			String price = info.getElementsByClass("col-3").first().text();
			try {
				if (where.indexOf("1室") > 0) {// 忽略1室
					return;
				}
				String zoon = detail.split("\\|")[0];
				if (ingores.contains(zoon)) {// 忽略区域
					return;
				}
				int floor = Integer.valueOf(detail.split("\\|")[1].replaceAll("[^0-9]", ""));
				if (floor < 7) {// 忽略非电梯房
					return;
				}
				// if (Integer.valueOf(price.split(" ")[0].replace("万", "")) > 450) {// 忽略总价过高的
				// return;
				// }
				// Matcher m = p.matcher(where);
				// if (m.find() && new BigDecimal(m.group(0)).compareTo(new BigDecimal("60")) < 0) {// 忽略面积过小的
				// return;
				// }
				if (!result.containsKey(zoon)) {
					result.put(zoon, Lists.newArrayList());
				}
				result.get(zoon).add(String.format("[%s][%s][%s]%s%s", where, detail, price, host, detailUrl));
			} catch (Exception e) {
				err(e);
			}
		});
	}

	void report() {
		AtomicInteger tot = new AtomicInteger(0);
		result.forEach((k, v) -> {
			log().info("[{}-共{}]", k, v.size());
			tot.addAndGet(v.size());
			Collections.sort(v);
			v.forEach(l -> {
				log().info(l);
			});
		});
		log().info("[{}] found.", tot);
	}

	void reportHtml() {
		System.out.println("\t<ol  class='dd-list'>");
		result.forEach((k, v) -> {
			System.out.println("\t\t<li class='dd-item'><div class='dd-handle'>" + k + "--共【" + v.size() + "】套</div>");
			System.out.println("\t\t\t<ol  class='dd-list'>");
			Collections.sort(v);
			v.forEach(l -> {
				System.out.println("\t\t\t\t<li class='dd-item'><div class='dd-handle'>" + transfer(l) + "</div></li>");
			});
			System.out.println("\t\t\t</ol>");
			System.out.println("\t\t</li>");
		});
		System.out.println("\t</ol>");
	}

	String transfer(String s) {
		StringBuilder sb = new StringBuilder("<a href='");
		sb.append(s.substring(s.indexOf("http")));
		sb.append("' target='_blank'>");
		sb.append(s.substring(0, s.indexOf("http")));
		sb.append("</a>");
		return sb.toString();
	}

}
