package com.bricks.utils.test.chinesechars;

import org.junit.Test;

import com.bricks.lang.log.LogAble;
import com.bricks.utils.chinesechars.ChineseCharacters;

/**
 * @author bricks <long1795@gmail.com>
 */
public class ChineseCharactersTest implements LogAble {
	@Test
	public void test() throws Exception {
		String cc = ChineseCharacters.chars();
		for (int i = 0; i < 3500; i++) {
			log().info("若" + cc.charAt(i));
		}
		log().info(cc);
	}
}
