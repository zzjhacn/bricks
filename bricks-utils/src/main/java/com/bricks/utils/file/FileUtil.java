package com.bricks.utils.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * 文件助手类
 * 
 * @author bricks <long1795@gmail.com>
 */
public class FileUtil {
	private FileUtil() {}

	/**
	 * 简单文件名过滤器（后缀过滤，不限于扩展名）
	 * 
	 * @param fileNameSuffix
	 *            后缀过滤（不限于扩展名）
	 * @return
	 */
	public static FilenameFilter simpleFileFilter(final String fileNameSuffix) {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.getName().endsWith(fileNameSuffix) || name.endsWith(fileNameSuffix);
			}
		};
	}

	public static void mkdir(String path) {
		mkdir(new File(path));
	}

	public static void mkdir(File f) {
		if (!f.exists() || !f.isDirectory()) {
			f.mkdirs();
		}
	}

	public static void copy(String src, String target) throws IOException {
		org.aspectj.util.FileUtil.copyDir(new File(src), new File(target));
	}
}
