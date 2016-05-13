package com.bricks.utils.test.file;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class FileTest implements LogAble {

	@Test
	public void test() throws Exception {
		listSvnFiles(new File("D:\\dev\\workspace\\jichu"));
		listSvnFiles(new File("D:\\dev\\workspace\\fengkong"));
	}

	private void listSvnFiles(File f) {
		Arrays.asList(f.listFiles()).forEach(sf -> {
			if (".svn".equals(sf.getName())) {
				log().info(f.getAbsolutePath());
			} else if (sf.isDirectory()) {
				listSvnFiles(sf);
			}
		});
	}
}
