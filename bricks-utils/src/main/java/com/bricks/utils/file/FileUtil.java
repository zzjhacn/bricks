package com.bricks.utils.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * 文件助手类
 * 
 * @author bricks <long1795@gmail.com>
 */
public final class FileUtil {

	/**
	 * 缺省的路径分隔符 : "/"
	 */
	static final String FILE_SEPARATOR = "/";

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
//				return dir.getName().endsWith(fileNameSuffix) || name.endsWith(fileNameSuffix);
				return name.endsWith(fileNameSuffix);
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

	/**
	 * 将路径中的"\"和"."替换为"/"
	 *
	 * @param path
	 *            原始路径 如: "D:\path\subpath"
	 * @return 修饰后的路径 如: "D:/path/subpath/"
	 */
	public static String parsePath(final String path) {
		String result = path.replaceAll("[\\\\\\.]", FILE_SEPARATOR);
		return result.endsWith(FILE_SEPARATOR) ? result : result + FILE_SEPARATOR;
	}
}
