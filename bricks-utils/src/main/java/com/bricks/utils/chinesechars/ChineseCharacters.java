package com.bricks.utils.chinesechars;

import static com.bricks.lang.log.LogAble.slog;

import java.io.InputStream;
import java.util.concurrent.Executors;

/**
 * @author bricks <long1795@gmail.com>
 */
public final class ChineseCharacters {
	private ChineseCharacters() {}

	static String chars = "";

	public static String chars() {
		if ("".equals(chars)) {
			synchronized (chars) {
				if ("".equals(chars)) {
					// 延迟加载，节约内存
					load();

					Executors.newSingleThreadExecutor().execute(() -> {
						try {
							// 10分钟后自动卸载，释放内存
							Thread.sleep(10 * 60 * 60 * 1000);
						} catch (Exception e) {
							slog().error(e.getMessage(), e);
						}
						unload();
					});
				}
			}
		}
		return chars;
	}

	static void load() {
		try {
			InputStream is = ChineseCharacters.class.getResourceAsStream("chinesechars.txt");
			byte[] data = new byte[7000];
			is.read(data);
			chars = new String(data, "GBK");
			is.close();
		} catch (Exception e) {
			slog().error(e.getMessage(), e);
		}
	}

	static void unload() {
		chars = "";
	}
}
