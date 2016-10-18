package com.bricks.utils.test.velocity;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.parser.ParserTreeConstants;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.apache.velocity.runtime.parser.node.Node;
import org.junit.Test;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class VelocityTest implements LogAble {

	@Test
	public void test() throws Exception {
		String pattern = "(\\d*)第一(\\d*)\n，第二(\\d*)，共(\\d*)";
		String fact = "21第一22\n"
				+ "，第二24，共26";

		Matcher m = Pattern.compile(pattern).matcher(fact);
		if (m.find()) {
			IntStream.range(0, m.groupCount() + 1).forEach(i -> log().info("{}:\t{}", i, m.group(i)));
		} else {
			log().info("Str[{}] not matched by pattern[{}]", fact, pattern);
		}
		List<kv> l = new LinkedList<>();
		Template t = Velocity.getTemplate("temp.vm", "utf-8");
		ASTprocess p = (ASTprocess) t.getData();
		StringBuffer sb = new StringBuffer(fact);
		IntStream.range(0, p.jjtGetNumChildren()).forEach(i -> {
			Node n = p.jjtGetChild(i);
			Token token = n.getFirstToken();
			switch (n.getType()) {
			case ParserTreeConstants.JJTREFERENCE:
				l.add(new kv(token.image));
				break;
			case ParserTreeConstants.JJTTEXT:
			case ParserTreeConstants.JJTEXPRESSION:
				if (!l.isEmpty()) {
					l.get(l.size() - 1).v = sb.substring(0, sb.indexOf(token.image));
				}
				sb.delete(0, sb.indexOf(token.image));
				String tmp = sb.toString().replaceFirst(token.image, "");
				sb.delete(0, sb.length());
				sb.append(tmp);
				break;

			default:
			}
		});
		if (p.jjtGetChild(p.jjtGetNumChildren() - 1).getType() == ParserTreeConstants.JJTREFERENCE) {
			l.get(l.size() - 1).v = sb.toString();
		}
		l.forEach(kv -> {
			log().info("[{}]===[{}]", kv.k, kv.v);
		});
		log().info("done");
	}

	class kv {
		String k, v;

		kv(String k) {
			this.k = k;
		}
	}
}
