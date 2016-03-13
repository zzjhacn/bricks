package com.bricks.helper.code.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.bricks.helper.code.CodeGenerator;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class AbsCodeGenerator implements CodeGenerator {

	protected void write(VelocityContext vc, String templatePath, String outputPath) {
		FileWriter out = null;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(templatePath));
			out = new FileWriter(new File(outputPath));

			Velocity.init();
			VelocityEngine ve = new VelocityEngine();
			ve.evaluate(vc, out, "code-gen", inputStreamReader);
		} catch (Exception e) {
			log().error(e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				log().error(e.getMessage(), e);
			}
		}
	}
}
