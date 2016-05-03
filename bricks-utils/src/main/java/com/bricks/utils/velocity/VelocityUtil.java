package com.bricks.utils.velocity;

import static com.bricks.lang.log.LogAble.slog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.bricks.lang.log.LogAble;

/**
 * Velocity助手类
 * 
 * @author bricks <long1795@gmail.com>
 */
public final class VelocityUtil implements LogAble {

	/**
	 * Velocity文件生成
	 * 
	 * @param vc
	 *            Velocity上下文
	 * @param templatePath
	 *            模板地址
	 * @param outputPath
	 *            输出地址
	 */
	public static void write(final VelocityContext vc, final String templatePath, final String outputPath) {
		File f = new File(templatePath);
		if (!f.exists() || f.isDirectory()) {
			slog().warn("Template file [{}] not exists!", templatePath);
			return;
		}
		FileWriter out = null;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(templatePath));
			out = new FileWriter(new File(outputPath));

			Velocity.init();
			VelocityEngine ve = new VelocityEngine();
			ve.evaluate(vc, out, "code-gen", inputStreamReader);
		} catch (Exception e) {
			slog().error(e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				slog().error(e.getMessage(), e);
			}
		}
	}
}
